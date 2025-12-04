package com.proyecto.recordnote.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.proyecto.recordnote.data.local.dao.CategoriaDao
import com.proyecto.recordnote.data.local.dao.NotaDao
import com.proyecto.recordnote.data.local.dao.UsuarioDao
import com.proyecto.recordnote.data.local.entidades.CategoriaEntity
import com.proyecto.recordnote.data.local.entidades.NotaEntity
import com.proyecto.recordnote.data.local.entidades.UsuarioEntity
import timber.log.Timber
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Base de datos principal de RecordNote usando Room
 * Define las entidades, DAOs y configuración de la base de datos
 *
 * Versión 1: Implementación con tablas de notas, usuarios y categorías
 *
 * ¿Por qué Room?
 * - ORM (Object-Relational Mapping) para SQLite
 * - Compilación en tiempo de compilación (errores detectados antes)
 * - Queries type-safe
 * - Soporte para Coroutines y Flow
 */
@Database(
    entities = [
        NotaEntity::class,
        UsuarioEntity::class,
        CategoriaEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Proporciona acceso al DAO de notas
     */
    abstract fun notaDao(): NotaDao

    /**
     * Proporciona acceso al DAO de usuarios
     */
    abstract fun usuarioDao(): UsuarioDao

    /**
     * Proporciona acceso al DAO de categorías
     */
    abstract fun categoriaDao(): CategoriaDao

    companion object {
        /**
         * Nombre de la base de datos en el dispositivo
         */
        const val DATABASE_NAME = "recordnote_database"

        /**
         * Versión actual de la base de datos
         */
        const val DATABASE_VERSION = 1

        /**
         * Instancia singleton de la base de datos
         */
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Obtiene la instancia única de la base de datos
         * Thread-safe usando doble verificación
         *
         * ¿Por qué @Volatile?
         * - Garantiza que los cambios sean visibles entre threads
         * - Evita race conditions en acceso concurrente
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    // ⚠️ SOLO PARA DESARROLLO
                    // En producción, implementar migraciones
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                Timber.d("✅ Base de datos inicializada")
                instance
            }
        }

        /**
         * Cierra la base de datos y limpia la instancia
         * Útil para testing o logout
         */
        fun closeDatabase() {
            INSTANCE?.close()
            INSTANCE = null
            Timber.d("Base de datos cerrada")
        }
    }
}

/**
 * Clase de conversores de tipos para Room
 * Permite almacenar tipos complejos en SQLite (que solo soporta tipos primitivos)
 *
 * ¿Cómo funciona?
 * - @TypeConverter marca el método como convertidor
 * - Room los llama automáticamente en INSERT/SELECT
 * - Permiten usar tipos como LocalDateTime, List<String>, etc
 */
class Converters {

    private val gson = Gson()
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    // ========== CONVERSORES PARA LOCALDATETIME ==========

    /**
     * Convierte LocalDateTime a String para almacenar en BD
     * Formato ISO 8601: 2025-11-04T19:30:45
     */
    @TypeConverter
    fun fromLocalDateTime(date: LocalDateTime?): String? {
        return try {
            date?.format(dateFormatter)
        } catch (e: Exception) {
            Timber.e(e, "Error al convertir LocalDateTime")
            null
        }
    }

    /**
     * Convierte String a LocalDateTime al leer de BD
     * Revierte el formato ISO 8601
     */
    @TypeConverter
    fun toLocalDateTime(dateString: String?): LocalDateTime? {
        return try {
            dateString?.let { LocalDateTime.parse(it, dateFormatter) }
        } catch (e: Exception) {
            Timber.e(e, "Error al parsear LocalDateTime: $dateString")
            null
        }
    }

    // ========== CONVERSORES PARA LISTA DE STRINGS ==========

    /**
     * Convierte lista de strings (etiquetas) a JSON para almacenar
     * Ejemplo: ["importante", "reunión"] → '["importante","reunión"]'
     */
    @TypeConverter
    fun fromStringList(list: List<String>?): String? {
        return try {
            list?.let { gson.toJson(it) }
        } catch (e: Exception) {
            Timber.e(e, "Error al convertir List<String>")
            null
        }
    }

    /**
     * Convierte JSON a lista de strings al leer de BD
     * Ejemplo: '["importante","reunión"]' → ["importante", "reunión"]
     */
    @TypeConverter
    fun toStringList(json: String?): List<String> {
        if (json.isNullOrBlank()) return emptyList()

        return try {
            val type = object : TypeToken<List<String>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            Timber.e(e, "Error al parsear List<String>: $json")
            emptyList()
        }
    }

    // ========== CONVERSORES PARA MAP (Optional, para futuros usos) ==========

    /**
     * Convierte Map a JSON (útil para metadatos adicionales)
     */
    @TypeConverter
    fun fromStringMap(map: Map<String, String>?): String? {
        return try {
            map?.let { gson.toJson(it) }
        } catch (e: Exception) {
            Timber.e(e, "Error al convertir Map")
            null
        }
    }

    /**
     * Convierte JSON a Map
     */
    @TypeConverter
    fun toStringMap(json: String?): Map<String, String> {
        if (json.isNullOrBlank()) return emptyMap()

        return try {
            val type = object : TypeToken<Map<String, String>>() {}.type
            gson.fromJson(json, type) ?: emptyMap()
        } catch (e: Exception) {
            Timber.e(e, "Error al parsear Map: $json")
            emptyMap()
        }
    }
}
