package com.proyecto.recordnote

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.proyecto.recordnote.BuildConfig
import timber.log.Timber

/**
 * Clase Application de RecordNote
 */
class RecordNoteApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Inicializar Timber
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // Crear canales de notificación
        crearCanalesNotificacion()

        Timber.d("✅ RecordNote Application iniciada")
    }

    private fun crearCanalesNotificacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NotificationManager::class.java)

            val canalGeneral = NotificationChannel(
                "recordnote_general",
                "General",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notificaciones generales"
            }

            val canalGrabacion = NotificationChannel(
                "recordnote_recording",
                "Grabación",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificaciones de grabación activa"
                setSound(null, null)
            }

            val canalSync = NotificationChannel(
                "recordnote_sync",
                "Sincronización",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Notificaciones de sincronización"
                setShowBadge(false)
            }

            notificationManager.createNotificationChannels(
                listOf(canalGeneral, canalGrabacion, canalSync)
            )

            Timber.d("✅ Canales de notificación creados")
        }
    }
}
