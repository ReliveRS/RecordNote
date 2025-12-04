package com.proyecto.recordnote.presentation.ui.notas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyecto.recordnote.theme.RecordNoteTheme

/**
 * Note Detail Screen - Pantalla de detalle de nota
 */
@Composable
fun DetalleNotaScreen(
    noteTitle: String = "Meeting with Alex",
    onNavigateBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onEditNote: () -> Unit
) {
    val primary = Color(0x64FFDA)
    val backgroundDark = Color(0x0A192F)
    val textPrimary = Color(0xE6F1FF)
    val textSecondary = Color(0x8892B0)
    val cardBg = Color(0x112240)
    val borderColor = Color(0x233554)

    var selectedTab by remember { mutableStateOf(1) }
    var isPlaying by remember { mutableStateOf(false) }

    // Mock data
    val noteContent = """The meeting was productive, with key decisions made on the marketing strategy for Q3. 
        |The team discussed the budget allocation, focusing on digital campaigns and social media engagement. 
        |Action items include finalizing the campaign calendar and securing partnerships with influencers. 
        |Follow-up tasks involve a review of competitor activities and a detailed analysis of target audience demographics."""
        .trimMargin()

    val noteDate = "July 15, 2024"
    val duration = "12:34"

    RecordNoteTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(noteTitle, color = textPrimary, fontSize = 18.sp) },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = textPrimary
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* Menu */ }) {
                            Icon(
                                Icons.Default.MoreVert,
                                contentDescription = "More",
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
                        icon = { Icon(Icons.Default.Edit, contentDescription = "Notes") },
                        label = { Text("All Notes", fontSize = 12.sp) }
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
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // ===== PLAYER SECTION =====
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = cardBg)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            // Play Button
                            Button(
                                onClick = { isPlaying = !isPlaying },
                                modifier = Modifier
                                    .size(80.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = primary
                                )
                            ) {
                                Icon(
                                    Icons.Default.PlayArrow,
                                    contentDescription = "Play",
                                    tint = Color.Black,
                                    modifier = Modifier.size(40.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Duration
                            Text(
                                text = duration,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = textPrimary
                            )
                        }
                    }

                    // ===== ACTIONS BUTTONS =====
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = onEditNote,
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = primary
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = Color.Black,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Edit Text", color = Color.Black, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { /* Delete */ },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFF5252)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Delete Note", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }

                    // ===== METADATA =====
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Date: $noteDate",
                            fontSize = 14.sp,
                            color = textSecondary
                        )
                        Text(
                            text = "Duration: $duration",
                            fontSize = 14.sp,
                            color = textSecondary
                        )
                    }

                    Divider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = borderColor
                    )

                    // ===== CONTENT SECTION =====
                    Text(
                        text = "Transcription",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = cardBg)
                    ) {
                        Text(
                            text = noteContent,
                            fontSize = 14.sp,
                            color = textPrimary,
                            modifier = Modifier.padding(16.dp),
                            lineHeight = 20.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}
