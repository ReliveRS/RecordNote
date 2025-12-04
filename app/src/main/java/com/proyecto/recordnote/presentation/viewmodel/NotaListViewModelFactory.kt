// presentation/viewmodel/NotaListViewModelFactory.kt
package com.proyecto.recordnote.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.proyecto.recordnote.domain.usecase.nota.EliminarNotaUseCase
import com.proyecto.recordnote.domain.usecase.nota.ObtenerNotasUseCase

class NotaListViewModelFactory(
    private val obtenerNotasUseCase: ObtenerNotasUseCase,
    private val eliminarNotaUseCase: EliminarNotaUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NotaListViewModel(
            obtenerNotasUseCase,
            eliminarNotaUseCase
        ) as T
    }
}
