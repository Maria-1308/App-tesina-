package com.example.poderjudicialkotlin

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import com.example.poderjudicialkotlin.data.network.NoticiaDto
import com.example.poderjudicialkotlin.data.network.RetrofitClient
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.poderjudicialkotlin.ui.theme.BlancoPerla
import com.example.poderjudicialkotlin.ui.theme.BorgonaPJ
import com.example.poderjudicialkotlin.ui.theme.GrisClaroPJ
import com.example.poderjudicialkotlin.ui.theme.GrisOscuroPJ
import com.example.poderjudicialkotlin.ui.theme.MagentaPurpuraPJ
import com.example.poderjudicialkotlin.ui.theme.PoderJudicialKotlinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PoderJudicialKotlinTheme {
                AppNav()
            }
        }
    }
}

@Composable
fun AppNav() {
    val navController = rememberNavController()

    // Para que siempre abra y no dependa del login real
    val start = "login"

    NavHost(
        navController = navController,
        startDestination = start
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("home") {
            HomeScreen(
                onGoId = { navController.navigate("id") },
                onGoTramites = { navController.navigate("tramites") },
                onGoNomina = { navController.navigate("nomina") },
                onGoNoticias = { navController.navigate("noticias") },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        composable("id") {
            AppScaffold(
                title = "Identificación",
                currentRoute = "id",
                onBack = { navController.popBackStack() },
                onGoHome = { navController.navigate("home") },
                onGoId = { },
                onGoTramites = { navController.navigate("tramites") },
                onGoNomina = { navController.navigate("nomina") },
                onGoNoticias = { navController.navigate("noticias") }
            ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    IdScreen()
                }
            }
        }

        composable("tramites") {
            AppScaffold(
                title = "Trámites",
                currentRoute = "tramites",
                onBack = { navController.popBackStack() },
                onGoHome = { navController.navigate("home") },
                onGoId = { navController.navigate("id") },
                onGoTramites = { },
                onGoNomina = { navController.navigate("nomina") },
                onGoNoticias = { navController.navigate("noticias") }
            ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    TramitesScreen()
                }
            }
        }

        composable("nomina") {
            AppScaffold(
                title = "Nómina",
                currentRoute = "nomina",
                onBack = { navController.popBackStack() },
                onGoHome = { navController.navigate("home") },
                onGoId = { navController.navigate("id") },
                onGoTramites = { navController.navigate("tramites") },
                onGoNomina = { },
                onGoNoticias = { navController.navigate("noticias") }
            ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    NominaScreen(navController)
                }
            }
        }

        composable("mi_tramite/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
            MiTramiteDetailScreen(id)
        }

        composable("comprobante/{codigo}/{titulo}") { backStackEntry ->
            val codigo = backStackEntry.arguments?.getString("codigo") ?: ""
            val titulo = backStackEntry.arguments?.getString("titulo") ?: ""
            ComprobanteScreen(codigoAcceso = codigo, titulo = titulo)
        }

        composable("noticias") {
            AppScaffold(
                title = "Noticias",
                currentRoute = "noticias",
                onBack = { navController.popBackStack() },
                onGoHome = { navController.navigate("home") },
                onGoId = { navController.navigate("id") },
                onGoTramites = { navController.navigate("tramites") },
                onGoNomina = { navController.navigate("nomina") },
                onGoNoticias = { }
            ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    NoticiasScreen()
                }
            }
        }
    }
}

