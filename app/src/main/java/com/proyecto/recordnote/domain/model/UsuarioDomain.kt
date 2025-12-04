package com.proyecto.recordnote.domain.model

import java.time.LocalDateTime

/**
 * Modelo de dominio para un usuario
 */
data class UsuarioDomain(
    val id: String,
    val nombre: String,
    val email: String,
    val fotoPerfil: String? = null,
    val fechaRegistro: LocalDateTime,
    val ultimoAcceso: LocalDateTime,
    val preferencias: PreferenciasUsuario = PreferenciasUsuario()
) {
    /**
     * Verifica si el usuario tiene una foto de perfil configurada
     */
    fun tieneFotoPerfil(): Boolean = !fotoPerfil.isNullOrBlank()

    /**
     * Obtiene las iniciales del nombre del usuario
     */
    fun obtenerIniciales(): String {
        return nombre.split(" ")
            .mapNotNull { it.firstOrNull()?.uppercase() }
            .take(2)
            .joinToString("")
    }
}
