package com.proyecto.recordnote.domain.usecase.auth

import com.proyecto.recordnote.domain.repository.IAuthRepository
import timber.log.Timber

/**
 * Caso de uso para obtener información del usuario autenticado
 */
class ObtenerUsuarioActualUseCase(
    private val authRepository: IAuthRepository
) {
    /**
     * Verifica si hay sesión activa
     */
    suspend fun haySesionActiva(): Boolean {
        return try {
            authRepository.haySesionActiva()
        } catch (e: Exception) {
            Timber.e(e, "Error verificando sesión")
            false
        }
    }
}
