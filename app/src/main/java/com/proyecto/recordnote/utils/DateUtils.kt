package com.proyecto.recordnote.utils


import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

/**
 * Utilidad para operaciones con fechas y horas
 * Proporciona formateo, conversión y cálculos con fechas
 */
object DateUtils {

    private val dateFormatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)
    private val timeFormatter = DateTimeFormatter.ofPattern(Constants.TIME_FORMAT)
    private val dateTimeFormatter = DateTimeFormatter.ofPattern(Constants.DATETIME_FORMAT)
    private val isoFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    private val filenameFormatter = DateTimeFormatter.ofPattern(Constants.FILENAME_DATETIME_FORMAT)

    /**
     * Formatea una fecha a formato legible (ej: "01/01/2024")
     * @param date Fecha a formatear
     * @return String con fecha formateada
     */
    fun formatDate(date: LocalDateTime): String {
        return date.format(dateFormatter)
    }

    /**
     * Formatea una hora a formato legible (ej: "14:30")
     * @param date Fecha con hora a formatear
     * @return String con hora formateada
     */
    fun formatTime(date: LocalDateTime): String {
        return date.format(timeFormatter)
    }

    /**
     * Formatea fecha y hora completa (ej: "01/01/2024 14:30")
     * @param date Fecha a formatear
     * @return String con fecha y hora formateadas
     */
    fun formatDateTime(date: LocalDateTime): String {
        return date.format(dateTimeFormatter)
    }

    /**
     * Formatea fecha en formato ISO para APIs
     * @param date Fecha a formatear
     * @return String en formato ISO
     */
    fun formatIso(date: LocalDateTime): String {
        return date.format(isoFormatter)
    }

    /**
     * Formatea fecha para nombres de archivo (ej: "20240101_143000")
     * @param date Fecha a formatear
     * @return String seguro para nombres de archivo
     */
    fun formatForFilename(date: LocalDateTime = LocalDateTime.now()): String {
        return date.format(filenameFormatter)
    }

    /**
     * Convierte String ISO a LocalDateTime
     * @param isoString String en formato ISO
     * @return LocalDateTime o null si formato inválido
     */
    fun parseIso(isoString: String): LocalDateTime? {
        return try {
            LocalDateTime.parse(isoString, isoFormatter)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Formatea fecha relativa (ej: "Hace 5 minutos", "Ayer", "Hace 3 días")
     * @param date Fecha a formatear
     * @return String con tiempo relativo en español
     */
    fun formatRelativeTime(date: LocalDateTime): String {
        val now = LocalDateTime.now()
        val duration = Duration.between(date, now)

        return when {
            duration.toMinutes() < 1 -> "Justo ahora"
            duration.toMinutes() < 60 -> "Hace ${duration.toMinutes()} minutos"
            duration.toHours() < 24 -> {
                val hours = duration.toHours()
                if (hours == 1L) "Hace 1 hora" else "Hace $hours horas"
            }
            duration.toDays() == 1L -> "Ayer a las ${formatTime(date)}"
            duration.toDays() < 7 -> "Hace ${duration.toDays()} días"
            duration.toDays() < 30 -> {
                val weeks = duration.toDays() / 7
                if (weeks == 1L) "Hace 1 semana" else "Hace $weeks semanas"
            }
            duration.toDays() < 365 -> {
                val months = duration.toDays() / 30
                if (months == 1L) "Hace 1 mes" else "Hace $months meses"
            }
            else -> {
                val years = duration.toDays() / 365
                if (years == 1L) "Hace 1 año" else "Hace $years años"
            }
        }
    }

    /**
     * Obtiene el inicio del día para una fecha
     * @param date Fecha base
     * @return LocalDateTime al inicio del día (00:00:00)
     */
    fun startOfDay(date: LocalDateTime = LocalDateTime.now()): LocalDateTime {
        return date.truncatedTo(ChronoUnit.DAYS)
    }

    /**
     * Obtiene el fin del día para una fecha
     * @param date Fecha base
     * @return LocalDateTime al fin del día (23:59:59)
     */
    fun endOfDay(date: LocalDateTime = LocalDateTime.now()): LocalDateTime {
        return date.truncatedTo(ChronoUnit.DAYS)
            .plusDays(1)
            .minusSeconds(1)
    }

    /**
     * Verifica si una fecha es hoy
     * @param date Fecha a verificar
     * @return true si la fecha es hoy
     */
    fun isToday(date: LocalDateTime): Boolean {
        val today = LocalDateTime.now().toLocalDate()
        return date.toLocalDate() == today
    }

    /**
     * Verifica si una fecha fue ayer
     * @param date Fecha a verificar
     * @return true si la fecha fue ayer
     */
    fun isYesterday(date: LocalDateTime): Boolean {
        val yesterday = LocalDateTime.now().minusDays(1).toLocalDate()
        return date.toLocalDate() == yesterday
    }

    /**
     * Verifica si una fecha está en esta semana
     * @param date Fecha a verificar
     * @return true si está en la semana actual
     */
    fun isThisWeek(date: LocalDateTime): Boolean {
        val now = LocalDateTime.now()
        val weekAgo = now.minusWeeks(1)
        return date.isAfter(weekAgo) && date.isBefore(now)
    }

    /**
     * Calcula días transcurridos entre dos fechas
     * @param from Fecha inicial
     * @param to Fecha final (por defecto ahora)
     * @return Número de días transcurridos
     */
    fun daysBetween(from: LocalDateTime, to: LocalDateTime = LocalDateTime.now()): Long {
        return ChronoUnit.DAYS.between(from, to)
    }

    /**
     * Calcula horas transcurridas entre dos fechas
     * @param from Fecha inicial
     * @param to Fecha final (por defecto ahora)
     * @return Número de horas transcurridas
     */
    fun hoursBetween(from: LocalDateTime, to: LocalDateTime = LocalDateTime.now()): Long {
        return ChronoUnit.HOURS.between(from, to)
    }

    /**
     * Formatea duración en segundos a formato legible (MM:SS o HH:MM:SS)
     * @param seconds Duración en segundos
     * @return String formateado
     */
    fun formatDuration(seconds: Int): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60

        return if (hours > 0) {
            String.format("%02d:%02d:%02d", hours, minutes, secs)
        } else {
            String.format("%02d:%02d", minutes, secs)
        }
    }

    /**
     * Formatea duración en milisegundos a formato legible
     * @param milliseconds Duración en milisegundos
     * @return String formateado
     */
    fun formatDurationMillis(milliseconds: Long): String {
        return formatDuration((milliseconds / 1000).toInt())
    }

    /**
     * Obtiene el nombre del día de la semana en español
     * @param date Fecha
     * @return Nombre del día (ej: "Lunes")
     */
    fun getDayOfWeekName(date: LocalDateTime): String {
        return when (date.dayOfWeek) {
            java.time.DayOfWeek.MONDAY -> "Lunes"
            java.time.DayOfWeek.TUESDAY -> "Martes"
            java.time.DayOfWeek.WEDNESDAY -> "Miércoles"
            java.time.DayOfWeek.THURSDAY -> "Jueves"
            java.time.DayOfWeek.FRIDAY -> "Viernes"
            java.time.DayOfWeek.SATURDAY -> "Sábado"
            java.time.DayOfWeek.SUNDAY -> "Domingo"
        }
    }

    /**
     * Obtiene el nombre del mes en español
     * @param date Fecha
     * @return Nombre del mes (ej: "Enero")
     */
    fun getMonthName(date: LocalDateTime): String {
        return when (date.month) {
            java.time.Month.JANUARY -> "Enero"
            java.time.Month.FEBRUARY -> "Febrero"
            java.time.Month.MARCH -> "Marzo"
            java.time.Month.APRIL -> "Abril"
            java.time.Month.MAY -> "Mayo"
            java.time.Month.JUNE -> "Junio"
            java.time.Month.JULY -> "Julio"
            java.time.Month.AUGUST -> "Agosto"
            java.time.Month.SEPTEMBER -> "Septiembre"
            java.time.Month.OCTOBER -> "Octubre"
            java.time.Month.NOVEMBER -> "Noviembre"
            java.time.Month.DECEMBER -> "Diciembre"
        }
    }

    /**
     * Formatea fecha en formato narrativo español
     * Ejemplos: "Hoy a las 14:30", "Ayer a las 09:15", "Lunes 1 de Enero a las 18:00"
     * @param date Fecha a formatear
     * @return String con formato narrativo
     */
    fun formatNarrative(date: LocalDateTime): String {
        return when {
            isToday(date) -> "Hoy a las ${formatTime(date)}"
            isYesterday(date) -> "Ayer a las ${formatTime(date)}"
            isThisWeek(date) -> "${getDayOfWeekName(date)} a las ${formatTime(date)}"
            else -> "${getDayOfWeekName(date)} ${date.dayOfMonth} de ${getMonthName(date)} a las ${formatTime(date)}"
        }
    }

    /**
     * Convierte timestamp en milisegundos a LocalDateTime
     * @param timestamp Timestamp en milisegundos
     * @return LocalDateTime
     */
    fun fromTimestamp(timestamp: Long): LocalDateTime {
        return LocalDateTime.ofInstant(
            java.time.Instant.ofEpochMilli(timestamp),
            ZoneId.systemDefault()
        )
    }

    /**
     * Convierte LocalDateTime a timestamp en milisegundos
     * @param date Fecha a convertir
     * @return Timestamp en milisegundos
     */
    fun toTimestamp(date: LocalDateTime): Long {
        return date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
}