package com.proyecto.recordnote.data.audio

import android.content.Context
import android.media.MediaPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File

/**
 * Manager para reproducir audio usando MediaPlayer
 */
class AudioPlayerManager(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    private var currentFile: File? = null

    // Estados
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentPosition = MutableStateFlow(0)
    val currentPosition: StateFlow<Int> = _currentPosition.asStateFlow()

    private val _duration = MutableStateFlow(0)
    val duration: StateFlow<Int> = _duration.asStateFlow()

    /**
     * Inicia la reproducción
     */
    fun play(audioPath: String): Boolean {
        return try {
            val file = File(audioPath)
            if (!file.exists()) {
                return false
            }

            // Si es el mismo archivo y está pausado, reanudar
            if (currentFile?.absolutePath == audioPath && mediaPlayer != null) {
                mediaPlayer?.start()
                _isPlaying.value = true
                return true
            }

            // Detener reproducción anterior
            stop()

            // Crear nuevo MediaPlayer
            mediaPlayer = MediaPlayer().apply {
                setDataSource(audioPath)
                prepare()
                start()

                setOnCompletionListener {
                    _isPlaying.value = false
                    _currentPosition.value = 0
                }
            }

            currentFile = file
            _duration.value = mediaPlayer?.duration ?: 0
            _isPlaying.value = true

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Pausa la reproducción
     */
    fun pause() {
        try {
            mediaPlayer?.pause()
            _isPlaying.value = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Detiene completamente la reproducción
     */
    fun stop() {
        try {
            mediaPlayer?.apply {
                stop()
                release()
            }
            mediaPlayer = null
            currentFile = null
            _isPlaying.value = false
            _currentPosition.value = 0
            _duration.value = 0
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Salta a una posición específica
     */
    fun seekTo(position: Int) {
        try {
            mediaPlayer?.seekTo(position)
            _currentPosition.value = position
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Actualiza la posición actual (llamar desde un timer)
     */
    fun updatePosition() {
        try {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    _currentPosition.value = it.currentPosition
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Verifica si está reproduciendo
     */
    fun isPlayingFile(audioPath: String): Boolean {
        return currentFile?.absolutePath == audioPath && _isPlaying.value
    }

    /**
     * Libera recursos
     */
    fun release() {
        stop()
    }
}
