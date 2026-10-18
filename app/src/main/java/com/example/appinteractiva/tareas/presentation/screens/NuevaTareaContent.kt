package com.example.appinteractiva.tareas.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appinteractiva.R
import com.example.appinteractiva.tareas.data.model.Prioridad
import com.example.appinteractiva.tareas.presentation.components.PrioridadChips
import com.example.appinteractiva.tareas.presentation.preview.DatosEjemplo
import com.example.appinteractiva.ui.theme.TareasEscolaresTheme

/**
 * Pantalla con el formulario de creación de una nueva tarea escolar.
 *
 * Es un Composable completamente STATELESS: delega cualquier cambio en los campos
 * mediante callbacks hacia el llamador (State Hoisting).
 *
 * @param uiState Estado inmutable con los valores actuales de los campos y sus errores de validación.
 * @param onTituloChange Callback emitido cuando el usuario modifica el título de la tarea.
 * @param onMateriaChange Callback emitido al escribir en el campo de la materia.
 * @param onPrioridadChange Callback emitido al elegir una prioridad en los chips.
 * @param onFechaChange Callback emitido al escribir la fecha límite.
 * @param onGuardarClick Callback emitido al pulsar el botón principal "Guardar tarea".
 * @param onCancelarClick Callback emitido al presionar el botón de texto "Cancelar".
 * @param onVolverClick Callback emitido al tocar la flecha de regreso en la barra superior.
 * @param modifier Modificador para el diseño externo de la pantalla.
 */
@Composable
fun NuevaTareaContent(
    uiState: FormularioUiState,
    onTituloChange: (String) -> Unit,
    onMateriaChange: (String) -> Unit,
    onPrioridadChange: (Prioridad) -> Unit,
    onFechaChange: (String) -> Unit,
    onGuardarClick: () -> Unit,
    onCancelarClick: () -> Unit,
    onVolverClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.surface,
        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
        disabledContainerColor = MaterialTheme.colorScheme.surface,
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
        errorBorderColor = MaterialTheme.colorScheme.error,
        focusedLabelColor = MaterialTheme.colorScheme.primary,
        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        errorLabelColor = MaterialTheme.colorScheme.error,
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 1. Barra superior con fondo azul y esquinas inferiores curvas
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .statusBarsPadding()
                    .padding(horizontal = 12.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onVolverClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.cd_volver),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Text(
                    text = stringResource(R.string.titulo_nueva_tarea),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // 2. Formulario desplazable
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .imePadding()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                // Campo "Título"
                OutlinedTextField(
                    value = uiState.titulo,
                    onValueChange = onTituloChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            text = stringResource(R.string.campo_titulo),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.placeholder_titulo),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors,
                    isError = uiState.errorTitulo != null,
                    supportingText = uiState.errorTitulo?.let { errorRes ->
                        {
                            Text(
                                text = stringResource(errorRes),
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                )

                // Campo "Materia"
                OutlinedTextField(
                    value = uiState.materia,
                    onValueChange = onMateriaChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            text = stringResource(R.string.campo_materia),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.placeholder_materia),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors,
                    isError = uiState.errorMateria != null,
                    supportingText = uiState.errorMateria?.let { errorRes ->
                        {
                            Text(
                                text = stringResource(errorRes),
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                )

                // Campo "Prioridad"
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.etiqueta_prioridad),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    PrioridadChips(
                        prioridadSeleccionada = uiState.prioridad,
                        onPrioridadSelected = onPrioridadChange
                    )
                }

                // Campo "Fecha límite"
                OutlinedTextField(
                    value = uiState.fechaLimite,
                    onValueChange = onFechaChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            text = stringResource(R.string.campo_fecha_limite),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.placeholder_fecha_limite),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Botón "Guardar tarea"
                Button(
                    onClick = onGuardarClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = stringResource(R.string.boton_guardar_tarea),
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                // Botón "Cancelar" centrado
                TextButton(
                    onClick = onCancelarClick,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = stringResource(R.string.boton_cancelar),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun NuevaTareaContentPreview() {
    TareasEscolaresTheme {
        NuevaTareaContent(
            uiState = DatosEjemplo.formularioUiStateEjemplo,
            onTituloChange = {},
            onMateriaChange = {},
            onPrioridadChange = {},
            onFechaChange = {},
            onGuardarClick = {},
            onCancelarClick = {},
            onVolverClick = {}
        )
    }
}
