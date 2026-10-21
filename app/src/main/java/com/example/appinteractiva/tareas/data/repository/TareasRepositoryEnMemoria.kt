package com.example.appinteractiva.tareas.data.repository

import com.example.appinteractiva.tareas.data.model.Prioridad
import com.example.appinteractiva.tareas.data.model.Tarea
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Implementación TEMPORAL en memoria del repositorio de tareas escolares.
 *
 * Inicia con una lista vacía para que el usuario agregue sus propias tareas.
 * Mantiene las tareas reactivamente mediante un [MutableStateFlow]. Esta clase será
 * reemplazada en la siguiente etapa del proyecto por una implementación con DataStore
 * y datos persistentes, sin requerir cambios en el ViewModel gracias al principio
 * de inversión de dependencias (DIP) de SOLID.
 */
class TareasRepositoryEnMemoria : TareasRepository {

    // Inicia con lista vacía para que el usuario ingrese sus tareas desde cero
    private val _tareas = MutableStateFlow<List<Tarea>>(emptyList())
    override val tareas: Flow<List<Tarea>> = _tareas.asStateFlow()

    override suspend fun agregarTarea(
        titulo: String,
        materia: String,
        prioridad: Prioridad,
        fechaLimite: String
    ) {
        _tareas.update { listaActual ->
            val nuevoId = (listaActual.maxOfOrNull { it.id } ?: 0) + 1
            val nuevaTarea = Tarea(
                id = nuevoId,
                titulo = titulo,
                materia = materia,
                prioridad = prioridad,
                fechaLimite = fechaLimite,
                completada = false
            )
            listaActual + nuevaTarea
        }
    }

    override suspend fun alternarCompletada(id: Int) {
        _tareas.update { listaActual ->
            listaActual.map { tarea ->
                if (tarea.id == id) {
                    tarea.copy(completada = !tarea.completada)
                } else {
                    tarea
                }
            }
        }
    }

    override suspend fun eliminarTarea(id: Int) {
        _tareas.update { listaActual ->
            listaActual.filter { it.id != id }
        }
    }
}
