package com.example.poderjudicialkotlin

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.poderjudicialkotlin.data.network.NoticiaDto
import com.example.poderjudicialkotlin.data.network.RetrofitClient
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

    val context = LocalContext.current
    val session = remember { SessionManager(context) }
    val start = if (session.isLoggedIn()) "home" else "login"

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
                    session.clear()
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
    val noticiasDemo = listOf(
        "La Magistrada Presidenta asistió a la ceremonia cívica",
        "Te invitamos a acudir a la jornada de Medicina Preventiva"
    )

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
                    label = { Text("ID") },
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
                    onClick = onGoTramites,
                    icon = { Icon(Icons.Default.List, contentDescription = "Trámites") },
                    label = { Text("Trámites") },
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
                    onClick = onGoNomina,
                    icon = { Icon(Icons.Default.Description, contentDescription = "Nómina") },
                    label = { Text("Nómina") },
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
                    onClick = onGoNoticias,
                    icon = { Icon(Icons.Default.Notifications, contentDescription = "Noticias") },
                    label = { Text("Noticias") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MagentaPurpuraPJ,
                        selectedTextColor = MagentaPurpuraPJ,
                        indicatorColor = BlancoPerla,
                        unselectedIconColor = GrisClaroPJ,
                        unselectedTextColor = GrisClaroPJ
                    )
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

                IconButton(
                    onClick = onLogout,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Cerrar sesión",
                        tint = Color.White
                    )
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
                items(noticiasDemo) { noticia ->
                    Card(
                        modifier = Modifier
                            .width(240.dp)
                            .height(180.dp)
                            .clickable { onGoNoticias() },
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(105.dp)
                                    .background(GrisClaroPJ.copy(alpha = 0.25f))
                            )

                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = noticia,
                                    color = GrisOscuroPJ,
                                    style = MaterialTheme.typography.titleMedium,
                                    maxLines = 2,
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
fun NoticiasScreen() {
    val context = LocalContext.current
    val url = "https://www.facebook.com/share/1FGGXmWkYw/?mibextid=wwXIfr"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Noticias", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver en Facebook")
        }
    }
}