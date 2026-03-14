package com.example.poderjudicialkotlin.data.network


import okhttp3.ResponseBody

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

data class IdentificacionResponse(
    val success: Boolean? = null,
    val message: String? = null,
    val errors: List<String>? = null,
    val data: IdentificacionDto? = null
)

data class IdentificacionDto(
    val identificacionGuid: String? = null,
    val numeroEmpleado: Int? = null,
    val nombre: String? = null,
    val apellidoPaterno: String? = null,
    val apellidoMaterno: String? = null,
    val curp: String? = null,
    val adscripcionNombre: String? = null,
    val plazaNombre: String? = null,
    val municipioNombre: String? = null,
    val puestoNombre: String? = null,
    val tipoSangre: String? = null,
    val alergias: String? = null,
    val añoExpedicion: Int? = null
)
interface IdentificacionApi {

    @GET("identificacion")
    suspend fun getIdentificacion(
        @Header("Authorization") bearer: String
    ): Response<IdentificacionResponse>



    @GET("identificacion/{guid}/foto")
    suspend fun getFoto(
        @Path("guid") guid: String,
        @Header("Authorization") bearer: String
    ): Response<ResponseBody>
}