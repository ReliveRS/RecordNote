package com.proyecto.recordnote.domain.model

import java.time.LocalDateTime

/**
 * Modelo de dominio para la sesión del usuario
 * Gestiona la información de autenticación y sesión activa
 *
 * @property usuario Información del usuario autenticado
 * @property token Token JWT para autenticación en el servidor
 * @property tokenExpiracion Fecha de expiración del token
 * @property estaActiva Indica si la sesión está actualmente activa
 * @property dispositivoId Identificador único del dispositivo
 * @property ultimaActividad Fecha de la última actividad registrada
 */
data class SessionUsuario(
    val usuario: UsuarioDomain,
    val token: String,
    val tokenExpiracion: LocalDateTime,
    val estaActiva: Boolean = true,
    val dispositivoId: String,
    val ultimaActividad: LocalDateTime = LocalDateTime.now()
) {
    /**
     * Verifica si el token ha expirado
     * @return true si el token está vencido
     */
    fun tokenExpirado(): Boolean {
        return LocalDateTime.now().isAfter(tokenExpiracion)
    }

    /**
     * Verifica si la sesión es válida (activa y token no expirado)
     * @return true si la sesión puede ser utilizada
     */
    fun esValida(): Boolean {
        return estaActiva && !tokenExpirado()
    }

    /**
     * Calcula el tiempo restante hasta la expiración del token
     * @return Minutos restantes antes de expirar, o 0 si ya expiró
     */
    fun tiempoRestanteMinutos(): Long {
        if (tokenExpirado()) return 0
        return java.time.Duration.between(LocalDateTime.now(), tokenExpiracion).toMinutes()
    }

    /**
     * Verifica si el token está próximo a expirar (menos de 30 minutos)
     * @return true si debe renovarse el token
     */
    fun necesitaRenovacion(): Boolean {
        return tiempoRestanteMinutos() < 30
    }

    /**
     * Crea una copia de la sesión con la última actividad actualizada
     * @return Nueva instancia con timestamp actualizado
     */
    fun actualizarActividad(): SessionUsuario {
        return copy(ultimaActividad = LocalDateTime.now())
    }

    /**
     * Crea una copia de la sesión marcada como inactiva
     * @return Nueva instancia con sesión cerrada
     */
    fun cerrarSesion(): SessionUsuario {
        return copy(estaActiva = false)
    }
}

/**
 * Resultado de operaciones de autenticación
 * Sealed class para manejar diferentes estados de autenticación
 */
sealed class ResultadoAuth {
    /**
     * Autenticación exitosa
     * @property sesion Sesión del usuario autenticado
     */
    data class Exito(val sesion: SessionUsuario) : ResultadoAuth()

    /**
     * Error en la autenticación
     * @property mensaje Descripción del error
     * @property codigo Código de error opcional
     */
    data class Error(val mensaje: String, val codigo: Int? = null) : ResultadoAuth()

    /**
     * Operación en progreso
     */
    object Cargando : ResultadoAuth()

    /**
     * Sesión no encontrada o expirada
     */
    object SesionInvalida : ResultadoAuth()
}