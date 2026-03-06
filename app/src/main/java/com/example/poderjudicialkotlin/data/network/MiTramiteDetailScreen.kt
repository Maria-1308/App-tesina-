package com.example.poderjudicialkotlin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.poderjudicialkotlin.data.network.RetrofitClient

@Composable
fun MiTramiteDetailScreen(id: Int) {
    val context = LocalContext.current
    val session = remember { SessionManager(context) }

    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var detail by remember { mutableStateOf<com.example.poderjudicialkotlin.data.network.MiTramiteDetailDto?>(null) }

    LaunchedEffect(id) {
        val token = session.getToken()
        if (token.isNullOrBlank()) {
            loading = false
            error = "Sesión inválida"
            return@LaunchedEffect
        }

        try {
            val res = RetrofitClient.tramitesApi.getMiTramiteDetail(id, "Bearer $token")
            loading = false

            if (res.isSuccessful) {
                val body = res.body()
                if (body?.success == true) {
                    detail = body.data
                    if (detail == null) error = "No se encontró el detalle."
                } else {
                    error = body?.message ?: "No se pudo cargar el detalle."
                }
            } else {
                error = "Error (${res.code()})"
            }
        } catch (e: Exception) {
            loading = false
            error = "Error de conexión"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Detalle de mi trámite", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(12.dp))

        when {
            loading -> CircularProgressIndicator()
            error != null -> Text(error!!)
            detail != null -> {
                Text("Trámite: ${detail!!.tramiteNombre ?: "-"}")
                Text("Estatus: ${detail!!.estatus ?: "-"}")
                Text("Fecha: ${detail!!.fecha ?: "-"}")
                Spacer(modifier = Modifier.height(10.dp))
                Text("Observaciones:")
                Text(detail!!.observaciones ?: "-")
            }
        }
    }
}