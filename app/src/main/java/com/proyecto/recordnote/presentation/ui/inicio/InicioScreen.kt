package com.proyecto.recordnote.presentation.screens.inicio

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.proyecto.recordnote.domain.model.NotaDomain
import com.proyecto.recordnote.presentation.viewmodel.InicioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(
    navController: NavController,
    inicioViewModel: InicioViewModel
) {
    val primary = Color(0xFF4A90E2)
    val secondary = Color(0xFFB4D962)
    val backgroundDark = Color(0xFF1C2834)
    val cardBg = Color(0xFF2A3844)
    val textPrimary = Color.White
    val textSecondary = Color(0xFF8892B0)
    val borderColor = Color(0xFF233554)

    // ✅ CORRECTO - Sin delegate
    val recentNotes = inicioViewModel.recentNotes.collectAsState().value
    val isLoading = inicioViewModel.isLoading.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Mic,
                            contentDescription = null,
                            tint = primary,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "RecordNote",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = textPrimary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("configuracion") }) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = textPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = backgroundDark)
            )
        },
        bottomBar = {
            BottomNav(navController = navController, currentRoute = "inicio")
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundDark)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Botón de grabación
            Box(
                modifier = Modifier
                    .size(250.dp)
                    .clip(CircleShape)
                    .background(secondary)
                    .clickable { navController.navigate("grabacion") },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Mic,
                    contentDescription = "Record",
                    modifier = Modifier.size(100.dp),
                    tint = backgroundDark
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Tap to Record",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Notas recientes
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Recent Notes",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )
                TextButton(onClick = { navController.navigate("notas") }) {
                    Text("View All", color = secondary)
                }
            }

            // Contenido
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = primary)
                    }
                }
                recentNotes.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Note,
                                contentDescription = "No notes",
                                tint = textSecondary,
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "No notes yet",
                                color = textSecondary,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(recentNotes, key = { it.id }) { nota ->
                            NoteCard(
                                nota = nota,
                                cardBg = cardBg,
                                textPrimary = textPrimary,
                                textSecondary = textSecondary,
                                borderColor = borderColor,
                                primary = primary,
                                onClick = { navController.navigate("notas") }
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NoteCard(
    nota: NotaDomain,
    cardBg: Color,
    textPrimary: Color,
    textSecondary: Color,
    borderColor: Color,
    primary: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Description,
                contentDescription = null,
                tint = primary,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    nota.titulo,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    nota.fechaCreacion.toLocalDate().toString(),
                    fontSize = 14.sp,
                    color = textSecondary
                )
            }
        }
    }
}

@Composable
private fun BottomNav(
    navController: NavController,
    currentRoute: String
) {
    val secondary = Color(0xFFB4D962)
    val textSecondary = Color(0xFF8892B0)

    NavigationBar(containerColor = Color(0xFF14202B)) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentRoute == "inicio",
            onClick = { },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = secondary,
                selectedTextColor = Color.White,
                unselectedIconColor = textSecondary,
                unselectedTextColor = Color.White
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Description, contentDescription = "Notes") },
            label = { Text("Notes") },
            selected = currentRoute == "notas",
            onClick = { navController.navigate("notas") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = secondary,
                selectedTextColor = Color.White,
                unselectedIconColor = textSecondary,
                unselectedTextColor = Color.White
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text("Settings") },
            selected = currentRoute == "configuracion",
            onClick = { navController.navigate("configuracion") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = secondary,
                selectedTextColor = Color.White,
                unselectedIconColor = textSecondary,
                unselectedTextColor = Color.White
            )
        )
    }
}
