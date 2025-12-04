package com.proyecto.recordnote.data.remote.interceptor

/*
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Interceptor personalizado para logging de peticiones HTTP
 * Registra información detallada de requests y responses para debugging
 * Solo debe usarse en builds de desarrollo
 */
@Singleton
class LoggingInterceptor @Inject constructor() : Interceptor {

    companion object {
        private const val TAG = "HTTP"
        private const val MAX_BODY_LENGTH = 1000 // Caracteres máximos del body a mostrar
    }

    /**
     * Intercepta y registra información de la petición y respuesta HTTP
     * @param chain Cadena de interceptores
     * @return Response después de registrar la información
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val startTime = System.currentTimeMillis()

        // Log de la petición
        logRequest(request)

        // Ejecutar la petición
        val response: Response
        try {
            response = chain.proceed(request)
        } catch (e: IOException) {
            // Log de error de red
            Log.e(TAG, "❌ Error de red: ${e.message}")
            throw e
        }

        // Calcular tiempo de respuesta
        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime

        // Log de la respuesta
        logResponse(response, duration)

        return response
    }

    /**
     * Registra información de la petición HTTP
     * @param request Petición a registrar
     */
    private fun logRequest(request: okhttp3.Request) {
        Log.d(TAG, "╔══════════════════════════════════════════════")
        Log.d(TAG, "║ ➡️ REQUEST")
        Log.d(TAG, "║ ${request.method} ${request.url}")

        // Log de headers (excepto sensibles)
        request.headers.forEach { (name, value) ->
            if (name.lowercase() != "authorization") {
                Log.d(TAG, "║ Header: $name = $value")
            } else {
                Log.d(TAG, "║ Header: $name = Bearer ***")
            }
        }

        // Log del body si existe
        request.body?.let { body ->
            try {
                val buffer = okio.Buffer()
                body.writeTo(buffer)
                val bodyString = buffer.readUtf8()

                if (bodyString.length > MAX_BODY_LENGTH) {
                    Log.d(TAG, "║ Body: ${bodyString.take(MAX_BODY_LENGTH)}...")
                    Log.d(TAG, "║ Body truncado (${bodyString.length} caracteres)")
                } else {
                    Log.d(TAG, "║ Body: $bodyString")
                }
            } catch (e: Exception) {
                Log.d(TAG, "║ Body: [No se pudo leer]")
            }
        }

        Log.d(TAG, "╚══════════════════════════════════════════════")
    }

    /**
     * Registra información de la respuesta HTTP
     * @param response Respuesta a registrar
     * @param duration Duración de la petición en milisegundos
     */
    private fun logResponse(response: Response, duration: Long) {
        val emoji = when (response.code) {
            in 200..299 -> "✅" // Éxito
            in 300..399 -> "↪️" // Redirección
            in 400..499 -> "⚠️" // Error del cliente
            in 500..599 -> "❌" // Error del servidor
            else -> "❓"
        }

        Log.d(TAG, "╔══════════════════════════════════════════════")
        Log.d(TAG, "║ $emoji RESPONSE (${duration}ms)")
        Log.d(TAG, "║ ${response.code} ${response.message}")
        Log.d(TAG, "║ URL: ${response.request.url}")

        // Log de headers relevantes
        response.headers.forEach { (name, value) ->
            if (name.lowercase() in listOf("content-type", "content-length")) {
                Log.d(TAG, "║ Header: $name = $value")
            }
        }

        // Log del body si existe y es exitoso
        if (response.isSuccessful) {
            try {
                val responseBody = response.peekBody(MAX_BODY_LENGTH.toLong())
                val bodyString = responseBody.string()

                if (bodyString.isNotBlank()) {
                    Log.d(TAG, "║ Body: $bodyString")
                }
            } catch (e: Exception) {
                Log.d(TAG, "║ Body: [No se pudo leer]")
            }
        } else {
            // Para errores, intentar mostrar el mensaje de error
            try {
                val errorBody = response.peekBody(MAX_BODY_LENGTH.toLong()).string()
                Log.e(TAG, "║ Error Body: $errorBody")
            } catch (e: Exception) {
                Log.e(TAG, "║ Error Body: [No se pudo leer]")
            }
        }

        Log.d(TAG, "╚══════════════════════════════════════════════")
    }
}

/**
 * Factory para crear interceptor de logging según el tipo de build
 * En producción, retorna un interceptor con logging mínimo
 * En desarrollo, retorna logging completo
 */
object LoggingInterceptorFactory {

    /**
     * Crea un interceptor de logging apropiado para el build
     * @param isDebug true si es build de desarrollo
     * @return Interceptor configurado
     */
    fun create(isDebug: Boolean): Interceptor {
        return if (isDebug) {
            // En desarrollo: logging completo personalizado
            LoggingInterceptor()
        } else {
            // En producción: logging básico solo de errores
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.NONE
            }
        }
    }
}*/