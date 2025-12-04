// receivers/NetworkChangeReceiver.kt
package com.proyecto.recordnote.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import timber.log.Timber

/**
 * Receptor que detecta cambios en la conectividad de red
 */
class NetworkChangeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        val activeNetwork = connectivityManager?.activeNetwork
        val isConnected = activeNetwork != null

        Timber.d("Red ${if (isConnected) "conectada" else "desconectada"}")

        if (isConnected) {
            // TODO: Iniciar sincronización cuando hay conexión
            Timber.d("Iniciando sincronización automática")
        }
    }
}
