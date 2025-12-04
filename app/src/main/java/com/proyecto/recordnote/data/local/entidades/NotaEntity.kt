package com.proyecto.recordnote.data.local.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import androidx.room.ForeignKey
import androidx.room.Index
import com.proyecto.recordnote.data.local.database.Converters
import java.time.LocalDateTime

/**
 * Entidad Room para almacenar notas en la base de datos local
 * Representa la tabla 'notas' en SQLite
 *
 * @property id Clave primaria única de la nota (UUID)
 * @property titulo Título de la nota
 * @property contenido Contenido textual resumido de la nota
 * @property rutaAudio Ruta del archivo de audio grabado (nullable)
 * @property duracionAudio Duración en segundos del audio
 * @property duracionFormato Duración formateada (ej: "5:30")
 * @property esTranscrita Indica si el audio fue transcrito con Whisper
 * @property transcripcionCompleta Texto completo de la transcripción de Whisper
 * @property categoriaId ID de la categoría asociada (nullable)
 * @property etiquetas Lista de etiquetas personalizadas
 * @property colorFondo Color en formato hexadecimal
 * @property esFavorita Marcador de favorito (estrella)
 * @property fechaCreacion Timestamp de creación
 * @property fechaModificacion Timestamp de última modificación
 * @property usuarioId ID del usuario propietario
 * @property sincronizada Estado de sincronización con Firebase/servidor
 */
@Entity(
    tableName = "notas",
    foreignKeys = [
        ForeignKey(
            entity = CategoriaEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoriaId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["id"],
            childColumns = ["usuarioId"],
            onDelete = ForeignKey.CASCADE // Si borras usuario, borras sus notas
        )
    ],
    indices = [
        Index(value = ["categoriaId"]),
        Index(value = ["usuarioId"]),
        Index(value = ["esFavorita"]),
        Index(value = ["fechaCreacion"])
    ]
)
@TypeConverters(Converters::class)
data class NotaEntity(
    @PrimaryKey
    val id: String,

    val titulo: String,
    val contenido: String,

    // Audio
    val rutaAudio: String? = null,
    val duracionAudio: Int? = null, // en segundos
    val duracionFormato: String? = null, // "5:30"

    // Transcripción
    val esTranscrita: Boolean = false,
    val transcripcionCompleta: String? = null,

    // Organización
    val categoriaId: String? = null,
    val etiquetas: List<String> = emptyList(),
    val colorFondo: String = "#FFFFFF",
    val esFavorita: Boolean = false,

    // Metadata
    val fechaCreacion: LocalDateTime,
    val fechaModificacion: LocalDateTime,
    val usuarioId: String,
    val sincronizada: Boolean = false
)
