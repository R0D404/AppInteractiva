package com.example.appinteractiva.tareas.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appinteractiva.ui.theme.TareasEscolaresTheme

/**
 * Chip interactivo reutilizable para filtros y selección de opciones.
 *
 * Aplica automáticamente los colores del tema según si está seleccionado o no,
 * admitiendo cualquier forma geométrica [shape].
 *
 * @param texto Texto a mostrar dentro del chip.
 * @param seleccionado Indica si la opción está actualmente activa.
 * @param onClick Acción a ejecutar al pulsar el chip.
 * @param shape Forma geométrica para los bordes del chip (por ejemplo CircleShape o RoundedCornerShape).
 * @param modifier Modificador para el diseño externo del chip.
 */
@Composable
fun ChipOpcion(
    texto: String,
    seleccionado: Boolean,
    onClick: () -> Unit,
    shape: Shape,
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = seleccionado,
        onClick = onClick,
        label = {
            Text(
                text = texto,
                style = MaterialTheme.typography.labelMedium
            )
        },
        shape = shape,
        border = BorderStroke(
            width = 1.dp,
            color = if (seleccionado) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outline
            }
        ),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = MaterialTheme.colorScheme.surface,
            labelColor = MaterialTheme.colorScheme.secondary,
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun ChipOpcionPreview() {
    TareasEscolaresTheme {
        ChipOpcion(
            texto = "Seleccionado",
            seleccionado = true,
            onClick = {},
            shape = CircleShape,
            modifier = Modifier.padding(16.dp)
        )
    }
}
