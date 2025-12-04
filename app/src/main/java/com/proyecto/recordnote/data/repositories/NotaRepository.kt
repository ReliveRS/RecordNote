package com.proyecto.recordnote.data.repositories

import com.proyecto.recordnote.data.local.dao.NotaDao
import com.proyecto.recordnote.data.local.entidades.NotaEntity
import com.proyecto.recordnote.data.mapper.dtoToDomain
import com.proyecto.recordnote.data.mapper.entityToDomain
import com.proyecto.recordnote.data.mapper.toDomain
import com.proyecto.recordnote.data.mapper.toDto
import com.proyecto.recordnote.data.mapper.toEntity
import com.proyecto.recordnote.domain.model.NotaDomain
import com.proyecto.recordnote.domain.repository.INotaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Implementación del repositorio de Notas
 * Maneja la lógica de acceso a datos (local + remoto)
 */
class NotaRepository(
    private val notaDao: NotaDao,
    private val apiService: com.proyecto.recordnote.data.remote.api.NotaApiService? = null
) : INotaRepository {

    // ========== CONSULTAS BÁSICAS (Flow para observar cambios) ==========

    /**
     * Obtiene todas las notas como Flow (para observar en tiempo real)
     */
    fun obtenerTodasFlow(): Flow<List<NotaEntity>> {
        return notaDao.obtenerTodasLasNotasFlow()
    }

    /**
     * Obtiene notas favoritas como Flow
     */
    fun obtenerFavoritasFlow(): Flow<List<NotaEntity>> {
        return notaDao.obtenerFavoritasFlow()
    }

    /**
     * Obtiene notas por categoría como Flow
     */
    fun obtenerPorCategoriaFlow(categoriaId: String): Flow<List<NotaEntity>> {
        return notaDao.obtenerPorCategoriaFlow(categoriaId)
    }

    /**
     * Busca notas como Flow
     */
    fun buscarFlow(query: String): Flow<List<NotaEntity>> {
        return notaDao.buscarFlow(query)
    }

    /**
     * Obtiene notas con audio como Flow
     */
    fun obtenerConAudioFlow(): Flow<List<NotaEntity>> {
        return notaDao.obtenerConAudioFlow()
    }

    /**
     * Obtiene notas transcritas como Flow
     */
    fun obtenerTranscritasFlow(): Flow<List<NotaEntity>> {
        return notaDao.obtenerTranscritasFlow()
    }

    // ========== OPERACIONES BÁSICAS (INotaRepository) ==========

    /**
     * Obtiene todas las notas (local + remoto si disponible)
     */
    override suspend fun obtenerTodasLasNotas(): Result<List<NotaDomain>> =
        withContext(Dispatchers.IO) {
            try {
                // Intentar desde API
                if (apiService != null) {
                    try {
                        val response = apiService.obtenerTodasLasNotas()
                        if (response.isSuccessful && response.body() != null) {
                            val notasDomain = response.body()!!.dtoToDomain()  // ✅ CAMBIO
                            notaDao.insertarNotas(notasDomain.map { it.toEntity() })
                            return@withContext Result.success(notasDomain)
                        }
                    } catch (e: Exception) {
                        Timber.e(e, "Error en API, usando datos locales")
                    }
                }

                // Fallback a datos locales
                val notasLocales = notaDao.obtenerTodasLasNotas().entityToDomain()  // ✅ CAMBIO
                Result.success(notasLocales)
            } catch (e: Exception) {
                Timber.e(e, "Error obteniendo notas")
                Result.failure(e)
            }
        }

    /**
     * Obtiene una nota específica por ID
     */
    override suspend fun obtenerNotaPorId(id: String): Result<NotaDomain> =
        withContext(Dispatchers.IO) {
            try {
                // Intentar desde API
                if (apiService != null) {
                    try {
                        val response = apiService.obtenerNotaPorId(id)
                        if (response.isSuccessful && response.body() != null) {
                            val notaDomain = response.body()!!.toDomain()
                            notaDao.insertarNota(notaDomain.toEntity())
                            return@withContext Result.success(notaDomain)
                        }
                    } catch (e: Exception) {
                        Timber.e(e, "Error en API")
                    }
                }

                // Fallback a datos locales
                val notaLocal = notaDao.obtenerNotaPorId(id)?.toDomain()
                if (notaLocal != null) {
                    Result.success(notaLocal)
                } else {
                    Result.failure(Exception("Nota no encontrada"))
                }
            } catch (e: Exception) {
                Timber.e(e, "Error")
                Result.failure(e)
            }
        }

    /**
     * Crea una nueva nota
     */
    override suspend fun crearNota(nota: NotaDomain): Result<NotaDomain> =
        withContext(Dispatchers.IO) {
            try {
                notaDao.insertarNota(nota.toEntity())

                // Intentar sincronizar con servidor
                if (apiService != null) {
                    try {
                        val response = apiService.crearNota(nota.toDto())
                        if (response.isSuccessful && response.body() != null) {
                            val notaCreada = response.body()!!.toDomain()
                            notaDao.actualizarNota(notaCreada.toEntity())
                            return@withContext Result.success(notaCreada)
                        }
                    } catch (e: Exception) {
                        Timber.e(e, "Error sincronizando con servidor")
                    }
                }

                Result.success(nota)
            } catch (e: Exception) {
                Timber.e(e, "Error creando nota")
                Result.failure(e)
            }
        }

    /**
     * Actualiza una nota existente
     */
    override suspend fun actualizarNota(nota: NotaDomain): Result<NotaDomain> =
        withContext(Dispatchers.IO) {
            try {
                // Actualizar localmente
                notaDao.actualizarNota(nota.toEntity())

                // Intentar sincronizar
                if (apiService != null) {
                    try {
                        val response = apiService.actualizarNota(nota.id, nota.toDto())
                        if (response.isSuccessful && response.body() != null) {
                            val notaActualizada = response.body()!!.toDomain()
                            notaDao.actualizarNota(notaActualizada.toEntity())
                            return@withContext Result.success(notaActualizada)
                        }
                    } catch (e: Exception) {
                        Timber.e(e, "Error sincronizando actualización")
                    }
                }

                Result.success(nota)
            } catch (e: Exception) {
                Timber.e(e, "Error actualizando")
                Result.failure(e)
            }
        }

    /**
     * Elimina una nota
     */
    override suspend fun eliminarNota(id: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                notaDao.eliminarNota(id)

                // Intentar sincronizar
                if (apiService != null) {
                    try {
                        apiService.eliminarNota(id)
                    } catch (e: Exception) {
                        Timber.e(e, "Error eliminando en servidor")
                    }
                }

                Result.success(Unit)
            } catch (e: Exception) {
                Timber.e(e, "Error eliminando")
                Result.failure(e)
            }
        }

    // ========== BÚSQUEDA Y FILTRADO ==========

    /**
     * Busca notas por término de búsqueda
     */
    override suspend fun buscarNotas(query: String): Result<List<NotaDomain>> =
        withContext(Dispatchers.IO) {
            try {
                // Intentar desde API
                if (apiService != null) {
                    try {
                        val response = apiService.buscarNotas(query)
                        if (response.isSuccessful && response.body() != null) {
                            val notasDomain = response.body()!!.dtoToDomain()  // ✅ CAMBIO
                            notaDao.insertarNotas(notasDomain.map { it.toEntity() })
                            return@withContext Result.success(notasDomain)
                        }
                    } catch (e: Exception) {
                        Timber.e(e, "Error en búsqueda API")
                    }
                }

                // Fallback a datos locales
                val notasLocales = notaDao.buscar(query).entityToDomain()  // ✅ CAMBIO
                Result.success(notasLocales)
            } catch (e: Exception) {
                Timber.e(e, "Error buscando notas")
                Result.failure(e)
            }
        }

    /**
     * Obtiene notas por etiqueta
     */
    override suspend fun obtenerPorEtiqueta(etiqueta: String): Result<List<NotaDomain>> =
        withContext(Dispatchers.IO) {
            try {
                val notasLocales = notaDao.obtenerPorEtiqueta(etiqueta).entityToDomain()  // ✅ CAMBIO
                Result.success(notasLocales)
            } catch (e: Exception) {
                Timber.e(e, "Error filtrando por etiqueta")
                Result.failure(e)
            }
        }

    /**
     * Obtiene notas favoritas
     */
    override suspend fun obtenerFavoritas(): Result<List<NotaDomain>> =
        withContext(Dispatchers.IO) {
            try {
                val notasLocales = notaDao.obtenerFavoritas().entityToDomain()  // ✅ CAMBIO
                Result.success(notasLocales)
            } catch (e: Exception) {
                Timber.e(e, "Error obteniendo favoritas")
                Result.failure(e)
            }
        }

    /**
     * Obtiene notas con audio
     */
    override suspend fun obtenerConAudio(): Result<List<NotaDomain>> =
        withContext(Dispatchers.IO) {
            try {
                val notasLocales = notaDao.obtenerConAudio().entityToDomain()  // ✅ CAMBIO
                Result.success(notasLocales)
            } catch (e: Exception) {
                Timber.e(e, "Error obteniendo notas con audio")
                Result.failure(e)
            }
        }

    /**
     * Obtiene notas transcritas
     */
    override suspend fun obtenerTranscritas(): Result<List<NotaDomain>> =
        withContext(Dispatchers.IO) {
            try {
                val notasLocales = notaDao.obtenerTranscritas().entityToDomain()  // ✅ CAMBIO
                Result.success(notasLocales)
            } catch (e: Exception) {
                Timber.e(e, "Error obteniendo notas transcritas")
                Result.failure(e)
            }
        }

    // ========== ACTUALIZACIONES ESPECÍFICAS ==========

    /**
     * Actualiza si una nota es favorita
     */
    override suspend fun actualizarFavorito(notaId: String, esFavorita: Boolean): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                notaDao.actualizarFavorito(notaId, esFavorita)
                Result.success(Unit)
            } catch (e: Exception) {
                Timber.e(e, "Error actualizando favorito")
                Result.failure(e)
            }
        }

    /**
     * Actualiza la transcripción de una nota
     */
    override suspend fun actualizarTranscripcion(
        notaId: String,
        esTranscrita: Boolean,
        transcripcion: String?
    ): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                notaDao.actualizarTranscripcion(notaId, esTranscrita, transcripcion)
                Result.success(Unit)
            } catch (e: Exception) {
                Timber.e(e, "Error actualizando transcripción")
                Result.failure(e)
            }
        }

    /**
     * Actualiza la categoría de una nota
     */
    override suspend fun actualizarCategoria(notaId: String, categoriaId: String?): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                notaDao.actualizarCategoria(notaId, categoriaId)
                Result.success(Unit)
            } catch (e: Exception) {
                Timber.e(e, "Error actualizando categoría")
                Result.failure(e)
            }
        }

    // ========== SINCRONIZACIÓN ==========

    /**
     * Sincroniza notas locales no sincronizadas con el servidor
     */
    override suspend fun sincronizarConServidor(): Result<Unit> =
        withContext(Dispatchers.IO) {
            if (apiService == null) {
                return@withContext Result.failure(Exception("API no disponible"))
            }

            try {
                val notasLocales = notaDao.obtenerNotasNoSincronizadas()
                notasLocales.forEach { entity ->
                    try {
                        val notaDomain = entity.toDomain()
                        apiService.crearNota(notaDomain.toDto())
                        notaDao.marcarComoSincronizada(entity.id)
                    } catch (e: Exception) {
                        Timber.e(e, "Error sincronizando nota: ${entity.id}")
                    }
                }
                Result.success(Unit)
            } catch (e: Exception) {
                Timber.e(e, "Error sincronizando")
                Result.failure(e)
            }
        }

    /**
     * Obtiene notas no sincronizadas
     */
    override suspend fun obtenerNoSincronizadas(): Result<List<NotaDomain>> =
        withContext(Dispatchers.IO) {
            try {
                val notasNoSync = notaDao.obtenerNotasNoSincronizadas().entityToDomain()  // ✅ CAMBIO
                Result.success(notasNoSync)
            } catch (e: Exception) {
                Timber.e(e, "Error obteniendo no sincronizadas")
                Result.failure(e)
            }
        }

    // ========== ESTADÍSTICAS ==========

    suspend fun contarNotas(): Int {
        return notaDao.contarNotas()
    }

    suspend fun contarFavoritas(): Int {
        return notaDao.contarFavoritas()
    }

    suspend fun contarNotasConAudio(): Int {
        return notaDao.contarNotasConAudio()
    }

    suspend fun obtenerDuracionTotalAudios(): Int {
        return notaDao.obtenerDuracionTotalAudios() ?: 0
    }

    suspend fun contarNoSincronizadas(): Int {
        return notaDao.contarNoSincronizadas()
    }

    suspend fun contarTranscritas(): Int {
        return notaDao.contarTranscritas()
    }
}
