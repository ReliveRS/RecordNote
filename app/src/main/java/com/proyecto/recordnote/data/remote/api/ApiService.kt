package com.proyecto.recordnote.data.remote.api


import com.proyecto.recordnote.data.remote.dto.NotaDto
import retrofit2.Response
import retrofit2.http.*

/**
 * Servicio API REST para operaciones CRUD de notas
 * Define los endpoints del servidor backend
 */
interface ApiService {

    // ========== OPERACIONES CRUD ==========

    /**
     * Obtiene todas las notas de un usuario desde el servidor
     * @param usuarioId ID del usuario
     * @return Response con lista de NotaDto
     */
    @GET("api/v1/notas/usuario/{usuarioId}")
    suspend fun obtenerNotas(
        @Path("usuarioId") usuarioId: String
    ): Response<List<NotaDto>>

    /**
     * Obtiene una nota específica por su ID
     * @param notaId ID de la nota
     * @return Response con NotaDto
     */
    @GET("api/v1/notas/{notaId}")
    suspend fun obtenerNotaPorId(
        @Path("notaId") notaId: String
    ): Response<NotaDto>

    /**
     * Crea una nueva nota en el servidor
     * @param nota DTO con los datos de la nota
     * @return Response con la nota creada (incluye ID generado)
     */
    @POST("api/v1/notas")
    suspend fun crearNota(
        @Body nota: NotaDto
    ): Response<NotaDto>

    /**
     * Actualiza una nota existente en el servidor
     * @param notaId ID de la nota a actualizar
     * @param nota DTO con los datos actualizados
     * @return Response con la nota actualizada
     */
    @PUT("api/v1/notas/{notaId}")
    suspend fun actualizarNota(
        @Path("notaId") notaId: String,
        @Body nota: NotaDto
    ): Response<NotaDto>

    /**
     * Elimina una nota del servidor
     * @param notaId ID de la nota a eliminar
     * @return Response vacía indicando éxito o error
     */
    @DELETE("api/v1/notas/{notaId}")
    suspend fun eliminarNota(
        @Path("notaId") notaId: String
    ): Response<Unit>

    // ========== SINCRONIZACIÓN ==========

    /**
     * Sincroniza múltiples notas en una sola petición
     * Útil para sincronización por lotes
     * @param notas Lista de notas a sincronizar
     * @return Response con lista de notas sincronizadas
     */
    @POST("api/v1/notas/sincronizar")
    suspend fun sincronizarNotas(
        @Body notas: List<NotaDto>
    ): Response<List<NotaDto>>

    /**
     * Obtiene las notas actualizadas desde una fecha específica
     * Optimiza la sincronización obteniendo solo cambios recientes
     * @param usuarioId ID del usuario
     * @param desdeFecha Timestamp en formato ISO (ej: "2024-01-01T00:00:00")
     * @return Response con notas modificadas desde esa fecha
     */
    @GET("api/v1/notas/actualizaciones/{usuarioId}")
    suspend fun obtenerActualizaciones(
        @Path("usuarioId") usuarioId: String,
        @Query("desde") desdeFecha: String
    ): Response<List<NotaDto>>

    // ========== BÚSQUEDA Y FILTRADO ==========

    /**
     * Busca notas por texto en el servidor
     * @param usuarioId ID del usuario
     * @param query Texto a buscar
     * @return Response con notas que coinciden
     */
    @GET("api/v1/notas/buscar/{usuarioId}")
    suspend fun buscarNotas(
        @Path("usuarioId") usuarioId: String,
        @Query("q") query: String
    ): Response<List<NotaDto>>

    /**
     * Obtiene notas filtradas por etiqueta
     * @param usuarioId ID del usuario
     * @param etiqueta Etiqueta a filtrar
     * @return Response con notas filtradas
     */
    @GET("api/v1/notas/etiqueta/{usuarioId}")
    suspend fun obtenerPorEtiqueta(
        @Path("usuarioId") usuarioId: String,
        @Query("tag") etiqueta: String
    ): Response<List<NotaDto>>

    /**
     * Obtiene solo las notas favoritas del usuario
     * @param usuarioId ID del usuario
     * @return Response con notas favoritas
     */
    @GET("api/v1/notas/favoritas/{usuarioId}")
    suspend fun obtenerFavoritas(
        @Path("usuarioId") usuarioId: String
    ): Response<List<NotaDto>>

    // ========== GESTIÓN DE ARCHIVOS DE AUDIO ==========

    /**
     * Sube un archivo de audio al servidor
     * @param notaId ID de la nota asociada
     * @param audio Datos del archivo en formato multipart
     * @return Response con URL del audio subido
     */
    @Multipart
    @POST("api/v1/notas/{notaId}/audio")
    suspend fun subirAudio(
        @Path("notaId") notaId: String,
        @Part("audio") audio: okhttp3.MultipartBody.Part
    ): Response<UploadAudioResponse>

    /**
     * Elimina el archivo de audio de una nota
     * @param notaId ID de la nota
     * @return Response indicando éxito
     */
    @DELETE("api/v1/notas/{notaId}/audio")
    suspend fun eliminarAudio(
        @Path("notaId") notaId: String
    ): Response<Unit>

    // ========== OPERACIONES MASIVAS ==========

    /**
     * Elimina múltiples notas en una sola petición
     * @param notasIds DTO con lista de IDs a eliminar
     * @return Response con número de notas eliminadas
     */
    @HTTP(method = "DELETE", path = "api/v1/notas/batch", hasBody = true)
    suspend fun eliminarVarias(
        @Body notasIds: EliminarVariasRequest
    ): Response<EliminarVariasResponse>

    /**
     * Exporta todas las notas del usuario en formato específico
     * @param usuarioId ID del usuario
     * @param formato Formato de exportación ("json", "txt", "pdf")
     * @return Response con URL del archivo exportado
     */
    @GET("api/v1/notas/exportar/{usuarioId}")
    suspend fun exportarNotas(
        @Path("usuarioId") usuarioId: String,
        @Query("formato") formato: String
    ): Response<ExportarResponse>
}

/**
 * Respuesta al subir un archivo de audio
 * @property audioUrl URL del audio en el servidor
 * @property duracionSegundos Duración del audio procesado
 */
data class UploadAudioResponse(
    val audioUrl: String,
    val duracionSegundos: Int
)

/**
 * Request para eliminar múltiples notas
 * @property ids Lista de IDs de notas a eliminar
 */
data class EliminarVariasRequest(
    val ids: List<String>
)

/**
 * Respuesta de eliminación masiva
 * @property eliminadas Número de notas eliminadas exitosamente
 */
data class EliminarVariasResponse(
    val eliminadas: Int
)

/**
 * Respuesta de exportación
 * @property archivoUrl URL del archivo exportado
 * @property formato Formato del archivo
 * @property expiraEn Timestamp de expiración del enlace
 */
data class ExportarResponse(
    val archivoUrl: String,
    val formato: String,
    val expiraEn: String
)