package com.example.appinteractiva.tareas.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appinteractiva.tareas.data.model.Prioridad
import com.example.appinteractiva.ui.theme.TareasEscolaresTheme

/**
 * Selector horizontal con chips para elegir el nivel de prioridad de una tarea.
 *
 * @param prioridadSeleccionada Nivel de prioridad actualmente activo.
 * @param onPrioridadSelected Callback ejecutado al seleccionar una opción de prioridad.
 * @param modifier Modificador para el diseño externo del componente.
 */
@Composable
fun PrioridadChips(
    prioridadSeleccionada: Prioridad,
    onPrioridadSelected: (Prioridad) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Prioridad.entries.forEach { prioridad ->
            ChipOpcion(
                texto = stringResource(prioridad.etiquetaRes()),
                seleccionado = prioridad == prioridadSeleccionada,
                onClick = { onPrioridadSelected(prioridad) },
                shape = RoundedCornerShape(10.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PrioridadChipsPreview() {
    TareasEscolaresTheme {
        PrioridadChips(
            prioridadSeleccionada = Prioridad.MEDIA,
            onPrioridadSelected = {}
        )
    }
}
