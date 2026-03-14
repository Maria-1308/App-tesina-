package com.example.poderjudicialkotlin.data.network

import retrofit2.Response
import retrofit2.http.GET

data class NoticiaDto(
    val image: String? = null,
    val link: String? = null,
    val message: String? = null
)

interface NoticiasApi {

    @GET("difusion/noticias-recientes")
    suspend fun getNoticiasRecientes(): Response<List<NoticiaDto>>
}