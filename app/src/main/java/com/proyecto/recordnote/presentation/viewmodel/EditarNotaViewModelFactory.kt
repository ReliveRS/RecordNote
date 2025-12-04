// presentation/viewmodel/EditarNotaViewModelFactory.kt
package com.proyecto.recordnote.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.proyecto.recordnote.data.audio.AudioPlayerManager
import com.proyecto.recordnote.domain.usecase.nota.GuardarNotaUseCase
import com.proyecto.recordnote.domain.usecase.nota.ObtenerNotasUseCase

class EditarNotaViewModelFactory(
    private val obtenerNotasUseCase: ObtenerNotasUseCase,
    private val guardarNotaUseCase: GuardarNotaUseCase,
    private val audioPlayerManager: AudioPlayerManager?
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditarNotaViewModel(
            obtenerNotasUseCase,
            guardarNotaUseCase,
            audioPlayerManager
        ) as T
    }
}
