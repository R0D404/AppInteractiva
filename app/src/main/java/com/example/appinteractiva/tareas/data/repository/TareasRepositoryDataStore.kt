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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

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
     * Si ocurre un error al leer, emite preferencias vacías (lista vacía).
     */
    override val tareas: Flow<List<Tarea>> = context.dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { preferences ->
            val json = preferences[TAREAS_KEY] ?: "[]"
            parsearTareasDesdeJson(json)
        }

    /**
     * Agrega una nueva tarea al DataStore.
     * El ID se calcula como el máximo ID existente + 1.
     */
    override suspend fun agregarTarea(
        titulo: String,
        materia: String,
        prioridad: Prioridad,
        fechaLimite: String
    ) {
        context.dataStore.edit { preferences ->
            // Leer la lista actual desde el JSON guardado
            val jsonActual = preferences[TAREAS_KEY] ?: "[]"
            val listaActual = parsearTareasDesdeJson(jsonActual)

            // Calcular el nuevo ID
            val nuevoId = (listaActual.maxOfOrNull { it.id } ?: 0) + 1

            // Crear la nueva tarea
            val nuevaTarea = Tarea(
                id = nuevoId,
                titulo = titulo,
                materia = materia,
                prioridad = prioridad,
                fechaLimite = fechaLimite
            )

            // Guardar la lista actualizada como JSON
            val listaActualizada = listaActual + nuevaTarea
            preferences[TAREAS_KEY] = tareasAJson(listaActualizada)
        }
    }

    /**
     * Cambia el estado de completada de una tarea identificada por su [id].
     * Si la tarea estaba pendiente pasa a completada, y viceversa.
     */
    override suspend fun alternarCompletada(id: Int) {
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

    /**
     * Elimina del DataStore la tarea que tenga el [id] indicado.
     */
    override suspend fun eliminarTarea(id: Int) {
        context.dataStore.edit { preferences ->
            val jsonActual = preferences[TAREAS_KEY] ?: "[]"
            val listaActual = parsearTareasDesdeJson(jsonActual)

            // Filtrar para quitar la tarea con ese ID
            val listaActualizada = listaActual.filter { it.id != id }

            preferences[TAREAS_KEY] = tareasAJson(listaActualizada)
        }
    }

    // ─── Funciones auxiliares de serialización ──────────────────────────────

    /**
     * Convierte una lista de [Tarea] a un String JSON usando la librería org.json.
     * Ejemplo de salida:
     * [{"id":1,"titulo":"Matemáticas","materia":"Mat","prioridad":"ALTA","fechaLimite":"2025-01-01","completada":false}]
     */
    private fun tareasAJson(lista: List<Tarea>): String {
        val array = JSONArray()
        for (tarea in lista) {
            val obj = JSONObject()
            obj.put("id", tarea.id)
            obj.put("titulo", tarea.titulo)
            obj.put("materia", tarea.materia)
            obj.put("prioridad", tarea.prioridad.name)   // guarda "ALTA", "MEDIA" o "BAJA"
            obj.put("fechaLimite", tarea.fechaLimite)
            obj.put("completada", tarea.completada)
            array.put(obj)
        }
        return array.toString()
    }

    /**
     * Convierte un String JSON de vuelta a una lista de [Tarea].
     * Si el JSON está vacío o malformado, devuelve lista vacía.
     */
    private fun parsearTareasDesdeJson(json: String): List<Tarea> {
        return try {
            val array = JSONArray(json)
            val lista = mutableListOf<Tarea>()
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                lista.add(
                    Tarea(
                        id          = obj.getInt("id"),
                        titulo      = obj.getString("titulo"),
                        materia     = obj.getString("materia"),
                        prioridad   = Prioridad.valueOf(obj.getString("prioridad")),
                        fechaLimite = obj.getString("fechaLimite"),
                        completada  = obj.getBoolean("completada")
                    )
                )
            }
            lista
        } catch (e: Exception) {
            // Si el JSON es inválido, devolvemos lista vacía para no crashear
            emptyList()
        }
    }
}
