package com.example.appinteractiva.tareas.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.appinteractiva.tareas.data.model.Prioridad
import com.example.appinteractiva.tareas.data.model.Tarea
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

// Extensión que crea/accede al DataStore de preferencias asociado al contexto de la app.
// El archivo en disco se llamará "tareas_escolares_prefs.preferences_pb"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "tareas_escolares_prefs"
)

/**
 * Implementación de [TareasRepository] que persiste las tareas en disco
 * usando Jetpack Preferences DataStore y JSON nativo de Android (org.json).
 *
 * @param context Contexto de la aplicación, necesario para acceder al DataStore.
 */
class TareasRepositoryDataStore(private val context: Context) : TareasRepository {

    // Clave que identifica el valor JSON dentro del DataStore
    private val TAREAS_KEY = stringPreferencesKey("tareas_json")

    /**
     * Flujo que emite la lista de tareas cada vez que el DataStore cambia.
     * Filtra excepciones de E/S ([IOException]) emitiendo preferencias vacías de forma segura
     * y ejecuta el parseo de JSON en el despachador de E/S ([Dispatchers.IO]).
     */
    override val tareas: Flow<List<Tarea>> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val json = preferences[TAREAS_KEY] ?: "[]"
            parsearTareasDesdeJson(json)
        }
        .flowOn(Dispatchers.IO)

    /**
     * Agrega una nueva tarea al DataStore.
     * El ID se calcula como el máximo ID existente + 1.
     * Se ejecuta de forma segura en [Dispatchers.IO].
     */
    override suspend fun agregarTarea(
        titulo: String,
        materia: String,
        prioridad: Prioridad,
        fechaLimite: String
    ) {
        withContext(Dispatchers.IO) {
            context.dataStore.edit { preferences ->
                // Leer la lista actual desde el JSON guardado
                val jsonActual = preferences[TAREAS_KEY] ?: "[]"
                val listaActual = parsearTareasDesdeJson(jsonActual)

                // Calcular el nuevo ID autoincremental
                val nuevoId = (listaActual.maxOfOrNull { it.id } ?: 0) + 1

                // Crear la nueva tarea (inicia no completada)
                val nuevaTarea = Tarea(
                    id = nuevoId,
                    titulo = titulo,
                    materia = materia,
                    prioridad = prioridad,
                    fechaLimite = fechaLimite,
                    completada = false
                )

                // Guardar la lista actualizada como JSON
                val listaActualizada = listaActual + nuevaTarea
                preferences[TAREAS_KEY] = tareasAJson(listaActualizada)
            }
        }
    }

    /**
     * Cambia el estado de completada de una tarea identificada por su [id].
     * Si la tarea estaba pendiente pasa a completada, y viceversa.
     * Se ejecuta de forma segura en [Dispatchers.IO].
     */
    override suspend fun alternarCompletada(id: Int) {
        withContext(Dispatchers.IO) {
            context.dataStore.edit { preferences ->
                val jsonActual = preferences[TAREAS_KEY] ?: "[]"
                val listaActual = parsearTareasDesdeJson(jsonActual)

                // Invertir el campo "completada" solo en la tarea con el ID indicado
                val listaActualizada = listaActual.map { tarea ->
                    if (tarea.id == id) tarea.copy(completada = !tarea.completada)
                    else tarea
                }

                preferences[TAREAS_KEY] = tareasAJson(listaActualizada)
            }
        }
    }

    /**
     * Elimina del DataStore la tarea que tenga el [id] indicado.
     * Se ejecuta de forma segura en [Dispatchers.IO].
     */
    override suspend fun eliminarTarea(id: Int) {
        withContext(Dispatchers.IO) {
            context.dataStore.edit { preferences ->
                val jsonActual = preferences[TAREAS_KEY] ?: "[]"
                val listaActual = parsearTareasDesdeJson(jsonActual)

                // Filtrar para quitar la tarea con ese ID
                val listaActualizada = listaActual.filter { it.id != id }

                preferences[TAREAS_KEY] = tareasAJson(listaActualizada)
            }
        }
    }

    // ─── Funciones auxiliares de serialización ──────────────────────────────

    /**
     * Convierte una lista de [Tarea] a un String JSON usando la librería org.json.
     * Ejemplo de salida:
     * [{"id":1,"titulo":"Matemáticas","materia":"Mat","prioridad":"ALTA","fechaLimite":"12/10/2026","completada":false}]
     */
    private fun tareasAJson(lista: List<Tarea>): String {
        val array = JSONArray()
        for (tarea in lista) {
            val obj = JSONObject().apply {
                put("id", tarea.id)
                put("titulo", tarea.titulo)
                put("materia", tarea.materia)
                put("prioridad", tarea.prioridad.name)
                put("fechaLimite", tarea.fechaLimite)
                put("completada", tarea.completada)
            }
            array.put(obj)
        }
        return array.toString()
    }

    /**
     * Convierte un String JSON de vuelta a una lista de [Tarea].
     * Maneja casos con JSONs vacíos, nulos o elementos malformados de forma tolerante a fallos.
     */
    private fun parsearTareasDesdeJson(json: String): List<Tarea> {
        if (json.isBlank()) return emptyList()
        return try {
            val array = JSONArray(json)
            val lista = mutableListOf<Tarea>()
            for (i in 0 until array.length()) {
                try {
                    val obj = array.getJSONObject(i)
                    val id = obj.getInt("id")
                    val titulo = obj.optString("titulo", "")
                    val materia = obj.optString("materia", "")
                    val prioridadStr = obj.optString("prioridad", Prioridad.MEDIA.name)
                    val prioridad = runCatching { Prioridad.valueOf(prioridadStr) }.getOrDefault(Prioridad.MEDIA)
                    val fechaLimite = obj.optString("fechaLimite", "")
                    val completada = obj.optBoolean("completada", false)

                    lista.add(
                        Tarea(
                            id = id,
                            titulo = titulo,
                            materia = materia,
                            prioridad = prioridad,
                            fechaLimite = fechaLimite,
                            completada = completada
                        )
                    )
                } catch (e: Exception) {
                    // Ignorar un elemento individual corrupto para no descartar las demás tareas
                }
            }
            lista
        } catch (e: Exception) {
            emptyList()
        }
    }
}

