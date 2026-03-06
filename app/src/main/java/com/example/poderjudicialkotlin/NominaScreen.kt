package com.example.poderjudicialkotlin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.poderjudicialkotlin.data.network.NominaDto
import com.example.poderjudicialkotlin.data.network.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

@Composable
fun NominaScreen(navController: NavController) {
    val context = LocalContext.current
    val session = remember { SessionManager(context) }

    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val years = (2000..currentYear).toList().reversed()

    var selectedYear by remember { mutableStateOf(currentYear) }
    var nominas by remember { mutableStateOf<List<NominaDto>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(selectedYear) {
        val token = session.getToken()

        if (token.isNullOrBlank()) {
            error = "Sesión inválida. Inicia sesión nuevamente."
            return@LaunchedEffect
        }

        loading = true
        error = null

        try {
            val res = RetrofitClient.nominaApi.getNominas(
                anio = selectedYear,
                bearer = "Bearer $token"
            )

            println("NOMINAS code=${res.code()} success=${res.isSuccessful}")
            println("NOMINAS body=${res.body()}")
            println("NOMINAS errorBody=${res.errorBody()?.string()}")

            loading = false

            if (!res.isSuccessful) {
                error = "Error al obtener nóminas (${res.code()})"
                return@LaunchedEffect
            }

            val body = res.body()
            if (body == null) {
                error = "Respuesta vacía del servidor."
                return@LaunchedEffect
            }

            if (body.success == true) {
                nominas = body.data ?: emptyList()
                error = if (nominas.isEmpty()) "No hay nóminas para este año." else null
            } else {
                error = body.message ?: "No se pudieron obtener las nóminas."
            }

        } catch (e: Exception) {
            loading = false
            println("NOMINAS exception=${e.message}")
            error = "Error de conexión: ${e.message}"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Nómina", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(12.dp))

        Text("Para continuar, selecciona los criterios de búsqueda:")
        Spacer(modifier = Modifier.height(10.dp))

        YearDropdown(
            years = years,
            selectedYear = selectedYear,
            onYearSelected = { selectedYear = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        when {
            loading -> CircularProgressIndicator()
            error != null -> Text(error!!)
            else -> {
                LazyColumn {
                    items(nominas) { n ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .clickable {
                                    generarAccesoNomina(
                                        session = session,
                                        nomina = n,
                                        onOk = { codigo ->
                                            navController.navigate(
                                                "comprobante/$codigo/${n.quincena ?: "Nomina"}"
                                            )
                                        },
                                        onError = { msg ->
                                            error = msg
                                        }
                                    )
                                }
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = n.quincena ?: "Nómina",
                                    style = MaterialTheme.typography.titleMedium
                                )

                                if (!n.folio.isNullOrBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Folio: ${n.folio}")
                                }

                                Spacer(modifier = Modifier.height(6.dp))
                                Text("Toca para generar acceso")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun YearDropdown(
    years: List<Int>,
    selectedYear: Int,
    onYearSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    OutlinedButton(
        onClick = { expanded = true },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Año: $selectedYear")
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        years.forEach { y ->
            DropdownMenuItem(
                text = { Text(y.toString()) },
                onClick = {
                    expanded = false
                    onYearSelected(y)
                }
            )
        }
    }
}

private fun generarAccesoNomina(
    session: SessionManager,
    nomina: NominaDto,
    onOk: (String) -> Unit,
    onError: (String) -> Unit
) {
    val token = session.getToken()

    if (token.isNullOrBlank()) {
        onError("Sesión inválida. Inicia sesión nuevamente.")
        return
    }

    val body = com.example.poderjudicialkotlin.data.network.GenerarAccesoRequest(
        quincena = nomina.quincena,
        folio = nomina.folio,
        tablan = nomina.tablan,
        añoClave = nomina.añoClave
    )

    CoroutineScope(Dispatchers.Main).launch {
        try {
            println("ACCESO request body=$body")

            val res = RetrofitClient.nominaApi.generarAcceso(
                bearer = "Bearer $token",
                nomina = body
            )

            println("ACCESO code=${res.code()} success=${res.isSuccessful}")
            println("ACCESO body=${res.body()}")
            println("ACCESO errorBody=${res.errorBody()?.string()}")

            if (!res.isSuccessful) {
                onError("Error al generar acceso (${res.code()})")
                return@launch
            }

            val r = res.body()
            if (r == null) {
                onError("Respuesta vacía al generar acceso.")
                return@launch
            }

            if (r.success == true) {
                val codigo = r.data?.codigoAcceso
                if (!codigo.isNullOrBlank()) {
                    onOk(codigo)
                } else {
                    onError("No se recibió código de acceso.")
                }
            } else {
                onError(r.message ?: "No se pudo generar el acceso.")
            }

        } catch (e: Exception) {
            println("ACCESO exception=${e.message}")
            onError("Error de conexión: ${e.message}")
        }
    }
}