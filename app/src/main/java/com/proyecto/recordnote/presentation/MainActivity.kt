package com.proyecto.recordnote.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.proyecto.recordnote.BuildConfig
import com.proyecto.recordnote.data.local.database.AppDatabase
import com.proyecto.recordnote.data.repositories.NotaRepository
import com.proyecto.recordnote.presentation.navigation.AppNavigation
import com.proyecto.recordnote.theme.RecordNoteTheme
import timber.log.Timber

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar Timber para logs
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        Timber.d("🚀 MainActivity iniciado")

        setContent {
            RecordNoteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Inicializar dependencias
                    val database = AppDatabase.getDatabase(this@MainActivity)
                    val notaRepository = NotaRepository(database.notaDao())

                    // Navegar
                    AppNavigation(notaRepository = notaRepository)
                }
            }
        }
    }
}
