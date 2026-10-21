package com.example.appinteractiva.tareas.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appinteractiva.tareas.data.model.FiltroEstado
import com.example.appinteractiva.tareas.data.model.Prioridad
import com.example.appinteractiva.tareas.data.model.Tarea
import com.example.appinteractiva.tareas.data.repository.TareasRepository
import com.example.appinteractiva.tareas.presentation.screens.TareasUiState
import com.example.appinteractiva.tareas.util.FechaUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel que gestiona la lógica de presentación de la pantalla principal de tareas.
 *
 * Mantiene el estado inmutable [TareasUiState] y procesa las acciones del usuario
 * respetando el flujo unidireccional de datos (UDF).
 *
 * @param repository Repositorio de tareas inyectado mediante su interfaz.
 */
class TareasViewModel(
    private val repository: TareasRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TareasUiState(estaCargando = true))
    val uiState: StateFlow<TareasUiState> = _uiState.asStateFlow()

    private var todasLasTareas: List<Tarea> = emptyList()
    private var filtroEstadoActual: FiltroEstado = FiltroEstado.TODAS
    private var filtroPrioridadActual: Prioridad? = null

    init {
        viewModelScope.launch {
            repository.tareas.collect { tareasEmitidas ->
                todasLasTareas = tareasEmitidas
                _uiState.update { estadoActual ->
                    calcularEstado(
                        estadoBase = estadoActual,
                        tareasCompletas = todasLasTareas,
                        filtroEstado = filtroEstadoActual,
                        filtroPrioridad = filtroPrioridadActual
                    )
                }
            }
        }
    }

    /**
     * Alterna el estado completado de una tarea específica.
     */
    fun onToggleCompletada(id: Int) {
        viewModelScope.launch {
            repository.alternarCompletada(id)
        }
    }

    /**
     * Elimina permanentemente una tarea escolar.
     */
    fun onEliminar(id: Int) {
        viewModelScope.launch {
            repository.eliminarTarea(id)
        }
    }

    /**
     * Aplica un nuevo filtro por estado (Todas, Pendientes, Completadas).
     */
    fun onFiltroEstadoChange(filtro: FiltroEstado) {
        filtroEstadoActual = filtro
        _uiState.update { estadoActual ->
            calcularEstado(
                estadoBase = estadoActual,
                tareasCompletas = todasLasTareas,
                filtroEstado = filtroEstadoActual,
                filtroPrioridad = filtroPrioridadActual
            )
        }
    }

    /**
     * Aplica o deselecciona el filtro por nivel de prioridad.
     */
    fun onFiltroPrioridadChange(prioridad: Prioridad?) {
        filtroPrioridadActual = prioridad
        _uiState.update { estadoActual ->
            calcularEstado(
                estadoBase = estadoActual,
                tareasCompletas = todasLasTareas,
                filtroEstado = filtroEstadoActual,
                filtroPrioridad = filtroPrioridadActual
            )
        }
    }

    /**
     * Función pura que calcula y retorna una copia del estado con las reglas de negocio aplicadas:
     * - RN-10: Combinación de filtros de estado y prioridad.
     * - RN-11: Ordenación por fecha límite ascendente y desempate por prioridad (ALTA > MEDIA > BAJA).
     * - RN-12: Métricas calculadas sobre todas las tareas guardadas, no sobre el subconjunto filtrado.
     */
    private fun calcularEstado(
        estadoBase: TareasUiState,
        tareasCompletas: List<Tarea>,
        filtroEstado: FiltroEstado,
        filtroPrioridad: Prioridad?
    ): TareasUiState {
        // Métricas globales (RN-12)
        val total = tareasCompletas.size
        val completadas = tareasCompletas.count { it.completada }
        val progreso = if (total > 0) completadas.toFloat() / total else 0f

        // Comparador para ordenar tareas (RN-11)
        val comparador = Comparator<Tarea> { t1, t2 ->
            val fecha1 = FechaUtil.parsearFecha(t1.fechaLimite)
            val fecha2 = FechaUtil.parsearFecha(t2.fechaLimite)
            when {
                fecha1 == null && fecha2 == null -> 0
                fecha1 == null -> 1  // Fecha inválida va al final
                fecha2 == null -> -1 // Fecha inválida va al final
                else -> fecha1.compareTo(fecha2)
            }
        }.thenBy { tarea ->
            // Desempate por orden de prioridad (ALTA primero, luego MEDIA, luego BAJA)
            when (tarea.prioridad) {
                Prioridad.ALTA -> 0
                Prioridad.MEDIA -> 1
                Prioridad.BAJA -> 2
            }
        }

        // Filtrado y ordenamiento combinado (RN-10 y RN-11)
        val tareasFiltradasYOrdenadas = tareasCompletas
            .filter { tarea ->
                val coincideEstado = when (filtroEstado) {
                    FiltroEstado.TODAS -> true
                    FiltroEstado.PENDIENTES -> !tarea.completada
                    FiltroEstado.COMPLETADAS -> tarea.completada
                }
                val coincidePrioridad = filtroPrioridad == null || tarea.prioridad == filtroPrioridad
                coincideEstado && coincidePrioridad
            }
            .sortedWith(comparador)

        return estadoBase.copy(
            tareas = tareasFiltradasYOrdenadas,
            filtroEstado = filtroEstado,
            filtroPrioridad = filtroPrioridad,
            totalTareas = total,
            tareasCompletadas = completadas,
            porcentajeProgreso = progreso,
            estaCargando = false
        )
    }
}
