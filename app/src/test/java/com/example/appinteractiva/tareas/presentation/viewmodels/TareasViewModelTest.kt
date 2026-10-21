package com.example.appinteractiva.tareas.presentation.viewmodels

import com.example.appinteractiva.tareas.data.model.FiltroEstado
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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TareasViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeRepository: FakeTareasRepository
    private lateinit var viewModel: TareasViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeTareasRepository()
        viewModel = TareasViewModel(fakeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun emiteTareasYCalculaProgresoCorrectamente() = runTest {
        fakeRepository.agregarTarea("Tarea 1", "Materia A", Prioridad.ALTA, "15/10/2026")
        fakeRepository.agregarTarea("Tarea 2", "Materia B", Prioridad.MEDIA, "16/10/2026")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(2, state.totalTareas)
        assertEquals(0, state.tareasCompletadas)
        assertEquals(0f, state.porcentajeProgreso, 0.001f)
        assertFalse(state.estaCargando)
    }

    @Test
    fun alternarCompletada_actualizaMétricasYEstado() = runTest {
        fakeRepository.agregarTarea("Tarea 1", "Materia A", Prioridad.ALTA, "15/10/2026")
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onToggleCompletada(1)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, state.tareasCompletadas)
        assertEquals(1f, state.porcentajeProgreso, 0.001f)
    }

    @Test
    fun filtrarPorEstado_muestraSoloPendientes() = runTest {
        fakeRepository.agregarTarea("Tarea 1", "Materia A", Prioridad.ALTA, "15/10/2026")
        fakeRepository.agregarTarea("Tarea 2", "Materia B", Prioridad.MEDIA, "16/10/2026")
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onToggleCompletada(1)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onFiltroEstadoChange(FiltroEstado.PENDIENTES)
        val state = viewModel.uiState.value

        assertEquals(1, state.tareas.size)
        assertEquals("Tarea 2", state.tareas[0].titulo)
        // Progreso global debe seguir siendo 50%
        assertEquals(2, state.totalTareas)
        assertEquals(1, state.tareasCompletadas)
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
