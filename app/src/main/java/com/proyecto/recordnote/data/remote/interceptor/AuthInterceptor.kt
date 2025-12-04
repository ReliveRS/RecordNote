package com.proyecto.recordnote.data.remote.interceptor
/*
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Interceptor de OkHttp para agregar token de autenticación a las peticiones
 * Intercepta todas las peticiones HTTP y agrega el header Authorization
 *
 * @property tokenProvider Proveedor del token JWT actual
 */
@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenProvider: TokenProvider
) : Interceptor {

    /**
     * Intercepta la petición HTTP y agrega el token de autenticación
     * @param chain Cadena de interceptores
     * @return Response con el token agregado
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Obtener el token actual de forma síncrona
        val token = runBlocking {
            tokenProvider.getToken()
        }

        // Si no hay token, continuar sin modificar la petición
        // Útil para endpoints públicos como login y registro
        if (token.isNullOrBlank()) {
            return chain.proceed(originalRequest)
        }

        // Agregar el header Authorization con el token JWT
        val authenticatedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        // Continuar con la petición autenticada
        val response = chain.proceed(authenticatedRequest)

        // Si el servidor responde 401 (No autorizado), el token puede haber expirado
        if (response.code == 401) {
            // Aquí podrías implementar lógica de renovación automática del token
            // Por ahora, simplemente retornamos la respuesta
            return response
        }

        return response
    }
}

/**
 * Interfaz para proveer el token de autenticación
 * Permite inyectar diferentes implementaciones según el contexto
 */
interface TokenProvider {
    /**
     * Obtiene el token JWT actual del usuario autenticado
     * @return Token JWT o null si no hay sesión activa
     */
    suspend fun getToken(): String?

    /**
     * Actualiza el token almacenado
     * @param token Nuevo token JWT
     */
    suspend fun setToken(token: String?)

    /**
     * Limpia el token almacenado (al hacer logout)
     */
    suspend fun clearToken()
}

/**
 * Implementación de TokenProvider usando SharedPreferences
 * Almacena el token de forma persistente en el dispositivo
 *
 * @property sharedPreferences Preferencias compartidas de Android
 */
@Singleton
class SharedPreferencesTokenProvider @Inject constructor(
    private val sharedPreferences: android.content.SharedPreferences
) : TokenProvider {

    companion object {
        private const val KEY_AUTH_TOKEN = "auth_token"
    }

    /**
     * Obtiene el token almacenado en SharedPreferences
     * @return Token JWT o null
     */
    override suspend fun getToken(): String? {
        return sharedPreferences.getString(KEY_AUTH_TOKEN, null)
    }

    /**
     * Guarda el token en SharedPreferences
     * @param token Nuevo token JWT
     */
    override suspend fun setToken(token: String?) {
        sharedPreferences.edit()
            .putString(KEY_AUTH_TOKEN, token)
            .apply()
    }

    /**
     * Elimina el token de SharedPreferences
     */
    override suspend fun clearToken() {
        sharedPreferences.edit()
            .remove(KEY_AUTH_TOKEN)
            .apply()
    }
}*/