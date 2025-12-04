package com.proyecto.recordnote.presentation.ui.configuracion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfiguracionScreen(navController: NavController) {
    val primary = Color(0xFF4A90E2)
    val secondary = Color(0xFFB4D962)
    val backgroundDark = Color(0xFF1C2834)
    val cardBg = Color(0xFF2A3844)
    val textPrimary = Color.White
    val textSecondary = Color(0xFF8892B0)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = textPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = backgroundDark)
            )
        },
        bottomBar = {
            BottomNav(navController = navController, currentRoute = "configuracion")
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundDark)
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Sección de apariencia
            Text(
                "Appearance",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "Dark Theme",
                        fontSize = 14.sp,
                        color = textPrimary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }

            // Sección de notificaciones
            Text(
                "Notifications",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary,
                modifier = Modifier.padding(bottom = 12.dp, top = 16.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "Enable Notifications",
                        fontSize = 14.sp,
                        color = textPrimary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }

            // Sección de almacenamiento
            Text(
                "Storage",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary,
                modifier = Modifier.padding(bottom = 12.dp, top = 16.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "Backup Data",
                        fontSize = 14.sp,
                        color = textPrimary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }

            // Sección de acerca de
            Text(
                "About",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary,
                modifier = Modifier.padding(bottom = 12.dp, top = 16.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = cardBg)
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    Text("RecordNote v1.0.0", fontSize = 14.sp, color = textPrimary)
                    Text("© 2024 RecordNote", fontSize = 12.sp, color = textSecondary)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun BottomNav(navController: NavController, currentRoute: String) {
    NavigationBar(containerColor = Color(0xFF14202B)) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentRoute == "inicio",
            onClick = { navController.navigate("inicio") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFFB4D962),
                unselectedIconColor = Color(0xFF8892B0)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Description, contentDescription = "Notes") },
            label = { Text("Notes") },
            selected = currentRoute == "notas",
            onClick = { navController.navigate("notas") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFFB4D962),
                unselectedIconColor = Color(0xFF8892B0)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text("Settings") },
            selected = currentRoute == "configuracion",
            onClick = { },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFFB4D962),
                unselectedIconColor = Color(0xFF8892B0)
            )
        )
    }
}
