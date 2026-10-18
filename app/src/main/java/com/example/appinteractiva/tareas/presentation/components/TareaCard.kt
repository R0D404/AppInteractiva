package com.example.appinteractiva.tareas.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appinteractiva.R
import com.example.appinteractiva.tareas.data.model.Tarea
import com.example.appinteractiva.tareas.presentation.preview.DatosEjemplo
import com.example.appinteractiva.ui.theme.IconoEliminar
import com.example.appinteractiva.ui.theme.TareasEscolaresTheme

/**
 * Tarjeta individual que muestra la información de una tarea escolar y permite interactuar con ella.
 *
 * @param tarea Datos de la tarea escolar a renderizar.
 * @param onToggleCompletada Callback ejecutado cuando se pulsa el checkbox para alternar el estado.
 * @param onEliminar Callback ejecutado cuando se presiona el botón de eliminar tarea.
 * @param modifier Modificador para el diseño externo del componente.
 */
@Composable
fun TareaCard(
    tarea: Tarea,
    onToggleCompletada: (Int) -> Unit,
    onEliminar: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox a la izquierda para completar tarea
            Checkbox(
                checked = tarea.completada,
                onCheckedChange = { onToggleCompletada(tarea.id) },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.outline,
                    checkmarkColor = MaterialTheme.colorScheme.onPrimary
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Columna central con información de la tarea
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = tarea.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (tarea.completada) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                    textDecoration = if (tarea.completada) TextDecoration.LineThrough else null
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = tarea.materia,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PrioridadBadge(prioridad = tarea.prioridad)

                    Text(
                        text = stringResource(R.string.fecha_vence, tarea.fechaLimite),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Botón de eliminar con icono de papelera a la derecha
            IconButton(
                onClick = { onEliminar(tarea.id) }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.cd_eliminar_tarea),
                    tint = IconoEliminar
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TareaCardPendientePreview() {
    TareasEscolaresTheme {
        TareaCard(
            tarea = DatosEjemplo.listaTareasEjemplo[0],
            onToggleCompletada = {},
            onEliminar = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TareaCardCompletadaPreview() {
    TareasEscolaresTheme {
        TareaCard(
            tarea = DatosEjemplo.listaTareasEjemplo[1],
            onToggleCompletada = {},
            onEliminar = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
