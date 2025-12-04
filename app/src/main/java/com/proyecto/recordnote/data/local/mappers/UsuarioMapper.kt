package com.proyecto.recordnote.data.local.mappers

import com.proyecto.recordnote.data.local.entidades.UsuarioEntity
import com.proyecto.recordnote.domain.model.PreferenciasUsuario
import com.proyecto.recordnote.domain.model.UsuarioDomain

/**
 * Convierte UsuarioEntity (Room) a UsuarioDomain (Domain)
 */
fun UsuarioEntity.toDomain(): UsuarioDomain {
    return UsuarioDomain(
        id = id,
        nombre = nombre,
        email = email,
        fotoPerfil = fotoPerfil,
        fechaRegistro = fechaRegistro,
        ultimoAcceso = ultimoAcceso,
        preferencias = PreferenciasUsuario(
            temaModo = temaModo,
            calidadAudio = calidadAudio,
            transcripcionAutomatica = transcripcionAutomatica,
            sincronizacionAutomatica = sincronizacionAutomatica,
            notificacionesActivas = notificacionesActivas,
            backupAutomatico = backupAutomatico,
            frecuenciaBackup = frecuenciaBackup,
            idiomaTranscripcion = idiomaTranscripcion
        )
    )
}

/**
 * Convierte UsuarioDomain (Domain) a UsuarioEntity (Room)
 */
fun UsuarioDomain.toEntity(): UsuarioEntity {
    return UsuarioEntity(
        id = id,
        nombre = nombre,
        email = email,
        fotoPerfil = fotoPerfil,
        telefono = null, // Se puede añadir después si lo necesitas
        fechaRegistro = fechaRegistro,
        ultimoAcceso = ultimoAcceso,
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
