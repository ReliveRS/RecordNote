package com.proyecto.recordnote.domain.usecase.auth

import com.proyecto.recordnote.domain.repository.IAuthRepository
import timber.log.Timber

/**
 * Caso de uso para registro de nuevo usuario
 */
class RegistroUseCase(
    private val authRepository: IAuthRepository
) {
    /**
     * Realiza el registro de un nuevo usuario
     */
    suspend operator fun invoke(
        nombre: String,
        email: String,
        password: String
    ): Result<String> {  // ✅ Retorna Result<String>
        // Validar formato de email
        if (!esEmailValido(email)) {
            return Result.failure(Exception("El formato del email no es válido"))
        }

        // Validar nombre no vacío
        if (nombre.isBlank()) {
            return Result.failure(Exception("El nombre no puede estar vacío"))
        }

        // Validar longitud mínima de contraseña
        if (password.length < 6) {
            return Result.failure(Exception("La contraseña debe tener al menos 6 caracteres"))
        }

        // Verificar que el email no exista
        val emailExiste = authRepository.verificarEmailExiste(email)
        if (emailExiste) {
            return Result.failure(Exception("El email ya está registrado"))
        }

        // Realizar el registro
        return try {
            authRepository.registrar(nombre, email, password)
        } catch (e: Exception) {
            Timber.e(e, "Error en registro")
            Result.failure(e)
        }
    }

    private fun esEmailValido(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return email.matches(emailRegex)
    }
}
