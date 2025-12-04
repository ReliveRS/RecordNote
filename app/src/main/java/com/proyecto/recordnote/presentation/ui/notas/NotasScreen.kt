package com.proyecto.recordnote.presentation.screens.notas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.time.format.DateTimeFormatter

/**
 * NotasScreen - Pantalla de "All Notes"
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotasScreen(navController: NavController) {
    // Colores
    val primary = Color(0xFF4A90E2)
    val secondary = Color(0xFFB4D962)
    val backgroundDark = Color(0xFF1C2834)
    val cardBg = Color(0xFF2A3844)
    val textPrimary = Color.White
    val textSecondary = Color(0xFF8892B0)
    val borderColor = Color(0xFF3D4F5F)

    // Estados
    var searchQuery by remember { mutableStateOf("") }
    var sortBy by remember { mutableStateOf("Date") }

    // Notas de ejemplo (después de BD)
    val allNotes = remember {
        listOf(
            NoteItemSimple(
                id = "1",
                title = "Meeting with Alex",
                description = "Discussed the Q3 roadmap and the new marketing campaign.",
                date = "2024-07-15",
                category = "Work"
            ),
            NoteItemSimple(
                id = "2",
                title = "Project Brainstorm",
                description = "Initial ideas for the 'Phoenix' project.",
                date = "2024-07-14",
                category = "Project"
            ),
            NoteItemSimple(
                id = "3",
                title = "Client Feedback",
                description = "Client requested changes to the dashboard layout.",
                date = "2024-07-13",
                category = "Client"
            )
        )
    }

    // Filtrar y ordenar
    val filteredNotes = remember(searchQuery, sortBy, allNotes) {
        val filtered = if (searchQuery.isEmpty()) {
            allNotes
        } else {
            allNotes.filter {
                it.title.contains(searchQuery, ignoreCase = true) ||
                        it.description.contains(searchQuery, ignoreCase = true)
            }
        }

        when (sortBy) {
            "Date" -> filtered.sortedByDescending { it.date }
            "Title" -> filtered.sortedBy { it.title }
            else -> filtered
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Description,
                            contentDescription = null,
                            tint = primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "All Notes",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = textPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = backgroundDark)
            )
        },
        bottomBar = {
            BottomNavBarSimple(navController = navController, currentRoute = "notas")
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundDark)
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search notes...", color = textSecondary) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = textSecondary) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primary,
                    unfocusedBorderColor = borderColor,
                    focusedTextColor = textPrimary,
                    unfocusedTextColor = textPrimary,
                    focusedContainerColor = cardBg,
                    unfocusedContainerColor = cardBg
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Sort buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = sortBy == "Date",
                    onClick = { sortBy = "Date" },
                    label = { Text("Date", fontSize = 14.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = cardBg,
                        selectedContainerColor = primary.copy(alpha = 0.2f),
                        labelColor = textSecondary,
                        selectedLabelColor = primary
                    )
                )
                FilterChip(
                    selected = sortBy == "Title",
                    onClick = { sortBy = "Title" },
                    label = { Text("Title", fontSize = 14.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = cardBg,
                        selectedContainerColor = primary.copy(alpha = 0.2f),
                        labelColor = textSecondary,
                        selectedLabelColor = primary
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "${filteredNotes.size} notes",
                fontSize = 14.sp,
                color = textSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Notes list
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredNotes) { note ->
                    NoteCardItem(
                        note = note,
                        onClick = { navController.navigate("detalle_nota/${note.id}") },
                        cardBg = cardBg,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary,
                        borderColor = borderColor,
                        primary = primary
                    )
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
fun NoteCardItem(
    note: NoteItemSimple,
    onClick: () -> Unit,
    cardBg: Color,
    textPrimary: Color,
    textSecondary: Color,
    borderColor: Color,
    primary: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                note.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                note.description,
                fontSize = 14.sp,
                color = textSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(note.date, fontSize = 12.sp, color = textSecondary)
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = primary.copy(alpha = 0.15f)
                ) {
                    Text(
                        note.category,
                        fontSize = 11.sp,
                        color = primary,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavBarSimple(navController: NavController, currentRoute: String) {
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
            selected = true,
            onClick = { },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFFB4D962),
                unselectedIconColor = Color(0xFF8892B0)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text("Settings") },
            selected = false,
            onClick = { navController.navigate("configuracion") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFFB4D962),
                unselectedIconColor = Color(0xFF8892B0)
            )
        )
    }
}

data class NoteItemSimple(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val category: String
)
