package com.example.appinteractiva.tareas.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.appinteractiva.tareas.presentation.screens.NuevaTareaPage
import com.example.appinteractiva.tareas.presentation.screens.TareasPage

/**
 * Composable raíz de la aplicación que gestiona la navegación entre pantallas.
 *
 * Mantiene la pantalla activa mediante [rememberSaveable] para sobrevivir
 * a cambios de configuración como la rotación de pantalla.
 *
 * @param modifier Modificador para el diseño externo del contenedor de la aplicación.
 */
@Composable
fun TareasApp(
    modifier: Modifier = Modifier
) {
    var pantallaActual by rememberSaveable { mutableStateOf(Pantalla.LISTA) }

    when (pantallaActual) {
        Pantalla.LISTA -> {
            TareasPage(
                onAgregarClick = { pantallaActual = Pantalla.FORMULARIO },
                modifier = modifier
            )
        }
        Pantalla.FORMULARIO -> {
            NuevaTareaPage(
                onVolver = { pantallaActual = Pantalla.LISTA },
                modifier = modifier
            )
        }
    }
}
