package com.proyecto.recordnote.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class GrabacionViewModel : ViewModel() {

    private val _recordingTime = MutableStateFlow("00:00:00")
    val recordingTime: StateFlow<String> = _recordingTime

    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording

    var mediaRecorder: android.media.MediaRecorder? = null
    var audioFile: File? = null

    fun startRecording(audioPath: String) {
        try {
            audioFile = File(audioPath, "nota_${System.currentTimeMillis()}.m4a")
            mediaRecorder = android.media.MediaRecorder().apply {
                setAudioSource(android.media.MediaRecorder.AudioSource.MIC)
                setOutputFormat(android.media.MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(android.media.MediaRecorder.AudioEncoder.AAC)
                setOutputFile(audioFile?.absolutePath)
                prepare()
                start()
            }
            _isRecording.value = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stopRecording() {
        try {
            mediaRecorder?.stop()
            mediaRecorder?.release()
            mediaRecorder = null
            _isRecording.value = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (isRecording.value) {
            stopRecording()
        }
    }
}
