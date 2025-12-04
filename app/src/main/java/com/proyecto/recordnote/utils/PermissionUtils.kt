package com.proyecto.recordnote.utils


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

/**
 * Utilidad para gestión de permisos en Android
 * Simplifica la verificación y solicitud de permisos
 */
object PermissionUtils {

    /**
     * Permisos necesarios para grabación de audio
     */
    val AUDIO_PERMISSIONS = arrayOf(
        Manifest.permission.RECORD_AUDIO
    )

    /**
     * Permisos necesarios para almacenamiento (depende de versión de Android)
     */
    val STORAGE_PERMISSIONS = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.READ_MEDIA_AUDIO
        )
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    } else {
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    /**
     * Permiso para notificaciones (Android 13+)
     */
    val NOTIFICATION_PERMISSION = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(Manifest.permission.POST_NOTIFICATIONS)
    } else {
        emptyArray()
    }

    /**
     * Todos los permisos necesarios para la app
     */
    val ALL_REQUIRED_PERMISSIONS = AUDIO_PERMISSIONS + STORAGE_PERMISSIONS + NOTIFICATION_PERMISSION

    /**
     * Verifica si un permiso específico está concedido
     * @param context Contexto de la aplicación
     * @param permission Permiso a verificar
     * @return true si el permiso está concedido
     */
    fun hasPermission(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Verifica si todos los permisos de un array están concedidos
     * @param context Contexto de la aplicación
     * @param permissions Array de permisos a verificar
     * @return true si todos los permisos están concedidos
     */
    fun hasPermissions(context: Context, permissions: Array<String>): Boolean {
        return permissions.all { hasPermission(context, it) }
    }

    /**
     * Verifica si tiene permisos para grabar audio
     * @param context Contexto de la aplicación
     * @return true si puede grabar audio
     */
    fun hasAudioPermission(context: Context): Boolean {
        return hasPermissions(context, AUDIO_PERMISSIONS)
    }

    /**
     * Verifica si tiene permisos para acceder al almacenamiento
     * @param context Contexto de la aplicación
     * @return true si puede acceder al almacenamiento
     */
    fun hasStoragePermission(context: Context): Boolean {
        return hasPermissions(context, STORAGE_PERMISSIONS)
    }

    /**
     * Verifica si tiene permiso para mostrar notificaciones
     * @param context Contexto de la aplicación
     * @return true si puede mostrar notificaciones (siempre true en Android < 13)
     */
    fun hasNotificationPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            hasPermission(context, Manifest.permission.POST_NOTIFICATIONS)
        } else {
            true // En versiones anteriores no se necesita permiso
        }
    }

    /**
     * Verifica si todos los permisos críticos están concedidos
     * @param context Contexto de la aplicación
     * @return true si tiene todos los permisos necesarios
     */
    fun hasAllRequiredPermissions(context: Context): Boolean {
        return hasAudioPermission(context) && hasStoragePermission(context)
    }

    /**
     * Obtiene lista de permisos que faltan por conceder
     * @param context Contexto de la aplicación
     * @param permissions Permisos a verificar
     * @return Lista de permisos no concedidos
     */
    fun getMissingPermissions(context: Context, permissions: Array<String>): List<String> {
        return permissions.filter { !hasPermission(context, it) }
    }

    /**
     * Verifica si se debe mostrar explicación del permiso
     * (usuario ha rechazado el permiso anteriormente)
     * @param activity Activity desde donde se solicita
     * @param permission Permiso a verificar
     * @return true si se debe mostrar explicación
     */
    fun shouldShowRationale(activity: ComponentActivity, permission: String): Boolean {
        return activity.shouldShowRequestPermissionRationale(permission)
    }

    /**
     * Crea un launcher para solicitar múltiples permisos
     * @param activity Activity desde donde se solicita
     * @param onResult Callback con mapa de permisos y sus estados
     * @return ActivityResultLauncher configurado
     */
    fun createPermissionLauncher(
        activity: ComponentActivity,
        onResult: (Map<String, Boolean>) -> Unit
    ): ActivityResultLauncher<Array<String>> {
        return activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            onResult(permissions)
        }
    }

    /**
     * Crea un launcher para solicitar un solo permiso
     * @param activity Activity desde donde se solicita
     * @param onResult Callback con resultado del permiso
     * @return ActivityResultLauncher configurado
     */
    fun createSinglePermissionLauncher(
        activity: ComponentActivity,
        onResult: (Boolean) -> Unit
    ): ActivityResultLauncher<String> {
        return activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            onResult(isGranted)
        }
    }
}

/**
 * Resultado de solicitud de permisos
 */
sealed class PermissionResult {
    /**
     * Todos los permisos fueron concedidos
     */
    object Granted : PermissionResult()

    /**
     * Algunos permisos fueron denegados
     * @property deniedPermissions Lista de permisos denegados
     */
    data class Denied(val deniedPermissions: List<String>) : PermissionResult()

    /**
     * Permisos denegados permanentemente (usuario marcó "No volver a preguntar")
     * @property permanentlyDeniedPermissions Lista de permisos denegados permanentemente
     */
    data class PermanentlyDenied(
        val permanentlyDeniedPermissions: List<String>
    ) : PermissionResult()
}

/**
 * Extension function para verificar resultado de permisos múltiples
 */
fun Map<String, Boolean>.toPermissionResult(
    activity: ComponentActivity
): PermissionResult {
    val deniedPermissions = filterValues { !it }.keys.toList()

    if (deniedPermissions.isEmpty()) {
        return PermissionResult.Granted
    }

    val permanentlyDenied = deniedPermissions.filter { permission ->
        !activity.shouldShowRequestPermissionRationale(permission)
    }

    return if (permanentlyDenied.isNotEmpty()) {
        PermissionResult.PermanentlyDenied(permanentlyDenied)
    } else {
        PermissionResult.Denied(deniedPermissions)
    }
}