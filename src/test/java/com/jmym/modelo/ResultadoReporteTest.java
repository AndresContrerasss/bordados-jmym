package com.jmym.modelo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/** Clase probada: {@link ResultadoReporte} (record). Lo arma ReporteControlador para la vista de Reportes. */
@DisplayName("ResultadoReporte - columnas, filas y resumen de dinero")
class ResultadoReporteTest {

    @Test
    @DisplayName("saldo() = facturado - cobrado")
    void saldo() {
        ResultadoReporte r = new ResultadoReporte(new String[]{"Sede"}, new ArrayList<>(), 100000, 60000);
        assertEquals(40000.0, r.saldo());
    }

    @Test
    @DisplayName("Si todo está cobrado el saldo es cero")
    void saldoCero() {
        assertEquals(0.0, new ResultadoReporte(new String[0], new ArrayList<>(), 5000, 5000).saldo());
    }

    @Test
    @DisplayName("Guarda las columnas y las filas tal como se le entregan")
    void datos() {
        ArrayList<Object[]> filas = new ArrayList<>();
        filas.add(new Object[]{"Parche", 10, 4});
        ResultadoReporte r = new ResultadoReporte(new String[]{"Tipo", "Unidades", "Órdenes"}, filas, 0, 0);
        assertEquals(3, r.columnas().length);
        assertEquals(1, r.filas().size());
        assertEquals("Parche", r.filas().get(0)[0]);
    }
}
