package com.proyecto.recordnote.data.local.dao

import androidx.room.*
import com.proyecto.recordnote.data.local.entidades.NotaEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

/**
 * Data Access Object para operaciones con notas en Room
 * Proporciona métodos optimizados para CRUD y consultas
 */
@Dao
interface NotaDao {

    // ========== OPERACIONES BÁSICAS ==========

    /**
     * Inserta una nueva nota o la reemplaza si ya existe
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarNota(nota: NotaEntity)

    /**
     * Inserta múltiples notas en una sola transacción
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarNotas(notas: List<NotaEntity>)

    /**
     * Actualiza una nota existente
     */
    @Update
    suspend fun actualizarNota(nota: NotaEntity)

    /**
     * Elimina una nota por su ID
     */
    @Query("DELETE FROM notas WHERE id = :notaId")
    suspend fun eliminarNota(notaId: String)

    // ========== CONSULTAS BÁSICAS ==========

    /**
     * Obtiene todas las notas ordenadas por fecha de modificación
     */
    @Query("SELECT * FROM notas ORDER BY fechaModificacion DESC")
    suspend fun obtenerTodasLasNotas(): List<NotaEntity>

    /**
     * Obtiene todas las notas como Flow para observar cambios en tiempo real
     */
    @Query("SELECT * FROM notas ORDER BY fechaModificacion DESC")
    fun obtenerTodasLasNotasFlow(): Flow<List<NotaEntity>>

    /**
     * Obtiene una nota específica por su ID
     */
    @Query("SELECT * FROM notas WHERE id = :notaId")
    suspend fun obtenerNotaPorId(notaId: String): NotaEntity?

    /**
     * Obtiene el total de notas
     */
    @Query("SELECT COUNT(*) FROM notas")
    suspend fun contarNotas(): Int

    // ========== BÚSQUEDA Y FILTRADO ==========

    /**
     * Busca notas por texto en título y contenido
     */
    @Query("""
        SELECT * FROM notas 
        WHERE (titulo LIKE '%' || :query || '%' 
             OR contenido LIKE '%' || :query || '%'
             OR transcripcionCompleta LIKE '%' || :query || '%')
        ORDER BY fechaModificacion DESC
    """)
    suspend fun buscar(query: String): List<NotaEntity>

    /**
     * Obtiene notas que contienen una etiqueta específica
     */
    @Query("""
        SELECT * FROM notas 
        WHERE etiquetas LIKE '%' || :etiqueta || '%'
        ORDER BY fechaModificacion DESC
    """)
    suspend fun obtenerPorEtiqueta(etiqueta: String): List<NotaEntity>

    /**
     * Obtiene notas por categoría
     */
    @Query("SELECT * FROM notas WHERE categoriaId = :categoriaId ORDER BY fechaModificacion DESC")
    suspend fun obtenerPorCategoria(categoriaId: String): List<NotaEntity>

    /**
     * Obtiene solo las notas marcadas como favoritas
     */
    @Query("SELECT * FROM notas WHERE esFavorita = 1 ORDER BY fechaModificacion DESC")
    suspend fun obtenerFavoritas(): List<NotaEntity>

    /**
     * Obtiene notas que tienen audio adjunto
     */
    @Query("SELECT * FROM notas WHERE rutaAudio IS NOT NULL ORDER BY fechaModificacion DESC")
    suspend fun obtenerConAudio(): List<NotaEntity>

    /**
     * Obtiene notas que han sido transcritas
     */
    @Query("SELECT * FROM notas WHERE esTranscrita = 1 ORDER BY fechaModificacion DESC")
    suspend fun obtenerTranscritas(): List<NotaEntity>

    // ========== SINCRONIZACIÓN ==========

    /**
     * Obtiene notas que necesitan sincronización con el servidor
     */
    @Query("SELECT * FROM notas WHERE sincronizada = 0")
    suspend fun obtenerNotasNoSincronizadas(): List<NotaEntity>

