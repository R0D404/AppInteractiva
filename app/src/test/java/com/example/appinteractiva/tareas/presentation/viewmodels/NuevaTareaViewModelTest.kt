package com.example.appinteractiva.tareas.presentation.viewmodels

import com.example.appinteractiva.R
import com.example.appinteractiva.tareas.data.model.Prioridad
import com.example.appinteractiva.tareas.data.model.Tarea
import com.example.appinteractiva.tareas.data.repository.TareasRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NuevaTareaViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeRepository: FakeTareasRepository
    private lateinit var viewModel: NuevaTareaViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeTareasRepository()
        viewModel = NuevaTareaViewModel(fakeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun guardarTarea_conCamposVacios_muestraErroresDeValidacion() = runTest {
        viewModel.onGuardarClick()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(R.string.error_titulo_obligatorio, state.errorTitulo)
        assertEquals(R.string.error_materia_obligatoria, state.errorMateria)
        assertEquals(R.string.error_fecha_invalida, state.errorFecha)
    }

    @Test
    fun guardarTarea_conCamposValidos_guardaEnRepositorio() = runTest {
        viewModel.onTituloChange("Estudiar Kotlin")
        viewModel.onMateriaChange("Móviles")
        viewModel.onPrioridadChange(Prioridad.ALTA)
        viewModel.onFechaChange("20/11/2026")

        viewModel.onGuardarClick()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.guardadoExitoso)
        assertNull(state.errorTitulo)
        assertNull(state.errorMateria)
        assertNull(state.errorFecha)

        val tareas = fakeRepository.tareas.first()
        assertEquals(1, tareas.size)
        assertEquals("Estudiar Kotlin", tareas[0].titulo)
    }

    private class FakeTareasRepository : TareasRepository {
        private val _tareas = MutableStateFlow<List<Tarea>>(emptyList())
        override val tareas: Flow<List<Tarea>> = _tareas.asStateFlow()

        override suspend fun agregarTarea(
            titulo: String,
            materia: String,
            prioridad: Prioridad,
            fechaLimite: String
        ) {
            _tareas.update { lista ->
                val nuevoId = (lista.maxOfOrNull { it.id } ?: 0) + 1
                lista + Tarea(nuevoId, titulo, materia, prioridad, fechaLimite, completada = false)
            }
        }

        override suspend fun alternarCompletada(id: Int) {
            _tareas.update { lista ->
                lista.map { if (it.id == id) it.copy(completada = !it.completada) else it }
            }
        }

        override suspend fun eliminarTarea(id: Int) {
            _tareas.update { lista -> lista.filter { it.id != id } }
        }
    }
}
