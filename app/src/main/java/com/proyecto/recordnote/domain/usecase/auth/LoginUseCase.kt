package com.proyecto.recordnote.domain.usecase.auth

import com.proyecto.recordnote.domain.repository.IAuthRepository
import timber.log.Timber

/**
 * Caso de uso para realizar el login de un usuario
 */
class LoginUseCase(
    private val authRepository: IAuthRepository
) {
    /**
     * Ejecuta el proceso de login
     */
    suspend operator fun invoke(email: String, password: String): Result<String> {
        // Validar formato de email
        if (!esEmailValido(email)) {
            return Result.failure(Exception("El formato del email no es válido"))
        }

        // Validar que la contraseña no esté vacía
        if (password.isBlank()) {
            return Result.failure(Exception("La contraseña no puede estar vacía"))
        }

        // Validar longitud mínima de contraseña
        if (password.length < 6) {
            return Result.failure(Exception("La contraseña debe tener al menos 6 caracteres"))
        }

        // Realizar el login a través del repositorio
        return try {
            val resultado = authRepository.login(email.trim(), password)
            resultado.onSuccess { token ->
                Timber.d("✅ Login exitoso")
            }.onFailure { exception ->
                Timber.e(exception, "❌ Error en login")
            }
            resultado
        } catch (e: Exception) {
            Timber.e(e, "Excepción en login")
            Result.failure(e)
        }
    }

    /**
     * Valida el formato del email usando expresión regular
     */
    private fun esEmailValido(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return email.matches(emailRegex)
    }
}
