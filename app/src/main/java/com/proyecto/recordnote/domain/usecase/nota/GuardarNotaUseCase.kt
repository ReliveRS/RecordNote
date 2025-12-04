// domain/usecase/nota/GuardarNotaUseCase.kt
package com.proyecto.recordnote.domain.usecase.nota

import com.proyecto.recordnote.domain.model.NotaDomain
import com.proyecto.recordnote.domain.repository.INotaRepository
import timber.log.Timber
import java.time.LocalDateTime

/**
 * Caso de uso para actualizar/guardar cambios en notas existentes
 * Valida los cambios y los persiste en el repositorio
 */
class GuardarNotaUseCase(
    private val notaRepository: INotaRepository
) {

    /**
     * Actualiza una nota existente
     */
    suspend operator fun invoke(nota: NotaDomain): Result<NotaDomain> {
        // Validar que tenga ID
        if (nota.id.isBlank()) {
            return Result.failure(
                IllegalArgumentException("La nota debe tener un ID para ser actualizada")
            )
        }

        // Validar contenido
        if (nota.titulo.isBlank() && nota.contenido.isBlank()) {
            return Result.failure(
                IllegalArgumentException("La nota debe tener título o contenido")
            )
        }

        return try {
            // Actualizar fecha de modificación
            val notaActualizada = nota.copy(
                fechaModificacion = LocalDateTime.now(),
                sincronizada = false
            )

            notaRepository.actualizarNota(notaActualizada)
        } catch (e: Exception) {
            Timber.e(e, "Error actualizando nota")
            Result.failure(Exception("Error al guardar la nota: ${e.message}"))
        }
    }

    /**
     * Marca una nota como favorita o no
     */
    suspend fun toggleFavorito(notaId: String, esFavorita: Boolean): Result<Unit> {
        if (notaId.isBlank()) {
            return Result.failure(
                IllegalArgumentException("El ID no puede estar vacío")
            )
        }

        return try {
            notaRepository.actualizarFavorito(notaId, esFavorita)
        } catch (e: Exception) {
            Timber.e(e, "Error actualizando favorito")
            Result.failure(e)
        }
    }

    /**
     * Actualiza la transcripción de una nota
     */
    suspend fun actualizarTranscripcion(
        notaId: String,
        transcripcion: String
    ): Result<Unit> {
        if (notaId.isBlank() || transcripcion.isBlank()) {
            return Result.failure(
                IllegalArgumentException("ID y transcripción no pueden estar vacíos")
            )
        }

        return try {
            notaRepository.actualizarTranscripcion(
                notaId,
                esTranscrita = true,
                transcripcion = transcripcion
            )
        } catch (e: Exception) {
            Timber.e(e, "Error actualizando transcripción")
            Result.failure(e)
        }
    }

    /**
     * Actualiza la categoría de una nota
     */
    suspend fun actualizarCategoria(
        notaId: String,
        categoriaId: String?
    ): Result<Unit> {
        if (notaId.isBlank()) {
            return Result.failure(
                IllegalArgumentException("El ID no puede estar vacío")
            )
        }

        return try {
            notaRepository.actualizarCategoria(notaId, categoriaId)
        } catch (e: Exception) {
            Timber.e(e, "Error actualizando categoría")
            Result.failure(e)
        }
    }

    /**
     * Actualiza solo el contenido de una nota
     */
    suspend fun actualizarContenido(
        notaId: String,
        nuevoContenido: String
    ): Result<Unit> {
        if (notaId.isBlank()) {
            return Result.failure(
                IllegalArgumentException("El ID no puede estar vacío")
            )
        }

        if (nuevoContenido.isBlank()) {
            return Result.failure(
                IllegalArgumentException("El contenido no puede estar vacío")
            )
        }

        return try {
            // Obtener la nota actual
            val resultNota = notaRepository.obtenerNotaPorId(notaId)
            if (resultNota.isFailure) {
                return Result.failure(Exception("Nota no encontrada"))
            }

            val notaActual = resultNota.getOrNull() ?: return Result.failure(
                Exception("No se pudo obtener la nota")
            )

            // Actualizar con nuevo contenido
            val notaActualizada = notaActual.copy(
                contenido = nuevoContenido.trim(),
                fechaModificacion = LocalDateTime.now(),
                sincronizada = false
            )

            notaRepository.actualizarNota(notaActualizada)
            Result.success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Error actualizando contenido")
            Result.failure(e)
        }
    }
    /**
     * Alias para actualizar una nota (más legible que invoke)
     */
    suspend fun actualizar(nota: NotaDomain): Result<NotaDomain> {
        return this.invoke(nota)
    }

}
