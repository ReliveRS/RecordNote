package com.proyecto.recordnote.domain.model

import java.time.LocalDateTime

/**
 * Clase que encapsula las preferencias y configuraciones del usuario
 */
data class PreferenciasUsuario(
    val temaModo: String = "sistema",
    val calidadAudio: String = "media",
    val transcripcionAutomatica: Boolean = true,
    val sincronizacionAutomatica: Boolean = true,
    val notificacionesActivas: Boolean = true,
    val backupAutomatico: Boolean = true,
    val frecuenciaBackup: Int = 24,
    val idiomaTranscripcion: String = "es"
) {
    /**
     * Obtiene la calidad de audio en formato numérico
     */
    fun obtenerBitrateAudio(): Int {
        return when (calidadAudio) {
            "baja" -> 64
            "media" -> 128
            "alta" -> 256
            else -> 128
        }
    }

    /**
     * Verifica si debe realizar backup
     */
    fun debeRealizarBackup(ultimoBackup: LocalDateTime): Boolean {
        if (!backupAutomatico) return false
        val horasDesdeUltimoBackup = java.time.Duration.between(
            ultimoBackup,
            LocalDateTime.now()
        ).toHours()
        return horasDesdeUltimoBackup >= frecuenciaBackup
    }
}
