// data/remote/RetrofitClient.kt
package com.proyecto.recordnote.data.remote

import com.google.gson.GsonBuilder
import com.proyecto.recordnote.data.remote.adapter.LocalDateTimeAdapter
import com.proyecto.recordnote.data.remote.api.NotaApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

object RetrofitClient {

    // Para emulador: 10.0.2.2
    // Para dispositivo físico: tu IP local (ej: 192.168.1.50)
    private const val BASE_URL = "http://10.0.2.2:8080/"

    private val loggingInterceptor = HttpLoggingInterceptor { message ->
        Timber.tag("OkHttp").d(message)
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        .create()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val notaApiService: NotaApiService by lazy {
        retrofit.create(NotaApiService::class.java)
    }
}