    /**
     * Marca una nota como sincronizada
     */
    @Query("UPDATE notas SET sincronizada = 1 WHERE id = :notaId")
    suspend fun marcarComoSincronizada(notaId: String)

    /**
     * Marca múltiples notas como sincronizadas
     */
    @Query("UPDATE notas SET sincronizada = 1 WHERE id IN (:notasIds)")
    suspend fun marcarVariasComoSincronizadas(notasIds: List<String>)

    // ========== ACTUALIZACIONES ESPECÍFICAS ==========

    /**
     * Actualiza solo el estado de favorito de una nota
     */
    @Query("UPDATE notas SET esFavorita = :esFavorita, sincronizada = 0 WHERE id = :notaId")
    suspend fun actualizarFavorito(notaId: String, esFavorita: Boolean)

    /**
     * Actualiza el contenido de una nota
     */
    @Query("UPDATE notas SET contenido = :contenido, fechaModificacion = :fechaModificacion, sincronizada = 0 WHERE id = :notaId")
    suspend fun actualizarContenido(
        notaId: String,
        contenido: String,
        fechaModificacion: LocalDateTime
    )

    /**
     * Actualiza el estado de transcripción
     */
    @Query("UPDATE notas SET esTranscrita = :esTranscrita, transcripcionCompleta = :transcripcion, sincronizada = 0 WHERE id = :notaId")
    suspend fun actualizarTranscripcion(
        notaId: String,
        esTranscrita: Boolean,
        transcripcion: String?
    )

    /**
     * Actualiza la categoría de una nota
     */
    @Query("UPDATE notas SET categoriaId = :categoriaId, sincronizada = 0 WHERE id = :notaId")
    suspend fun actualizarCategoria(notaId: String, categoriaId: String?)

    /**
     * Actualiza el título de una nota
     */
    @Query("UPDATE notas SET titulo = :titulo, fechaModificacion = :fechaModificacion, sincronizada = 0 WHERE id = :notaId")
    suspend fun actualizarTitulo(
        notaId: String,
        titulo: String,
        fechaModificacion: LocalDateTime
    )

    /**
     * Actualiza la ruta del audio
     */
    @Query("UPDATE notas SET rutaAudio = :rutaAudio, duracionAudio = :duracion, sincronizada = 0 WHERE id = :notaId")
    suspend fun actualizarAudio(notaId: String, rutaAudio: String, duracion: Int)

    /**
     * Actualiza el color de fondo
     */
    @Query("UPDATE notas SET colorFondo = :color, sincronizada = 0 WHERE id = :notaId")
    suspend fun actualizarColor(notaId: String, color: String)

    /**
     * Actualiza las etiquetas
     */
    @Query("UPDATE notas SET etiquetas = :etiquetas, sincronizada = 0 WHERE id = :notaId")
    suspend fun actualizarEtiquetas(notaId: String, etiquetas: String)

    // ========== ORDENAMIENTO ==========

    /**
     * Obtiene notas ordenadas por fecha de creación (más recientes primero)
     */
    @Query("SELECT * FROM notas ORDER BY fechaCreacion DESC")
    suspend fun obtenerOrdenadasPorCreacion(): List<NotaEntity>

    /**
     * Obtiene notas ordenadas alfabéticamente por título
     */
    @Query("SELECT * FROM notas ORDER BY titulo ASC")
    suspend fun obtenerOrdenadasPorTitulo(): List<NotaEntity>

    /**
     * Obtiene como Flow ordenadas por creación
     */
    @Query("SELECT * FROM notas ORDER BY fechaCreacion DESC")
    fun obtenerOrdenadasPorCreacionFlow(): Flow<List<NotaEntity>>

    /**
     * Obtiene como Flow ordenadas por título
     */
    @Query("SELECT * FROM notas ORDER BY titulo ASC")
    fun obtenerOrdenadasPorTituloFlow(): Flow<List<NotaEntity>>

    // ========== ESTADÍSTICAS ==========

