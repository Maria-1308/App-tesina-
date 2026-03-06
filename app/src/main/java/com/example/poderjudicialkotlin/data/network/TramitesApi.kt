package com.example.poderjudicialkotlin.data.network

import okhttp3.ResponseBody

import retrofit2.http.Path
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface TramitesApi {

    @GET("personal/tramites")
    suspend fun getTramites(
        @Header("Authorization") bearer: String
    ): Response<TramiteResponse>

    @GET("personal/solicitudes-tramites")
    suspend fun getMisSolicitudes(
        @Header("Authorization") bearer: String
    ): Response<SolicitudResponse>

    @GET("personal/solicitudes-tramites/{id}")
    suspend fun getSolicitudDetail(
        @Path("id") id: Int,
        @Header("Authorization") bearer: String
    ): Response<SolicitudDetailResponse>

    @GET("personal/solicitudes-tramites/{id}")
    suspend fun getMiTramiteDetail(
        @Path("id") id: Int,
        @Header("Authorization") bearer: String
    ): Response<MiTramiteDetailResponse>


}













