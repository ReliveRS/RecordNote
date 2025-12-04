package com.proyecto.recordnote.data.repositories

import com.proyecto.recordnote.data.local.dao.UsuarioDao
import com.proyecto.recordnote.data.local.entidades.UsuarioEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import timber.log.Timber

/**
 * Repository para gestionar usuarios
 * Maneja la información del perfil y preferencias
 */
class UsuarioRepository(
    private val usuarioDao: UsuarioDao
) {

    // ========== CONSULTAS BÁSICAS ==========

    suspend fun obtenerPorIdSync(usuarioId: String): UsuarioEntity? {
        return usuarioDao.obtenerPorIdSync(usuarioId)
    }

    suspend fun obtenerPorEmail(email: String): UsuarioEntity? {
        return usuarioDao.obtenerPorEmail(email)
    }

    suspend fun existeEmail(email: String): Boolean {
        return usuarioDao.existeEmail(email)
    }

    // ========== OPERACIONES DE ESCRITURA ==========

    suspend fun insertar(usuario: UsuarioEntity) {
        usuarioDao.insertar(usuario)
    }

    suspend fun actualizar(usuario: UsuarioEntity) {
        usuarioDao.actualizar(usuario)
    }

    suspend fun eliminarTodos() {
        usuarioDao.eliminarTodos()
    }

    // ========== GESTIÓN DE SESIÓN ==========

    suspend fun establecerUsuarioActivo(usuarioId: String) {
        usuarioDao.establecerUsuarioActivo(usuarioId)
        actualizarUltimoAcceso(usuarioId)
    }

    suspend fun desactivarTodos() {
        usuarioDao.desactivarTodos()
    }

    suspend fun hayUsuarioActivo(): Boolean {
        return usuarioDao.hayUsuarioActivo()
    }

    // ========== ACTUALIZACIONES ESPECÍFICAS ==========

    suspend fun actualizarUltimoAcceso(usuarioId: String) {
        usuarioDao.actualizarUltimoAcceso(usuarioId, LocalDateTime.now())
    }

    suspend fun actualizarNombre(usuarioId: String, nombre: String) {
        usuarioDao.actualizarNombre(usuarioId, nombre)
    }

    suspend fun actualizarEmail(usuarioId: String, email: String) {
        usuarioDao.actualizarEmail(usuarioId, email)
    }

    suspend fun actualizarFotoPerfil(usuarioId: String, fotoPerfil: String?) {
        usuarioDao.actualizarFotoPerfil(usuarioId, fotoPerfil)
    }

    // ========== USUARIO INICIAL ==========

    suspend fun crearUsuarioInicial(): String {
        val usuarioId = "usuario_local_001"
        val usuarioExistente = obtenerPorIdSync(usuarioId)

        if (usuarioExistente == null) {
            val usuarioInicial = UsuarioEntity(
                id = usuarioId,
                nombre = "Usuario Local",
                email = "local@recordnote.com",
                fechaRegistro = LocalDateTime.now(),
                ultimoAcceso = LocalDateTime.now(),
                activo = true
            )
            insertar(usuarioInicial)
            Timber.d("✅ Usuario inicial creado")
        }

        return usuarioId
    }

    // ========== ESTADÍSTICAS ==========

    suspend fun contarUsuarios(): Int {
        return usuarioDao.contarUsuarios()
    }
}