    /**
     * Cuenta notas con audio
     */
    @Query("SELECT COUNT(*) FROM notas WHERE rutaAudio IS NOT NULL")
    suspend fun contarNotasConAudio(): Int

    /**
     * Cuenta notas favoritas
     */
    @Query("SELECT COUNT(*) FROM notas WHERE esFavorita = 1")
    suspend fun contarFavoritas(): Int

    /**
     * Obtiene la duración total de todos los audios en segundos
     */
    @Query("SELECT SUM(duracionAudio) FROM notas WHERE duracionAudio IS NOT NULL")
    suspend fun obtenerDuracionTotalAudios(): Int?

    /**
     * Cuenta notas por categoría
     */
    @Query("SELECT COUNT(*) FROM notas WHERE categoriaId = :categoriaId")
    suspend fun contarNotasPorCategoria(categoriaId: String): Int

    /**
     * Cuenta notas no sincronizadas
     */
    @Query("SELECT COUNT(*) FROM notas WHERE sincronizada = 0")
    suspend fun contarNoSincronizadas(): Int

    /**
     * Cuenta notas transcritas
     */
    @Query("SELECT COUNT(*) FROM notas WHERE esTranscrita = 1")
    suspend fun contarTranscritas(): Int

    // ========== ELIMINACIÓN MASIVA ==========

    /**
     * Elimina todas las notas
     */
    @Query("DELETE FROM notas")
    suspend fun eliminarTodas()

    /**
     * Elimina notas por lista de IDs
     */
    @Query("DELETE FROM notas WHERE id IN (:notasIds)")
    suspend fun eliminarVarias(notasIds: List<String>)

    /**
     * Elimina notas antiguas basadas en fecha
     */
    @Query("DELETE FROM notas WHERE fechaModificacion < :fechaLimite")
    suspend fun eliminarAnterioresA(fechaLimite: LocalDateTime)

    /**
     * Elimina todas las notas no sincronizadas
     */
    @Query("DELETE FROM notas WHERE sincronizada = 0")
    suspend fun eliminarNoSincronizadas()

    // ========== MÉTODOS FLOW (Para observar cambios en tiempo real) ==========

    /**
     * Obtiene notas favoritas como Flow
     */
    @Query("SELECT * FROM notas WHERE esFavorita = 1 ORDER BY fechaModificacion DESC")
    fun obtenerFavoritasFlow(): Flow<List<NotaEntity>>

    /**
     * Obtiene notas por categoría como Flow
     */
    @Query("SELECT * FROM notas WHERE categoriaId = :categoriaId ORDER BY fechaModificacion DESC")
    fun obtenerPorCategoriaFlow(categoriaId: String): Flow<List<NotaEntity>>

    /**
     * Busca notas como Flow
     */
    @Query("""
        SELECT * FROM notas 
        WHERE (titulo LIKE '%' || :query || '%' 
             OR contenido LIKE '%' || :query || '%'
             OR transcripcionCompleta LIKE '%' || :query || '%')
        ORDER BY fechaModificacion DESC
    """)
    fun buscarFlow(query: String): Flow<List<NotaEntity>>

    /**
     * Obtiene notas con audio como Flow
     */
    @Query("SELECT * FROM notas WHERE rutaAudio IS NOT NULL ORDER BY fechaModificacion DESC")
    fun obtenerConAudioFlow(): Flow<List<NotaEntity>>

    /**
     * Obtiene notas transcritas como Flow
     */
    @Query("SELECT * FROM notas WHERE esTranscrita = 1 ORDER BY fechaModificacion DESC")
    fun obtenerTranscritasFlow(): Flow<List<NotaEntity>>
    /**
     * Obtiene las últimas N notas (más recientes primero)
     */
    @Query("SELECT * FROM notas ORDER BY fechaModificacion DESC LIMIT :cantidad")
    suspend fun obtenerUltimas(cantidad: Int): List<NotaEntity>
}
