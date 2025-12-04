package com.proyecto.recordnote.domain.model

import androidx.compose.ui.graphics.Color

/**
 * Modelo de dominio para categorías de notas
 * Representa una categoría en la lógica de negocio de la app
 *
 * @property id Identificador único de la categoría (UUID)
 * @property nombre Nombre de la categoría (ej: "Trabajo", "Personal", "Estudio")
 * @property color Color de la categoría en Compose Color
 * @property icono Nombre del icono para mostrar (para futuras implementaciones)
 * @property cantidadNotas Número de notas asociadas a esta categoría
 */
data class Categoria(
    val id: String,
    val nombre: String,
    val color: Color,
    val icono: String = "label",
    val cantidadNotas: Int = 0
)
