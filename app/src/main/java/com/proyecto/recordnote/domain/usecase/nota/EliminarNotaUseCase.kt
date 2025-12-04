package com.proyecto.recordnote.domain.usecase.nota

import com.proyecto.recordnote.domain.repository.INotaRepository
import timber.log.Timber
import java.time.LocalDateTime

/**
 * Caso de uso para eliminar notas
 * Maneja la eliminación individual y masiva de notas
 *
 * @property notaRepository Repositorio de notas
 */
class EliminarNotaUseCase(
    private val notaRepository: INotaRepository
) {

    /**
     * Elimina una nota específica por su ID
     * Verifica que la nota exista antes de eliminarla
     *
     * @param notaId ID de la nota a eliminar
     * @return Result indicando éxito o fallo
     */
    suspend operator fun invoke(notaId: String): Result<Unit> {
        // Validar que el ID no esté vacío
        if (notaId.isBlank()) {
            return Result.failure(
                IllegalArgumentException("El ID de la nota no puede estar vacío")
            )
        }

        // Verificar que la nota existe
        val resultNota = notaRepository.obtenerNotaPorId(notaId)
        if (resultNota.isFailure) {
            return Result.failure(
                NoSuchElementException("La nota con ID $notaId no existe")
            )
        }

        // Eliminar la nota
        return try {
            notaRepository.eliminarNota(notaId)
        } catch (e: Exception) {
            Timber.e(e, "Error al eliminar la nota")
            Result.failure(Exception("Error al eliminar la nota: ${e.message}"))
        }
    }

    /**
     * Elimina múltiples notas a la vez
     * @param notasIds Lista de IDs de notas a eliminar
     * @return Result con el número de notas eliminadas exitosamente
     */
    suspend fun eliminarMultiples(notasIds: List<String>): Result<Int> {
        if (notasIds.isEmpty()) {
            return Result.failure(
                IllegalArgumentException("La lista de notas a eliminar está vacía")
            )
        }

        return try {
            var eliminadas = 0
            notasIds.forEach { notaId ->
                val resultado = notaRepository.eliminarNota(notaId)
                if (resultado.isSuccess) {
                    eliminadas++
                }
            }
            Result.success(eliminadas)
        } catch (e: Exception) {
            Timber.e(e, "Error al eliminar múltiples notas")
            Result.failure(Exception("Error al eliminar notas: ${e.message}"))
        }
    }

    /**
     * Elimina todas las notas (requiere confirmación)
     * @param confirmacion Debe ser true para confirmar la eliminación
     * @return Result indicando éxito o fallo
     */
    suspend fun eliminarTodas(confirmacion: Boolean): Result<Unit> {
        if (!confirmacion) {
            return Result.failure(
                IllegalStateException("Se requiere confirmación para eliminar todas las notas")
            )
        }

        return try {
            // Obtener todas las notas
            val resultado = notaRepository.obtenerTodasLasNotas()

            if (resultado.isSuccess) {
                val notas = resultado.getOrNull() ?: emptyList()
                notas.forEach { nota ->
                    notaRepository.eliminarNota(nota.id)
                }
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error obteniendo notas para eliminar"))
            }
        } catch (e: Exception) {
            Timber.e(e, "Error al eliminar todas las notas")
            Result.failure(Exception("Error al eliminar todas las notas: ${e.message}"))
        }
    }

    /**
     * Elimina notas antiguas que cumplan ciertos criterios
     * @param diasAntiguedad Eliminar notas más antiguas que este número de días
     * @param excluirFavoritas Si es true, no elimina las notas favoritas
     * @return Result con el número de notas eliminadas
     */
    suspend fun eliminarAntiguas(
        diasAntiguedad: Int,
        excluirFavoritas: Boolean = true
    ): Result<Int> {
        if (diasAntiguedad < 30) {
            return Result.failure(
                IllegalArgumentException("Solo se pueden eliminar notas con más de 30 días de antigüedad")
            )
        }

        return try {
            // Obtener todas las notas
            val resultado = notaRepository.obtenerTodasLasNotas()

            if (resultado.isFailure) {
                return Result.failure(Exception("Error obteniendo notas"))
            }

            val notas = resultado.getOrNull() ?: emptyList()
            val fechaLimite = LocalDateTime.now().minusDays(diasAntiguedad.toLong())

            var eliminadas = 0
            val notasAEliminar = notas.filter { nota ->
                nota.fechaModificacion.isBefore(fechaLimite) &&
                        (!excluirFavoritas || !nota.esFavorita)
            }

            notasAEliminar.forEach { nota ->
                val resultado = notaRepository.eliminarNota(nota.id)
                if (resultado.isSuccess) {
                    eliminadas++
                }
            }

            Result.success(eliminadas)
        } catch (e: Exception) {
            Timber.e(e, "Error al eliminar notas antiguas")
            Result.failure(Exception("Error al eliminar notas antiguas: ${e.message}"))
        }
    }

    /**
     * Elimina notas sin sincronizar (que fallaron en sincronización)
     * @return Result con el número de notas eliminadas
     */
    suspend fun eliminarNoSincronizadas(): Result<Int> {
        return try {
            val resultado = notaRepository.obtenerNoSincronizadas()

            if (resultado.isFailure) {
                return Result.failure(Exception("Error obteniendo notas no sincronizadas"))
            }

            val notas = resultado.getOrNull() ?: emptyList()
            var eliminadas = 0

            notas.forEach { nota ->
                val elimResult = notaRepository.eliminarNota(nota.id)
                if (elimResult.isSuccess) {
                    eliminadas++
                }
            }

            Result.success(eliminadas)
        } catch (e: Exception) {
            Timber.e(e, "Error eliminando no sincronizadas")
            Result.failure(Exception("Error al eliminar notas no sincronizadas: ${e.message}"))
        }
    }
}
