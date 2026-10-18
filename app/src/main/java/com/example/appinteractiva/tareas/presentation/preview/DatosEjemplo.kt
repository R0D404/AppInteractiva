package com.example.appinteractiva.tareas.presentation.preview

import com.example.appinteractiva.R
import com.example.appinteractiva.tareas.data.model.FiltroEstado
import com.example.appinteractiva.tareas.data.model.Prioridad
import com.example.appinteractiva.tareas.data.model.Tarea
import com.example.appinteractiva.tareas.presentation.screens.FormularioUiState
import com.example.appinteractiva.tareas.presentation.screens.TareasUiState

/**
 * Proveedor de datos ficticios para Previews de Compose y visualización inicial en el emulador.
 */
object DatosEjemplo {

    val listaTareasEjemplo: List<Tarea> = listOf(
        Tarea(
            id = 1,
            titulo = "Mockups de la app",
            materia = "Desarrollo de apps móviles",
            prioridad = Prioridad.ALTA,
            fechaLimite = "12/10/2026",
            completada = false
        ),
        Tarea(
            id = 2,
            titulo = "Resumen de planificación de CPU",
            materia = "Sistemas operativos",
            prioridad = Prioridad.MEDIA,
            fechaLimite = "13/10/2026",
            completada = true
        ),
        Tarea(
            id = 3,
            titulo = "Repasar estructuras de control",
            materia = "Programación orientada a objetos",
            prioridad = Prioridad.MEDIA,
            fechaLimite = "14/10/2026",
            completada = false
        ),
        Tarea(
            id = 4,
            titulo = "Ejercicios de normalización",
            materia = "Bases de datos",
            prioridad = Prioridad.MEDIA,
            fechaLimite = "15/10/2026",
            completada = true
        )
    )

    val tareasUiStateEjemplo: TareasUiState = TareasUiState(
        tareas = listaTareasEjemplo,
        filtroEstado = FiltroEstado.TODAS,
        filtroPrioridad = null,
        totalTareas = 7,
        tareasCompletadas = 2,
        porcentajeProgreso = 2f / 7f,
        estaCargando = false
    )

    val tareasUiStateVacio: TareasUiState = TareasUiState(
        tareas = emptyList(),
        filtroEstado = FiltroEstado.TODAS,
        filtroPrioridad = null,
        totalTareas = 0,
        tareasCompletadas = 0,
        porcentajeProgreso = 0f,
        estaCargando = false
    )

    val formularioUiStateEjemplo: FormularioUiState = FormularioUiState(
        titulo = "Estudiar para el examen de BD",
        materia = "",
        prioridad = Prioridad.MEDIA,
        fechaLimite = "15/10/2026",
        errorMateria = R.string.error_materia_obligatoria
    )
}
