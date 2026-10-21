package com.example.appinteractiva.tareas.presentation.screens

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appinteractiva.TareasApplication
import com.example.appinteractiva.tareas.presentation.viewmodels.NuevaTareaViewModel

/**
 * Pantalla con estado (Stateful Page) para la creación de una nueva tarea escolar.
 *
 * Conecta [NuevaTareaViewModel] con [NuevaTareaContent], interceptando el botón
 * de retroceso del sistema y gestionando el evento de guardado exitoso.
 *
 * @param onVolver Acción a ejecutar para retornar a la pantalla principal.
 * @param modifier Modificador para el diseño externo de la pantalla.
 */
@Composable
fun NuevaTareaPage(
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val application = LocalContext.current.applicationContext as TareasApplication
    val viewModel: NuevaTareaViewModel = viewModel(factory = application.tareasModule.factory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Manejador centralizado para cancelar o descartar cambios
    val descartarYVolver = {
        viewModel.onFormularioDescartado()
        onVolver()
    }

    // Intercepta el botón de retroceso físico/gestual del sistema Android
    BackHandler(onBack = descartarYVolver)

    // Reacciona al guardado exitoso: consume el evento y navega de regreso a la lista
    LaunchedEffect(uiState.guardadoExitoso) {
        if (uiState.guardadoExitoso) {
            viewModel.onGuardadoConsumido()
            onVolver()
        }
    }

    NuevaTareaContent(
        uiState = uiState,
        onTituloChange = viewModel::onTituloChange,
        onMateriaChange = viewModel::onMateriaChange,
        onPrioridadChange = viewModel::onPrioridadChange,
        onFechaChange = viewModel::onFechaChange,
        onGuardarClick = viewModel::onGuardarClick,
        onCancelarClick = descartarYVolver,
        onVolverClick = descartarYVolver,
        modifier = modifier
    )
}
