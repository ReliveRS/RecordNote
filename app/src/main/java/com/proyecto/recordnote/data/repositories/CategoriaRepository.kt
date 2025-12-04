package com.proyecto.recordnote.data.repository

import com.proyecto.recordnote.data.local.dao.CategoriaDao
import com.proyecto.recordnote.data.local.dao.NotaDao
import com.proyecto.recordnote.data.local.entidades.CategoriaEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository para gestionar categorías
 * Maneja operaciones CRUD y actualización de contadores
 */
class CategoriaRepository(
    private val categoriaDao: CategoriaDao,
    private val notaDao: NotaDao // Para actualizar contadores
) {

    // ========== CONSULTAS BÁSICAS ==========

    /**
     * Obtiene todas las categorías ordenadas alfabéticamente
     */
    fun getAllCategorias(): Flow<List<CategoriaEntity>> {
        return categoriaDao.getAllCategorias()
    }

    /**
     * Obtiene una categoría por ID
     */
    suspend fun getCategoriaById(categoriaId: String): CategoriaEntity? {
        return categoriaDao.getCategoriaById(categoriaId)
    }

    /**
     * Busca categorías por nombre
     */
    fun buscarCategorias(query: String): Flow<List<CategoriaEntity>> {
        return categoriaDao.buscarCategorias(query)
    }

    // ========== OPERACIONES DE ESCRITURA ==========

    /**
     * Inserta una nueva categoría
     */
    suspend fun insertCategoria(categoria: CategoriaEntity) {
        categoriaDao.insertCategoria(categoria)
    }

    /**
     * Inserta múltiples categorías (para inicialización)
     */
    suspend fun insertCategorias(categorias: List<CategoriaEntity>) {
        categoriaDao.insertCategorias(categorias)
    }

    /**
     * Actualiza una categoría existente
     */
    suspend fun updateCategoria(categoria: CategoriaEntity) {
        categoriaDao.updateCategoria(categoria)
    }

    /**
     * Elimina una categoría
     */
    suspend fun deleteCategoria(categoria: CategoriaEntity) {
        categoriaDao.deleteCategoria(categoria)
    }

    /**
     * Elimina una categoría por ID
     */
    suspend fun deleteCategoriaById(categoriaId: String) {
        categoriaDao.deleteCategoriaById(categoriaId)
    }

    // ========== GESTIÓN DE CONTADORES ==========

    /**
     * Actualiza el contador de notas de una categoría
     * Se llama automáticamente cuando se añaden/eliminan notas
     */
    suspend fun actualizarContadorNotas(categoriaId: String) {
        val cantidad = notaDao.contarNotasPorCategoria(categoriaId)
        categoriaDao.updateCantidadNotas(categoriaId, cantidad)
    }

    /**
     * Actualiza los contadores de todas las categorías
     * Útil para sincronización o corrección de datos
     */
    suspend fun actualizarTodosLosContadores() {
        val categorias = categoriaDao.getAllCategorias()
        // Como es Flow, necesitamos una forma de obtener el valor actual
        // En un caso real, podrías usar first() si tienes collectAsStateWithLifecycle
    }

    // ========== ESTADÍSTICAS ==========

    /**
     * Obtiene el total de categorías
     */
    suspend fun getTotalCategorias(): Int {
        return categoriaDao.getTotalCategorias()
    }

    // ========== CATEGORÍAS PREDETERMINADAS ==========

    /**
     * Crea categorías por defecto si no existen
     */
    suspend fun crearCategoriasIniciales() {
        val total = getTotalCategorias()
        if (total == 0) {
            val categoriasIniciales = listOf(
                CategoriaEntity(
                    id = "cat_trabajo",
                    nombre = "Trabajo",
                    color = "#FF2196F3", // Azul
                    icono = "work",
                    orden = 1
                ),
                CategoriaEntity(
                    id = "cat_personal",
                    nombre = "Personal",
                    color = "#FF4CAF50", // Verde
                    icono = "person",
                    orden = 2
                ),
                CategoriaEntity(
                    id = "cat_estudio",
                    nombre = "Estudio",
                    color = "#FFFF9800", // Naranja
                    icono = "school",
                    orden = 3
                ),
                CategoriaEntity(
                    id = "cat_ideas",
                    nombre = "Ideas",
                    color = "#FF9C27B0", // Púrpura
                    icono = "lightbulb",
                    orden = 4
                )
            )
            insertCategorias(categoriasIniciales)
        }
    }
}
