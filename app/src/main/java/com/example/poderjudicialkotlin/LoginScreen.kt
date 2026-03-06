package com.example.poderjudicialkotlin

import okhttp3.ResponseBody

import com.example.poderjudicialkotlin.data.network.LoginResponse
import retrofit2.Response

import androidx.compose.ui.platform.LocalContext
import com.example.poderjudicialkotlin.SessionManager

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp


import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch


import com.example.poderjudicialkotlin.data.network.LoginRequest
import com.example.poderjudicialkotlin.data.network.RetrofitClient

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit
) {


    val scope = rememberCoroutineScope()

    //  Estados
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val session = remember { SessionManager(context) }
    var tipoEmpleado by remember { mutableStateOf("nomina") } // nomina | contrato
    var rememberMe by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {

        //  Logo
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(150.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        //  Usuario
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Ingresa tu usuario") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Contraseña
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Ingresa tu contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Tipo de empleado
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = tipoEmpleado == "nomina",
                onClick = { tipoEmpleado = "nomina" }
            )
            Text("Nómina", modifier = Modifier.padding(end = 16.dp))

            RadioButton(
                selected = tipoEmpleado == "contrato",
                onClick = { tipoEmpleado = "contrato" }
            )
            Text("Contrato")
        }

        // Recordarme
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = rememberMe,
                onCheckedChange = { rememberMe = it }
            )
            Text("Recordarme")
        }

        //  Mensaje de error
        if (errorMsg.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMsg,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        //  Boton Login
        Button(
            onClick = {

                errorMsg = when {
                    username.isBlank() || password.isBlank() ->
                        "Por favor, ingresa el usuario y contraseña"
                    else -> ""
                }

                if (errorMsg.isEmpty()) {

                    isLoading = true

                    scope.launch {
                        try {
                            val res: Response<LoginResponse> = RetrofitClient.api.login(
                                LoginRequest(username, password, tipoEmpleado)
                            )


                            isLoading = false

                            // res ya existe aqui arriba

                            if (res.isSuccessful) {
                                val body = res.body()
                                val token = body?.data?.authToken
                                val msg = body?.message

                                if (!token.isNullOrBlank()) {
                                    session.setToken(token)

                                    if (rememberMe) {
                                        session.setLoggedIn(true)
                                    }

                                    onLoginSuccess()
                                } else {
                                    errorMsg = msg ?: "Usuario o contraseña incorrectos."
                                }
                            } else {
                                errorMsg = "Error al iniciar sesión (${res.code()})"
                            }



                        } catch (e: Exception) {
                            isLoading = false
                            errorMsg = "Error de conexión. Intenta de nuevo."
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text(
                text = if (isLoading) "Cargando..." else "Entrar"
            )
        }
    }
}