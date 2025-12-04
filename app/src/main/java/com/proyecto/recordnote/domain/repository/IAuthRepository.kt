// domain/repository/IAuthRepository.kt
package com.proyecto.recordnote.domain.repository

import com.proyecto.recordnote.domain.model.ResultadoAuth
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz que define el contrato para las operaciones de autenticación
 */
interface IAuthRepository {

    suspend fun login(email: String, password: String): Result<String>

    suspend fun registrar(nombre: String, email: String, password: String): Result<String>

    suspend fun logout(): Result<Unit>

    suspend fun haySesionActiva(): Boolean

    suspend fun renovarToken(tokenActual: String): Result<String>

    suspend fun recuperarPassword(email: String): Result<Unit>

    suspend fun restablecerPassword(
        email: String,
        codigo: String,
        nuevaPassword: String
    ): Result<Unit>

    suspend fun cambiarPassword(
        passwordActual: String,
        nuevaPassword: String
    ): Result<Unit>

    suspend fun verificarEmailExiste(email: String): Boolean

    suspend fun guardarSesionLocal(token: String): Result<Unit>

    suspend fun eliminarSesionLocal(): Result<Unit>
}
