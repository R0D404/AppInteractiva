package com.example.appinteractiva.tareas.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appinteractiva.R
import com.example.appinteractiva.tareas.data.model.FiltroEstado
import com.example.appinteractiva.tareas.data.model.Prioridad
import com.example.appinteractiva.ui.theme.TareasEscolaresTheme

/**
 * Sección de filtros para clasificar la lista por estado (Todas, Pendientes, Completadas)
 * y opcionalmente por nivel de prioridad.
 *
 * @param filtroEstado Filtro de estado actualmente seleccionado.
 * @param filtroPrioridad Filtro de prioridad activo, o null si se muestran todas.
 * @param onFiltroEstadoChange Evento emitido al seleccionar un nuevo filtro de estado.
 * @param onFiltroPrioridadChange Evento emitido al cambiar o deseleccionar el filtro de prioridad.
 * @param modifier Modificador para el diseño externo del componente.
 */
@Composable
fun FiltrosRow(
    filtroEstado: FiltroEstado,
    filtroPrioridad: Prioridad?,
    onFiltroEstadoChange: (FiltroEstado) -> Unit,
    onFiltroPrioridadChange: (Prioridad?) -> Unit,
    modifier: Modifier = Modifier
) {
    val filtrosEstadoOpciones = listOf(
        FiltroEstado.TODAS to R.string.filtro_todas,
        FiltroEstado.PENDIENTES to R.string.filtro_pendientes,
        FiltroEstado.COMPLETADAS to R.string.filtro_completadas
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Fila 1: Filtros de estado principal con forma circular
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filtrosEstadoOpciones.forEach { (estado, stringRes) ->
                ChipOpcion(
                    texto = stringResource(stringRes),
                    seleccionado = estado == filtroEstado,
                    onClick = { onFiltroEstadoChange(estado) },
                    shape = CircleShape
                )
            }
        }

        // Fila 2: Filtro por prioridad con esquinas redondeadas
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.etiqueta_prioridad),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.width(2.dp))

            Prioridad.entries.forEach { prioridad ->
                val seleccionado = prioridad == filtroPrioridad
                ChipOpcion(
                    texto = stringResource(prioridad.etiquetaRes()),
                    seleccionado = seleccionado,
                    onClick = {
                        // Si ya está seleccionado, al tocarlo se deselecciona (null)
                        if (seleccionado) {
                            onFiltroPrioridadChange(null)
                        } else {
                            onFiltroPrioridadChange(prioridad)
                        }
                    },
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FiltrosRowPreview() {
    TareasEscolaresTheme {
        FiltrosRow(
            filtroEstado = FiltroEstado.TODAS,
            filtroPrioridad = Prioridad.ALTA,
            onFiltroEstadoChange = {},
            onFiltroPrioridadChange = {},
            modifier = Modifier.padding(vertical = 12.dp)
        )
    }
}
