package com.proyecto.recordnote.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.proyecto.recordnote.data.repositories.NotaRepository
import com.proyecto.recordnote.presentation.screens.inicio.InicioScreen
import com.proyecto.recordnote.presentation.screens.notas.NotasScreen
import com.proyecto.recordnote.presentation.screens.grabacion.GrabacionScreen
import com.proyecto.recordnote.presentation.ui.configuracion.ConfiguracionScreen
import com.proyecto.recordnote.presentation.viewmodel.InicioViewModel
import com.proyecto.recordnote.presentation.viewmodel.NotasViewModel
import com.proyecto.recordnote.presentation.viewmodel.NotaViewModel

@Composable
fun AppNavigation(notaRepository: NotaRepository) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "inicio"
    ) {
        // Pantalla de inicio
        composable("inicio") {
            val viewModel = InicioViewModel(notaRepository)
            InicioScreen(
                navController = navController,
                inicioViewModel = viewModel
            )
        }

        // Pantalla de grabación
        composable("grabacion") {
            val viewModel = NotaViewModel()
            GrabacionScreen(
                navController = navController,
                notaViewModel = viewModel
            )
        }

        // Pantalla de todas las notas
        composable("notas") {
            val viewModel = NotasViewModel(notaRepository)
            NotasScreen(
                navController = navController
            )
        }

        // Pantalla de configuración
        composable("configuracion") {
            ConfiguracionScreen(navController = navController)
        }

        // Pantalla de detalle de nota
        composable("detalle_nota/{notaId}") { backStackEntry ->
            val notaId = backStackEntry.arguments?.getString("notaId") ?: ""
            navController.navigate("notas") {
                popUpTo("notas") { inclusive = false }
            }
        }
    }
}
