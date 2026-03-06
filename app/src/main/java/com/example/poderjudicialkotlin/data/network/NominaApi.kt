package com.example.poderjudicialkotlin.data.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

// ----- LISTA DE NÓMINAS (GET /nominas/comprobantes/{anio}) -----
data class NominasResponse(
    val success: Boolean? = null,
    val message: String? = null,
    val errors: List<String>? = null,
    val data: List<NominaDto>? = null
)

data class NominaDto(
    val quincena: String? = null,
    val folio: String? = null,
    val tablan: String? = null,
    val añoClave: Int? = null
)

// ----- GENERAR ACCESO (POST /nominas/comprobantes/generar-acceso) -----
data class GenerarAccesoResponse(
    val success: Boolean? = null,
    val message: String? = null,
    val errors: List<String>? = null,
    val data: GenerarAccesoData? = null
)

data class GenerarAccesoData(
    val codigoAcceso: String? = null
)

data class GenerarAccesoRequest(
    val quincena: String? = null,
    val folio: String? = null,
    val tablan: String? = null,
    val añoClave: Int? = null
)

interface NominaApi {

    @GET("nominas/comprobantes/{anio}")
    suspend fun getNominas(
        @Path("anio") anio: Int,
        @Header("Authorization") bearer: String
    ): Response<NominasResponse>


    @POST("nominas/comprobantes/generar-acceso")
    suspend fun generarAcceso(
        @Header("Authorization") bearer: String,
        @Body nomina: GenerarAccesoRequest
    ): Response<GenerarAccesoResponse>


    @GET("nominas/comprobantes/{codigoAcceso}/comprobante")
    suspend fun descargarComprobantePdf(
        @Path("codigoAcceso") codigoAcceso: String
    ): Response<ResponseBody>

}

