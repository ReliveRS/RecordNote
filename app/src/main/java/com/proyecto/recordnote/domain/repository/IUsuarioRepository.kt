package com.proyecto.recordnote.domain.repository

import com.proyecto.recordnote.domain.model.PreferenciasUsuario
import com.proyecto.recordnote.domain.model.UsuarioDomain

import kotlinx.coroutines.flow.Flow

/**
 * Interfaz que define el contrato para las operaciones con usuarios
 * Gestiona la información del perfil y preferencias del usuario
 */
interface IUsuarioRepository {

    /**
     * Obtiene la información del usuario actual
     * @param usuarioId ID del usuario
     * @return Flow con los datos del usuario que se actualiza automáticamente
     */
    fun obtenerUsuario(usuarioId: String): Flow<UsuarioDomain?>

    /**
     * Actualiza la información del perfil del usuario
     * @param usuario Datos actualizados del usuario
     * @return Result con el usuario actualizado o un error
     */
    suspend fun actualizarPerfil(usuario: UsuarioDomain): Result<UsuarioDomain>

    /**
     * Actualiza solo las preferencias del usuario
     * @param usuarioId ID del usuario
     * @param preferencias Nuevas preferencias
     * @return Result indicando éxito o fallo
     */
    suspend fun actualizarPreferencias(
        usuarioId: String,
        preferencias: PreferenciasUsuario
    ): Result<Unit>

    /**
     * Obtiene las preferencias actuales del usuario
     * @param usuarioId ID del usuario
     * @return Preferencias del usuario o valores por defecto
     */
    suspend fun obtenerPreferencias(usuarioId: String): PreferenciasUsuario

    /**
     * Actualiza la foto de perfil del usuario
     * @param usuarioId ID del usuario
     * @param rutaFoto Ruta local o URL de la nueva foto
     * @return Result con la URL de la foto subida
     */
    suspend fun actualizarFotoPerfil(usuarioId: String, rutaFoto: String): Result<String>

    /**
     * Elimina la foto de perfil del usuario
     * @param usuarioId ID del usuario
     * @return Result indicando éxito o fallo
     */
    suspend fun eliminarFotoPerfil(usuarioId: String): Result<Unit>

    /**
     * Actualiza el timestamp del último acceso del usuario
     * @param usuarioId ID del usuario
     * @return Result indicando éxito o fallo
     */
    suspend fun registrarAcceso(usuarioId: String): Result<Unit>

    /**
     * Obtiene estadísticas de uso del usuario
     * @param usuarioId ID del usuario
     * @return Mapa con estadísticas (total_notas, total_audios, etc.)
     */
    suspend fun obtenerEstadisticas(usuarioId: String): Map<String, Any>

    /**
     * Elimina permanentemente la cuenta del usuario y todos sus datos
     * @param usuarioId ID del usuario
     * @return Result indicando éxito o fallo
     */
    suspend fun eliminarCuenta(usuarioId: String): Result<Unit>

    /**
     * Sincroniza los datos del usuario con el servidor
     * @param usuarioId ID del usuario
     * @return Result indicando éxito o fallo
     */
    suspend fun sincronizarDatos(usuarioId: String): Result<Unit>
}
