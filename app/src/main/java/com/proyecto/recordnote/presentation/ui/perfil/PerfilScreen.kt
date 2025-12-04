package com.proyecto.recordnote.presentation.ui.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyecto.recordnote.theme.RecordNoteTheme

/**
 * Edit Profile Screen - Pantalla de edición de perfil
 */
@Composable
fun PerfilScreen(
    onNavigateBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val primary = Color(0x64FFDA)
    val backgroundDark = Color(0x0A192F)
    val textPrimary = Color(0xE6F1FF)
    val textSecondary = Color(0x8892B0)
    val cardBg = Color(0x112240)
    val borderColor = Color(0x233554)

    var selectedTab by remember { mutableStateOf(0) }
    var nombre by remember { mutableStateOf("Sophia Carter") }
    var email by remember { mutableStateOf("sophia.carter@email.com") }
    var password by remember { mutableStateOf("•••••••") }

    RecordNoteTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Edit Profile", color = textPrimary, fontSize = 18.sp) },
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
                        icon = { Icon(Icons.Default.CameraAlt, contentDescription = "Profile") },
                        label = { Text("Profile", fontSize = 12.sp) }
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
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // ===== PROFILE PICTURE =====
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "SC",
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }

                        // Camera Icon Button
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(primary)
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.CameraAlt,
                                contentDescription = "Change Picture",
                                tint = Color.Black,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    // ===== NAME =====
                    Text(
                        text = nombre,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )

                    // ===== EMAIL =====
                    Text(
                        text = email,
                        fontSize = 14.sp,
                        color = textSecondary
                    )

                    // ===== PERSONAL INFORMATION SECTION =====
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Personal Information",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = textPrimary
                        )

                        // Name Field
                        ProfileTextField(
                            label = "Name",
                            value = nombre,
                            onValueChange = { nombre = it },
                            cardBg = cardBg,
                            textPrimary = textPrimary,
                            textSecondary = textSecondary,
                            borderColor = borderColor
                        )

                        // Email Field
                        ProfileTextField(
                            label = "Email",
                            value = email,
                            onValueChange = { email = it },
                            cardBg = cardBg,
                            textPrimary = textPrimary,
                            textSecondary = textSecondary,
                            borderColor = borderColor
                        )

                        // Password Field
                        ProfileTextField(
                            label = "Password",
                            value = password,
                            onValueChange = { password = it },
                            cardBg = cardBg,
                            textPrimary = textPrimary,
                            textSecondary = textSecondary,
                            borderColor = borderColor,
                            isPassword = true
                        )
                    }

                    // ===== TRANSCRIPTION PREFERENCES =====
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Transcription Preferences",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = textPrimary
                        )

                        // Language
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = cardBg)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Language",
                                        fontSize = 12.sp,
                                        color = textSecondary
                                    )
                                    Text(
                                        text = "English",
                                        fontSize = 14.sp,
                                        color = textPrimary,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                Icon(
                                    Icons.Default.Settings,
                                    contentDescription = "Select",
                                    tint = primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    // ===== SAVE BUTTON =====
                    Button(
                        onClick = { /* Save */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primary
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Save Changes",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

/**
 * Componente para texto editable en perfil
 */
@Composable
fun ProfileTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    cardBg: Color,
    textPrimary: Color,
    textSecondary: Color,
    borderColor: Color,
    isPassword: Boolean = false
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = textSecondary
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = textPrimary,
                unfocusedBorderColor = borderColor,
                focusedTextColor = textPrimary,
                unfocusedTextColor = textPrimary,
                focusedContainerColor = cardBg,
                unfocusedContainerColor = cardBg
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true
        )
    }
}