@Composable
fun HomeScreen(
    onGoId: () -> Unit,
    onGoTramites: () -> Unit,
    onGoNomina: () -> Unit,
    onGoNoticias: () -> Unit,
    onLogout: () -> Unit
) {
    var noticias by remember { mutableStateOf<List<NoticiaDto>>(emptyList()) }
    var loadingNoticias by remember { mutableStateOf(true) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        try {
            val res = RetrofitClient.noticiasApi.getNoticiasRecientes()
            if (res.isSuccessful) {
                noticias = res.body() ?: emptyList()
            }
        } catch (e: Exception) {
            println("Error cargando noticias en Home: ${e.message}")
        } finally {
            loadingNoticias = false
        }
    }

    Scaffold(
        containerColor = BlancoPerla,
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MagentaPurpuraPJ,
                        selectedTextColor = MagentaPurpuraPJ,
                        indicatorColor = BlancoPerla,
                        unselectedIconColor = GrisClaroPJ,
                        unselectedTextColor = GrisClaroPJ
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onGoId,
                    icon = { Icon(Icons.Default.Person, contentDescription = "ID") },
                    label = { Text("ID") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onGoTramites,
                    icon = { Icon(Icons.Default.List, contentDescription = "Trámites") },
                    label = { Text("Trámites") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onGoNomina,
                    icon = { Icon(Icons.Default.Description, contentDescription = "Nómina") },
                    label = { Text("Nómina") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onGoNoticias,
                    icon = { Icon(Icons.Default.Notifications, contentDescription = "Noticias") },
                    label = { Text("Noticias") }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BlancoPerla)
                .padding(innerPadding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .clip(RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp))
                    .background(BorgonaPJ)
                    .padding(24.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Bienvenido,",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "Usuario",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    modifier = Modifier.align(Alignment.TopEnd),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Perfil",
                            tint = Color.White
                        )
                    }

                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Cerrar sesión",
                            tint = Color.White
                        )
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .offset(y = (-35).dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        QuickAccessItem("ID", onGoId)
                        QuickAccessItem("Trámites y Servicios", onGoTramites)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        QuickAccessItem("Nómina", onGoNomina)
                        QuickAccessItem("Noticias", onGoNoticias)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Noticias recientes",
                    style = MaterialTheme.typography.headlineSmall,
                    color = GrisOscuroPJ,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Ver todas",
                    color = MagentaPurpuraPJ,
                    modifier = Modifier.clickable { onGoNoticias() }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            LazyRow(
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (loadingNoticias) {
                    items(3) {
                        Card(
                            modifier = Modifier
                                .width(240.dp)
                                .height(180.dp),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(GrisClaroPJ.copy(alpha = 0.2f))
                            )
                        }
                    }
                } else {
                    items(noticias.take(5)) { noticia ->
                        NewsCard(
                            title = noticia.message ?: "Sin título",
                            imageUrl = noticia.image,
                            onClick = {
                                val link = noticia.link
                                if (!link.isNullOrBlank()) {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                                    context.startActivity(intent)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NewsCard(
    title: String,
    imageUrl: String?,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(240.dp)
            .height(220.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column {
            if (!imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .background(GrisClaroPJ.copy(alpha = 0.25f))
                )
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = title,
                    color = GrisOscuroPJ,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
@Composable
fun QuickAccessItem(
    title: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .height(100.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = BlancoPerla)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                color = GrisOscuroPJ,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }
    }
}
@Composable
fun NoticiasScreen() {
    var noticias by remember { mutableStateOf<List<NoticiaDto>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        try {
            val res = RetrofitClient.noticiasApi.getNoticiasRecientes()
            if (res.isSuccessful) {
                noticias = res.body() ?: emptyList()
            }
        } catch (e: Exception) {
            println("Error cargando noticias: ${e.message}")
        } finally {
            loading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (loading) {
            Text("Cargando noticias...")
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(noticias) { noticia ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val link = noticia.link
                                if (!link.isNullOrBlank()) {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                                    context.startActivity(intent)
                                }
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column {
                            if (!noticia.image.isNullOrBlank()) {
                                AsyncImage(
                                    model = noticia.image,
                                    contentDescription = noticia.message,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Text(
                                    text = noticia.message ?: "Sin título",
                                    color = GrisOscuroPJ,
                                    style = MaterialTheme.typography.titleMedium,
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}