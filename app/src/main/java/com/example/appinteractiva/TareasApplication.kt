package com.example.appinteractiva

import android.app.Application
import com.example.appinteractiva.tareas.di.TareasModule

/**
 * Clase de aplicación que sirve como contenedor raíz para la inyección manual de dependencias.
 */
class TareasApplication : Application() {

    val tareasModule: TareasModule by lazy {
        TareasModule(applicationContext)
    }
}
