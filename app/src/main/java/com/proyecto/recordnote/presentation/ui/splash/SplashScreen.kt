package com.proyecto.recordnote.presentation.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyecto.recordnote.theme.RecordNoteTheme
import kotlinx.coroutines.delay

/**
 * Splash Screen - Pantalla de carga inicial
 */
@Composable
fun SplashScreen(onNavigateToHome: () -> Unit) {
    val primary = Color(0x64FFDA)
    val backgroundDark = Color(0x0A192F)
    val textPrimary = Color(0xE6F1FF)

    RecordNoteTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundDark),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                // Logo
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Logo",
                    tint = primary,
                    modifier = Modifier.size(80.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Título
                Text(
                    text = "RecordNote",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Subtítulo
                Text(
                    text = "Transform your voice into text",
                    fontSize = 16.sp,
                    color = Color(0x8892B0),
                    fontWeight = FontWeight.Light
                )
            }
        }
    }

    // ===== NAVEGAR DESPUÉS DE 2 SEGUNDOS =====
    LaunchedEffect(Unit) {
        delay(2000)
        onNavigateToHome()
    }
}
