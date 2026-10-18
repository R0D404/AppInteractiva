package com.example.appinteractiva.tareas.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appinteractiva.R
import com.example.appinteractiva.ui.theme.TareasEscolaresTheme

/**
 * Tarjeta visual que muestra el avance en el cumplimiento de tareas escolares.
 *
 * @param tareasCompletadas Número de tareas completadas.
 * @param totalTareas Cantidad total de tareas registradas.
 * @param porcentajeProgreso Razón de avance (de 0.0 a 1.0) para la barra gráfica.
 * @param porcentajeEntero Valor numérico entero del porcentaje (0 a 100).
 * @param modifier Modificador para el diseño externo del componente.
 */
@Composable
fun ProgresoCard(
    tareasCompletadas: Int,
    totalTareas: Int,
    porcentajeProgreso: Float,
    porcentajeEntero: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            // Fila superior: Título de sección y contador de tareas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.titulo_progreso),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = stringResource(
                        R.string.progreso_tareas_contador,
                        tareasCompletadas,
                        totalTareas
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Barra de progreso con esquinas redondeadas
            LinearProgressIndicator(
                progress = { porcentajeProgreso.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primaryContainer
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Texto inferior: Porcentaje completado
            Text(
                text = stringResource(R.string.progreso_porcentaje, porcentajeEntero),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProgresoCardPreview() {
    TareasEscolaresTheme {
        ProgresoCard(
            tareasCompletadas = 2,
            totalTareas = 7,
            porcentajeProgreso = 2f / 7f,
            porcentajeEntero = 29,
            modifier = Modifier.padding(16.dp)
        )
    }
}
