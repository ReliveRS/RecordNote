package com.proyecto.recordnote.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object para notas
 * Representa la estructura JSON que viaja entre cliente y servidor
 */
data class NotaDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("titulo")
    val titulo: String,

    @SerializedName("contenido")
    val contenido: String,

    @SerializedName("audio_url")
    val audioUrl: String? = null,

    @SerializedName("duracion_audio")
    val duracionAudio: Int? = null,

    @SerializedName("es_transcrita")
    val esTranscrita: Boolean = false,

    @SerializedName("etiquetas")
    val etiquetas: List<String> = emptyList(),

    @SerializedName("color_fondo")
    val colorFondo: String = "#FFFFFF",

    @SerializedName("es_favorita")
    val esFavorita: Boolean = false,

    @SerializedName("fecha_creacion")
    val fechaCreacion: String,

    @SerializedName("fecha_modificacion")
    val fechaModificacion: String,

    @SerializedName("usuario_id")
    val usuarioId: String
)
