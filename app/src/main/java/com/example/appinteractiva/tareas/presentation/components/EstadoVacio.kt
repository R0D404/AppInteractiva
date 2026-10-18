package com.example.appinteractiva.tareas.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appinteractiva.R
import com.example.appinteractiva.ui.theme.TareasEscolaresTheme

/**
 * Componente que se presenta cuando la lista de tareas no contiene elementos.
 *
 * Ofrece retroalimentación visual al usuario invitándolo a crear una nueva tarea
 * o ajustar los filtros activos.
 *
 * @param modifier Modificador para el diseño externo del componente.
 */
@Composable
fun EstadoVacio(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icono suave de gran tamaño
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Assignment,
            contentDescription = stringResource(R.string.cd_icono_vacio),
            modifier = Modifier.size(76.dp),
            tint = MaterialTheme.colorScheme.outline
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Mensaje principal de estado vacío
        Text(
            text = stringResource(R.string.estado_vacio_titulo),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Mensaje orientativo secundario
        Text(
            text = stringResource(R.string.estado_vacio_subtitulo),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EstadoVacioPreview() {
    TareasEscolaresTheme {
        EstadoVacio()
    }
}
