package com.example.appinteractiva.tareas.util

import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class FechaUtilTest {

    @Test
    fun parsearFecha_fechaValida_devuelveDate() {
        val date = FechaUtil.parsearFecha("15/10/2026")
        assertNotNull(date)
    }

    @Test
    fun parsearFecha_fechaInvalida_devuelveNull() {
        assertNull(FechaUtil.parsearFecha("31/02/2026"))
        assertNull(FechaUtil.parsearFecha("2026-10-15"))
        assertNull(FechaUtil.parsearFecha("texto"))
    }
}
