// services/AudioRecordingService.kt
package com.proyecto.recordnote.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import timber.log.Timber

/**
 * Servicio para grabar audio en foreground
 * Permite grabar mientras la app está en background
 */
class AudioRecordingService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d("AudioRecordingService iniciado")
        // TODO: Inicializar grabación de audio aquí
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("AudioRecordingService detenido")
        // TODO: Liberar recursos de grabación
    }
}
