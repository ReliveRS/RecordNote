package com.proyecto.recordnote.domain.usecase.nota

import com.proyecto.recordnote.domain.model.NotaDomain
import com.proyecto.recordnote.domain.repository.INotaRepository
import timber.log.Timber
import java.time.LocalDateTime

/**
 * Caso de uso para crear nuevas notas
 * Valida los datos y delega la creación al repositorio
 */
class CrearNotaUseCase(
    private val notaRepository: INotaRepository
) {

    /**
     * Crea una nueva nota con los datos proporcionados
     * @param titulo Título de la nota
     * @param contenido Contenido textual
     * @param usuarioId ID del usuario propietario
     * @param rutaAudio URL/ruta del audio (opcional)
     * @param etiquetas Lista de etiquetas
     * @param colorFondo Color en formato hexadecimal
     * @return Result con la nota creada
     */
    suspend operator fun invoke(
        titulo: String,
        contenido: String,
        usuarioId: String,
        rutaAudio: String? = null,
        etiquetas: List<String> = emptyList(),
        colorFondo: String = "#FFFFFF"
    ): Result<NotaDomain> {
        // Validaciones
        if (titulo.isBlank()) {
            return Result.failure(
                IllegalArgumentException("El título no puede estar vacío")
            )
        }

        if (contenido.isBlank()) {
            return Result.failure(
                IllegalArgumentException("El contenido no puede estar vacío")
            )
        }

        if (usuarioId.isBlank()) {
            return Result.failure(
                IllegalArgumentException("El ID del usuario no puede estar vacío")
            )
        }

        if (titulo.length > 200) {
            return Result.failure(
                IllegalArgumentException("El título no puede exceder 200 caracteres")
            )
        }

        // Crear la nota
        return try {
            val ahora = LocalDateTime.now()
            val nuevaNota = NotaDomain(
                id = java.util.UUID.randomUUID().toString(),
                titulo = titulo.trim(),
                contenido = contenido.trim(),
                rutaAudio = rutaAudio,
                duracionAudio = null,
                esTranscrita = false,
                etiquetas = etiquetas,
                colorFondo = colorFondo,
                esFavorita = false,
                fechaCreacion = ahora,
                fechaModificacion = ahora,
                usuarioId = usuarioId,
                sincronizada = false
            )

            notaRepository.crearNota(nuevaNota)
        } catch (e: Exception) {
            Timber.e(e, "Error creando nota")
            Result.failure(Exception("Error al crear la nota: ${e.message}"))
        }
    }

    /**
     * Crea una nota con audio grabado
     */
    suspend fun crearNotaConAudio(
        titulo: String,
        rutaAudio: String,
        duracionAudio: Int,
        usuarioId: String,
        etiquetas: List<String> = emptyList()
    ): Result<NotaDomain> {
        if (rutaAudio.isBlank()) {
            return Result.failure(
                IllegalArgumentException("La ruta del audio no puede estar vacía")
            )
        }

        if (duracionAudio <= 0) {
            return Result.failure(
                IllegalArgumentException("La duración del audio debe ser mayor a 0")
            )
        }

        return try {
            val ahora = LocalDateTime.now()
            val nuevaNota = NotaDomain(
                id = java.util.UUID.randomUUID().toString(),
                titulo = titulo.trim(),
                contenido = "", // Sin contenido inicial
                rutaAudio = rutaAudio,
                duracionAudio = duracionAudio,
                esTranscrita = false,
                etiquetas = etiquetas,
                colorFondo = "#FFFFFF",
                esFavorita = false,
                fechaCreacion = ahora,
                fechaModificacion = ahora,
                usuarioId = usuarioId,
                sincronizada = false
            )

            notaRepository.crearNota(nuevaNota)
        } catch (e: Exception) {
            Timber.e(e, "Error creando nota con audio")
            Result.failure(Exception("Error al crear la nota: ${e.message}"))
        }
    }
}
