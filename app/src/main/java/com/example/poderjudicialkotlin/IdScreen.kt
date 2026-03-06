package com.example.poderjudicialkotlin

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.poderjudicialkotlin.data.network.IdentificacionDto
import com.example.poderjudicialkotlin.data.network.RetrofitClient
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import kotlinx.coroutines.launch

@Composable
fun IdScreen() {

    val context = LocalContext.current
    val session = remember { SessionManager(context) }
    val scope = rememberCoroutineScope()

    var qrBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var photo by remember { mutableStateOf<ImageBitmap?>(null) }
    var data by remember { mutableStateOf<IdentificacionDto?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(true) }
    var isFront by remember { mutableStateOf(true) }


    LaunchedEffect(Unit) {
        val token = session.getToken()

        if (token == null) {
            error = "Sesión inválida"
            loading = false
            return@LaunchedEffect
        }

        scope.launch {
            try {
                val res = RetrofitClient.identificacionApi
                    .getIdentificacion("Bearer $token")

                loading = false

                if (res.isSuccessful) {
                    data = res.body()?.data
                    if (data == null) {
                        error = "No se recibieron datos"
                    }
                } else {
                    error = "No se pudo cargar la identificación"
                }

            } catch (e: Exception) {
                loading = false
                error = "Error de conexión"
            }
        }
    }


    LaunchedEffect(data?.identificacionGuid) {
        val guid = data?.identificacionGuid ?: return@LaunchedEffect
        val url = "https://www.tribunalelectronico.gob.mx/identificacion-oficial/$guid"
        qrBitmap = generateQrBitmap(url, 600)
    }


    LaunchedEffect(data?.identificacionGuid) {
        val guid = data?.identificacionGuid ?: return@LaunchedEffect
        val token = session.getToken() ?: return@LaunchedEffect

        try {
            val resFoto = RetrofitClient.identificacionApi.getFoto(
                guid = guid,
                bearer = "Bearer $token"
            )

            if (resFoto.isSuccessful) {
                val bytes = resFoto.body()?.bytes()
                if (bytes != null) {
                    val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    photo = bmp?.asImageBitmap()
                }
            }
        } catch (_: Exception) {
            photo = null
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        when {
            loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

            error != null -> Text(error!!, modifier = Modifier.align(Alignment.Center))

            data != null -> {
                Column(modifier = Modifier.fillMaxWidth()) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { isFront = !isFront }) {
                            Text(if (isFront) "Ver reverso" else "Ver frente")
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (isFront) {
                        CredencialFront(
                            data = data!!,
                            photo = photo
                        )
                    } else {
                        CredencialBack(
                            data = data!!,
                            qrBitmap = qrBitmap
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CredencialFront(
    data: IdentificacionDto,
    photo: ImageBitmap?
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(120.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row {
                if (photo != null) {
                    Image(
                        bitmap = photo,
                        contentDescription = "Foto",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) { Text("Sin foto") }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "${data.nombre.orEmpty()} ${data.apellidoPaterno.orEmpty()} ${data.apellidoMaterno.orEmpty()}",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        data.puestoNombre ?: "-",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                data.adscripcionNombre ?: "-",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "www.pjetam.gob.mx",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun CredencialBack(
    data: IdentificacionDto,
    qrBitmap: Bitmap?
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {

            Text("NO. DE EMPLEADO: ${data.numeroEmpleado ?: "-"}")
            Text("MUNICIPIO: ${data.municipioNombre ?: "-"}")
            Text("CURP: ${data.curp ?: "-"}")
            Text("TIPO DE SANGRE: ${data.tipoSangre ?: "-"}")
            Text("ALERGIAS: ${data.alergias ?: "-"}")
            Text("EXPEDICIÓN: ${data.añoExpedicion ?: "-"}")
            Text("TIPO DE CONTRATACIÓN: ${data.plazaNombre ?: "-"}")

            Spacer(modifier = Modifier.height(20.dp))

            if (qrBitmap != null) {
                Image(
                    bitmap = qrBitmap.asImageBitmap(),
                    contentDescription = "QR",
                    modifier = Modifier
                        .size(220.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                "MAGISTRADO HERNÁN DE LA GARZA TAMEZ",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

fun generateQrBitmap(text: String, size: Int = 600): Bitmap {
    val bitMatrix = MultiFormatWriter()
        .encode(text, BarcodeFormat.QR_CODE, size, size)

    val bmp = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)

    for (x in 0 until size) {
        for (y in 0 until size) {
            bmp.setPixel(
                x,
                y,
                if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
            )
        }
    }

    return bmp
}