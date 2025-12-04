package com.proyecto.recordnote.data.repositories

import com.proyecto.recordnote.data.local.dao.UsuarioDao
import com.proyecto.recordnote.data.local.entidades.UsuarioEntity
import com.proyecto.recordnote.data.remote.api.AuthApiService
import com.proyecto.recordnote.domain.repository.IAuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.time.LocalDateTime

class AuthRepository(
    private val authApiService: AuthApiService,
    private val usuarioDao: UsuarioDao
) : IAuthRepository {

    override suspend fun login(email: String, password: String): Result<String> =
        withContext(Dispatchers.IO) {
            try {
                Timber.d("Login: $email")
                val token = "mock_${System.currentTimeMillis()}"

                val usuario = UsuarioEntity(
                    id = "usuario_1",
                    nombre = email.split("@")[0],
                    email = email,
                    fechaRegistro = LocalDateTime.now(),
                    ultimoAcceso = LocalDateTime.now(),
                    activo = true
                )
                usuarioDao.insertar(usuario)

                Timber.d("✅ Login ok")
                Result.success(token)
            } catch (e: Exception) {
                Timber.e(e, "Error login")
                Result.failure(e)
            }
        }

    override suspend fun registrar(
        nombre: String,
        email: String,
        password: String
    ): Result<String> =
        withContext(Dispatchers.IO) {
            try {
                if (usuarioDao.obtenerPorEmail(email) != null) {
                    return@withContext Result.failure(Exception("Email existe"))
                }

                val token = "mock_${System.currentTimeMillis()}"
                val usuario = UsuarioEntity(
                    id = "usuario_${System.currentTimeMillis()}",
                    nombre = nombre,
                    email = email,
                    fechaRegistro = LocalDateTime.now(),
                    ultimoAcceso = LocalDateTime.now(),
                    activo = true
                )
                usuarioDao.insertar(usuario)

                Timber.d("✅ Registro ok")
                Result.success(token)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun logout(): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                usuarioDao.desactivarTodos()
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun haySesionActiva(): Boolean =
        withContext(Dispatchers.IO) {
            try {
                usuarioDao.hayUsuarioActivo()
            } catch (e: Exception) {
                false
            }
        }

    override suspend fun renovarToken(tokenActual: String): Result<String> =
        withContext(Dispatchers.IO) {
            try {
                Result.success("mock_renovado_${System.currentTimeMillis()}")
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun recuperarPassword(email: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                if (usuarioDao.obtenerPorEmail(email) == null) {
                    return@withContext Result.failure(Exception("Email no existe"))
                }
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun restablecerPassword(
        email: String,
        codigo: String,
        nuevaPassword: String
    ): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun cambiarPassword(
        passwordActual: String,
        nuevaPassword: String
    ): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun verificarEmailExiste(email: String): Boolean =
        withContext(Dispatchers.IO) {
            try {
                usuarioDao.existeEmail(email)
            } catch (e: Exception) {
                false
            }
        }

    override suspend fun guardarSesionLocal(token: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun eliminarSesionLocal(): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                usuarioDao.desactivarTodos()
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}
