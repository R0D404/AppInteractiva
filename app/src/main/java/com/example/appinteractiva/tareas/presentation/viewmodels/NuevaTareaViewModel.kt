package com.example.appinteractiva.tareas.presentation.viewmodels

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appinteractiva.R
import com.example.appinteractiva.tareas.data.model.Prioridad
import com.example.appinteractiva.tareas.data.repository.TareasRepository
import com.example.appinteractiva.tareas.presentation.screens.FormularioUiState
import com.example.appinteractiva.tareas.util.FechaUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel encargado del estado y las validaciones del formulario de creación de tareas.
 *
 * @param repository Repositorio de tareas inyectado mediante su interfaz.
 */
class NuevaTareaViewModel(
    private val repository: TareasRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FormularioUiState())
    val uiState: StateFlow<FormularioUiState> = _uiState.asStateFlow()

    /**
     * Actualiza el título y limpia su mensaje de error si existía.
     */
    fun onTituloChange(nuevoTitulo: String) {
        _uiState.update { it.copy(titulo = nuevoTitulo, errorTitulo = null) }
    }

    /**
     * Actualiza la materia y limpia su mensaje de error si existía.
     */
    fun onMateriaChange(nuevaMateria: String) {
        _uiState.update { it.copy(materia = nuevaMateria, errorMateria = null) }
    }

    /**
     * Actualiza la prioridad seleccionada para la nueva tarea.
     */
    fun onPrioridadChange(nuevaPrioridad: Prioridad) {
        _uiState.update { it.copy(prioridad = nuevaPrioridad) }
    }

    /**
     * Actualiza la fecha límite escrita y limpia su mensaje de error si existía.
     */
    fun onFechaChange(nuevaFecha: String) {
        _uiState.update { it.copy(fechaLimite = nuevaFecha, errorFecha = null) }
    }

    /**
     * Valida los datos introducidos en el formulario.
     * Si hay errores, los emite conservando el texto ingresado (RN-15).
     * Si los datos son válidos, guarda la tarea en el repositorio con textos recortados.
     */
    fun onGuardarClick() {
        val estadoActual = _uiState.value
        val tituloTrim = estadoActual.titulo.trim()
        val materiaTrim = estadoActual.materia.trim()
        val fechaTrim = estadoActual.fechaLimite.trim()

        val errorTitulo = validarTitulo(tituloTrim)
        val errorMateria = validarMateria(materiaTrim)
        val errorFecha = validarFecha(fechaTrim)

        val hayErrores = errorTitulo != null || errorMateria != null || errorFecha != null

        if (hayErrores) {
            _uiState.update {
                it.copy(
                    errorTitulo = errorTitulo,
                    errorMateria = errorMateria,
                    errorFecha = errorFecha
                )
            }
        } else {
            viewModelScope.launch {
                repository.agregarTarea(
                    titulo = tituloTrim,
                    materia = materiaTrim,
                    prioridad = estadoActual.prioridad,
                    fechaLimite = fechaTrim
                )
                _uiState.update { it.copy(guardadoExitoso = true) }
            }
        }
    }

    /**
     * Reinicia el formulario al valor inicial una vez que la UI navegó de vuelta tras guardar.
     */
    fun onGuardadoConsumido() {
        _uiState.value = FormularioUiState()
    }

    /**
     * Reinicia el formulario cuando el usuario decide cancelar o presionar regresar.
     */
    fun onFormularioDescartado() {
        _uiState.value = FormularioUiState()
    }

    /**
     * Valida las reglas de negocio para el título (RN-01):
     * Obligatorio y máximo 60 caracteres.
     */
    @StringRes
    private fun validarTitulo(titulo: String): Int? {
        return when {
            titulo.isBlank() -> R.string.error_titulo_obligatorio
            titulo.length > 60 -> R.string.error_titulo_demasiado_largo
            else -> null
        }
    }

    /**
     * Valida las reglas de negocio para la materia (RN-02):
     * Obligatoria y máximo 40 caracteres.
     */
    @StringRes
    private fun validarMateria(materia: String): Int? {
        return when {
            materia.isBlank() -> R.string.error_materia_obligatoria
            materia.length > 40 -> R.string.error_materia_demasiado_larga
            else -> null
        }
    }

    /**
     * Valida las reglas de negocio para la fecha límite (RN-04):
     * Debe cumplir formato dd/MM/yyyy, ser una fecha real del calendario y no ser anterior a hoy.
     */
    @StringRes
    private fun validarFecha(fechaTexto: String): Int? {
        val fecha = FechaUtil.parsearFecha(fechaTexto)
            ?: return R.string.error_fecha_invalida

        return if (FechaUtil.esAnteriorAHoy(fecha)) {
            R.string.error_fecha_anterior_a_hoy
        } else {
            null
        }
    }
}
