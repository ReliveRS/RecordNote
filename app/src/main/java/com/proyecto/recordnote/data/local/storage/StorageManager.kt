// data/local/storage/StorageManager.kt
package com.proyecto.recordnote.data.local.storage

import android.content.Context
import android.os.Environment
import android.os.StatFs
import java.io.File

object StorageManager {

    /**
     * Obtiene directorio de audios
     */
    fun getAudioDirectory(context: Context): File {
        return File(context.getExternalFilesDir("RecordNote/Audio"), "").apply {
            if (!exists()) mkdirs()
        }
    }

    /**
     * Obtiene directorio de transcripciones
     */
    fun getTranscriptionDirectory(context: Context): File {
        return File(context.getExternalFilesDir("RecordNote/Transcriptions"), "").apply {
            if (!exists()) mkdirs()
        }
    }

    /**
     * Obtiene directorio de backups
     */
    fun getBackupDirectory(context: Context): File {
        return File(context.getExternalFilesDir("RecordNote/Backup"), "").apply {
            if (!exists()) mkdirs()
        }
    }

    /**
     * Obtiene directorio de exportación
     */
    fun getExportDirectory(context: Context): File {
        return File(context.getExternalFilesDir("RecordNote/Export"), "").apply {
            if (!exists()) mkdirs()
        }
    }

    /**
     * Obtiene directorio temporal
     */
    fun getTempDirectory(context: Context): File {
        return File(context.getExternalFilesDir("RecordNote/Temp"), "").apply {
            if (!exists()) mkdirs()
        }
    }

    /**
     * Verifica si hay almacenamiento disponible
     */
    fun isExternalStorageAvailable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    /**
     * Verifica espacio disponible (en MB)
     * ✅ CORREGIDO: Ahora retorna Long correctamente
     */
    fun getAvailableSpaceInMB(context: Context): Long {
        val externalFilesDir = context.getExternalFilesDir(null) ?: return 0L
        val stat = StatFs(externalFilesDir.absolutePath)

        return try {
            // Convertir a Long: bloques disponibles × tamaño de bloque / 1024 / 1024
            val availableBlocks = stat.availableBlocksLong
            val blockSize = stat.blockSizeLong

            (availableBlocks * blockSize) / (1024 * 1024)
        } catch (e: Exception) {
            0L
        }
    }

    /**
     * Obtiene espacio total (en MB)
     */
    fun getTotalSpaceInMB(context: Context): Long {
        val externalFilesDir = context.getExternalFilesDir(null) ?: return 0L
        val stat = StatFs(externalFilesDir.absolutePath)

        return try {
            val totalBlocks = stat.blockCountLong
            val blockSize = stat.blockSizeLong

            (totalBlocks * blockSize) / (1024 * 1024)
        } catch (e: Exception) {
            0L
        }
    }

    /**
     * Obtiene espacio usado (en MB)
     */
    fun getUsedSpaceInMB(context: Context): Long {
        return getTotalSpaceInMB(context) - getAvailableSpaceInMB(context)
    }

    /**
     * Limpia archivos temporales antiguos (más de 7 días)
     */
    fun cleanupOldTempFiles(context: Context) {
        val tempDir = getTempDirectory(context)
        val now = System.currentTimeMillis()
        val sevenDaysMs = 7 * 24 * 60 * 60 * 1000L

        tempDir.listFiles()?.forEach { file ->
            if (now - file.lastModified() > sevenDaysMs) {
                file.delete()
            }
        }
    }

    /**
     * Obtiene el tamaño de un directorio en MB
     */
    fun getDirectorySizeInMB(directory: File): Long {
        return if (directory.exists()) {
            directory.walkTopDown().map { it.length() }.sum() / (1024 * 1024)
        } else {
            0L
        }
    }
}
