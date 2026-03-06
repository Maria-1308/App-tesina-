package com.example.poderjudicialkotlin.data.network

data class TramiteResponse(
    val success: Boolean? = null,
    val message: String? = null,
    val errors: List<String>? = null,
    val data: List<TramiteDto>? = null
)

data class TramiteDto(
    val id: Int? = null,
    val nombre: String? = null,
    val descripcion: String? = null
)

data class SolicitudResponse(
    val success: Boolean? = null,
    val message: String? = null,
    val errors: List<String>? = null,
    val data: List<SolicitudDto>? = null
)

data class SolicitudDto(
    val id: Int? = null,
    val tramiteNombre: String? = null,
    val estatus: String? = null,
    val fecha: String? = null


)

data class SolicitudDetailResponse(
    val success: Boolean? = null,
    val message: String? = null,
    val errors: List<String>? = null,
    val data: SolicitudDetailDto? = null
)

data class SolicitudDetailDto(
    val id: Int? = null,
    val tramiteNombre: String? = null,
    val estatus: String? = null,
    val fecha: String? = null,
    val observaciones: String? = null
)

data class MiTramiteDetailResponse(
    val success: Boolean? = null,
    val message: String? = null,
    val errors: List<String>? = null,
    val data: MiTramiteDetailDto? = null
)

data class MiTramiteDetailDto(
    val id: Int? = null,
    val tramiteNombre: String? = null,
    val estatus: String? = null,
    val fecha: String? = null,
    val observaciones: String? = null
)