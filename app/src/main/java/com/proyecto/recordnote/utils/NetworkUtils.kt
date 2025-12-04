package com.proyecto.recordnote.utils


import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

/**
 * Utilidad para operaciones relacionadas con conectividad de red
 * Proporciona métodos para verificar estado de red y monitorear cambios
 */
object NetworkUtils {

    /**
     * Verifica si hay conexión a internet disponible
     * @param context Contexto de la aplicación
     * @return true si hay conexión activa
     */
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager

        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo
            @Suppress("DEPRECATION")
            networkInfo?.isConnected == true
        }
    }

    /**
     * Verifica si hay conexión WiFi activa
     * @param context Contexto de la aplicación
     * @return true si está conectado por WiFi
     */
    fun isWifiConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager

        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo
            @Suppress("DEPRECATION")
            networkInfo?.type == ConnectivityManager.TYPE_WIFI
        }
    }

    /**
     * Verifica si hay conexión de datos móviles activa
     * @param context Contexto de la aplicación
     * @return true si está conectado por datos móviles
     */
    fun isMobileDataConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager

        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo
            @Suppress("DEPRECATION")
            networkInfo?.type == ConnectivityManager.TYPE_MOBILE
        }
    }

    /**
     * Verifica si hay conexión real a internet (no solo conectividad)
     * Hace ping a un servidor confiable para verificar
     * @param timeout Tiempo máximo de espera en milisegundos
     * @return true si hay conexión real a internet
     */
    suspend fun hasInternetConnection(timeout: Int = 3000): Boolean {
        return try {
            val socket = Socket()
            val socketAddress = InetSocketAddress("8.8.8.8", 53) // DNS de Google
            socket.connect(socketAddress, timeout)
            socket.close()
            true
        } catch (e: IOException) {
            false
        }
    }

    /**
     * Observa cambios en la conectividad de red
     * Emite true cuando hay conexión, false cuando se pierde
     * @param context Contexto de la aplicación
     * @return Flow que emite estados de conectividad
     */
    fun observeNetworkConnectivity(context: Context): Flow<Boolean> = callbackFlow {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(true)
            }

            override fun onLost(network: Network) {
                trySend(false)
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                val hasInternet = networkCapabilities.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_INTERNET
                ) && networkCapabilities.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_VALIDATED
                )
                trySend(hasInternet)
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(request, callback)

        // Emitir estado inicial
        trySend(isNetworkAvailable(context))

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }

    /**
     * Obtiene el tipo de red actual
     * @param context Contexto de la aplicación
     * @return Tipo de red (WIFI, MOBILE, ETHERNET, NONE)
     */
    fun getNetworkType(context: Context): NetworkType {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager

        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return NetworkType.NONE
            val capabilities = connectivityManager.getNetworkCapabilities(network)
                ?: return NetworkType.NONE

            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkType.WIFI
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkType.MOBILE
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> NetworkType.ETHERNET
                else -> NetworkType.NONE
            }
        } else {
            @Suppress("DEPRECATION")
            when (connectivityManager.activeNetworkInfo?.type) {
                ConnectivityManager.TYPE_WIFI -> NetworkType.WIFI
                ConnectivityManager.TYPE_MOBILE -> NetworkType.MOBILE
                ConnectivityManager.TYPE_ETHERNET -> NetworkType.ETHERNET
                else -> NetworkType.NONE
            }
        }
    }

    /**
     * Verifica si la red es medida (datos móviles con límite)
     * Útil para decidir si realizar operaciones costosas de datos
     * @param context Contexto de la aplicación
     * @return true si la red es medida
     */
    fun isMeteredConnection(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        return connectivityManager.isActiveNetworkMetered
    }

    /**
     * Obtiene la velocidad estimada de descarga en Kbps
     * @param context Contexto de la aplicación
     * @return Velocidad en Kbps o null si no disponible
     */
    fun getNetworkSpeed(context: Context): Int? {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager

        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return null
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return null
            capabilities.linkDownstreamBandwidthKbps
        } else {
            null
        }
    }

    /**
     * Determina si se debe sincronizar basado en el tipo de red y preferencias
     * @param context Contexto de la aplicación
     * @param onlyWifi Si solo se debe sincronizar en WiFi
     * @return true si se debe realizar sincronización
     */
    fun shouldSync(context: Context, onlyWifi: Boolean): Boolean {
        if (!isNetworkAvailable(context)) return false
        if (!onlyWifi) return true
        return isWifiConnected(context)
    }
}

/**
 * Enumeración de tipos de red
 */
enum class NetworkType {
    WIFI,
    MOBILE,
    ETHERNET,
    NONE
}

/**
 * Estado de conectividad de red
 */
sealed class NetworkState {
    object Available : NetworkState()
    object Unavailable : NetworkState()
    data class Losing(val maxMsToLive: Int) : NetworkState()
    data class Lost(val networkType: NetworkType) : NetworkState()
}