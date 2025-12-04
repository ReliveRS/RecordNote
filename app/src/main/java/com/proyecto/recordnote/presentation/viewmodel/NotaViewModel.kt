package com.proyecto.recordnote.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class NotaViewModel : ViewModel() {

    fun crearNota(
        titulo: String,
        descripcion: String,
        contenido: String
    ) {
        // Por ahora solo imprimimos en log
        // Después conectaremos con la BD
        println("✅ Nota creada:")
        println("  Título: $titulo")
        println("  Descripción: $descripcion")
        println("  Contenido: $contenido")
        println("  Fecha: ${LocalDateTime.now()}")
    }

    fun obtenerNotas() {
        // Implementar después
        println("Obteniendo notas...")
    }

    fun eliminarNota(notaId: String) {
        println("Eliminando nota: $notaId")
    }

    fun actualizarNota(notaId: String, titulo: String, descripcion: String) {
        println("Actualizando nota: $notaId")
    }
}
