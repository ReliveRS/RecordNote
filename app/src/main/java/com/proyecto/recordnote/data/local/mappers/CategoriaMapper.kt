package com.proyecto.recordnote.data.local.mappers

import androidx.compose.ui.graphics.Color
import com.proyecto.recordnote.data.local.entidades.CategoriaEntity
import com.proyecto.recordnote.domain.model.Categoria

/**
 * Convierte CategoriaEntity (Room) a Categoria (Domain)
 */
fun CategoriaEntity.toDomain(): Categoria {
    return Categoria(
        id = id,
        nombre = nombre,
        color = Color(android.graphics.Color.parseColor(color)), // Convierte hex a Color
        icono = icono,
        cantidadNotas = cantidadNotas
    )
}

/**
 * Convierte Categoria (Domain) a CategoriaEntity (Room)
 */
fun Categoria.toEntity(): CategoriaEntity {
    return CategoriaEntity(
        id = id,
        nombre = nombre,
        color = String.format("#%08X", color.value.toInt()), // Convierte Color a hex
        icono = icono,
        cantidadNotas = cantidadNotas
    )
}
