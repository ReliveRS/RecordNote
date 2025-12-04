package com.proyecto.recordnote.utils


import android.content.Context
import android.net.Uri
import android.os.Environment
import android.webkit.MimeTypeMap
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.DecimalFormat
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * Utilidad para operaciones con archivos
 * Proporciona métodos para crear, copiar, mover y gestionar archivos
 */
object FileUtils {

    /**
     * Obtiene el directorio de la aplicación en almacenamiento externo
     * @param context Contexto de la aplicación
     * @return Directorio principal de la app
     */
    fun getAppDirectory(context: Context): File {
        val baseDir = context.getExternalFilesDir(null) ?: context.filesDir
        return File(baseDir, Constants.APP_NAME)
    }

    /**
     * Obtiene el directorio para archivos de audio
     * @param context Contexto de la aplicación
     * @return Directorio de audio
     */
    fun getAudioDirectory(context: Context): File {
        val dir = File(getAppDirectory(context), "Audio")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }

    /**
     * Obtiene el directorio para backups
     * @param context Contexto de la aplicación
     * @return Directorio de backups
     */
    fun getBackupDirectory(context: Context): File {
        val dir = File(getAppDirectory(context), "Backup")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }

    /**
     * Obtiene el directorio para exportaciones
     * @param context Contexto de la aplicación
     * @return Directorio de exportaciones
     */
    fun getExportDirectory(context: Context): File {
        val dir = File(getAppDirectory(context), "Export")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }

    /**
     * Obtiene el directorio temporal
     * @param context Contexto de la aplicación
     * @return Directorio temporal
     */
    fun getTempDirectory(context: Context): File {
        val dir = File(context.cacheDir, "temp")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }

    /**
     * Crea un archivo de audio con nombre único basado en timestamp
     * @param context Contexto de la aplicación
     * @return Archivo creado
     */
    fun createAudioFile(context: Context): File {
        val audioDir = getAudioDirectory(context)
        val timestamp = DateUtils.formatForFilename()
        val fileName = "AUDIO_${timestamp}${Constants.AUDIO_FILE_EXTENSION}"
        return File(audioDir, fileName)
    }

    /**
     * Crea un archivo de backup con nombre único
     * @param context Contexto de la aplicación
     * @return Archivo de backup creado
     */
    fun createBackupFile(context: Context): File {
        val backupDir = getBackupDirectory(context)
        val timestamp = DateUtils.formatForFilename()
        val fileName = "BACKUP_${timestamp}${Constants.BACKUP_FILE_EXTENSION}"
        return File(backupDir, fileName)
    }

