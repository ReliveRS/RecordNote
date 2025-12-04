package com.proyecto.recordnote.utils

import android.content.Context
import com.google.gson.GsonBuilder
import com.proyecto.recordnote.domain.model.NotaDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileWriter
import java.time.LocalDateTime
//import javax.inject.Inject
//import javax.inject.Singleton

/**
 * Gestor de exportación de notas
 * Soporta múltiples formatos: JSON, TXT, CSV, Markdown
 */
//@Singleton
class GestorExportacion () {

    /**
     * Exporta notas al formato especificado
     * @param context Contexto de la aplicación
     * @param notas Lista de notas a exportar
     * @param formato Formato de exportación
     * @return Result con ruta del archivo exportado o error
     */
    suspend fun exportarNotas(
        context: Context,
        notas: List<NotaDomain>,
        formato: FormatoExportacion
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            if (notas.isEmpty()) {
                return@withContext Result.failure(Exception("No hay notas para exportar"))
            }

            val exportFile = crearArchivoExportacion(context, formato)

            when (formato) {
                FormatoExportacion.JSON -> exportarJSON(notas, exportFile)
                FormatoExportacion.TXT -> exportarTXT(notas, exportFile)
                FormatoExportacion.CSV -> exportarCSV(notas, exportFile)
                FormatoExportacion.MARKDOWN -> exportarMarkdown(notas, exportFile)
                FormatoExportacion.HTML -> exportarHTML(notas, exportFile)
            }

            Result.success(exportFile.absolutePath)
        } catch (e: Exception) {
            android.util.Log.e(Constants.LOG_TAG, "Error al exportar: ${e.message}")
            Result.failure(Exception("Error al exportar notas: ${e.message}"))
        }
    }

    /**
     * Exporta una sola nota
     * @param context Contexto de la aplicación
     * @param nota Nota a exportar
     * @param formato Formato de exportación
     * @return Result con ruta del archivo o error
     */
    suspend fun exportarNota(
        context: Context,
        nota: NotaDomain,
        formato: FormatoExportacion
    ): Result<String> {
        return exportarNotas(context, listOf(nota), formato)
    }

    // ========== MÉTODOS DE EXPORTACIÓN POR FORMATO ==========

    /**
     * Exporta notas a formato JSON
     */
    private fun exportarJSON(notas: List<NotaDomain>, file: File) {
        val gson = GsonBuilder()
            .setPrettyPrinting()
            .create()

        val jsonData = mapOf(
            "exportDate" to LocalDateTime.now().toString(),
            "totalNotes" to notas.size,
            "notes" to notas.map { nota ->
                mapOf(
                    "id" to nota.id,
                    "title" to nota.titulo,
                    "content" to nota.contenido,
                    "tags" to nota.etiquetas,
                    "favorite" to nota.esFavorita,
                    "hasAudio" to nota.tieneAudio(),
                    "transcribed" to nota.esTranscrita,
                    "createdAt" to nota.fechaCreacion.toString(),
                    "updatedAt" to nota.fechaModificacion.toString()
                )
            }
        )

        FileWriter(file).use { writer ->
            gson.toJson(jsonData, writer)
        }
    }

    /**
     * Exporta notas a formato TXT plano
     */
    private fun exportarTXT(notas: List<NotaDomain>, file: File) {
        FileWriter(file).use { writer ->
            writer.appendLine("=" .repeat(80))
            writer.appendLine("EXPORTACIÓN DE NOTAS - RecordNote")
            writer.appendLine("Fecha: ${DateUtils.formatDateTime(LocalDateTime.now())}")
            writer.appendLine("Total de notas: ${notas.size}")
            writer.appendLine("=".repeat(80))
            writer.appendLine()

            notas.forEachIndexed { index, nota ->
                writer.appendLine("NOTA ${index + 1}")
                writer.appendLine("-".repeat(80))
                writer.appendLine("Título: ${nota.titulo}")
                writer.appendLine("Fecha: ${DateUtils.formatDateTime(nota.fechaModificacion)}")

                if (nota.etiquetas.isNotEmpty()) {
                    writer.appendLine("Etiquetas: ${nota.etiquetas.joinToString(", ")}")
                }

                if (nota.esFavorita) {
                    writer.appendLine("★ Favorita")
                }

                if (nota.tieneAudio()) {
                    writer.appendLine("🎤 Tiene audio (${nota.obtenerDuracionFormateada()})")
                }

                writer.appendLine()
                writer.appendLine("Contenido:")
                writer.appendLine(nota.contenido)
                writer.appendLine()
                writer.appendLine("=".repeat(80))
                writer.appendLine()
            }
        }
    }

    /**
     * Exporta notas a formato CSV
     */
    private fun exportarCSV(notas: List<NotaDomain>, file: File) {
        FileWriter(file).use { writer ->
            // Encabezados
            writer.appendLine("ID,Título,Contenido,Etiquetas,Favorita,Tiene Audio,Transcrita,Fecha Creación,Fecha Modificación")

            // Datos
            notas.forEach { nota ->
                val row = listOf(
                    escapeCsv(nota.id),
                    escapeCsv(nota.titulo),
                    escapeCsv(nota.contenido),
                    escapeCsv(nota.etiquetas.joinToString(";")),
                    nota.esFavorita.toString(),
                    nota.tieneAudio().toString(),
                    nota.esTranscrita.toString(),
                    nota.fechaCreacion.toString(),
                    nota.fechaModificacion.toString()
                )
                writer.appendLine(row.joinToString(","))
            }
        }
    }

    /**
     * Exporta notas a formato Markdown
     */
    private fun exportarMarkdown(notas: List<NotaDomain>, file: File) {
        FileWriter(file).use { writer ->
            writer.appendLine("# Exportación de Notas - RecordNote")
            writer.appendLine()
            writer.appendLine("**Fecha de exportación:** ${DateUtils.formatDateTime(LocalDateTime.now())}")
            writer.appendLine()
            writer.appendLine("**Total de notas:** ${notas.size}")
            writer.appendLine()
            writer.appendLine("---")
            writer.appendLine()

            notas.forEach { nota ->
                writer.appendLine("## ${nota.titulo}")
                writer.appendLine()

                // Metadatos
                val metadatos = mutableListOf<String>()
                metadatos.add("📅 ${DateUtils.formatDateTime(nota.fechaModificacion)}")

                if (nota.etiquetas.isNotEmpty()) {
                    metadatos.add("🏷️ ${nota.etiquetas.joinToString(", ") { "`$it`" }}")
                }

                if (nota.esFavorita) {
                    metadatos.add("⭐ Favorita")
                }

                if (nota.tieneAudio()) {
                    metadatos.add("🎤 Audio (${nota.obtenerDuracionFormateada()})")
                }

                writer.appendLine(metadatos.joinToString(" · "))
                writer.appendLine()

                // Contenido
                writer.appendLine(nota.contenido)
                writer.appendLine()
                writer.appendLine("---")
                writer.appendLine()
            }
        }
    }

    /**
     * Exporta notas a formato HTML
     */
    private fun exportarHTML(notas: List<NotaDomain>, file: File) {
        FileWriter(file).use { writer ->
            writer.appendLine("<!DOCTYPE html>")
            writer.appendLine("<html lang='es'>")
            writer.appendLine("<head>")
            writer.appendLine("    <meta charset='UTF-8'>")
            writer.appendLine("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>")
            writer.appendLine("    <title>Exportación de Notas - RecordNote</title>")
            writer.appendLine("    <style>")
            writer.appendLine("""
                body { font-family: Arial, sans-serif; max-width: 900px; margin: 0 auto; padding: 20px; }
                h1 { color: #333; border-bottom: 3px solid #4CAF50; padding-bottom: 10px; }
                .note { border: 1px solid #ddd; margin: 20px 0; padding: 15px; border-radius: 8px; }
                .note-title { color: #2196F3; font-size: 1.5em; margin-bottom: 10px; }
                .note-meta { color: #666; font-size: 0.9em; margin-bottom: 10px; }
                .note-content { line-height: 1.6; white-space: pre-wrap; }
                .tag { background: #E3F2FD; padding: 2px 8px; border-radius: 12px; margin-right: 5px; }
                .favorite { color: #FFD700; }
            """.trimIndent())
            writer.appendLine("    </style>")
            writer.appendLine("</head>")
            writer.appendLine("<body>")
            writer.appendLine("    <h1>📝 Exportación de Notas - RecordNote</h1>")
            writer.appendLine("    <p><strong>Fecha:</strong> ${DateUtils.formatDateTime(LocalDateTime.now())}</p>")
            writer.appendLine("    <p><strong>Total de notas:</strong> ${notas.size}</p>")
            writer.appendLine("    <hr>")

            notas.forEach { nota ->
                writer.appendLine("    <div class='note'>")
                writer.appendLine("        <div class='note-title'>")
                if (nota.esFavorita) writer.append("<span class='favorite'>⭐</span> ")
                writer.appendLine("${escapeHtml(nota.titulo)}</div>")

                writer.append("        <div class='note-meta'>")
                writer.append("📅 ${DateUtils.formatDateTime(nota.fechaModificacion)}")

                if (nota.tieneAudio()) {
                    writer.append(" · 🎤 Audio (${nota.obtenerDuracionFormateada()})")
                }

                writer.appendLine("</div>")

                if (nota.etiquetas.isNotEmpty()) {
                    writer.append("        <div class='note-meta'>")
                    nota.etiquetas.forEach { tag ->
                        writer.append("<span class='tag'>${escapeHtml(tag)}</span>")
                    }
                    writer.appendLine("</div>")
                }

                writer.appendLine("        <div class='note-content'>${escapeHtml(nota.contenido)}</div>")
                writer.appendLine("    </div>")
            }

            writer.appendLine("</body>")
            writer.appendLine("</html>")
        }
    }

    // ========== MÉTODOS AUXILIARES ==========

    /**
     * Crea archivo de exportación con nombre único
     */
    private fun crearArchivoExportacion(context: Context, formato: FormatoExportacion): File {
        val exportDir = FileUtils.getExportDirectory(context)
        val timestamp = DateUtils.formatForFilename()
        val extension = formato.extension
        val fileName = "RecordNote_Export_${timestamp}.$extension"
        return File(exportDir, fileName)
    }

    /**
     * Escapa caracteres especiales para CSV
     */
    private fun escapeCsv(value: String): String {
        val escaped = value.replace("\"", "\"\"")
        return if (escaped.contains(",") || escaped.contains("\n") || escaped.contains("\"")) {
            "\"$escaped\""
        } else {
            escaped
        }
    }

    /**
     * Escapa caracteres HTML
     */
    private fun escapeHtml(value: String): String {
        return value
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;")
    }
}

/**
 * Formatos de exportación disponibles
 */
enum class FormatoExportacion(val extension: String, val descripcion: String) {
    JSON("json", "JSON - Formato estructurado"),
    TXT("txt", "Texto plano"),
    CSV("csv", "CSV - Compatible con Excel"),
    MARKDOWN("md", "Markdown - Formato enriquecido"),
    HTML("html", "HTML - Visualización web")
}