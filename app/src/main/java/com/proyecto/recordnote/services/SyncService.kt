// services/SyncService.kt
package com.proyecto.recordnote.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import timber.log.Timber

/**
 * Servicio para sincronización de datos en foreground
 * Sincroniza notas con el servidor en background
 */
class SyncService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d("SyncService iniciado")
        // TODO: Inicializar sincronización aquí
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("SyncService detenido")
        // TODO: Liberar recursos de sincronización
    }
}
