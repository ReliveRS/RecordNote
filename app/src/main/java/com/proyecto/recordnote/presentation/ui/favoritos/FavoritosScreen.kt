package com.proyecto.recordnote.presentation.ui.favoritos

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyecto.recordnote.domain.model.NotaDomain
import com.proyecto.recordnote.domain.usecase.nota.GuardarNotaUseCase
import com.proyecto.recordnote.domain.usecase.nota.ObtenerNotasUseCase
import com.proyecto.recordnote.theme.RecordNoteTheme
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * FavoritosScreen - Notas Favoritas
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritosScreen(
    onBackClick: () -> Unit,
    onNotaClick: (String) -> Unit,
    obtenerNotasUseCase: ObtenerNotasUseCase,
    guardarNotaUseCase: GuardarNotaUseCase
) {
    val primary = Color(0x64FFDA)
    val backgroundDark = Color(0x0A192F)
    val textPrimary = Color(0xE6F1FF)
    val textSecondary = Color(0x8892B0)
    val cardBg = Color(0x112240)

    var notas by remember { mutableStateOf<List<NotaDomain>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val resultado = obtenerNotasUseCase.obtenerFavoritas()
                resultado.onSuccess { favoritas ->
                    notas = favoritas
                    Timber.d("✅ Favoritos cargados: ${favoritas.size}")
                }
                isLoading = false
            } catch (e: Exception) {
                Timber.e(e, "Error al cargar favoritos")
                isLoading = false
            }
        }
    }

    RecordNoteTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("⭐ Favoritos", color = textPrimary) },
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
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundDark)
                    .padding(paddingValues)
            ) {
                when {
                    isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = primary
                        )
                    }
                    notas.isEmpty() -> {
                        Text(
                            text = "No tienes notas favoritas",
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp),
                            color = textSecondary,
                            fontSize = 16.sp
                        )
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(notas) { nota ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onNotaClick(nota.id) },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = cardBg)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Column(
                                            modifier = Modifier.weight(1f),
                                            verticalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Text(
                                                text = nota.titulo,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = textPrimary
                                            )
                                            Text(
                                                text = nota.contenido.take(50),
                                                fontSize = 12.sp,
                                                color = textSecondary,
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                        IconButton(
                                            onClick = {
                                                scope.launch {
                                                    try {
                                                        guardarNotaUseCase.toggleFavorito(nota.id, false)
                                                        notas = notas.filter { it.id != nota.id }
                                                        Timber.d("❌ Quitado de favoritos: ${nota.id}")
                                                    } catch (e: Exception) {
                                                        Timber.e(e, "Error al quitar de favoritos")
                                                    }
                                                }
                                            }
                                        ) {
                                            Icon(
                                                Icons.Default.Delete,
                                                contentDescription = "Quitar de favoritos",
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
    }
}
