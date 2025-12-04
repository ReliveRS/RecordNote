// receivers/BootReceiver.kt
package com.proyecto.recordnote.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import timber.log.Timber

/**
 * Receptor que reinicia servicios después del boot
 */
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            Timber.d("Boot completado, reiniciando servicios")
            // TODO: Reiniciar sincronización si es necesario
        }
    }
}
