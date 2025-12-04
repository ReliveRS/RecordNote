// domain/repository/INotaRepository.kt
package com.proyecto.recordnote.domain.repository

import com.proyecto.recordnote.domain.model.NotaDomain
import kotlinx.coroutines.flow.Flow

interface INotaRepository {

    // ========== OPERACIONES BÁSICAS ==========

    /**
     * Obtiene todas las notas del servidor o base de datos local
     */
    suspend fun obtenerTodasLasNotas(): Result<List<NotaDomain>>

    /**
     * Obtiene una nota específica por ID
     */
    suspend fun obtenerNotaPorId(id: String): Result<NotaDomain>

    /**
     * Crea una nueva nota (guarda localmente y sincroniza)
     */
    suspend fun crearNota(nota: NotaDomain): Result<NotaDomain>

    /**
     * Actualiza una nota existente
     */
    suspend fun actualizarNota(nota: NotaDomain): Result<NotaDomain>

    /**
     * Elimina una nota
     */
    suspend fun eliminarNota(id: String): Result<Unit>

    // ========== BÚSQUEDA Y FILTRADO ==========

    /**
     * Busca notas por texto
     */
    suspend fun buscarNotas(query: String): Result<List<NotaDomain>>

    /**
     * Obtiene notas por etiqueta
     */
    suspend fun obtenerPorEtiqueta(etiqueta: String): Result<List<NotaDomain>>

    /**
     * Obtiene solo las notas favoritas
     */
    suspend fun obtenerFavoritas(): Result<List<NotaDomain>>

    /**
     * Obtiene notas con audio
     */
    suspend fun obtenerConAudio(): Result<List<NotaDomain>>

    /**
     * Obtiene notas transcritas
     */
    suspend fun obtenerTranscritas(): Result<List<NotaDomain>>

    // ========== ACTUALIZACIONES ESPECÍFICAS ==========

    /**
     * Marca/desmarca una nota como favorita
     */
    suspend fun actualizarFavorito(notaId: String, esFavorita: Boolean): Result<Unit>

    /**
     * Actualiza la transcripción de una nota
     */
    suspend fun actualizarTranscripcion(
        notaId: String,
        esTranscrita: Boolean,
        transcripcion: String?
    ): Result<Unit>

    /**
     * Actualiza la categoría de una nota
     */
    suspend fun actualizarCategoria(notaId: String, categoriaId: String?): Result<Unit>

    // ========== SINCRONIZACIÓN ==========

    /**
     * Sincroniza las notas locales no sincronizadas con el servidor
     */
    suspend fun sincronizarConServidor(): Result<Unit>

    /**
     * Obtiene notas que aún no están sincronizadas
     */
    suspend fun obtenerNoSincronizadas(): Result<List<NotaDomain>>
}
