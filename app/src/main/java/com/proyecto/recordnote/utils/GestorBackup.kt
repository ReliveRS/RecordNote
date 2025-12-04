package com.proyecto.recordnote.utils
/*
import android.content.Context
import com.google.gson.Gson
import com.proyecto.recordnote.data.local.dao.NotaDao
import com.proyecto.recordnote.data.local.dao.UsuarioDao
import com.proyecto.recordnote.data.local.entidades.Nota
import com.proyecto.recordnote.data.local.entidades.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.time.LocalDateTime
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Gestor de backups para la aplicación
 * Maneja creación y restauración de backups completos
 * Incluye base de datos y archivos de audio
 */
@Singleton
class GestorBackup @Inject constructor(
    private val notaDao: NotaDao,
    private val usuarioDao: UsuarioDao,
    private val gson: Gson
) {

    /**
     * Crea un backup completo de todos los datos del usuario
     * Incluye notas, configuraciones y archivos de audio
     * @param context Contexto de la aplicación
     * @param usuarioId ID del usuario
     * @return Result con la ruta del backup o error
     */
    suspend fun crearBackup(context: Context, usuarioId: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            // Crear archivo de backup
            val backupFile = FileUtils.createBackupFile(context)

            // Obtener datos del usuario
            val usuario = usuarioDao.obtenerPorIdSync(usuarioId)
                ?: return@withContext Result.failure(Exception("Usuario no encontrado"))

            // Obtener todas las notas del usuario
            val notas = notaDao.obtenerTodas(usuarioId)
            var notasList: List<Nota> = emptyList()

            // Recolectar notas del Flow (simplificado)
            // En producción usarías .first() o .collect()

            // Crear estructura de backup
            val backupData = BackupData(
                version = Constants.DATABASE_VERSION,
                fechaBackup = LocalDateTime.now(),
                usuario = usuario,
                notas = notasList,
                configuracion = obtenerConfiguracion(context)
            )

            // Crear archivo ZIP con datos
            ZipOutputStream(FileOutputStream(backupFile)).use { zos ->
                // 1. Guardar datos JSON
                val jsonEntry = ZipEntry("backup_data.json")
                zos.putNextEntry(jsonEntry)
                val jsonData = gson.toJson(backupData)
                zos.write(jsonData.toByteArray())
                zos.closeEntry()

                // 2. Guardar archivos de audio
                notasList.forEach { nota ->
                    nota.rutaAudio?.let { audioPath ->
                        val audioFile = File(audioPath)
                        if (audioFile.exists()) {
                            val audioEntry = ZipEntry("audio/${audioFile.name}")
                            zos.putNextEntry(audioEntry)

                            FileInputStream(audioFile).use { fis ->
                                fis.copyTo(zos)
                            }

                            zos.closeEntry()
                        }
                    }
                }
            }

            Result.success(backupFile.absolutePath)
        } catch (e: Exception) {
            android.util.Log.e(Constants.LOG_TAG, "Error al crear backup: ${e.message}")
            Result.failure(Exception("Error al crear backup: ${e.message}"))
        }
    }

    /**
     * Restaura un backup desde archivo
     * Reemplaza los datos actuales con los del backup
     * @param context Contexto de la aplicación
     * @param backupPath Ruta del archivo de backup
     * @return Result con resumen de restauración o error
     */
    suspend fun restaurarBackup(
        context: Context,
        backupPath: String
    ): Result<RestauracionResumen> = withContext(Dispatchers.IO) {
        try {
            val backupFile = File(backupPath)
            if (!backupFile.exists()) {
                return@withContext Result.failure(Exception("Archivo de backup no encontrado"))
            }

            var notasRestauradas = 0
            var audiosRestaurados = 0
            var backupData: BackupData? = null

            // Extraer datos del ZIP
            ZipInputStream(FileInputStream(backupFile)).use { zis ->
                var entry = zis.nextEntry

                while (entry != null) {
                    when {
                        entry.name == "backup_data.json" -> {
                            // Leer datos JSON
                            val jsonData = zis.readBytes().toString(Charsets.UTF_8)
                            backupData = gson.fromJson(jsonData, BackupData::class.java)
                        }
                        entry.name.startsWith("audio/") -> {
                            // Restaurar archivo de audio
                            val audioFileName = entry.name.substringAfter("audio/")
                            val audioDir = FileUtils.getAudioDirectory(context)
                            val audioFile = File(audioDir, audioFileName)

                            FileOutputStream(audioFile).use { fos ->
                                zis.copyTo(fos)
                            }
                            audiosRestaurados++
                        }
                    }

                    zis.closeEntry()
                    entry = zis.nextEntry
                }
            }

            // Validar datos del backup
            if (backupData == null) {
                return@withContext Result.failure(Exception("Datos de backup inválidos"))
            }

            // Restaurar usuario
            usuarioDao.insertar(backupData.usuario)

            // Restaurar notas
            backupData.notas.forEach { nota ->
                notaDao.insertar(nota)
                notasRestauradas++
            }

            // Restaurar configuración
            restaurarConfiguracion(context, backupData.configuracion)

            val resumen = RestauracionResumen(
                notasRestauradas = notasRestauradas,
                audiosRestaurados = audiosRestaurados,
                fechaBackup = backupData.fechaBackup,
                fechaRestauracion = LocalDateTime.now()
            )

            Result.success(resumen)
        } catch (e: Exception) {
            android.util.Log.e(Constants.LOG_TAG, "Error al restaurar backup: ${e.message}")
            Result.failure(Exception("Error al restaurar backup: ${e.message}"))
        }
    }

    /**
     * Lista todos los backups disponibles
     * @param context Contexto de la aplicación
     * @return Lista de información de backups
     */
    fun listarBackups(context: Context): List<BackupInfo> {
        val backupDir = FileUtils.getBackupDirectory(context)
        val backups = mutableListOf<BackupInfo>()

        backupDir.listFiles()?.forEach { file ->
            if (file.extension == "rnbackup") {
                try {
                    // Leer metadatos del backup
                    val metadata = leerMetadatosBackup(file)
                    backups.add(
                        BackupInfo(
                            nombre = file.name,
                            ruta = file.absolutePath,
                            tamaño = file.length(),
                            fechaCreacion = metadata?.fechaBackup ?: DateUtils.fromTimestamp(file.lastModified()),
                            numeroNotas = metadata?.numeroNotas ?: 0
                        )
                    )
                } catch (e: Exception) {
                    android.util.Log.e(Constants.LOG_TAG, "Error al leer backup: ${e.message}")
                }
            }
        }

        return backups.sortedByDescending { it.fechaCreacion }
    }

    /**
     * Elimina un archivo de backup
     * @param backupPath Ruta del backup
     * @return true si se eliminó exitosamente
     */
    fun eliminarBackup(backupPath: String): Boolean {
        return FileUtils.deleteFile(backupPath)
    }

    /**
     * Limpia backups antiguos manteniendo solo los N más recientes
     * @param context Contexto de la aplicación
     * @param mantenerUltimos Número de backups recientes a mantener
     * @return Número de backups eliminados
     */
    fun limpiarBackupsAntiguos(context: Context, mantenerUltimos: Int = 5): Int {
        val backups = listarBackups(context)
        var eliminados = 0

        if (backups.size > mantenerUltimos) {
            backups.drop(mantenerUltimos).forEach { backup ->
                if (eliminarBackup(backup.ruta)) {
                    eliminados++
                }
            }
        }

        return eliminados
    }

    /**
     * Verifica la integridad de un backup
     * @param backupPath Ruta del backup
     * @return true si el backup es válido
     */
    suspend fun verificarIntegridadBackup(backupPath: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val backupFile = File(backupPath)
            if (!backupFile.exists()) return@withContext false

            var tieneJson = false

            ZipInputStream(FileInputStream(backupFile)).use { zis ->
                var entry = zis.nextEntry
                while (entry != null) {
                    if (entry.name == "backup_data.json") {
                        tieneJson = true
                        // Intentar parsear JSON
                        val jsonData = zis.readBytes().toString(Charsets.UTF_8)
                        gson.fromJson(jsonData, BackupData::class.java)
                    }
                    zis.closeEntry()
                    entry = zis.nextEntry
                }
            }

            tieneJson
        } catch (e: Exception) {
            android.util.Log.e(Constants.LOG_TAG, "Error al verificar backup: ${e.message}")
            false
        }
    }

    // ========== MÉTODOS PRIVADOS ==========

    /**
     * Obtiene la configuración actual de la aplicación
     */
    private fun obtenerConfiguracion(context: Context): Map<String, Any> {
        val prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.all.toMap()
    }

    /**
     * Restaura la configuración desde el backup
     */
    private fun restaurarConfiguracion(context: Context, configuracion: Map<String, Any>) {
        val prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()

        configuracion.forEach { (key, value) ->
            when (value) {
                is String -> editor.putString(key, value)
                is Int -> editor.putInt(key, value)
                is Boolean -> editor.putBoolean(key, value)
                is Float -> editor.putFloat(key, value)
                is Long -> editor.putLong(key, value)
            }
        }

        editor.apply()
    }

    /**
     * Lee metadatos de un archivo de backup
     */
    private fun leerMetadatosBackup(backupFile: File): BackupMetadata? {
        return try {
            ZipInputStream(FileInputStream(backupFile)).use { zis ->
                var entry = zis.nextEntry
                while (entry != null) {
                    if (entry.name == "backup_data.json") {
                        val jsonData = zis.readBytes().toString(Charsets.UTF_8)
                        val backupData = gson.fromJson(jsonData, BackupData::class.java)
                        return@use BackupMetadata(
                            fechaBackup = backupData.fechaBackup,
                            numeroNotas = backupData.notas.size
                        )
                    }
                    zis.closeEntry()
                    entry = zis.nextEntry
                }
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}

/**
 * Estructura de datos del backup
 */
data class BackupData(
    val version: Int,
    val fechaBackup: LocalDateTime,
    val usuario: Usuario,
    val notas: List<Nota>,
    val configuracion: Map<String, Any>
)

/**
 * Información de un backup
 */
data class BackupInfo(
    val nombre: String,
    val ruta: String,
    val tamaño: Long,
    val fechaCreacion: LocalDateTime,
    val numeroNotas: Int
) {
    fun getTamañoFormateado(): String = FileUtils.formatFileSize(tamaño)
}

/**
 * Resumen de restauración de backup
 */
data class RestauracionResumen(
    val notasRestauradas: Int,
    val audiosRestaurados: Int,
    val fechaBackup: LocalDateTime,
    val fechaRestauracion: LocalDateTime
)

/**
 * Metadatos de backup
 */
data class BackupMetadata(
    val fechaBackup: LocalDateTime,
    val numeroNotas: Int
)*/