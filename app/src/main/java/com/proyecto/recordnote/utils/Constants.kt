package com.proyecto.recordnote.utils


/**
 * Objeto que contiene todas las constantes globales de la aplicación
 * Centraliza valores compartidos para facilitar mantenimiento y configuración
 */
object Constants {

    // ========== CONFIGURACIÓN DE LA APLICACIÓN ==========

    /**
     * Nombre de la aplicación
     */
    const val APP_NAME = "RecordNote"

    /**
     * Versión de la API utilizada
     */
    const val API_VERSION = "v1"

    /**
     * Tiempo de espera para operaciones de red (milisegundos)
     */
    const val NETWORK_TIMEOUT = 30000L

    /**
     * Número máximo de reintentos para operaciones de red
     */
    const val MAX_RETRY_ATTEMPTS = 3

    // ========== CONFIGURACIÓN DE BASE DE DATOS ==========

    /**
     * Nombre de la base de datos
     */
    const val DATABASE_NAME = "recordnote_database"

    /**
     * Versión de la base de datos
     */
    const val DATABASE_VERSION = 1

    /**
     * Nombre de la tabla de notas
     */
    const val TABLE_NOTAS = "notas"

    /**
     * Nombre de la tabla de usuarios
     */
    const val TABLE_USUARIOS = "usuarios"

    // ========== CONFIGURACIÓN DE SHARED PREFERENCES ==========

    /**
     * Nombre del archivo de preferencias
     */
    const val PREFS_NAME = "recordnote_preferences"

    /**
     * Clave para token de autenticación
     */
    const val PREF_AUTH_TOKEN = "auth_token"

    /**
     * Clave para ID de usuario
     */
    const val PREF_USER_ID = "user_id"

    /**
     * Clave para tema de la aplicación
     */
    const val PREF_THEME_MODE = "theme_mode"

    /**
     * Clave para última sincronización
     */
    const val PREF_LAST_SYNC = "last_sync"

    // ========== CONFIGURACIÓN DE AUDIO ==========

    /**
     * Sample rate para grabación de audio (Hz)
     */
    const val AUDIO_SAMPLE_RATE = 44100

    /**
     * Canales de audio (mono = 1, estéreo = 2)
     */
    const val AUDIO_CHANNELS = 1

    /**
     * Encoding de audio (PCM 16 bits)
     */
    const val AUDIO_ENCODING = android.media.AudioFormat.ENCODING_PCM_16BIT

    /**
     * Bitrate bajo para audio (kbps)
     */
    const val AUDIO_BITRATE_LOW = 64000

    /**
     * Bitrate medio para audio (kbps)
     */
    const val AUDIO_BITRATE_MEDIUM = 128000

    /**
     * Bitrate alto para audio (kbps)
     */
    const val AUDIO_BITRATE_HIGH = 256000

    /**
     * Formato de archivo de audio
     */
    const val AUDIO_FILE_EXTENSION = ".m4a"

    /**
     * Tipo MIME para audio
     */
    const val AUDIO_MIME_TYPE = "audio/mp4a-latm"

    /**
     * Duración máxima de grabación en minutos
     */
    const val MAX_RECORDING_DURATION_MINUTES = 60

    // ========== CONFIGURACIÓN DE ARCHIVOS ==========

    /**
     * Carpeta para almacenar grabaciones de audio
     */
    const val AUDIO_FOLDER = "RecordNote/Audio"

    /**
     * Carpeta para backups
     */
    const val BACKUP_FOLDER = "RecordNote/Backup"

    /**
     * Carpeta para exportaciones
     */
    const val EXPORT_FOLDER = "RecordNote/Export"

    /**
     * Carpeta temporal
     */
    const val TEMP_FOLDER = "RecordNote/Temp"

    /**
     * Tamaño máximo de archivo en MB
     */
    const val MAX_FILE_SIZE_MB = 100

    /**
     * Extensión para archivos de backup
     */
    const val BACKUP_FILE_EXTENSION = ".rnbackup"

    // ========== CONFIGURACIÓN DE NOTIFICACIONES ==========

    /**
     * ID del canal de notificaciones generales
     */
    const val NOTIFICATION_CHANNEL_ID = "recordnote_general"

