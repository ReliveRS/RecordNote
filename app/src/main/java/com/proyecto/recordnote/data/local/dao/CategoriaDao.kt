package com.proyecto.recordnote.data.local.dao

import androidx.room.*
import com.proyecto.recordnote.data.local.entidades.CategoriaEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones con categorías en la base de datos
 */
@Dao
interface CategoriaDao {

    /**
     * Obtiene todas las categorías ordenadas alfabéticamente
     */
    @Query("SELECT * FROM categorias ORDER BY nombre ASC")
    fun getAllCategorias(): Flow<List<CategoriaEntity>>

    /**
     * Obtiene una categoría por su ID
     */
    @Query("SELECT * FROM categorias WHERE id = :categoriaId")
    suspend fun getCategoriaById(categoriaId: String): CategoriaEntity?

    /**
     * Inserta una nueva categoría
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategoria(categoria: CategoriaEntity)

    /**
     * Inserta múltiples categorías
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategorias(categorias: List<CategoriaEntity>)

    /**
     * Actualiza una categoría existente
     */
    @Update
    suspend fun updateCategoria(categoria: CategoriaEntity)

    /**
     * Elimina una categoría
     */
    @Delete
    suspend fun deleteCategoria(categoria: CategoriaEntity)

    /**
     * Elimina una categoría por ID
     */
    @Query("DELETE FROM categorias WHERE id = :categoriaId")
    suspend fun deleteCategoriaById(categoriaId: String)

    /**
     * Actualiza el contador de notas de una categoría
     */
    @Query("UPDATE categorias SET cantidadNotas = :cantidad WHERE id = :categoriaId")
    suspend fun updateCantidadNotas(categoriaId: String, cantidad: Int)

    /**
     * Obtiene el total de categorías
     */
    @Query("SELECT COUNT(*) FROM categorias")
    suspend fun getTotalCategorias(): Int

    /**
     * Busca categorías por nombre
     */
    @Query("SELECT * FROM categorias WHERE nombre LIKE '%' || :query || '%'")
    fun buscarCategorias(query: String): Flow<List<CategoriaEntity>>
}
