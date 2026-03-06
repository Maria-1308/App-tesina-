package com.example.poderjudicialkotlin

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun ComprobanteScreen(codigoAcceso: String, titulo: String) {
    val context = LocalContext.current
    val url = "https://tribunalelectronico.gob.mx/empleados-api/nominas/comprobantes/$codigoAcceso/comprobante"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Comprobante", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(12.dp))
        Text("Nómina: $titulo")
        Spacer(modifier = Modifier.height(12.dp))
        Text("Código: $codigoAcceso")
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Abrir comprobante")
        }
    }
}