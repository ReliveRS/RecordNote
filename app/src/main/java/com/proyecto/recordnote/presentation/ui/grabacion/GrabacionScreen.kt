package com.proyecto.recordnote.presentation.screens.grabacion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.proyecto.recordnote.presentation.viewmodel.NotaViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GrabacionScreen(
    navController: NavController,
    notaViewModel: NotaViewModel = viewModel()
) {
    val primary = Color(0xFF4A90E2)
    val secondary = Color(0xFFB4D962)
    val backgroundDark = Color(0xFF1C2834)
    val cardBg = Color(0xFF2A3844)
    val textPrimary = Color.White
    val textSecondary = Color(0xFF8892B0)

    var isRecording by remember { mutableStateOf(false) }
    var recordingTime by remember { mutableStateOf("00:00:00") }
    var transcription by remember { mutableStateOf("") }
    var notaTitle by remember { mutableStateOf("") }
    var showSaveDialog by remember { mutableStateOf(false) }

    // Simular contador de tiempo
    LaunchedEffect(isRecording) {
        var seconds = 0
        while (isRecording) {
            delay(1000)
            seconds++
            val hours = seconds / 3600
            val minutes = (seconds % 3600) / 60
            val secs = seconds % 60
            recordingTime = String.format("%02d:%02d:%02d", hours, minutes, secs)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "New Note",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = textPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = backgroundDark)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundDark)
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Tap the button to start recording",
                color = textSecondary,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Timer
            Text(
                recordingTime,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = if (isRecording) secondary else textPrimary
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Transcription Area
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        if (transcription.isEmpty())
                            "Transcription will appear here..."
                        else
                            transcription,
                        color = if (transcription.isEmpty()) textSecondary else textPrimary,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Record Button
            FloatingActionButton(
                onClick = {
                    isRecording = !isRecording
                    if (!isRecording && transcription.isEmpty()) {
                        // Simulación: transcribir cuando se detiene
                        transcription = "Nota grabada exitosamente. Esta es la transcripción simulada del audio."
                    }
                },
                modifier = Modifier.size(80.dp),
                containerColor = if (isRecording) Color.Red else secondary,
                shape = CircleShape
            ) {
                Icon(
                    Icons.Default.Mic,
                    contentDescription = if (isRecording) "Stop" else "Record",
                    modifier = Modifier.size(40.dp),
                    tint = if (isRecording) Color.White else backgroundDark
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Guardar nota - Solo mostrar si hay transcripción
            if (transcription.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            navController.navigateUp()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = cardBg)
                    ) {
                        Text("Discard", color = textPrimary)
                    }

                    Button(
                        onClick = { showSaveDialog = true },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primary)
                    ) {
                        Text("Save Note", color = Color.White)
                    }
                }
            }
        }
    }

    // Dialog para guardar nota
    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            title = { Text("Save Note") },
            text = {
                Column {
                    Text("Enter a title for your note:")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = notaTitle,
                        onValueChange = { notaTitle = it },
                        label = { Text("Note Title") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primary,
                            unfocusedBorderColor = Color(0xFF3D4F5F),
                            focusedTextColor = textPrimary,
                            unfocusedTextColor = textPrimary,
                            focusedContainerColor = cardBg,
                            unfocusedContainerColor = cardBg
                        ),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (notaTitle.isNotBlank()) {
                            // Guardar nota
                            notaViewModel.crearNota(
                                titulo = notaTitle,
                                descripcion = transcription.take(100),
                                contenido = transcription
                            )
                            showSaveDialog = false
                            notaTitle = ""
                            transcription = ""
                            recordingTime = "00:00:00"
                            isRecording = false

                            // Navegar a inicio
                            navController.navigate("inicio") {
                                popUpTo("grabacion") { inclusive = true }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = primary)
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showSaveDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = cardBg)
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
