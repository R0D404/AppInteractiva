package com.example.appinteractiva.tareas.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appinteractiva.tareas.data.model.Prioridad
import com.example.appinteractiva.ui.theme.PrioridadAltaFondo
import com.example.appinteractiva.ui.theme.PrioridadAltaTexto
import com.example.appinteractiva.ui.theme.PrioridadBajaFondo
import com.example.appinteractiva.ui.theme.PrioridadBajaTexto
import com.example.appinteractiva.ui.theme.PrioridadMediaFondo
import com.example.appinteractiva.ui.theme.PrioridadMediaTexto
import com.example.appinteractiva.ui.theme.TareasEscolaresTheme

/**
 * Insignia visual que resalta el nivel de prioridad de una tarea con un color distintivo.
 *
 * @param prioridad Nivel de prioridad a representar (ALTA, MEDIA o BAJA).
 * @param modifier Modificador para personalizar el diseño externo del componente.
 */
@Composable
fun PrioridadBadge(
    prioridad: Prioridad,
    modifier: Modifier = Modifier
) {
    val (fondo, textoColor) = when (prioridad) {
        Prioridad.ALTA -> PrioridadAltaFondo to PrioridadAltaTexto
        Prioridad.MEDIA -> PrioridadMediaFondo to PrioridadMediaTexto
        Prioridad.BAJA -> PrioridadBajaFondo to PrioridadBajaTexto
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(fondo)
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(
            text = stringResource(prioridad.etiquetaRes()),
            color = textoColor,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PrioridadBadgePreview() {
    TareasEscolaresTheme {
        Row(modifier = Modifier.padding(16.dp)) {
            PrioridadBadge(prioridad = Prioridad.ALTA)
            Spacer(modifier = Modifier.width(8.dp))
            PrioridadBadge(prioridad = Prioridad.MEDIA)
            Spacer(modifier = Modifier.width(8.dp))
            PrioridadBadge(prioridad = Prioridad.BAJA)
        }
    }
}
