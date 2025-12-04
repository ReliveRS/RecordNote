package com.proyecto.recordnote.data.audio

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import java.io.File
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Manager para grabar audio usando MediaRecorder
 * Maneja el ciclo de vida de la grabación y guarda archivos
 */
class AudioRecorderManager(private val context: Context) {

    private var mediaRecorder: MediaRecorder? = null
    private var audioFile: File? = null
    private var startTime: Long = 0

    // Estados
    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration.asStateFlow()

    /**
     * Inicia la grabación de audio
     * @return File donde se guardará el audio o null si hay error
     */
    fun startRecording(): File? {
        try {
            // Crear archivo para el audio
            audioFile = createAudioFile()

            // Configurar MediaRecorder
            mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(context)
            } else {
                @Suppress("DEPRECATION")
                MediaRecorder()
            }.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setAudioEncodingBitRate(128000) // 128 kbps
                setAudioSamplingRate(44100) // 44.1 kHz
                setOutputFile(audioFile?.absolutePath)

                prepare()
                start()
            }

            startTime = System.currentTimeMillis()
            _isRecording.value = true

            return audioFile

        } catch (e: IOException) {
            e.printStackTrace()
            stopRecording()
            return null
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            stopRecording()
            return null
        }
    }

    /**
     * Detiene la grabación
     * @return Duración en segundos
     */
    fun stopRecording(): Int {
        return try {
            mediaRecorder?.apply {
                stop()
                release()
            }
            mediaRecorder = null

            val endTime = System.currentTimeMillis()
            val durationSeconds = ((endTime - startTime) / 1000).toInt()

            _isRecording.value = false
            _duration.value = 0L

            durationSeconds

        } catch (e: Exception) {
            e.printStackTrace()
            _isRecording.value = false
            0
        }
    }

    /**
     * Pausa la grabación (Android 7+)
     */
    fun pauseRecording() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                mediaRecorder?.pause()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Reanuda la grabación (Android 7+)
     */
    fun resumeRecording() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                mediaRecorder?.resume()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Cancela la grabación y elimina el archivo
     */
    fun cancelRecording() {
        stopRecording()
        audioFile?.delete()
        audioFile = null
    }

    /**
     * Obtiene el archivo de audio actual
     */
    fun getAudioFile(): File? = audioFile

    /**
     * Actualiza la duración (llamar desde un timer)
     */
    fun updateDuration() {
        if (_isRecording.value) {
            val elapsed = System.currentTimeMillis() - startTime
            _duration.value = elapsed
        }
    }

    /**
     * Crea un archivo único para el audio
     */
    private fun createAudioFile(): File {
        val audioDir = File(context.filesDir, "audios")
        if (!audioDir.exists()) {
            audioDir.mkdirs()
        }

        val timestamp = LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
        )
        val fileName = "audio_$timestamp.m4a"

        return File(audioDir, fileName)
    }

    /**
     * Libera recursos
     */
    fun release() {
        try {
            mediaRecorder?.release()
            mediaRecorder = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
