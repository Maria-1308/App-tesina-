package com.example.poderjudicialkotlin



import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.poderjudicialkotlin.data.network.RetrofitClient
import com.example.poderjudicialkotlin.data.network.TramiteDto
import com.example.poderjudicialkotlin.data.network.SolicitudDto

@Composable
fun TramitesScreen() {

    var searchText by remember { mutableStateOf("") }

    val context = LocalContext.current
    val session = remember { SessionManager(context) }

    var selectedTab by remember { mutableStateOf(0) }

    var tramites by remember { mutableStateOf<List<TramiteDto>>(emptyList()) }
    var solicitudes by remember { mutableStateOf<List<SolicitudDto>>(emptyList()) }

    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    // Cargar la pestaña seleccionada
    LaunchedEffect(selectedTab) {
        val token = session.getToken()
        if (token.isNullOrBlank()) {
            loading = false
            error = "Sesión inválida. Inicia sesión nuevamente."
            return@LaunchedEffect
        }

        loading = true
        error = null

        try {
            if (selectedTab == 0) {
                val res = RetrofitClient.tramitesApi.getTramites("Bearer $token")
                loading = false

                if (res.isSuccessful) {
                    tramites = res.body()?.data ?: emptyList()
                    if (tramites.isEmpty()) error = "No hay trámites disponibles."
                } else {
                    error = "Error al cargar trámites (${res.code()})"
                }
            } else {
                val res = RetrofitClient.tramitesApi.getMisSolicitudes("Bearer $token")
                loading = false

                if (res.isSuccessful) {
                    val body = res.body()
                    val ok = body?.success == true

                    if (ok) {
                        solicitudes = body?.data ?: emptyList()
                        if (solicitudes.isEmpty()) {
                            error = "No hay solicitudes registradas."
                        }
                    } else {
                        error = body?.message ?: "No se pudieron cargar las solicitudes."
                    }
                } else {
                    error = "Error al cargar solicitudes (${res.code()})"
                }

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
        Text("Trámites", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(12.dp))

        // Buscador

        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Buscar") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )


        Spacer(modifier = Modifier.height(12.dp))

        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Activos") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Mis trámites") }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        val filteredTramites = tramites.filter { t ->
            val txt = searchText.trim()
            if (txt.isEmpty()) true
            else (t.nombre ?: "").contains(txt, ignoreCase = true) ||
                    (t.descripcion ?: "").contains(txt, ignoreCase = true)
        }

        val filteredSolicitudes = solicitudes.filter { s ->
            val txt = searchText.trim()
            if (txt.isEmpty()) true
            else (s.tramiteNombre ?: "").contains(txt, ignoreCase = true) ||
                    (s.estatus ?: "").contains(txt, ignoreCase = true) ||
                    (s.fecha ?: "").contains(txt, ignoreCase = true)
        }


        when {
            loading -> CircularProgressIndicator()
            error != null -> Text(error!!)
            else -> {
                LazyColumn {
                    if (selectedTab == 0) {
                        items(filteredTramites) { t ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = t.nombre ?: "Trámite",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    if (!t.descripcion.isNullOrBlank()) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(t.descripcion!!)
                                    }
                                }
                            }
                        }
                    } else {
                        items(filteredSolicitudes) { s ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = s.tramiteNombre ?: "Solicitud",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Estatus: ${s.estatus ?: "-"}")
                                    if (!s.fecha.isNullOrBlank()) {
                                        Text("Fecha: ${s.fecha}")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}