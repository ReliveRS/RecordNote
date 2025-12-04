package com.proyecto.recordnote.domain.usecase.nota

import com.proyecto.recordnote.domain.model.NotaDomain
import com.proyecto.recordnote.domain.repository.INotaRepository
import timber.log.Timber

/**
 * Caso de uso para obtener y filtrar notas
 * Proporciona diferentes formas de recuperar notas del repositorio
 */
class ObtenerNotasUseCase(
    private val notaRepository: INotaRepository
) {

    /**
     * Obtiene todas las notas
     */
    suspend operator fun invoke(): Result<List<NotaDomain>> {
        return try {
            notaRepository.obtenerTodasLasNotas()
        } catch (e: Exception) {
            Timber.e(e, "Error obteniendo todas las notas")
            Result.failure(e)
        }
    }

    /**
     * Obtiene una nota específica por ID
     */
    suspend fun obtenerPorId(notaId: String): Result<NotaDomain> {
        if (notaId.isBlank()) {
            return Result.failure(
                IllegalArgumentException("El ID no puede estar vacío")
            )
        }

        return try {
            notaRepository.obtenerNotaPorId(notaId)
        } catch (e: Exception) {
            Timber.e(e, "Error obteniendo nota por ID")
            Result.failure(e)
        }
    }

    /**
     * Obtiene las notas marcadas como favoritas
     */
    suspend fun obtenerFavoritas(): Result<List<NotaDomain>> {
        return try {
            notaRepository.obtenerFavoritas()
        } catch (e: Exception) {
            Timber.e(e, "Error obteniendo favoritas")
            Result.failure(e)
        }
    }

    /**
     * Obtiene las notas que contienen audio
     */
    suspend fun obtenerConAudio(): Result<List<NotaDomain>> {
        return try {
            notaRepository.obtenerConAudio()
        } catch (e: Exception) {
            Timber.e(e, "Error obteniendo notas con audio")
            Result.failure(e)
        }
    }

    /**
     * Obtiene las notas que han sido transcritas
     */
    suspend fun obtenerTranscritas(): Result<List<NotaDomain>> {
        return try {
            notaRepository.obtenerTranscritas()
        } catch (e: Exception) {
            Timber.e(e, "Error obteniendo notas transcritas")
            Result.failure(e)
        }
    }

    /**
     * Busca notas por texto (título, contenido o transcripción)
     */
    suspend fun buscar(query: String): Result<List<NotaDomain>> {
        if (query.isBlank()) {
            return Result.failure(
                IllegalArgumentException("La búsqueda no puede estar vacía")
            )
        }

        return try {
            notaRepository.buscarNotas(query)
        } catch (e: Exception) {
            Timber.e(e, "Error buscando notas")
            Result.failure(e)
        }
    }

    /**
     * Obtiene las notas que contienen una etiqueta específica
     */
    suspend fun obtenerPorEtiqueta(etiqueta: String): Result<List<NotaDomain>> {
        if (etiqueta.isBlank()) {
            return Result.failure(
                IllegalArgumentException("La etiqueta no puede estar vacía")
            )
        }

        return try {
            notaRepository.obtenerPorEtiqueta(etiqueta)
        } catch (e: Exception) {
            Timber.e(e, "Error obteniendo notas por etiqueta")
            Result.failure(e)
        }
    }

    /**
     * Obtiene las notas no sincronizadas
     */
    suspend fun obtenerNoSincronizadas(): Result<List<NotaDomain>> {
        return try {
            notaRepository.obtenerNoSincronizadas()
        } catch (e: Exception) {
            Timber.e(e, "Error obteniendo no sincronizadas")
            Result.failure(e)
        }
    }
}
