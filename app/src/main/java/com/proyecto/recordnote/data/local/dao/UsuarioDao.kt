package com.proyecto.recordnote.data.local.dao

import androidx.room.*
import com.proyecto.recordnote.data.local.entidades.UsuarioEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

/**
 * Data Access Object para operaciones con usuarios en Room
 * Gestiona la información del perfil y preferencias
 */
@Dao
interface UsuarioDao {

    // ========== OPERACIONES BÁSICAS ==========

    /**
     * Inserta un nuevo usuario o lo reemplaza si ya existe
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(usuario: UsuarioEntity)

    /**
     * Actualiza los datos de un usuario existente
     */
    @Update
    suspend fun actualizar(usuario: UsuarioEntity)

    /**
     * Elimina un usuario de la base de datos
     */
    @Delete
    suspend fun eliminar(usuario: UsuarioEntity)

    // ========== CONSULTAS ==========

    /**
     * Obtiene un usuario por su ID con actualizaciones reactivas
     */
    @Query("SELECT * FROM usuarios WHERE id = :usuarioId")
    fun obtenerPorId(usuarioId: String): Flow<UsuarioEntity?>

    /**
     * Obtiene un usuario por su ID de forma síncrona
     */
    @Query("SELECT * FROM usuarios WHERE id = :usuarioId")
    suspend fun obtenerPorIdSync(usuarioId: String): UsuarioEntity?

    /**
     * Obtiene un usuario por su email
     */
    @Query("SELECT * FROM usuarios WHERE email = :email")
    suspend fun obtenerPorEmail(email: String): UsuarioEntity?

    /**
     * Verifica si existe un usuario con un email específico
     */
    @Query("SELECT COUNT(*) > 0 FROM usuarios WHERE email = :email")
    suspend fun existeEmail(email: String): Boolean

    /**
     * Obtiene todos los usuarios registrados localmente
     */
    @Query("SELECT * FROM usuarios WHERE activo = 1 ORDER BY ultimoAcceso DESC")
    suspend fun obtenerTodos(): List<UsuarioEntity>

    /**
     * Obtiene el usuario activo (si solo manejas un usuario local)
     */
    @Query("SELECT * FROM usuarios WHERE activo = 1 LIMIT 1")
    suspend fun obtenerUsuarioActivo(): UsuarioEntity?

    // ========== ACTUALIZACIONES ESPECÍFICAS ==========

    /**
     * Actualiza solo la foto de perfil de un usuario
     */
    @Query("UPDATE usuarios SET fotoPerfil = :fotoPerfil WHERE id = :usuarioId")
    suspend fun actualizarFotoPerfil(usuarioId: String, fotoPerfil: String?)

    /**
     * Actualiza el timestamp del último acceso
     */
    @Query("UPDATE usuarios SET ultimoAcceso = :ultimoAcceso WHERE id = :usuarioId")
    suspend fun actualizarUltimoAcceso(usuarioId: String, ultimoAcceso: LocalDateTime)

    /**
     * Actualiza solo el nombre del usuario
     */
    @Query("UPDATE usuarios SET nombre = :nombre WHERE id = :usuarioId")
    suspend fun actualizarNombre(usuarioId: String, nombre: String)

    /**
     * Actualiza el email del usuario
     */
    @Query("UPDATE usuarios SET email = :email WHERE id = :usuarioId")
    suspend fun actualizarEmail(usuarioId: String, email: String)

    /**
     * Actualiza el teléfono del usuario
     */
    @Query("UPDATE usuarios SET telefono = :telefono WHERE id = :usuarioId")
    suspend fun actualizarTelefono(usuarioId: String, telefono: String?)

    // ========== PREFERENCIAS ==========

    /**
     * Actualiza el modo de tema del usuario
     */
    @Query("UPDATE usuarios SET temaModo = :temaModo WHERE id = :usuarioId")
    suspend fun actualizarTemaModo(usuarioId: String, temaModo: String)

    /**
     * Actualiza la calidad de audio preferida
     */
    @Query("UPDATE usuarios SET calidadAudio = :calidadAudio WHERE id = :usuarioId")
    suspend fun actualizarCalidadAudio(usuarioId: String, calidadAudio: String)

    /**
     * Actualiza el estado de transcripción automática
     */
    @Query("UPDATE usuarios SET transcripcionAutomatica = :transcripcionAutomatica WHERE id = :usuarioId")
    suspend fun actualizarTranscripcionAutomatica(usuarioId: String, transcripcionAutomatica: Boolean)

    /**
     * Actualiza el estado de sincronización automática
     */
    @Query("UPDATE usuarios SET sincronizacionAutomatica = :sincronizacionAutomatica WHERE id = :usuarioId")
    suspend fun actualizarSincronizacionAutomatica(usuarioId: String, sincronizacionAutomatica: Boolean)

    /**
     * Actualiza el estado de notificaciones
     */
    @Query("UPDATE usuarios SET notificacionesActivas = :notificacionesActivas WHERE id = :usuarioId")
    suspend fun actualizarNotificaciones(usuarioId: String, notificacionesActivas: Boolean)

    /**
     * Actualiza la configuración de backup automático
     */
    @Query("UPDATE usuarios SET backupAutomatico = :backupAutomatico, frecuenciaBackup = :frecuenciaBackup WHERE id = :usuarioId")
    suspend fun actualizarConfiguracionBackup(usuarioId: String, backupAutomatico: Boolean, frecuenciaBackup: Int)

    /**
     * Actualiza el idioma de transcripción
     */
    @Query("UPDATE usuarios SET idiomaTranscripcion = :idiomaTranscripcion WHERE id = :usuarioId")
    suspend fun actualizarIdiomaTranscripcion(usuarioId: String, idiomaTranscripcion: String)

    // ========== GESTIÓN DE SESIÓN ==========

    /**
     * Marca un usuario como activo y todos los demás como inactivos
     */
    @Transaction
    suspend fun establecerUsuarioActivo(usuarioId: String) {
        desactivarTodos()
        activarUsuario(usuarioId)
    }

    /**
     * Marca un usuario como activo
     */
    @Query("UPDATE usuarios SET activo = 1 WHERE id = :usuarioId")
    suspend fun activarUsuario(usuarioId: String)

    /**
     * Desactiva todos los usuarios
     */
    @Query("UPDATE usuarios SET activo = 0")
    suspend fun desactivarTodos()

    /**
     * Marca un usuario como inactivo (soft delete)
     */
    @Query("UPDATE usuarios SET activo = 0 WHERE id = :usuarioId")
    suspend fun desactivarUsuario(usuarioId: String)

    // ========== UTILIDADES ==========

    /**
     * Elimina todos los usuarios de la base de datos local
     */
    @Query("DELETE FROM usuarios")
    suspend fun eliminarTodos()

    /**
     * Cuenta el número total de usuarios registrados localmente
     */
    @Query("SELECT COUNT(*) FROM usuarios WHERE activo = 1")
    suspend fun contarUsuarios(): Int

    /**
     * Verifica si hay algún usuario activo
     */
    @Query("SELECT COUNT(*) > 0 FROM usuarios WHERE activo = 1")
    suspend fun hayUsuarioActivo(): Boolean
}
