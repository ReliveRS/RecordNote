package com.proyecto.recordnote.presentation.ui.categorias

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyecto.recordnote.theme.RecordNoteTheme
import timber.log.Timber

/**
 * CategoriasScreen - Gestión de Categorías
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriasScreen(
    onBackClick: () -> Unit,
    onCategoriaClick: (String) -> Unit
) {
    val primary = Color(0x64FFDA)
    val backgroundDark = Color(0x0A192F)
    val textPrimary = Color(0xE6F1FF)
    val textSecondary = Color(0x8892B0)
    val cardBg = Color(0x112240)

    var categorias by remember { mutableStateOf(listOf("Trabajo", "Personal", "Ideas")) }
    var mostrarDialogo by remember { mutableStateOf(false) }
    var nombreNuevaCategoria by remember { mutableStateOf("") }

    RecordNoteTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("📁 Categorías", color = textPrimary) },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver",
                                tint = textPrimary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = cardBg
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { mostrarDialogo = true },
                    containerColor = primary,
                    contentColor = Color.Black
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Nueva categoría")
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundDark)
                    .padding(paddingValues)
            ) {
                if (categorias.isEmpty()) {
                    Text(
                        text = "Sin categorías",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        color = textSecondary
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(categorias) { categoria ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onCategoriaClick(categoria) },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = cardBg)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = categoria,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = textPrimary
                                    )
                                    IconButton(onClick = {
                                        categorias = categorias.filter { it != categoria }
                                        Timber.d("Categoría eliminada: $categoria")
                                    }) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Eliminar",
                                            tint = Color(0xFFFF5252)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("Nueva Categoría", color = textPrimary) },
            text = {
                OutlinedTextField(
                    value = nombreNuevaCategoria,
                    onValueChange = { nombreNuevaCategoria = it },
                    label = { Text("Nombre") }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (nombreNuevaCategoria.isNotEmpty()) {
                            categorias = categorias + nombreNuevaCategoria
                            Timber.d("✅ Categoría creada: $nombreNuevaCategoria")
                            nombreNuevaCategoria = ""
                            mostrarDialogo = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = primary)
                ) {
                    Text("Crear", color = Color.Black)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
