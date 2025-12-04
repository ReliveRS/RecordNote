package com.proyecto.recordnote.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.proyecto.recordnote.data.local.entidades.UsuarioEntity
import com.proyecto.recordnote.domain.model.PreferenciasUsuario
import com.proyecto.recordnote.domain.model.UsuarioDomain
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Data Transfer Object para usuarios
 * Estructura JSON para transferencia de datos de usuario con el servidor
 */
data class UsuarioDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("foto_perfil")
    val fotoPerfil: String? = null,

    @SerializedName("fecha_registro")
    val fechaRegistro: String,

    @SerializedName("ultimo_acceso")
    val ultimoAcceso: String,

    @SerializedName("preferencias")
    val preferencias: PreferenciasDto
) {
    companion object {
        private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

        fun fromDomain(domain: UsuarioDomain): UsuarioDto {
            return UsuarioDto(
                id = domain.id,
                nombre = domain.nombre,
                email = domain.email,
                fotoPerfil = domain.fotoPerfil,
                fechaRegistro = domain.fechaRegistro.format(dateFormatter),
                ultimoAcceso = domain.ultimoAcceso.format(dateFormatter),
                preferencias = PreferenciasDto.fromDomain(domain.preferencias)
            )
        }

        fun fromEntity(entity: UsuarioEntity): UsuarioDto {
            return UsuarioDto(
                id = entity.id,
                nombre = entity.nombre,
                email = entity.email,
                fotoPerfil = entity.fotoPerfil,
                fechaRegistro = entity.fechaRegistro.format(dateFormatter),
                ultimoAcceso = entity.ultimoAcceso.format(dateFormatter),
                preferencias = PreferenciasDto(
                    temaModo = entity.temaModo,
                    calidadAudio = entity.calidadAudio,
                    transcripcionAutomatica = entity.transcripcionAutomatica,
                    sincronizacionAutomatica = entity.sincronizacionAutomatica,
                    notificacionesActivas = entity.notificacionesActivas,
                    backupAutomatico = entity.backupAutomatico,
                    frecuenciaBackup = entity.frecuenciaBackup,
                    idiomaTranscripcion = entity.idiomaTranscripcion
                )
            )
        }
    }

    fun toDomain(): UsuarioDomain {
        return UsuarioDomain(
            id = id,
            nombre = nombre,
            email = email,
            fotoPerfil = fotoPerfil,
            fechaRegistro = LocalDateTime.parse(fechaRegistro, dateFormatter),
            ultimoAcceso = LocalDateTime.parse(ultimoAcceso, dateFormatter),
            preferencias = preferencias.toDomain()
        )
    }

    fun toEntity(): UsuarioEntity {
        return UsuarioEntity(
            id = id,
            nombre = nombre,
            email = email,
            fotoPerfil = fotoPerfil,
            telefono = null, // Puedes mapear si lo necesitas
            fechaRegistro = LocalDateTime.parse(fechaRegistro, dateFormatter),
            ultimoAcceso = LocalDateTime.parse(ultimoAcceso, dateFormatter),
            activo = true,
            temaModo = preferencias.temaModo,
            calidadAudio = preferencias.calidadAudio,
            transcripcionAutomatica = preferencias.transcripcionAutomatica,
            sincronizacionAutomatica = preferencias.sincronizacionAutomatica,
            notificacionesActivas = preferencias.notificacionesActivas,
            backupAutomatico = preferencias.backupAutomatico,
            frecuenciaBackup = preferencias.frecuenciaBackup,
            idiomaTranscripcion = preferencias.idiomaTranscripcion
        )
    }
}

/**
 * DTO para preferencias de usuario
 */
data class PreferenciasDto(
    @SerializedName("tema_modo")
    val temaModo: String = "sistema",
    @SerializedName("calidad_audio")
    val calidadAudio: String = "media",
    @SerializedName("transcripcion_automatica")
    val transcripcionAutomatica: Boolean = true,
    @SerializedName("sincronizacion_automatica")
    val sincronizacionAutomatica: Boolean = true,
    @SerializedName("notificaciones_activas")
    val notificacionesActivas: Boolean = true,
    @SerializedName("backup_automatico")
    val backupAutomatico: Boolean = true,
    @SerializedName("frecuencia_backup")
    val frecuenciaBackup: Int = 24,
    @SerializedName("idioma_transcripcion")
    val idiomaTranscripcion: String = "es"
) {
    companion object {
        fun fromDomain(domain: PreferenciasUsuario): PreferenciasDto {
            return PreferenciasDto(
                temaModo = domain.temaModo,
                calidadAudio = domain.calidadAudio,
                transcripcionAutomatica = domain.transcripcionAutomatica,
                sincronizacionAutomatica = domain.sincronizacionAutomatica,
                notificacionesActivas = domain.notificacionesActivas,
                backupAutomatico = domain.backupAutomatico,
                frecuenciaBackup = domain.frecuenciaBackup,
                idiomaTranscripcion = domain.idiomaTranscripcion
            )
        }
    }

    fun toDomain(): PreferenciasUsuario {
        return PreferenciasUsuario(
            temaModo = temaModo,
            calidadAudio = calidadAudio,
            transcripcionAutomatica = transcripcionAutomatica,
            sincronizacionAutomatica = sincronizacionAutomatica,
            notificacionesActivas = notificacionesActivas,
            backupAutomatico = backupAutomatico,
            frecuenciaBackup = frecuenciaBackup,
            idiomaTranscripcion = idiomaTranscripcion
        )
    }
}
