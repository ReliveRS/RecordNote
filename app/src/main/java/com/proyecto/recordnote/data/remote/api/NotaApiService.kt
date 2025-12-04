// data/remote/api/NotaApiService.kt
package com.proyecto.recordnote.data.remote.api

import com.proyecto.recordnote.data.remote.dto.NotaDto
import retrofit2.Response
import retrofit2.http.*

interface NotaApiService {

    @GET("api/notas")
    suspend fun obtenerTodasLasNotas(): Response<List<NotaDto>>

    @GET("api/notas/{id}")
    suspend fun obtenerNotaPorId(
        @Path("id") id: String
    ): Response<NotaDto>

    @POST("api/notas")
    suspend fun crearNota(
        @Body nota: NotaDto
    ): Response<NotaDto>

    @PUT("api/notas/{id}")
    suspend fun actualizarNota(
        @Path("id") id: String,
        @Body nota: NotaDto
    ): Response<NotaDto>

    @DELETE("api/notas/{id}")
    suspend fun eliminarNota(
        @Path("id") id: String
    ): Response<Unit>

    @GET("api/notas/buscar")
    suspend fun buscarNotas(
        @Query("query") query: String
    ): Response<List<NotaDto>>
}
