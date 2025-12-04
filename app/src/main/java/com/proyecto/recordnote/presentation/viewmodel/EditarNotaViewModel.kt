// presentation/viewmodel/EditarNotaViewModel.kt
package com.proyecto.recordnote.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.recordnote.data.audio.AudioPlayerManager
import com.proyecto.recordnote.domain.model.NotaDomain
import com.proyecto.recordnote.domain.usecase.nota.GuardarNotaUseCase
import com.proyecto.recordnote.domain.usecase.nota.ObtenerNotasUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDateTime

/**
 * Estado UI para editar notas
 */
data class EditarNotaUiState(
    val nota: NotaDomain? = null,
    val titulo: String = "",
    val contenido: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val mostrarGuardado: Boolean = false,
    val tituloError: String? = null,
    val contenidoError: String? = null,
    val isPlayingAudio: Boolean = false,
    val audioPosition: Int = 0
)

/**
 * ViewModel para editar una nota existente
 * Usa UseCases e inyección de dependencias
 */
class EditarNotaViewModel(
    private val obtenerNotasUseCase: ObtenerNotasUseCase,
    private val guardarNotaUseCase: GuardarNotaUseCase,
    private val audioPlayerManager: AudioPlayerManager? = null
) : ViewModel() {

    // Estado principal
    private val _uiState = MutableStateFlow(EditarNotaUiState())
    val uiState: StateFlow<EditarNotaUiState> = _uiState.asStateFlow()

    // Estados individuales para Compose (opcional, para granularidad)
    private val _titulo = MutableStateFlow("")
    val titulo: StateFlow<String> = _titulo.asStateFlow()

    private val _contenido = MutableStateFlow("")
    val contenido: StateFlow<String> = _contenido.asStateFlow()

    // Estados de UI
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _mostrarGuardado = MutableStateFlow(false)
    val mostrarGuardado: StateFlow<Boolean> = _mostrarGuardado.asStateFlow()

    // Audio
    private val _isPlayingAudio = MutableStateFlow(false)
    val isPlayingAudio: StateFlow<Boolean> = _isPlayingAudio.asStateFlow()

    private val _audioPosition = MutableStateFlow(0)
    val audioPosition: StateFlow<Int> = _audioPosition.asStateFlow()

    private var audioTimerJob: Job? = null

    /**
     * Carga la nota a editar desde el repositorio
     */
    fun cargarNota(notaId: String) {
        if (notaId.isBlank()) {
            _error.value = "❌ ID de nota inválido"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val resultado = obtenerNotasUseCase.obtenerPorId(notaId)

                resultado.onSuccess { nota ->
                    _uiState.value = _uiState.value.copy(
                        nota = nota,
                        titulo = nota.titulo,
                        contenido = nota.contenido,
                        isLoading = false
                    )
                    _titulo.value = nota.titulo
                    _contenido.value = nota.contenido
                }.onFailure { exception ->
                    _error.value = "❌ Nota no encontrada: ${exception.message}"
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Nota no encontrada"
                    )
                    Timber.e(exception, "Error cargando nota")
                }
            } catch (e: Exception) {
                _error.value = "❌ Error al cargar: ${e.message}"
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
                Timber.e(e, "Error cargando nota")
            }
        }
    }

    /**
     * Actualiza el título en el estado
     */
    fun actualizarTitulo(nuevoTitulo: String) {
        _titulo.value = nuevoTitulo
        _uiState.value = _uiState.value.copy(
            titulo = nuevoTitulo,
            tituloError = null
        )
    }

    /**
     * Actualiza el contenido en el estado
     */
    fun actualizarContenido(nuevoContenido: String) {
        _contenido.value = nuevoContenido
        _uiState.value = _uiState.value.copy(
            contenido = nuevoContenido,
            contenidoError = null
        )
    }

    /**
     * Valida y guarda los cambios en la nota
     */
    fun guardarCambios() {
        // Validaciones
        if (_titulo.value.isBlank()) {
            _uiState.value = _uiState.value.copy(
                tituloError = "El título no puede estar vacío"
            )
            return
        }

        if (_contenido.value.isBlank()) {
            _uiState.value = _uiState.value.copy(
                contenidoError = "El contenido no puede estar vacío"
            )
            return
        }

        val notaActual = _uiState.value.nota ?: return

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val notaActualizada = notaActual.copy(
                    titulo = _titulo.value.trim(),
                    contenido = _contenido.value.trim(),
                    fechaModificacion = LocalDateTime.now(),
                    sincronizada = false
                )

                val resultado = guardarNotaUseCase(notaActualizada)

                resultado.onSuccess { nota ->
                    _uiState.value = _uiState.value.copy(
                        nota = nota,
                        isLoading = false,
                        mostrarGuardado = true
                    )
                    _mostrarGuardado.value = true

                    // Auto-ocultar mensaje después de 2 segundos
                    delay(2000)
                    _mostrarGuardado.value = false
                    _uiState.value = _uiState.value.copy(mostrarGuardado = false)
                }.onFailure { exception ->
                    _error.value = "❌ Error al guardar: ${exception.message}"
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message
                    )
                    Timber.e(exception, "Error guardando nota")
                }
            } catch (e: Exception) {
                _error.value = "❌ Error: ${e.message}"
                _uiState.value = _uiState.value.copy(isLoading = false)
                Timber.e(e, "Error guardando nota")
            }
        }
    }

    /**
     * Marca/desmarca la nota como favorita
     */
    fun toggleFavorito() {
        val notaActual = _uiState.value.nota ?: return
        val nuevoEstado = !notaActual.esFavorita

        viewModelScope.launch {
            try {
                val notaActualizada = notaActual.copy(esFavorita = nuevoEstado)
                val resultado = guardarNotaUseCase(notaActualizada)

                resultado.onSuccess { nota ->
                    _uiState.value = _uiState.value.copy(nota = nota)
                    _error.value = if (nuevoEstado) {
                        "❤️ Añadido a favoritos"
                    } else {
                        "Removido de favoritos"
                    }
                }.onFailure { exception ->
                    _error.value = "❌ Error: ${exception.message}"
                    Timber.e(exception, "Error toggling favorito")
                }
            } catch (e: Exception) {
                _error.value = "❌ Error: ${e.message}"
                Timber.e(e, "Error toggling favorito")
            }
        }
    }

    /**
     * Limpia el mensaje de error
     */
    fun limpiarError() {
        _error.value = null
        _uiState.value = _uiState.value.copy(error = null)
    }

    // ========== REPRODUCTOR DE AUDIO ==========

    /**
     * Reproduce el audio de la nota
     */
    fun playAudio(rutaAudio: String) {
        if (rutaAudio.isBlank() || audioPlayerManager == null) {
            _error.value = "❌ No hay audio para reproducir"
            return
        }

        viewModelScope.launch {
            try {
                val success = audioPlayerManager.play(rutaAudio)
                if (success) {
                    _isPlayingAudio.value = true
                    startAudioTimer()
                } else {
                    _error.value = "❌ Error al reproducir audio"
                }
            } catch (e: Exception) {
                _error.value = "❌ Error: ${e.message}"
                Timber.e(e, "Error reproducing audio")
            }
        }
    }

    /**
     * Pausa el audio
     */
    fun pauseAudio() {
        if (audioPlayerManager == null) return

        try {
            audioPlayerManager.pause()
            _isPlayingAudio.value = false
            audioTimerJob?.cancel()
        } catch (e: Exception) {
            Timber.e(e, "Error pausing audio")
        }
    }

    /**
     * Detiene el audio
     */
    fun stopAudio() {
        if (audioPlayerManager == null) return

        try {
            audioPlayerManager.stop()
            _isPlayingAudio.value = false
            _audioPosition.value = 0
            audioTimerJob?.cancel()
        } catch (e: Exception) {
            Timber.e(e, "Error stopping audio")
        }
    }

    /**
     * Actualiza la posición del audio cada 100ms
     */
    private fun startAudioTimer() {
        audioTimerJob?.cancel()
        audioTimerJob = viewModelScope.launch {
            while (audioPlayerManager != null && audioPlayerManager.isPlaying.value) {
                try {
                    audioPlayerManager.updatePosition()
                    _audioPosition.value = audioPlayerManager.currentPosition.value
                    delay(100)
                } catch (e: Exception) {
                    Timber.e(e, "Error updating audio position")
                    break
                }
            }
            _isPlayingAudio.value = false
        }
    }

    /**
     * Limpia recursos al destruir el ViewModel
     */
    override fun onCleared() {
        super.onCleared()
        audioTimerJob?.cancel()
        audioPlayerManager?.release()
        Timber.d("EditarNotaViewModel cleared")
    }
}
