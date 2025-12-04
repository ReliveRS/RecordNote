package com.proyecto.recordnote.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.recordnote.data.repositories.NotaRepository
import com.proyecto.recordnote.domain.model.NotaDomain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class NotasViewModel(
    private val notaRepository: NotaRepository
) : ViewModel() {

    private val _notas = MutableStateFlow<List<NotaDomain>>(emptyList())
    val notas: StateFlow<List<NotaDomain>> = _notas.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        cargarNotas()
    }

    fun cargarNotas() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val resultado = notaRepository.obtenerTodasLasNotas()
                resultado.onSuccess { notas ->
                    _notas.value = notas.sortedByDescending { it.fechaCreacion }
                    _error.value = null
                    Timber.d("✅ ${notas.size} notas cargadas")
                }
                resultado.onFailure { exception ->
                    Timber.e(exception, "Error cargando notas")
                    _error.value = exception.message ?: "Error desconocido"
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun buscarNotas(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                if (query.isBlank()) {
                    cargarNotas()
                    return@launch
                }

                val resultado = notaRepository.buscarNotas(query)
                resultado.onSuccess { notas ->
                    _notas.value = notas
                    _error.value = null
                }
                resultado.onFailure { exception ->
                    Timber.e(exception, "Error buscando")
                    _error.value = exception.message
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun eliminarNota(notaId: String) {
        viewModelScope.launch {
            try {
                val resultado = notaRepository.eliminarNota(notaId)
                resultado.onSuccess {
                    Timber.d("✅ Nota eliminada: $notaId")
                    cargarNotas() // Recargar lista
                }
                resultado.onFailure { exception ->
                    Timber.e(exception, "Error eliminando")
                    _error.value = exception.message
                }
            } catch (e: Exception) {
                Timber.e(e, "Error")
            }
        }
    }

    fun refrescar() {
        cargarNotas()
    }
}
