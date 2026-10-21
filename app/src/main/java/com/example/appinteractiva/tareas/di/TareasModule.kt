package com.example.appinteractiva.tareas.di

import android.content.Context
import com.example.appinteractiva.tareas.data.repository.TareasRepository
import com.example.appinteractiva.tareas.data.repository.TareasRepositoryDataStore
import com.example.appinteractiva.tareas.presentation.viewmodels.TareasViewModelFactory

/**
 * Módulo de inyección de dependencias manual para el contenedor de la aplicación.
 *
 * Centraliza la creación del repositorio mediante su interfaz [TareasRepository]
 * y la fábrica [TareasViewModelFactory].
 *
 * @param context Contexto de la aplicación Android.
 */
class TareasModule(
    private val context: Context
) {

    val repository: TareasRepository by lazy {
        TareasRepositoryDataStore(context)
    }

    val factory: TareasViewModelFactory by lazy {
        TareasViewModelFactory(repository)
    }
}