    /**
     * Nombre del canal de notificaciones
     */
    const val NOTIFICATION_CHANNEL_NAME = "RecordNote Notifications"

    /**
     * ID del canal para grabación activa
     */
    const val RECORDING_NOTIFICATION_CHANNEL_ID = "recordnote_recording"

    /**
     * ID de notificación para grabación activa
     */
    const val RECORDING_NOTIFICATION_ID = 1001

    /**
     * ID del canal para sincronización
     */
    const val SYNC_NOTIFICATION_CHANNEL_ID = "recordnote_sync"

    // ========== CONFIGURACIÓN DE SINCRONIZACIÓN ==========

    /**
     * Intervalo de sincronización automática (minutos)
     */
    const val SYNC_INTERVAL_MINUTES = 30

    /**
     * Tag para WorkManager de sincronización
     */
    const val SYNC_WORK_TAG = "sync_work"

    /**
     * Tag para WorkManager de backup
     */
    const val BACKUP_WORK_TAG = "backup_work"

    /**
     * Tag para WorkManager de limpieza
     */
    const val CLEANUP_WORK_TAG = "cleanup_work"

    /**
     * Nombre único del trabajo de sincronización periódica
     */
    const val SYNC_PERIODIC_WORK_NAME = "periodic_sync_work"

    // ========== CONFIGURACIÓN DE TRANSCRIPCIÓN ==========

    /**
     * Idioma por defecto para transcripción
     */
    const val DEFAULT_TRANSCRIPTION_LANGUAGE = "es-ES"

    /**
     * Tiempo máximo de espera para transcripción (segundos)
     */
    const val TRANSCRIPTION_TIMEOUT_SECONDS = 300

    /**
     * Tamaño de chunk para procesar audio (bytes)
     */
    const val TRANSCRIPTION_CHUNK_SIZE = 8192

    // ========== LÍMITES Y VALIDACIONES ==========

    /**
     * Longitud mínima de contraseña
     */
    const val MIN_PASSWORD_LENGTH = 8

    /**
     * Longitud máxima del título de nota
     */
    const val MAX_TITLE_LENGTH = 200

    /**
     * Longitud máxima del contenido de nota
     */
    const val MAX_CONTENT_LENGTH = 50000

    /**
     * Número máximo de etiquetas por nota
     */
    const val MAX_TAGS_PER_NOTE = 10

    /**
     * Longitud máxima de una etiqueta
     */
    const val MAX_TAG_LENGTH = 30

    /**
     * Número máximo de notas para operación batch
     */
    const val MAX_BATCH_NOTES = 50

    // ========== FORMATOS DE FECHA Y HORA ==========

    /**
     * Formato de fecha completa (ej: "01/01/2024")
     */
    const val DATE_FORMAT = "dd/MM/yyyy"

    /**
     * Formato de hora (ej: "14:30")
     */
    const val TIME_FORMAT = "HH:mm"

    /**
     * Formato de fecha y hora (ej: "01/01/2024 14:30")
     */
    const val DATETIME_FORMAT = "dd/MM/yyyy HH:mm"

    /**
     * Formato ISO 8601 para APIs
     */
    const val ISO_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"

    /**
     * Formato para nombres de archivo (ej: "20240101_143000")
     */
    const val FILENAME_DATETIME_FORMAT = "yyyyMMdd_HHmmss"

    // ========== CÓDIGOS DE RESULTADO ==========

    /**
     * Código de request para permisos de audio
     */
    const val REQUEST_AUDIO_PERMISSION = 1001

    /**
     * Código de request para permisos de almacenamiento
     */
    const val REQUEST_STORAGE_PERMISSION = 1002

    /**
     * Código de request para permisos de notificaciones
     */
    const val REQUEST_NOTIFICATION_PERMISSION = 1003

    // ========== INTENTS Y ACCIONES ==========

    /**
     * Acción para iniciar grabación desde notificación
     */
    const val ACTION_START_RECORDING = "com.recordnote.START_RECORDING"

    /**
     * Acción para detener grabación desde notificación
     */
    const val ACTION_STOP_RECORDING = "com.recordnote.STOP_RECORDING"