    /**
     * Verifica si un archivo existe
     * @param path Ruta del archivo
     * @return true si el archivo existe
     */
    fun fileExists(path: String): Boolean {
        return try {
            File(path).exists()
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Obtiene el tamaño de un archivo en bytes
     * @param path Ruta del archivo
     * @return Tamaño en bytes o 0 si no existe
     */
    fun getFileSize(path: String): Long {
        return try {
            File(path).length()
        } catch (e: Exception) {
            0L
        }
    }

    /**
     * Formatea el tamaño de archivo a formato legible (KB, MB, GB)
     * @param sizeInBytes Tamaño en bytes
     * @return String formateado (ej: "2.5 MB")
     */
    fun formatFileSize(sizeInBytes: Long): String {
        if (sizeInBytes <= 0) return "0 B"

        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (Math.log10(sizeInBytes.toDouble()) / Math.log10(1024.0)).toInt()

        val formatter = DecimalFormat("#,##0.#")
        return formatter.format(sizeInBytes / Math.pow(1024.0, digitGroups.toDouble())) + " " + units[digitGroups]
    }

    /**
     * Copia un archivo a una nueva ubicación
     * @param source Archivo origen
     * @param destination Archivo destino
     * @return true si la copia fue exitosa
     */
    fun copyFile(source: File, destination: File): Boolean {
        return try {
            if (!source.exists()) return false

            // Crear directorio padre si no existe
            destination.parentFile?.mkdirs()

            FileInputStream(source).use { input ->
                FileOutputStream(destination).use { output ->
                    val buffer = ByteArray(8192)
                    var read: Int
                    while (input.read(buffer).also { read = it } != -1) {
                        output.write(buffer, 0, read)
                    }
                }
            }
            true
        } catch (e: IOException) {
            android.util.Log.e(Constants.LOG_TAG, "Error al copiar archivo: ${e.message}")
            false
        }
    }

    /**
     * Mueve un archivo a una nueva ubicación
     * @param source Archivo origen
     * @param destination Archivo destino
     * @return true si el movimiento fue exitoso
     */
    fun moveFile(source: File, destination: File): Boolean {
        return try {
            if (!source.exists()) return false

            // Intentar renombrar primero (más rápido si están en el mismo filesystem)
            if (source.renameTo(destination)) {
                return true
            }

            // Si falla, copiar y eliminar
            if (copyFile(source, destination)) {
                source.delete()
                true
            } else {
                false
            }
        } catch (e: Exception) {
            android.util.Log.e(Constants.LOG_TAG, "Error al mover archivo: ${e.message}")
            false
        }
    }

    /**
     * Elimina un archivo
     * @param path Ruta del archivo
     * @return true si se eliminó exitosamente
     */
    fun deleteFile(path: String): Boolean {
        return try {
            File(path).delete()
        } catch (e: Exception) {
            android.util.Log.e(Constants.LOG_TAG, "Error al eliminar archivo: ${e.message}")
            false
        }
    }

    /**
     * Elimina un directorio y todo su contenido recursivamente
     * @param directory Directorio a eliminar
     * @return true si se eliminó exitosamente
     */
    fun deleteDirectory(directory: File): Boolean {
        return try {
            if (!directory.exists()) return true

            if (directory.isDirectory) {
                directory.listFiles()?.forEach { file ->
                    if (file.isDirectory) {
                        deleteDirectory(file)
                    } else {
                        file.delete()
                    }
                }
            }
            directory.delete()
        } catch (e: Exception) {
            android.util.Log.e(Constants.LOG_TAG, "Error al eliminar directorio: ${e.message}")
            false
        }
    }

    /**
     * Limpia el directorio temporal eliminando archivos antiguos
     * @param context Contexto de la aplicación
     * @param olderThanHours Eliminar archivos más antiguos que X horas
     * @return Número de archivos eliminados
     */
    fun cleanTempDirectory(context: Context, olderThanHours: Int = 24): Int {
        val tempDir = getTempDirectory(context)
        var deletedCount = 0

        val cutoffTime = System.currentTimeMillis() - (olderThanHours * 60 * 60 * 1000)

        tempDir.listFiles()?.forEach { file ->
            if (file.lastModified() < cutoffTime) {
                if (file.delete()) {
                    deletedCount++
                }
            }
        }

        return deletedCount
    }

    /**
     * Obtiene el tipo MIME de un archivo
     * @param file Archivo
     * @return Tipo MIME o null si no se puede determinar
     */
    fun getMimeType(file: File): String? {
        val extension = file.extension
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
    }

    /**
     * Obtiene el tipo MIME desde una URL
     * @param url URL del archivo
     * @return Tipo MIME o null
     */
    fun getMimeType(url: String): String? {
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
    }

    /**
     * Comprime múltiples archivos en un ZIP
     * @param files Lista de archivos a comprimir
     * @param zipFile Archivo ZIP de salida
     * @return true si la compresión fue exitosa
     */
    fun zipFiles(files: List<File>, zipFile: File): Boolean {
        return try {
            ZipOutputStream(FileOutputStream(zipFile)).use { zos ->
                files.forEach { file ->
                    if (file.exists() && file.isFile) {
                        FileInputStream(file).use { fis ->
                            val zipEntry = ZipEntry(file.name)
                            zos.putNextEntry(zipEntry)

                            val buffer = ByteArray(8192)
                            var length: Int
                            while (fis.read(buffer).also { length = it } > 0) {
                                zos.write(buffer, 0, length)
                            }

                            zos.closeEntry()
                        }
                    }
                }
            }
            true
        } catch (e: Exception) {
            android.util.Log.e(Constants.LOG_TAG, "Error al comprimir archivos: ${e.message}")
            false
        }
    }

    /**
     * Calcula el tamaño total de un directorio
     * @param directory Directorio a calcular
     * @return Tamaño total en bytes
     */
    fun getDirectorySize(directory: File): Long {
        var size = 0L

        if (directory.exists()) {
            directory.listFiles()?.forEach { file ->
                size += if (file.isDirectory) {
                    getDirectorySize(file)
                } else {
                    file.length()
                }
            }
        }

        return size
    }

    /**
     * Cuenta el número de archivos en un directorio (recursivo)
     * @param directory Directorio
     * @return Número de archivos
     */
    fun countFiles(directory: File): Int {
        var count = 0

        if (directory.exists() && directory.isDirectory) {
            directory.listFiles()?.forEach { file ->
                count += if (file.isDirectory) {
                    countFiles(file)
                } else {
                    1
                }
            }
        }

        return count
    }

    /**
     * Verifica si hay suficiente espacio disponible
     * @param context Contexto de la aplicación
     * @param requiredBytes Bytes requeridos
     * @return true si hay suficiente espacio
     */
    fun hasEnoughSpace(context: Context, requiredBytes: Long): Boolean {
        val availableBytes = getAvailableSpace(context)
        return availableBytes >= requiredBytes
    }

    /**
     * Obtiene el espacio disponible en el almacenamiento
     * @param context Contexto de la aplicación
     * @return Bytes disponibles
     */
    fun getAvailableSpace(context: Context): Long {
        val path = context.getExternalFilesDir(null) ?: context.filesDir
        return path.freeSpace
    }

    /**
     * Obtiene el espacio total del almacenamiento
     * @param context Contexto de la aplicación
     * @return Bytes totales
     */
    fun getTotalSpace(context: Context): Long {
        val path = context.getExternalFilesDir(null) ?: context.filesDir
        return path.totalSpace
    }

    /**
     * Genera un nombre de archivo único agregando sufijo numérico si es necesario
     * @param directory Directorio donde se creará
     * @param baseName Nombre base del archivo
     * @param extension Extensión del archivo (con punto)
     * @return Nombre único de archivo
     */
    fun generateUniqueFileName(directory: File, baseName: String, extension: String): String {
        var fileName = "$baseName$extension"
        var counter = 1

        while (File(directory, fileName).exists()) {
            fileName = "${baseName}_$counter$extension"
            counter++
        }

        return fileName
    }

    /**
     * Convierte URI de contenido a ruta de archivo
     * @param context Contexto de la aplicación
     * @param uri URI del archivo
     * @return Ruta del archivo o null
     */
    fun getPathFromUri(context: Context, uri: Uri): String? {
        return try {
            context.contentResolver.openInputStream(uri)?.use { input ->
                val tempFile = File(getTempDirectory(context), "temp_${System.currentTimeMillis()}")
                FileOutputStream(tempFile).use { output ->
                    input.copyTo(output)
                }
                tempFile.absolutePath
            }
        } catch (e: Exception) {
            android.util.Log.e(Constants.LOG_TAG, "Error al obtener ruta de URI: ${e.message}")
            null
        }
    }
}