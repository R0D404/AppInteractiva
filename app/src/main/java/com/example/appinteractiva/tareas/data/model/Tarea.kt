package com.example.appinteractiva.tareas.data.model

/**
 * Modelo inmutable que representa una tarea escolar individual.
 *
 * @property id Identificador único de la tarea.
 * @property titulo Nombre o descripción breve de la actividad.
 * @property materia Asignatura a la que corresponde la tarea.
 * @property prioridad Nivel de prioridad (ALTA, MEDIA, BAJA).
 * @property fechaLimite Fecha límite de entrega en formato de texto (dd/MM/yyyy).
 * @property completada Indica si la tarea ya ha sido finalizada por el estudiante.
 */
data class Tarea(
    val id: Int,
    val titulo: String,
    val materia: String,
    val prioridad: Prioridad,
    val fechaLimite: String,
    val completada: Boolean = false
)
