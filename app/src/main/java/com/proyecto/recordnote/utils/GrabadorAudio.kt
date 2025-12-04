package com.proyecto.recordnote.utils
/*
import android.content.Context
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Build
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import java.io.FileOutputStream
import java.io.RandomAccessFile
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Gestor profesional de grabación de audio
 * Maneja grabación en tiempo real con soporte para pausa/reanudar
 * Genera archivos WAV de alta calidad
 */
@Singleton
class GrabadorAudio @Inject constructor(
    private val context: Context
) {

    private var audioRecord: AudioRecord? = null
    private var isRecording = false
    private var isPaused = false
    private var recordingJob: Job? = null

    private var outputFile: File? = null
    private var tempFile: File? = null

    private val _estadoGrabacion = MutableStateFlow<EstadoGrabacion>(EstadoGrabacion.Detenido)
    val estadoGrabacion: StateFlow<EstadoGrabacion> = _estadoGrabacion

    private val _duracionActual = MutableStateFlow(0)
    val duracionActual: StateFlow<Int> = _duracionActual

    private val _nivelAmplitud = MutableStateFlow(0)
    val nivelAmplitud: StateFlow<Int> = _nivelAmplitud

    // Configuración de audio
    private val sampleRate = Constants.AUDIO_SAMPLE_RATE
    private val channelConfig = AudioFormat.CHANNEL_IN_MONO
    private val audioFormat = Constants.AUDIO_ENCODING

    // Tamaño del buffer
    private val bufferSize = AudioRecord.getMinBufferSize(
        sampleRate,
        channelConfig,
        audioFormat
    ) * 2

    /**
     * Inicia una nueva grabación de audio
     * @param calidadAudio Calidad de la grabación (baja, media, alta)
     * @return Result con archivo de salida o error
     */
    suspend fun iniciarGrabacion(calidadAudio: String = "media"): Result<File> = withContext(Dispatchers.IO) {
        try {
            if (isRecording) {
                return@withContext Result.failure(Exception("Ya hay una grabación en curso"))
            }

            // Verificar permisos
            if (!PermissionUtils.hasAudioPermission(context)) {
                return@withContext Result.failure(Exception("Permiso de audio no concedido"))
            }

            // Crear archivo de salida
            outputFile = FileUtils.createAudioFile(context)
            tempFile = File(FileUtils.getTempDirectory(context), "temp_recording.pcm")

            // Inicializar AudioRecord
            audioRecord = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                AudioRecord.Builder()
                    .setAudioSource(MediaRecorder.AudioSource.MIC)
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setSampleRate(sampleRate)
                            .setChannelMask(channelConfig)
                            .setEncoding(audioFormat)
                            .build()
                    )
                    .setBufferSizeInBytes(bufferSize)
                    .build()
            } else {
                @Suppress("DEPRECATION")
                AudioRecord(
                    MediaRecorder.AudioSource.MIC,
                    sampleRate,
                    channelConfig,
                    audioFormat,
                    bufferSize
                )
            }

            // Verificar que se inicializó correctamente
            if (audioRecord?.state != AudioRecord.STATE_INITIALIZED) {
                return@withContext Result.failure(Exception("Error al inicializar grabadora"))
            }

            // Iniciar grabación
            audioRecord?.startRecording()
            isRecording = true
            isPaused = false
            _estadoGrabacion.value = EstadoGrabacion.Grabando
            _duracionActual.value = 0

            // Iniciar proceso de grabación en coroutine
            recordingJob = CoroutineScope(Dispatchers.IO).launch {
                grabarAudio()
            }

            Result.success(outputFile!!)
        } catch (e: Exception) {
            android.util.Log.e(Constants.LOG_TAG, "Error al iniciar grabación: ${e.message}")
            limpiarRecursos()
            Result.failure(Exception("Error al iniciar grabación: ${e.message}"))
        }
    }

    /**
     * Pausa la grabación actual
     * @return Result indicando éxito o error
     */
    fun pausarGrabacion(): Result<Unit> {
        return try {
            if (!isRecording || isPaused) {
                return Result.failure(Exception("No hay grabación activa para pausar"))
            }

            isPaused = true
            _estadoGrabacion.value = EstadoGrabacion.Pausado

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Error al pausar: ${e.message}"))
        }
    }

    /**
     * Reanuda la grabación pausada
     * @return Result indicando éxito o error
     */
    fun reanudarGrabacion(): Result<Unit> {
        return try {
            if (!isRecording || !isPaused) {
                return Result.failure(Exception("No hay grabación pausada"))
            }

            isPaused = false
            _estadoGrabacion.value = EstadoGrabacion.Grabando

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Error al reanudar: ${e.message}"))
        }
    }

    /**
     * Detiene la grabación y guarda el archivo
     * @return Result con el archivo final o error
     */
    suspend fun detenerGrabacion(): Result<File> = withContext(Dispatchers.IO) {
        try {
            if (!isRecording) {
                return@withContext Result.failure(Exception("No hay grabación activa"))
            }

            // Detener grabación
            isRecording = false
            isPaused = false
            recordingJob?.cancel()

            audioRecord?.stop()
            audioRecord?.release()
            audioRecord = null

            _estadoGrabacion.value = EstadoGrabacion.Procesando

            // Convertir PCM a WAV
            tempFile?.let { pcm ->
                outputFile?.let { wav ->
                    convertirPCMaWAV(pcm, wav)
                }
            }

            // Limpiar archivo temporal
            tempFile?.delete()
            tempFile = null

            _estadoGrabacion.value = EstadoGrabacion.Completado

            outputFile?.let { file ->
                Result.success(file)
            } ?: Result.failure(Exception("Error al guardar archivo"))

        } catch (e: Exception) {
            android.util.Log.e(Constants.LOG_TAG, "Error al detener grabación: ${e.message}")
            limpiarRecursos()
            Result.failure(Exception("Error al detener grabación: ${e.message}"))
        }
    }

    /**
     * Cancela la grabación sin guardar
     * @return Result indicando éxito o error
     */
    fun cancelarGrabacion(): Result<Unit> {
        return try {
            if (!isRecording) {
                return Result.failure(Exception("No hay grabación activa"))
            }

            isRecording = false
            isPaused = false
            recordingJob?.cancel()

            limpiarRecursos()

            // Eliminar archivos
            tempFile?.delete()
            outputFile?.delete()

            _estadoGrabacion.value = EstadoGrabacion.Cancelado
            _duracionActual.value = 0

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Error al cancelar: ${e.message}"))
        }
    }

    /**
     * Obtiene la duración actual de la grabación en segundos
     * @return Duración en segundos
     */
    fun obtenerDuracionActual(): Int {
        return _duracionActual.value
    }

    /**
     * Obtiene el nivel de amplitud actual (para visualización de onda)
     * @return Amplitud normalizada (0-100)
     */
    fun obtenerNivelAmplitud(): Int {
        return _nivelAmplitud.value
    }

    // ========== MÉTODOS PRIVADOS ==========

    /**
     * Proceso principal de grabación en loop
     */
    private suspend fun grabarAudio() = withContext(Dispatchers.IO) {
        val buffer = ShortArray(bufferSize / 2)
        var totalSamples = 0L
        var startTime = System.currentTimeMillis()

        FileOutputStream(tempFile).use { outputStream ->
            while (isRecording) {
                if (!isPaused) {
                    // Leer datos del micrófono
                    val readResult = audioRecord?.read(buffer, 0, buffer.size) ?: 0

                    if (readResult > 0) {
                        // Escribir datos al archivo temporal
                        val byteBuffer = ByteArray(readResult * 2)
                        for (i in 0 until readResult) {
                            byteBuffer[i * 2] = (buffer[i].toInt() and 0xFF).toByte()
                            byteBuffer[i * 2 + 1] = ((buffer[i].toInt() shr 8) and 0xFF).toByte()
                        }
                        outputStream.write(byteBuffer)

                        // Actualizar duración
                        totalSamples += readResult
                        val duracionSegundos = (totalSamples / sampleRate).toInt()
                        _duracionActual.value = duracionSegundos

                        // Calcular amplitud para visualización
                        val amplitud = calcularAmplitud(buffer, readResult)
                        _nivelAmplitud.value = amplitud

                        // Verificar duración máxima
                        if (duracionSegundos >= Constants.MAX_RECORDING_DURATION_MINUTES * 60) {
                            isRecording = false
                            break
                        }
                    }
                } else {
                    // Pausado - esperar un poco
                    delay(100)
                }
            }
        }
    }

    /**
     * Calcula la amplitud promedio del buffer
     */
    private fun calcularAmplitud(buffer: ShortArray, size: Int): Int {
        var sum = 0L
        for (i in 0 until size) {
            sum += kotlin.math.abs(buffer[i].toInt())
        }
        val average = sum / size
        // Normalizar a 0-100
        return ((average.toFloat() / Short.MAX_VALUE) * 100).toInt()
    }

    /**
     * Convierte archivo PCM raw a formato WAV con headers
     */
    private fun convertirPCMaWAV(pcmFile: File, wavFile: File) {
        val pcmSize = pcmFile.length()
        val totalDataLen = pcmSize + 36
        val totalAudioLen = pcmSize
        val channels = 1
        val byteRate = (sampleRate * channels * 16) / 8

        RandomAccessFile(wavFile, "rw").use { wavOutput ->
            // Header RIFF
            wavOutput.writeBytes("RIFF")
            wavOutput.writeInt(Integer.reverseBytes(totalDataLen.toInt()))
            wavOutput.writeBytes("WAVE")

            // Sub-chunk fmt
            wavOutput.writeBytes("fmt ")
            wavOutput.writeInt(Integer.reverseBytes(16)) // Tamaño del sub-chunk
            wavOutput.writeShort(Short.reverseBytes(1).toInt()) // Audio format (1 = PCM)
            wavOutput.writeShort(Short.reverseBytes(channels.toShort()).toInt()) // Canales
            wavOutput.writeInt(Integer.reverseBytes(sampleRate)) // Sample rate
            wavOutput.writeInt(Integer.reverseBytes(byteRate)) // Byte rate
            wavOutput.writeShort(Short.reverseBytes((channels * 16 / 8).toShort()).toInt()) // Block align
            wavOutput.writeShort(Short.reverseBytes(16).toInt()) // Bits per sample

            // Sub-chunk data
            wavOutput.writeBytes("data")
            wavOutput.writeInt(Integer.reverseBytes(totalAudioLen.toInt()))

            // Copiar datos PCM
            pcmFile.inputStream().use { input ->
                val buffer = ByteArray(8192)
                var bytesRead: Int
                while (input.read(buffer).also { bytesRead = it } != -1) {
                    wavOutput.write(buffer, 0, bytesRead)
                }
            }
        }
    }

    /**
     * Limpia recursos de grabación
     */
    private fun limpiarRecursos() {
        try {
            audioRecord?.stop()
            audioRecord?.release()
            audioRecord = null
            recordingJob?.cancel()
            recordingJob = null
        } catch (e: Exception) {
            android.util.Log.e(Constants.LOG_TAG, "Error al limpiar recursos: ${e.message}")
        }
    }
}

/**
 * Estados posibles de la grabación
 */
sealed class EstadoGrabacion {
    object Detenido : EstadoGrabacion()
    object Grabando : EstadoGrabacion()
    object Pausado : EstadoGrabacion()
    object Procesando : EstadoGrabacion()
    object Completado : EstadoGrabacion()
    object Cancelado : EstadoGrabacion()
    data class Error(val mensaje: String) : EstadoGrabacion()
}

/**
 * Información de grabación
 */
data class InfoGrabacion(
    val duracion: Int,
    val tamaño: Long,
    val rutaArchivo: String,
    val sampleRate: Int,
    val bitRate: Int
)*/