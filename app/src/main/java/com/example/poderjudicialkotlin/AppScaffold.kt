package com.example.poderjudicialkotlin

import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Description

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.poderjudicialkotlin.ui.theme.BlancoPerla
import com.example.poderjudicialkotlin.ui.theme.BorgonaPJ
import com.example.poderjudicialkotlin.ui.theme.GrisClaroPJ
import com.example.poderjudicialkotlin.ui.theme.MagentaPurpuraPJ



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    title: String,
    currentRoute: String,
    onBack: (() -> Unit)? = null,
    onGoHome: () -> Unit,
    onGoId: () -> Unit,
    onGoTramites: () -> Unit,
    onGoNomina: () -> Unit,
    onGoNoticias: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {

    Scaffold(

        containerColor = BlancoPerla,

        topBar = {

            TopAppBar(

                title = { Text(title, color = Color.White) },

                navigationIcon = {

                    if (onBack != null) {

                        IconButton(onClick = onBack) {

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Regresar",
                                tint = Color.White
                            )

                        }

                    }

                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BorgonaPJ
                )

            )

        },

        bottomBar = {

            NavigationBar(containerColor = Color.White) {

                NavigationBarItem(
                    selected = currentRoute == "home",
                    onClick = onGoHome,
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    colors = navColors()
                )

                NavigationBarItem(
                    selected = currentRoute == "id",
                    onClick = onGoId,
                    icon = { Icon(Icons.Default.Person, contentDescription = "ID") },
                    label = { Text("ID") },
                    colors = navColors()
                )

                NavigationBarItem(
                    selected = currentRoute == "tramites",
                    onClick = onGoTramites,
                    icon = { Icon(Icons.Default.List, contentDescription = "Trámites") },
                    label = { Text("Trámites") },
                    colors = navColors()
                )

                NavigationBarItem(
                    selected = currentRoute == "nomina",
                    onClick = onGoNomina,
                    icon = { Icon(Icons.Default.Description, contentDescription = "Nómina") },
                    label = { Text("Nómina") },
                    colors = navColors()
                )

                NavigationBarItem(
                    selected = currentRoute == "noticias",
                    onClick = onGoNoticias,
                    icon = { Icon(Icons.Default.Notifications, contentDescription = "Noticias") },
                    label = { Text("Noticias") },
                    colors = navColors()
                )

            }

        }

    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BlancoPerla)
        ) {

            content(innerPadding)

        }

    }

}

@Composable
fun navColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = MagentaPurpuraPJ,
    selectedTextColor = MagentaPurpuraPJ,
    indicatorColor = BlancoPerla,
    unselectedIconColor = GrisClaroPJ,
    unselectedTextColor = GrisClaroPJ
)