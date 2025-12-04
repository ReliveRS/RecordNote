package com.proyecto.recordnote.data.mapper

import com.proyecto.recordnote.data.local.entidades.NotaEntity
import com.proyecto.recordnote.data.remote.dto.NotaDto
import com.proyecto.recordnote.domain.model.NotaDomain
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

// ==================== DTO → DOMAIN ====================
fun NotaDto.toDomain(): NotaDomain {
    return NotaDomain(
        id = id,
        titulo = titulo,
        contenido = contenido,
        rutaAudio = audioUrl,
        duracionAudio = duracionAudio,
        esTranscrita = esTranscrita,
        etiquetas = etiquetas,
        colorFondo = colorFondo,
        esFavorita = esFavorita,
        fechaCreacion = LocalDateTime.parse(fechaCreacion, dateFormatter),
        fechaModificacion = LocalDateTime.parse(fechaModificacion, dateFormatter),
        usuarioId = usuarioId,
        sincronizada = true
    )
}

// ==================== DOMAIN → DTO ====================
fun NotaDomain.toDto(): NotaDto {
    return NotaDto(
        id = id,
        titulo = titulo,
        contenido = contenido,
        audioUrl = rutaAudio,
        duracionAudio = duracionAudio,
        esTranscrita = esTranscrita,
        etiquetas = etiquetas,
        colorFondo = colorFondo,
        esFavorita = esFavorita,
        fechaCreacion = fechaCreacion.format(dateFormatter),
        fechaModificacion = fechaModificacion.format(dateFormatter),
        usuarioId = usuarioId
    )
}

// ==================== DTO → ENTITY ====================
fun NotaDto.toEntity(): NotaEntity {
    val fechaCreacionParsed = LocalDateTime.parse(fechaCreacion, dateFormatter)
    val fechaModificacionParsed = LocalDateTime.parse(fechaModificacion, dateFormatter)

    return NotaEntity(
        id = id,
        titulo = titulo,
        contenido = contenido,
        rutaAudio = audioUrl,
        duracionAudio = duracionAudio,
        duracionFormato = null,
        esTranscrita = esTranscrita,
        transcripcionCompleta = null,
        categoriaId = null,
        etiquetas = etiquetas,
        colorFondo = colorFondo,
        esFavorita = esFavorita,
        fechaCreacion = fechaCreacionParsed,
        fechaModificacion = fechaModificacionParsed,
        usuarioId = usuarioId,
        sincronizada = true
    )
}

// ==================== ENTITY → DOMAIN ====================
fun NotaEntity.toDomain(): NotaDomain {
    return NotaDomain(
        id = id,
        titulo = titulo,
        contenido = contenido,
        rutaAudio = rutaAudio,
        duracionAudio = duracionAudio,
        esTranscrita = esTranscrita,
        etiquetas = etiquetas,
        colorFondo = colorFondo,
        esFavorita = esFavorita,
        fechaCreacion = fechaCreacion,
        fechaModificacion = fechaModificacion,
        usuarioId = usuarioId,
        sincronizada = sincronizada
    )
}

// ==================== DOMAIN → ENTITY ====================
fun NotaDomain.toEntity(): NotaEntity {
    return NotaEntity(
        id = id,
        titulo = titulo,
        contenido = contenido,
        rutaAudio = rutaAudio,
        duracionAudio = duracionAudio,
        duracionFormato = null,
        esTranscrita = esTranscrita,
        transcripcionCompleta = null,
        categoriaId = null,
        etiquetas = etiquetas,
        colorFondo = colorFondo,
        esFavorita = esFavorita,
        fechaCreacion = fechaCreacion,
        fechaModificacion = fechaModificacion,
        usuarioId = usuarioId,
        sincronizada = sincronizada
    )
}

// ==================== LISTAS ====================

/**
 * Convierte lista de DTOs a lista de Domain
 */
fun List<NotaDto>.dtoToDomain(): List<NotaDomain> {
    return map { it.toDomain() }
}

/**
 * Convierte lista de Entities a lista de Domain
 */
fun List<NotaEntity>.entityToDomain(): List<NotaDomain> {
    return map { it.toDomain() }
}
