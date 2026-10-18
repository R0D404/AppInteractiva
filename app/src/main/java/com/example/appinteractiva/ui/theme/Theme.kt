package com.example.appinteractiva.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Esquema de colores de la aplicación basado en la paleta azul escolar requerida.
 */
private val LightColorScheme = lightColorScheme(
    primary = AzulPrimario,
    onPrimary = Color.White,
    primaryContainer = AzulClaro,
    onPrimaryContainer = AzulOscuro,
    secondary = AzulOscuro,
    onSecondary = Color.White,
    secondaryContainer = AzulClaro,
    onSecondaryContainer = AzulOscuro,
    background = AzulFondo,
    onBackground = TextoPrincipal,
    surface = BlancoSuperficie,
    onSurface = TextoPrincipal,
    surfaceVariant = BlancoSuperficie,
    onSurfaceVariant = TextoSecundario,
    outline = AzulBorde,
    outlineVariant = AzulBorde,
    error = ColorError,
    onError = Color.White
)

/**
 * Tema principal de la aplicación Tareas Escolares.
 * Aplica la paleta de colores azul fija y la escala tipográfica a todos los Composables.
 *
 * @param content Contenido composable que heredará este tema.
 */
@Composable
fun TareasEscolaresTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}