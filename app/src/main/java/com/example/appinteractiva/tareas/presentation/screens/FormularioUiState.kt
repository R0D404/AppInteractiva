package com.example.appinteractiva.tareas.presentation.screens

import androidx.annotation.StringRes
import com.example.appinteractiva.tareas.data.model.Prioridad

/**
 * Estado inmutable del formulario para registrar una nueva tarea.
 *
 * Los errores de validación se manejan como IDs de recurso (@get:StringRes Int?)
 * para mantener el estado desacoplado de Android Context.
 *
 * @property titulo Texto introducido para el título de la tarea.
 * @property materia Texto introducido para el nombre de la materia.
 * @property prioridad Nivel de prioridad seleccionado.
 * @property fechaLimite Fecha límite escrita en el campo de texto.
 * @property errorTitulo Identificador de recurso string del error en el título, o null si es válido.
 * @property errorMateria Identificador de recurso string del error en la materia, o null si es válido.
 * @property errorFecha Identificador de recurso string del error en la fecha límite, o null si es válido.
 * @property guardadoExitoso Indica si la tarea se guardó con éxito en el repositorio.
 */
data class FormularioUiState(
    val titulo: String = "",
    val materia: String = "",
    val prioridad: Prioridad = Prioridad.MEDIA,
    val fechaLimite: String = "",
    @get:StringRes val errorTitulo: Int? = null,
    @get:StringRes val errorMateria: Int? = null,
    @get:StringRes val errorFecha: Int? = null,
    val guardadoExitoso: Boolean = false
)
