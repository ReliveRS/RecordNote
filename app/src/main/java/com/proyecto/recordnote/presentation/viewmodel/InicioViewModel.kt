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

class InicioViewModel(
    private val notaRepository: NotaRepository
) : ViewModel() {

    private val _recentNotes = MutableStateFlow<List<NotaDomain>>(emptyList())
    val recentNotes: StateFlow<List<NotaDomain>> = _recentNotes.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        cargarNotasRecientes()
    }

    fun cargarNotasRecientes() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val resultado = notaRepository.obtenerTodasLasNotas()
                resultado.onSuccess { notas ->
                    // Tomar solo las 3 últimas
                    _recentNotes.value = notas
                        .sortedByDescending { it.fechaCreacion }
                        .take(3)
                    _error.value = null
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

    fun refrescar() {
        cargarNotasRecientes()
    }
}
