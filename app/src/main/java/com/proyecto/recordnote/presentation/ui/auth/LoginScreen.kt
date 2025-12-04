package com.proyecto.recordnote.presentation.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x0A192F)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título
            Text(
                "RecordNote",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFFFFF)
            )

            // Subtítulo
            Text(
                "Bienvenido",
                fontSize = 16.sp,
                color = Color(0xFFCCCCCC),
                modifier = Modifier.padding(bottom = 48.dp)
            )

            // Email Input
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", color = Color(0xFF999999)) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = Color(0x64FFDA),
                        modifier = Modifier.size(24.dp)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textStyle = androidx.compose.ui.text.TextStyle(
                    color = Color(0xFFFFFFFF),
                    fontSize = 16.sp
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0x64FFDA),
                    unfocusedBorderColor = Color(0xFF333333),
                    focusedLabelColor = Color(0x64FFDA),
                    unfocusedLabelColor = Color(0xFF999999),
                    focusedTextColor = Color(0xFFFFFFFF),
                    unfocusedTextColor = Color(0xFFFFFFFF),
                    focusedContainerColor = Color(0x15FFFFFF),
                    unfocusedContainerColor = Color(0x0AFFFFFF)
                )
            )

            // Password Input
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña", color = Color(0xFF999999)) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color(0x64FFDA),
                        modifier = Modifier.size(24.dp)
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                textStyle = androidx.compose.ui.text.TextStyle(
                    color = Color(0xFFFFFFFF),
                    fontSize = 16.sp
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0x64FFDA),
                    unfocusedBorderColor = Color(0xFF333333),
                    focusedLabelColor = Color(0x64FFDA),
                    unfocusedLabelColor = Color(0xFF999999),
                    focusedTextColor = Color(0xFFFFFFFF),
                    unfocusedTextColor = Color(0xFFFFFFFF),
                    focusedContainerColor = Color(0x15FFFFFF),
                    unfocusedContainerColor = Color(0x0AFFFFFF)
                )
            )

            // Login Button
            Button(
                onClick = {
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        onNavigateToHome()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF64FFDA),  // ← CYAN BRILLANTE
                    contentColor = Color(0xFF000000)    // ← NEGRO DE TEXTO
                )
            ) {
                Text(
                    "Iniciar Sesión",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF000000)  // NEGRO EXPLÍCITO
                )
            }


            Spacer(modifier = Modifier.height(24.dp))

            // Forgot Password
            TextButton(onClick = onNavigateToForgotPassword) {
                Text(
                    "¿Olvidaste tu contraseña?",
                    color = Color(0xFFCCCCCC),
                    fontSize = 14.sp
                )
            }

            // Register Link
            Row(
                modifier = Modifier.padding(top = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    "¿No tienes cuenta? ",
                    color = Color(0xFFCCCCCC),
                    fontSize = 14.sp
                )
                TextButton(onClick = onNavigateToRegister) {
                    Text(
                        "Regístrate",
                        color = Color(0x64FFDA),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
