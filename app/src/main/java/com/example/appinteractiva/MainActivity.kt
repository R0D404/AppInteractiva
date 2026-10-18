package com.example.appinteractiva

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.appinteractiva.tareas.presentation.preview.DatosEjemplo
import com.example.appinteractiva.tareas.presentation.screens.TareasContent
import com.example.appinteractiva.ui.theme.TareasEscolaresTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TareasEscolaresTheme {
                // Visualización temporal con datos de ejemplo para probar la UI en el emulador.
                // Más adelante se conectará al ViewModel correspondiente y al flujo de navegación.
                TareasContent(
                    uiState = DatosEjemplo.tareasUiStateEjemplo,
                    onToggleCompletada = {},
                    onEliminar = {},
                    onFiltroEstadoChange = {},
                    onFiltroPrioridadChange = {},
                    onAgregarClick = {}
                )
            }
        }
    }
}