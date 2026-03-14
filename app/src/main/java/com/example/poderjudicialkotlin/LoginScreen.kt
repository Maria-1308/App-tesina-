package com.example.poderjudicialkotlin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.poderjudicialkotlin.ui.theme.*
import kotlinx.coroutines.launch
import retrofit2.Response
import com.example.poderjudicialkotlin.data.network.*

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit
) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var tipoEmpleado by remember { mutableStateOf("nomina") }
    var rememberMe by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val session = remember { SessionManager(context) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlancoPerla)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(40.dp))

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Ingresa tu usuario") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MagentaPurpuraPJ,
                unfocusedBorderColor = GrisClaroPJ
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Ingresa tu contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MagentaPurpuraPJ,
                unfocusedBorderColor = GrisClaroPJ
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            RadioButton(
                selected = tipoEmpleado == "nomina",
                onClick = { tipoEmpleado = "nomina" },
                colors = RadioButtonDefaults.colors(
                    selectedColor = MagentaPurpuraPJ
                )
            )

            Text("Nómina", color = GrisOscuroPJ)

            Spacer(modifier = Modifier.width(20.dp))

            RadioButton(
                selected = tipoEmpleado == "contrato",
                onClick = { tipoEmpleado = "contrato" },
                colors = RadioButtonDefaults.colors(
                    selectedColor = MagentaPurpuraPJ
                )
            )

            Text("Contrato", color = GrisOscuroPJ)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Checkbox(
                checked = rememberMe,
                onCheckedChange = { rememberMe = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = BorgonaPJ
                )
            )

            Text("Recordarme", color = GrisOscuroPJ)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {

                errorMsg = when {
                    username.isBlank() || password.isBlank() ->
                        "Por favor ingresa usuario y contraseña"
                    else -> ""
                }

                if (errorMsg.isEmpty()) {

                    isLoading = true

                    scope.launch {
                        try {

                            val res: Response<LoginResponse> =
                                RetrofitClient.api.login(
                                    LoginRequest(username, password, tipoEmpleado)
                                )

                            isLoading = false

                            if (res.isSuccessful) {

                                val body = res.body()
                                val token = body?.data?.authToken

                                if (!token.isNullOrBlank()) {

                                    session.setToken(token)

                                    if (rememberMe) {
                                        session.setLoggedIn(true)
                                    }

                                    onLoginSuccess()

                                } else {

                                    errorMsg =
                                        body?.message ?: "Usuario o contraseña incorrectos."

                                }

                            } else {

                                errorMsg = "Error al iniciar sesión (${res.code()})"

                            }

                        } catch (e: Exception) {

                            isLoading = false
                            errorMsg = "Error de conexión"

                        }
                    }
                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MagentaPurpuraPJ
            )
        ) {
            Text(
                if (isLoading) "Cargando..." else "Entrar",
                color = White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "¿Olvidaste tu contraseña?",
            color = GrisClaroPJ
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Poder Judicial del Estado de Tamaulipas",
            style = MaterialTheme.typography.titleMedium,
            color = GrisOscuroPJ,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text =
                "Boulevard Praxedis Balboa #2207 entre López Velarde y \n" +
                        "Díaz Mirón Colonia Miguel Hidalgo C.P. 87090 Tel. (834)\n" +
                        " 318-7110 Cd. Victoria, Tamaulipas \n" ,
            style = MaterialTheme.typography.bodySmall,
            color = GrisOscuroPJ,
            textAlign = TextAlign.Center
        )
    }
}