    /**
     * Acción para pausar grabación desde notificación
     */
    const val ACTION_PAUSE_RECORDING = "com.recordnote.PAUSE_RECORDING"

    /**
     * Extra para ID de nota
     */
    const val EXTRA_NOTE_ID = "note_id"

    /**
     * Extra para ruta de audio
     */
    const val EXTRA_AUDIO_PATH = "audio_path"

    // ========== CONFIGURACIÓN DE EXPORTACIÓN ==========

    /**
     * Formato de exportación JSON
     */
    const val EXPORT_FORMAT_JSON = "json"

    /**
     * Formato de exportación TXT
     */
    const val EXPORT_FORMAT_TXT = "txt"

    /**
     * Formato de exportación PDF
     */
    const val EXPORT_FORMAT_PDF = "pdf"

    /**
     * Formato de exportación CSV
     */
    const val EXPORT_FORMAT_CSV = "csv"

    // ========== CONFIGURACIÓN DE COLORES ==========

    /**
     * Colores predefinidos para notas (hexadecimal)
     */
    val NOTE_COLORS = listOf(
        "#FFFFFF", // Blanco
        "#FFE5E5", // Rosa claro
        "#FFF4E5", // Naranja claro
        "#FFFFCC", // Amarillo claro
        "#E5FFE5", // Verde claro
        "#E5F5FF", // Azul claro
        "#F0E5FF", // Púrpura claro
        "#FFE5F5"  // Magenta claro
    )

    /**
     * Color por defecto para notas nuevas
     */
    const val DEFAULT_NOTE_COLOR = "#FFFFFF"

    // ========== MENSAJES DE ERROR ==========

    /**
     * Mensaje de error genérico
     */
    const val ERROR_GENERIC = "Ha ocurrido un error. Por favor, intenta de nuevo."

    /**
     * Mensaje de error de red
     */
    const val ERROR_NETWORK = "Error de conexión. Verifica tu conexión a internet."

    /**
     * Mensaje de error de autenticación
     */
    const val ERROR_AUTH = "Error de autenticación. Por favor, inicia sesión de nuevo."

    /**
     * Mensaje de error de permisos
     */
    const val ERROR_PERMISSION = "Se requieren permisos para realizar esta acción."

    // ========== CONFIGURACIÓN DE CACHÉ ==========

    /**
     * Tamaño de caché en MB
     */
    const val CACHE_SIZE_MB = 50

    /**
     * Tiempo de expiración de caché (horas)
     */
    const val CACHE_EXPIRATION_HOURS = 24

    // ========== URLS Y ENDPOINTS (ejemplos) ==========

    /**
     * URL de términos y condiciones
     */
    const val URL_TERMS = "https://recordnote.com/terms"

    /**
     * URL de política de privacidad
     */
    const val URL_PRIVACY = "https://recordnote.com/privacy"

    /**
     * URL de soporte
     */
    const val URL_SUPPORT = "https://recordnote.com/support"

    /**
     * Email de soporte
     */
    const val SUPPORT_EMAIL = "support@recordnote.com"

    // ========== CONFIGURACIÓN DE DEBUG ==========

    /**
     * Tag para logs de la aplicación
     */
    const val LOG_TAG = "RecordNote"

    /**
     * Habilitar logs detallados (solo en debug)
     */
   // val VERBOSE_LOGGING = com.recordnote.BuildConfig.DEBUG
}

/**
 * Objeto con constantes de rutas de navegación
 * Usado con Jetpack Compose Navigation
 */
object NavigationRoutes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val INICIO = "inicio"
    const val DETALLE_NOTA = "detalle_nota/{noteId}"
    const val NUEVA_NOTA = "nueva_nota"
    const val GRABACION = "grabacion"
    const val CONFIGURACION = "configuracion"
    const val PERFIL = "perfil"
    const val BUSQUEDA = "busqueda"

    /**
     * Crea ruta con parámetros para detalle de nota
     */
    fun detalleNota(noteId: String) = "detalle_nota/$noteId"
}

/**
 * Objeto con constantes de argumentos de navegación
 */
object NavigationArgs {
    const val NOTE_ID = "noteId"
    const val AUDIO_PATH = "audioPath"
    const val IS_EDITING = "isEditing"
}