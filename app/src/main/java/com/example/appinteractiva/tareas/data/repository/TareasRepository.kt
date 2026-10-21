package com.example.appinteractiva.tareas.data.repository

import com.example.appinteractiva.tareas.data.model.Prioridad
import com.example.appinteractiva.tareas.data.model.Tarea
import kotlinx.coroutines.flow.Flow

/**
 * Contrato del repositorio para la gestión de tareas escolares.
 *
 * Abstrae el origen de datos (en memoria, DataStore, base de datos)
 * permitiendo que la capa de dominio y presentación no dependa de detalles de persistencia.
 */
interface TareasRepository {

    /**
     * Flujo observable que emite la lista actualizada de tareas escolares.
     */
    val tareas: Flow<List<Tarea>>

    /**
     * Agrega una nueva tarea escolar al almacenamiento.
     *
     * @param titulo Nombre de la actividad escolar.
     * @param materia Asignatura a la que corresponde.
     * @param prioridad Grado de importancia o urgencia.
     * @param fechaLimite Fecha de entrega en formato dd/MM/yyyy.
     */
    suspend fun agregarTarea(
        titulo: String,
        materia: String,
        prioridad: Prioridad,
        fechaLimite: String
    )

    /**
     * Invierte el estado de finalización de una tarea (de pendiente a completada o viceversa).
     *
     * @param id Identificador único de la tarea.
     */
    suspend fun alternarCompletada(id: Int)

    /**
     * Remueve permanentemente una tarea del almacenamiento.
     *
     * @param id Identificador único de la tarea a eliminar.
     */
    suspend fun eliminarTarea(id: Int)
}
