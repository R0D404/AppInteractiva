package com.example.appinteractiva.tareas.presentation.components

import androidx.annotation.StringRes
import com.example.appinteractiva.R
import com.example.appinteractiva.tareas.data.model.Prioridad

/**
 * Función de extensión en la capa de presentación que asocia cada nivel de [Prioridad]
 * con su respectivo recurso de texto en strings.xml.
 *
 * Mantiene el modelo de datos desacoplado de los recursos de Android.
 */
@StringRes
fun Prioridad.etiquetaRes(): Int = when (this) {
    Prioridad.ALTA -> R.string.prioridad_alta
    Prioridad.MEDIA -> R.string.prioridad_media
    Prioridad.BAJA -> R.string.prioridad_baja
}
