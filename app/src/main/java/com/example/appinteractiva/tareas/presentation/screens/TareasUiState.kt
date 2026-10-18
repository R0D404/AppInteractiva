package com.example.appinteractiva.tareas.presentation.screens

import com.example.appinteractiva.tareas.data.model.FiltroEstado
import com.example.appinteractiva.tareas.data.model.Prioridad
import com.example.appinteractiva.tareas.data.model.Tarea

/**
 * Estado inmutable de la pantalla principal de lista de tareas.
 *
 * Expone propiedades calculadas para evitar lógica de negocio dentro de los Composables.
 *
 * @property tareas Lista de tareas preparadas para mostrarse en la UI.
 * @property filtroEstado Filtro activo por estado de realización.
 * @property filtroPrioridad Filtro activo por nivel de prioridad, o null si no se filtra por prioridad.
 * @property totalTareas Cantidad total de tareas registradas.
 * @property tareasCompletadas Cantidad de tareas marcadas como completadas.
 * @property porcentajeProgreso Proporción de tareas completadas en rango de 0.0 a 1.0.
 * @property estaCargando Indica si la aplicación está cargando información en segundo plano.
 */
data class TareasUiState(
    val tareas: List<Tarea> = emptyList(),
    val filtroEstado: FiltroEstado = FiltroEstado.TODAS,
    val filtroPrioridad: Prioridad? = null,
    val totalTareas: Int = 0,
    val tareasCompletadas: Int = 0,
    val porcentajeProgreso: Float = 0f,
    val estaCargando: Boolean = false
) {
    /**
     * Número de tareas aún no completadas.
     */
    val tareasPendientes: Int
        get() = (totalTareas - tareasCompletadas).coerceAtLeast(0)

    /**
     * Porcentaje de avance redondeado como número entero (0 a 100).
     */
    val porcentajeEntero: Int
        get() = (porcentajeProgreso * 100).toInt()
}
