package com.example.poderjudicialkotlin

import androidx.compose.runtime.remember

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
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

    NavHost(navController = navController, startDestination = start) {



        composable("login") {
            LoginScreen(
                onLoginSuccess = { navController.navigate("home") }
            )
        }

        composable("home") {
            HomeScreen(
                onGoId = { navController.navigate("id") },
                onGoTramites = { navController.navigate("tramites") },
                onGoNomina = { navController.navigate("nomina") },
                onGoNoticias = { navController.navigate("noticias") }
            )
        }

        composable("id") { SimpleScreen("ID") }
        composable("tramites") { SimpleScreen("Trámites") }
        composable("nomina") { SimpleScreen("Nómina") }
        composable("noticias") { NoticiasScreen() }
    }
}

@Composable
fun HomeScreen(
    onGoId: () -> Unit,
    onGoTramites: () -> Unit,
    onGoNomina: () -> Unit,
    onGoNoticias: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Menú", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = onGoId, modifier = Modifier.fillMaxWidth()) { Text("ID") }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = onGoTramites, modifier = Modifier.fillMaxWidth()) { Text("Trámites") }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = onGoNomina, modifier = Modifier.fillMaxWidth()) { Text("Nómina") }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = onGoNoticias, modifier = Modifier.fillMaxWidth()) { Text("Noticias") }
    }
}

@Composable
fun SimpleScreen(title: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(title, style = MaterialTheme.typography.headlineMedium)
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