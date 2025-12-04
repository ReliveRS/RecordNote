package com.proyecto.recordnote.presentation.ui.busqueda

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyecto.recordnote.theme.RecordNoteTheme

/**
 * Search Screen - Pantalla de búsqueda
 */
@Composable
fun BusquedaScreen(
    onNavigateBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onSelectNote: (String) -> Unit
) {
    val primary = Color(0x64FFDA)
    val backgroundDark = Color(0x0A192F)
    val textPrimary = Color(0xE6F1FF)
    val textSecondary = Color(0x8892B0)
    val cardBg = Color(0x112240)
    val borderColor = Color(0x233554)

    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(1) }

    // Mock search results
    val allNotes = listOf(
        SearchResultItem("Meeting with Alex", "Discussed the Q3 roadmap...", "July 15, 2024"),
        SearchResultItem("Project Brainstorm", "Initial ideas for the 'Phoenix' project...", "July 14, 2024"),
        SearchResultItem("Client Feedback", "Client was happy with the demo...", "July 13, 2024"),
        SearchResultItem("Team Sync", "Weekly sync to cover progress...", "July 12, 2024"),
        SearchResultItem("Marketing Strategy", "Planning for the upcoming product launch...", "July 11, 2024")
    )

    val searchResults = if (searchQuery.isBlank()) {
        emptyList()
    } else {
        allNotes.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
                    it.description.contains(searchQuery, ignoreCase = true)
        }
    }

    RecordNoteTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Search", color = textPrimary, fontSize = 18.sp) },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = textPrimary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = cardBg
                    )
                )
            },
            bottomBar = {
                NavigationBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(cardBg),
                    containerColor = cardBg,
                    contentColor = primary
                ) {
                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = {
                            selectedTab = 0
                            onNavigateToHome()
                        },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Home", fontSize = 12.sp) }
                    )
                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                        label = { Text("Search", fontSize = 12.sp) }
                    )
                    NavigationBarItem(
                        selected = selectedTab == 2,
                        onClick = {
                            selectedTab = 2
                            onNavigateToSettings()
                        },
                        icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                        label = { Text("Settings", fontSize = 12.sp) }
                    )
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundDark)
                    .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    // ===== SEARCH INPUT =====
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = {
                            Text("Search notes...", color = textSecondary)
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Search",
                                tint = textSecondary
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotBlank()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(
                                        Icons.Default.Clear,
                                        contentDescription = "Clear",
                                        tint = textSecondary
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(bottom = 24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primary,
                            unfocusedBorderColor = borderColor,
                            focusedTextColor = textPrimary,
                            unfocusedTextColor = textPrimary,
                            focusedContainerColor = cardBg,
                            unfocusedContainerColor = cardBg
                        ),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )

                    // ===== SEARCH RESULTS =====
                    if (searchQuery.isBlank()) {
                        // Estado vacío
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = 48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = textSecondary,
                                    modifier = Modifier.size(64.dp)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Start typing to search",
                                    fontSize = 16.sp,
                                    color = textSecondary
                                )
                            }
                        }
                    } else if (searchResults.isEmpty()) {
                        // Sin resultados
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = 48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "No results found",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textPrimary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Try a different search term",
                                    fontSize = 14.sp,
                                    color = textSecondary
                                )
                            }
                        }
                    } else {
                        // Resultados encontrados
                        Text(
                            text = "${searchResults.size} result${if (searchResults.size != 1) "s" else ""} found",
                            fontSize = 14.sp,
                            color = textSecondary,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            searchResults.forEach { result ->
                                SearchResultCard(
                                    result = result,
                                    cardBg = cardBg,
                                    textPrimary = textPrimary,
                                    textSecondary = textSecondary,
                                    onClick = { onSelectNote(result.title) }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

/**
 * Modelo de datos para resultado de búsqueda
 */
data class SearchResultItem(
    val title: String,
    val description: String,
    val date: String
)

/**
 * Componente para tarjeta de resultado de búsqueda
 */
@Composable
fun SearchResultCard(
    result: SearchResultItem,
    cardBg: Color,
    textPrimary: Color,
    textSecondary: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = result.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary
            )
            Text(
                text = result.description,
                fontSize = 14.sp,
                color = textSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = result.date,
                fontSize = 12.sp,
                color = textSecondary
            )
        }
    }
}
