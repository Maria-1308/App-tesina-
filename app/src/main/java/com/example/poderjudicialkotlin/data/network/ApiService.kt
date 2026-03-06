package com.example.poderjudicialkotlin.data.network





import okhttp3.ResponseBody
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

// Aún no sabemos la respuesta real de tu API.
// Por ahora lo dejamos flexible con campos comunes.
// Luego lo ajustamos cuando veamos handleResponse / respuesta.
data class LoginResponse(
    val success: Boolean? = null,
    val message: String? = null,
    val data: LoginData? = null
)

data class LoginData(
    val authToken: String? = null
)

interface ApiService {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>


}
