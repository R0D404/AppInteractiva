package com.example.appinteractiva.tareas.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appinteractiva.TareasApplication
import com.example.appinteractiva.tareas.presentation.viewmodels.TareasViewModel

/**
 * Pantalla con estado (Stateful Page) para la lista de tareas escolares.
 *
 * Conecta el [TareasViewModel] con el Composable sin estado [TareasContent],
 * observando el estado mediante flujo seguro con el ciclo de vida.
 *
 * @param onAgregarClick Acción a ejecutar al pulsar el botón para agregar una nueva tarea.
 * @param modifier Modificador para el diseño externo de la pantalla.
 */
@Composable
fun TareasPage(
    onAgregarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val application = LocalContext.current.applicationContext as TareasApplication
    val viewModel: TareasViewModel = viewModel(factory = application.tareasModule.factory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TareasContent(
        uiState = uiState,
        onToggleCompletada = viewModel::onToggleCompletada,
        onEliminar = viewModel::onEliminar,
        onFiltroEstadoChange = viewModel::onFiltroEstadoChange,
        onFiltroPrioridadChange = viewModel::onFiltroPrioridadChange,
        onAgregarClick = onAgregarClick,
        modifier = modifier
    )
}
