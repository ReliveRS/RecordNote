// presentation/viewmodel/NotaListViewModel.kt
package com.proyecto.recordnote.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.recordnote.domain.model.NotaDomain
import com.proyecto.recordnote.domain.usecase.nota.EliminarNotaUseCase
import com.proyecto.recordnote.domain.usecase.nota.ObtenerNotasUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Estado UI para listar notas
 */
data class NotaListUiState(
    val notas: List<NotaDomain> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val filtroActivo: String = "todas", // todas, favoritas, audio, transcritas
    val searchQuery: String = ""
)

/**
 * ViewModel para listar notas
 */
class NotaListViewModel(
    private val obtenerNotasUseCase: ObtenerNotasUseCase,
    private val eliminarNotaUseCase: EliminarNotaUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotaListUiState())
    val uiState: StateFlow<NotaListUiState> = _uiState.asStateFlow()

    init {
        cargarNotas()
    }

    /**
     * Carga todas las notas
     */
    private fun cargarNotas() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val resultado = obtenerNotasUseCase()

                resultado.onSuccess { notas ->
                    _uiState.value = _uiState.value.copy(
                        notas = notas,
                        isLoading = false
                    )
                }.onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Error cargando notas: ${exception.message}"
                    )
                    Timber.e(exception, "Error loading notas")
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
                Timber.e(e, "Error loading notas")
            }
        }
    }

    /**
     * Filtra las notas según el tipo seleccionado
     */
    fun filtrarNotas(tipo: String) {
        _uiState.value = _uiState.value.copy(filtroActivo = tipo)

        viewModelScope.launch {
            try {
                val resultado = when (tipo) {
                    "favoritas" -> obtenerNotasUseCase.obtenerFavoritas()
                    "audio" -> obtenerNotasUseCase.obtenerConAudio()
                    "transcritas" -> obtenerNotasUseCase.obtenerTranscritas()
                    else -> obtenerNotasUseCase()
                }

                resultado.onSuccess { notas ->
                    _uiState.value = _uiState.value.copy(
                        notas = notas,
                        isLoading = false
                    )
                }.onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = "Error filtrando: ${exception.message}"
                    )
                }
            } catch (e: Exception) {
                Timber.e(e, "Error filtering notas")
            }
        }
    }

    /**
     * Busca notas por query
     */
    fun buscarNotas(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)

        if (query.isBlank()) {
            cargarNotas()
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val resultado = obtenerNotasUseCase.buscar(query)

                resultado.onSuccess { notas ->
                    _uiState.value = _uiState.value.copy(
                        notas = notas,
                        isLoading = false
                    )
                }.onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Error buscando: ${exception.message}"
                    )
                }
            } catch (e: Exception) {
                Timber.e(e, "Error searching notas")
            }
        }
    }

    /**
     * Elimina una nota
     */
    fun eliminarNota(notaId: String) {
        viewModelScope.launch {
            try {
                val resultado = eliminarNotaUseCase(notaId)

                resultado.onSuccess {
                    _uiState.value = _uiState.value.copy(
                        notas = _uiState.value.notas.filter { it.id != notaId }
                    )
                }.onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = "Error eliminando: ${exception.message}"
                    )
                }
            } catch (e: Exception) {
                Timber.e(e, "Error deleting nota")
            }
        }
    }

    /**
     * Recarga las notas
     */
    fun refrescar() {
        cargarNotas()
    }

    /**
     * Limpia el error
     */
    fun limpiarError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
