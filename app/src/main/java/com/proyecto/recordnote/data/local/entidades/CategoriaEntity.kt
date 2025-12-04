package com.proyecto.recordnote.data.local.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad Room para categorías de notas
 * Representa la tabla 'categorias' en SQLite
 *
 * @property id Clave primaria única de la categoría (UUID)
 * @property nombre Nombre de la categoría (ej: "Trabajo", "Personal")
 * @property color Color en formato hexadecimal (ej: "#FF6200EE")
 * @property icono Nombre del icono (para futuras implementaciones)
 * @property orden Orden de visualización
 * @property cantidadNotas Contador de notas en esta categoría
 */
@Entity(tableName = "categorias")
data class CategoriaEntity(
    @PrimaryKey
    val id: String,
    val nombre: String,
    val color: String,
    val icono: String = "label",
    val orden: Int = 0,
    val cantidadNotas: Int = 0
)
