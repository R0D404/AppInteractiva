package com.example.appinteractiva.tareas.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.appinteractiva.tareas.data.repository.TareasRepository

/**
 * Fábrica unificada para crear instancias de [TareasViewModel] y [NuevaTareaViewModel]
 * inyectándoles la implementación de [TareasRepository].
 *
 * @param repository Instancia del repositorio de tareas a inyectar en los ViewModels.
 */
class TareasViewModelFactory(
    private val repository: TareasRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(TareasViewModel::class.java) -> {
                TareasViewModel(repository) as T
            }
            modelClass.isAssignableFrom(NuevaTareaViewModel::class.java) -> {
                NuevaTareaViewModel(repository) as T
            }
            else -> {
                throw IllegalArgumentException("Clase de ViewModel desconocida: ${modelClass.name}")
            }
        }
    }
}
