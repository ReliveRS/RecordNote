// domain/usecase/nota/TranscribirAudioUseCase.kt
package com.proyecto.recordnote.domain.usecase.nota

import com.proyecto.recordnote.domain.repository.INotaRepository
import timber.log.Timber

/**
 * Caso de uso para transcribir audio de notas
 * Gestiona la transcripción de archivos de audio usando APIs externas
 */
class TranscribirAudioUseCase(
    private val notaRepository: INotaRepository
) {

    /**
     * Transcribe el audio de una nota
     * En una implementación real, llamaría a una API de transcripción (Whisper, etc.)
     *
     * @param notaId ID de la nota con audio
     * @param rutaAudio Ruta del archivo de audio
     * @return Result con la transcripción generada
     */
    suspend operator fun invoke(
        notaId: String,
        rutaAudio: String
    ): Result<String> {
        // Validaciones
        if (notaId.isBlank()) {
            return Result.failure(
                IllegalArgumentException("El ID de la nota no puede estar vacío")
            )
        }

        if (rutaAudio.isBlank()) {
            return Result.failure(
                IllegalArgumentException("La ruta del audio no puede estar vacía")
            )
        }

        return try {
            // TODO: Implementar llamada a API de transcripción (Whisper, Google Cloud Speech, etc.)
            // Por ahora retorna un placeholder
            val transcripcion = "Transcripción pendiente de implementar para: $rutaAudio"

            // Guardar la transcripción en la nota
            notaRepository.actualizarTranscripcion(
                notaId,
                esTranscrita = true,
                transcripcion = transcripcion
            )

            Result.success(transcripcion)
        } catch (e: Exception) {
            Timber.e(e, "Error transcribiendo audio")
            Result.failure(Exception("Error al transcribir audio: ${e.message}"))
        }
    }

    /**
     * Transcribe múltiples audios de forma secuencial
     */
    suspend fun transcribirMultiples(
        audios: List<Pair<String, String>> // Pares de (notaId, rutaAudio)
    ): Result<Int> {
        if (audios.isEmpty()) {
            return Result.failure(
                IllegalArgumentException("La lista de audios no puede estar vacía")
            )
        }

        return try {
            var transcritas = 0
            audios.forEach { (notaId, rutaAudio) ->
                val resultado = invoke(notaId, rutaAudio)
                if (resultado.isSuccess) {
                    transcritas++
                }
            }
            Result.success(transcritas)
        } catch (e: Exception) {
            Timber.e(e, "Error transcribiendo múltiples audios")
            Result.failure(e)
        }
    }

    /**
     * Obtiene el estado de transcripción de una nota
     */
    suspend fun obtenerEstadoTranscripcion(notaId: String): Result<Boolean> {
        if (notaId.isBlank()) {
            return Result.failure(
                IllegalArgumentException("El ID no puede estar vacío")
            )
        }

        return try {
            val resultado = notaRepository.obtenerNotaPorId(notaId)
            if (resultado.isFailure) {
                return Result.failure(Exception("Nota no encontrada"))
            }

            val nota = resultado.getOrNull()
            Result.success(nota?.esTranscrita ?: false)
        } catch (e: Exception) {
            Timber.e(e, "Error obteniendo estado")
            Result.failure(e)
        }
    }

    /**
     * Anula la transcripción de una nota
     */
    suspend fun anularTranscripcion(notaId: String): Result<Unit> {
        if (notaId.isBlank()) {
            return Result.failure(
                IllegalArgumentException("El ID no puede estar vacío")
            )
        }

        return try {
            notaRepository.actualizarTranscripcion(
                notaId,
                esTranscrita = false,
                transcripcion = null
            )
        } catch (e: Exception) {
            Timber.e(e, "Error anulando transcripción")
            Result.failure(e)
        }
    }
}
