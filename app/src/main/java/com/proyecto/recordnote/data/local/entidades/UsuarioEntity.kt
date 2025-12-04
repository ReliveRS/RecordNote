package com.proyecto.recordnote.data.local.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.proyecto.recordnote.data.local.database.Converters
import java.time.LocalDateTime

/**
 * Entidad Room para almacenar usuarios en la base de datos local
 * Representa la tabla 'usuarios' en SQLite
 *
 * @property id Clave primaria única del usuario (UUID)
 * @property nombre Nombre completo del usuario
 * @property email Correo electrónico único
 * @property fotoPerfil URL o ruta de la foto de perfil
 * @property telefono Número de teléfono (opcional)
 * @property fechaRegistro Fecha de registro en la app
 * @property ultimoAcceso Última vez que accedió
 * @property activo Si el usuario está activo o eliminado
 *
 * Preferencias de usuario:
 * @property temaModo Preferencia de tema ("claro", "oscuro", "sistema")
 * @property calidadAudio Calidad de grabación ("baja", "media", "alta")
 * @property transcripcionAutomatica Auto-transcribir audios con Whisper
 * @property sincronizacionAutomatica Auto-sincronizar con Firebase/servidor
 * @property notificacionesActivas Estado de notificaciones push
 * @property backupAutomatico Backup automático activado
 * @property frecuenciaBackup Frecuencia en horas del backup
 * @property idiomaTranscripcion Código de idioma para transcripción ("es", "en", "fr", etc.)
 */
@Entity(tableName = "usuarios")
@TypeConverters(Converters::class)
data class UsuarioEntity(
    @PrimaryKey
    val id: String,

    // Información personal
    val nombre: String,
    val email: String,
    val fotoPerfil: String? = null,
    val telefono: String? = null,

    // Metadata
    val fechaRegistro: LocalDateTime,
    val ultimoAcceso: LocalDateTime,
    val activo: Boolean = true,

    // Preferencias de la app
    val temaModo: String = "sistema", // "claro", "oscuro", "sistema"
    val calidadAudio: String = "media", // "baja", "media", "alta"
    val transcripcionAutomatica: Boolean = true,
    val sincronizacionAutomatica: Boolean = true,
    val notificacionesActivas: Boolean = true,
    val backupAutomatico: Boolean = true,
    val frecuenciaBackup: Int = 24, // horas
    val idiomaTranscripcion: String = "es" // código ISO de idioma
)
