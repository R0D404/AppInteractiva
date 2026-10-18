package com.example.appinteractiva.tareas.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appinteractiva.R
import com.example.appinteractiva.tareas.data.model.FiltroEstado
import com.example.appinteractiva.tareas.data.model.Prioridad
import com.example.appinteractiva.tareas.presentation.components.EstadoVacio
import com.example.appinteractiva.tareas.presentation.components.FiltrosRow
import com.example.appinteractiva.tareas.presentation.components.ProgresoCard
import com.example.appinteractiva.tareas.presentation.components.TareaCard
import com.example.appinteractiva.tareas.presentation.preview.DatosEjemplo
import com.example.appinteractiva.ui.theme.TareasEscolaresTheme

/**
 * Pantalla principal que muestra el encabezado con progreso, filtros y el listado de tareas escolares.
 *
 * Este Composable es completamente STATELESS: no almacena ningún estado mutable internamente,
 * recibe todo su estado a través de [uiState] y comunica las interacciones del usuario
 * mediante funciones lambda hacia la capa superior.
 *
 * @param uiState Estado inmutable con las tareas preparadas, contadores y filtros activos.
 * @param onToggleCompletada Callback al presionar el checkbox de una tarea (recibe el ID).
 * @param onEliminar Callback al pulsar el botón de eliminar una tarea (recibe el ID).
 * @param onFiltroEstadoChange Callback al cambiar la selección entre Todas, Pendientes o Completadas.
 * @param onFiltroPrioridadChange Callback al seleccionar o alternar un filtro de prioridad (null para removerlo).
 * @param onAgregarClick Callback al pulsar el botón flotante (FAB) para crear una nueva tarea.
 * @param modifier Modificador para el diseño externo de la pantalla.
 */
@Composable
fun TareasContent(
    uiState: TareasUiState,
    onToggleCompletada: (Int) -> Unit,
    onEliminar: (Int) -> Unit,
    onFiltroEstadoChange: (FiltroEstado) -> Unit,
    onFiltroPrioridadChange: (Prioridad?) -> Unit,
    onAgregarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAgregarClick,
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.cd_agregar_tarea)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 1. Encabezado principal con fondo azul y esquinas inferiores curvas
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Text(
                    text = stringResource(R.string.titulo_lista_tareas),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                val textoSubtitulo = when (uiState.tareasPendientes) {
                    0 -> stringResource(R.string.subtitulo_sin_tareas_pendientes)
                    1 -> stringResource(R.string.subtitulo_una_tarea_pendiente)
                    else -> stringResource(
                        R.string.subtitulo_tareas_pendientes,
                        uiState.tareasPendientes
                    )
                }

                Text(
                    text = textoSubtitulo,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primaryContainer
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Tarjeta de progreso integrada en el encabezado
                ProgresoCard(
                    tareasCompletadas = uiState.tareasCompletadas,
                    totalTareas = uiState.totalTareas,
                    porcentajeProgreso = uiState.porcentajeProgreso,
                    porcentajeEntero = uiState.porcentajeEntero
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Barra de filtros de estado y de prioridad
            FiltrosRow(
                filtroEstado = uiState.filtroEstado,
                filtroPrioridad = uiState.filtroPrioridad,
                onFiltroEstadoChange = onFiltroEstadoChange,
                onFiltroPrioridadChange = onFiltroPrioridadChange
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 3. Contenido condicional: cargando, vacío o lista de tareas
            when {
                uiState.estaCargando -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }

                uiState.tareas.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        EstadoVacio()
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentPadding = PaddingValues(
                            start = 20.dp,
                            end = 20.dp,
                            top = 4.dp,
                            bottom = 88.dp // Espacio para no ocultar la última tarjeta con el FAB
                        ),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(
                            items = uiState.tareas,
                            key = { it.id }
                        ) { tarea ->
                            TareaCard(
                                tarea = tarea,
                                onToggleCompletada = onToggleCompletada,
                                onEliminar = onEliminar
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TareasContentPreview() {
    TareasEscolaresTheme {
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TareasContentVacioPreview() {
    TareasEscolaresTheme {
        TareasContent(
            uiState = DatosEjemplo.tareasUiStateVacio,
            onToggleCompletada = {},
            onEliminar = {},
            onFiltroEstadoChange = {},
            onFiltroPrioridadChange = {},
            onAgregarClick = {}
        )
    }
}
