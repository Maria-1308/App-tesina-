package com.example.poderjudicialkotlin.data.network



import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// Lo que manda React Native:
// { username, password, tipoEmpleado }
data class LoginRequest(
    val username: String,
    val password: String,
    val tipoEmpleado: String
)

// ⚠️ Aún no sabemos la respuesta real de tu API.
// Por ahora lo dejamos flexible con campos comunes.
// Luego lo ajustamos cuando veamos handleResponse / respuesta.
data class LoginResponse(
    val token: String? = null,
    val message: String? = null
)

interface ApiService {
    @POST("login")
    suspend fun login(@Body body: LoginRequest): Response<LoginResponse>
}