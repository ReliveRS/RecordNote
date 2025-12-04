package com.proyecto.recordnote.domain.model

import java.time.LocalDateTime

/**
 * Modelo de dominio para una nota
 * Representa la entidad principal de negocio sin dependencias de frameworks externos
 *
 * @property id Identificador único de la nota
 * @property titulo Título de la nota
 * @property contenido Contenido textual de la nota
 * @property rutaAudio Ruta opcional al archivo de audio asociado
 * @property duracionAudio Duración del audio en segundos (null si no hay audio)
 * @property esTranscrita Indica si el audio ha sido transcrito a texto
 * @property etiquetas Lista de etiquetas para categorizar la nota
 * @property colorFondo Color de fondo en formato hexadecimal
 * @property esFavorita Indica si la nota está marcada como favorita
 * @property fechaCreacion Fecha y hora de creación de la nota
 * @property fechaModificacion Fecha y hora de última modificación
 * @property usuarioId ID del usuario propietario de la nota
 * @property sincronizada Indica si la nota está sincronizada con el servidor
 */
data class NotaDomain(
    val id: String,
    val titulo: String,
    val contenido: String,
    val rutaAudio: String? = null,
    val duracionAudio: Int? = null,
    val esTranscrita: Boolean = false,
    val etiquetas: List<String> = emptyList(),
    val colorFondo: String = "#FFFFFF",
    val esFavorita: Boolean = false,
    val fechaCreacion: LocalDateTime,
    val fechaModificacion: LocalDateTime,
    val usuarioId: String,
    val sincronizada: Boolean = false
) {
    /**
     * Verifica si la nota tiene contenido de audio
     * @return true si existe una ruta de audio válida
     */
    fun tieneAudio(): Boolean = !rutaAudio.isNullOrBlank()

    /**
     * Verifica si la nota está vacía (sin título ni contenido)
     * @return true si tanto el título como el contenido están vacíos
     */
    fun estaVacia(): Boolean = titulo.isBlank() && contenido.isBlank()

    /**
     * Obtiene la duración del audio formateada como MM:SS
     * @return String con formato de duración o null si no hay audio
     */
    fun obtenerDuracionFormateada(): String? {
        return duracionAudio?.let {
            val minutos = it / 60
            val segundos = it % 60
            String.format("%02d:%02d", minutos, segundos)
        }
    }

    /**
     * Verifica si la nota necesita sincronización
     * @return true si hay cambios pendientes de sincronizar
     */
    fun necesitaSincronizacion(): Boolean = !sincronizada

    /**
     * Obtiene un resumen corto del contenido (primeros 100 caracteres)
     * @return String con el resumen del contenido
     */
    fun obtenerResumen(): String {
        return if (contenido.length > 100) {
            contenido.take(100) + "..."
        } else {
            contenido
        }
    }
}