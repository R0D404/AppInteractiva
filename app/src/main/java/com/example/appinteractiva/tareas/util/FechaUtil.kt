package com.example.appinteractiva.tareas.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Utilidad pura para parsear y validar fechas sin usar java.time,
 * manteniendo compatibilidad con minSdk 24 sin desugaring.
 */
object FechaUtil {

    private val regexFormatoFecha = Regex("^\\d{2}/\\d{2}/\\d{4}$")

    /**
     * Parsea una cadena de texto a [Date] si cumple exactamente con el formato dd/MM/yyyy
     * y representa una fecha válida en el calendario.
     *
     * @param texto Cadena con la fecha a verificar.
     * @return Objeto [Date] si es válida, o null si el formato o la fecha no existen (ej. 31/02/2026).
     */
    fun parsearFecha(texto: String): Date? {
        if (!regexFormatoFecha.matches(texto)) {
            return null
        }

        val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
            isLenient = false
        }

        return try {
            formato.parse(texto)
        } catch (e: ParseException) {
            null
        }
    }

    /**
     * Determina si la fecha dada es estrictamente anterior al inicio del día de hoy (00:00:00).
     * Una fecha correspondiente al día de hoy NO se considera anterior.
     *
     * @param fecha Fecha a comparar.
     * @return true si la fecha ya pasó (es anterior a hoy a las 00:00), false en caso contrario.
     */
    fun esAnteriorAHoy(fecha: Date): Boolean {
        val hoyMedianoche = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        return fecha.before(hoyMedianoche.time)
    }
}
