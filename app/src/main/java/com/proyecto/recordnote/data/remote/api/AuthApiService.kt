package com.proyecto.recordnote.data.remote.api


import com.proyecto.recordnote.data.remote.dto.UsuarioDto
import retrofit2.Response
import retrofit2.http.*

/**
 * Servicio API REST para operaciones de autenticación
 * Maneja login, registro y gestión de sesiones
 */
interface AuthApiService {

    // ========== AUTENTICACIÓN ==========

    /**
     * Inicia sesión con email y contraseña
     * @param request Credenciales del usuario
     * @return Response con datos de sesión y token JWT
     */
    @POST("api/v1/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    /**
     * Registra un nuevo usuario en el sistema
     * @param request Datos del nuevo usuario
     * @return Response con usuario creado y token
     */
    @POST("api/v1/auth/registro")
    suspend fun registrar(
        @Body request: RegistroRequest
    ): Response<LoginResponse>

    /**
     * Cierra la sesión del usuario actual
     * Invalida el token en el servidor
     * @return Response indicando éxito
     */
    @POST("api/v1/auth/logout")
    suspend fun logout(): Response<Unit>

    /**
     * Renueva el token de autenticación antes de que expire
     * @param request Token actual a renovar
     * @return Response con nuevo token
     */
    @POST("api/v1/auth/renovar-token")
    suspend fun renovarToken(
        @Body request: RenovarTokenRequest
    ): Response<RenovarTokenResponse>

    // ========== RECUPERACIÓN DE CONTRASEÑA ==========

    /**
     * Solicita un código de recuperación de contraseña
     * Envía email al usuario con código de verificación
     * @param request Email del usuario
     * @return Response indicando éxito
     */
    @POST("api/v1/auth/recuperar-password")
    suspend fun recuperarPassword(
        @Body request: RecuperarPasswordRequest
    ): Response<Unit>

    /**
     * Restablece la contraseña usando el código recibido
     * @param request Email, código y nueva contraseña
     * @return Response indicando éxito
     */
    @POST("api/v1/auth/restablecer-password")
    suspend fun restablecerPassword(
        @Body request: RestablecerPasswordRequest
    ): Response<Unit>

    /**
     * Cambia la contraseña del usuario autenticado
     * Requiere contraseña actual para validación
     * @param request Contraseña actual y nueva
     * @return Response indicando éxito
     */
    @POST("api/v1/auth/cambiar-password")
    suspend fun cambiarPassword(
        @Body request: CambiarPasswordRequest
    ): Response<Unit>

    // ========== GESTIÓN DE PERFIL ==========

    /**
     * Obtiene la información del usuario autenticado
     * @return Response con datos del usuario
     */
    @GET("api/v1/auth/perfil")
    suspend fun obtenerPerfil(): Response<UsuarioDto>

    /**
     * Actualiza la información del perfil
     * @param request Datos actualizados del usuario
     * @return Response con usuario actualizado
     */
    @PUT("api/v1/auth/perfil")
    suspend fun actualizarPerfil(
        @Body request: ActualizarPerfilRequest
    ): Response<UsuarioDto>

    /**
     * Sube o actualiza la foto de perfil
     * @param foto Imagen en formato multipart
     * @return Response con URL de la foto subida
     */
    @Multipart
    @POST("api/v1/auth/foto-perfil")
    suspend fun subirFotoPerfil(
        @Part foto: okhttp3.MultipartBody.Part
    ): Response<FotoPerfilResponse>

    /**
     * Elimina la foto de perfil del usuario
     * @return Response indicando éxito
     */
    @DELETE("api/v1/auth/foto-perfil")
    suspend fun eliminarFotoPerfil(): Response<Unit>

    // ========== VALIDACIONES ==========

    /**
     * Verifica si un email ya está registrado
     * Útil para validación en tiempo real durante el registro
     * @param email Email a verificar
     * @return Response indicando si existe
     */
    @GET("api/v1/auth/verificar-email")
    suspend fun verificarEmail(
        @Query("email") email: String
    ): Response<VerificarEmailResponse>

    // ========== ELIMINACIÓN DE CUENTA ==========

    /**
     * Elimina permanentemente la cuenta del usuario
     * Requiere confirmación con contraseña
     * @param request Contraseña para confirmación
     * @return Response indicando éxito
     */
    @HTTP(method = "DELETE", path = "api/v1/auth/cuenta", hasBody = true)
    suspend fun eliminarCuenta(
        @Body request: EliminarCuentaRequest
    ): Response<Unit>
}

// ========== REQUEST MODELS ==========

/**
 * Request para login
 * @property email Correo electrónico
 * @property password Contraseña
 */
data class LoginRequest(
    val email: String,
    val password: String
)

/**
 * Request para registro de nuevo usuario
 * @property nombre Nombre completo
 * @property email Correo electrónico
 * @property password Contraseña
 */
data class RegistroRequest(
    val nombre: String,
    val email: String,
    val password: String
)

/**
 * Response de login/registro exitoso
 * @property usuario Datos del usuario
 * @property token Token JWT para autenticación
 * @property tokenExpiracion Timestamp de expiración
 */
data class LoginResponse(
    val usuario: UsuarioDto,
    val token: String,
    val tokenExpiracion: String
)

/**
 * Request para renovar token
 * @property token Token actual
 */
data class RenovarTokenRequest(
    val token: String
)

/**
 * Response con nuevo token
 * @property token Nuevo token JWT
 * @property tokenExpiracion Nueva fecha de expiración
 */
data class RenovarTokenResponse(
    val token: String,
    val tokenExpiracion: String
)

/**
 * Request para recuperación de contraseña
 * @property email Email del usuario
 */
data class RecuperarPasswordRequest(
    val email: String
)

/**
 * Request para restablecer contraseña
 * @property email Email del usuario
 * @property codigo Código de verificación recibido
 * @property nuevaPassword Nueva contraseña
 */
data class RestablecerPasswordRequest(
    val email: String,
    val codigo: String,
    val nuevaPassword: String
)

/**
 * Request para cambiar contraseña
 * @property passwordActual Contraseña actual
 * @property nuevaPassword Nueva contraseña
 */
data class CambiarPasswordRequest(
    val passwordActual: String,
    val nuevaPassword: String
)

/**
 * Request para actualizar perfil
 * @property nombre Nuevo nombre (opcional)
 * @property email Nuevo email (opcional)
 */
data class ActualizarPerfilRequest(
    val nombre: String?,
    val email: String?
)

/**
 * Response de subida de foto de perfil
 * @property fotoUrl URL de la foto en el servidor
 */
data class FotoPerfilResponse(
    val fotoUrl: String
)

/**
 * Response de verificación de email
 * @property existe true si el email ya está registrado
 */
data class VerificarEmailResponse(
    val existe: Boolean
)

/**
 * Request para eliminar cuenta
 * @property password Contraseña para confirmación
 */
data class EliminarCuentaRequest(
    val password: String
)