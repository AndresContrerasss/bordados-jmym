package com.jmym.controlador;

import com.jmym.util.NegocioException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Clase probada: {@link ReporteControlador} (RF-007). Permisos y validación de fechas antes de la base de datos. */
@DisplayName("ReporteControlador - reportes (solo Gerente)")
class ReporteControladorTest {

    private final ReporteControlador reportes = new ReporteControlador();

    @AfterEach
    void limpiar() { Sesion.cerrar(); }

    @Test
    @DisplayName("generar() e indicadores() rechazan a quien no es Gerente")
    void soloGerente() {
        UsuariosDePrueba.iniciarComoVendedor();
        assertEquals("No tiene permisos para esta función.", assertThrows(NegocioException.class,
                () -> reportes.generar("Ventas por sede", null, "", "")).getMessage());
        assertThrows(NegocioException.class, reportes::indicadores);
    }

    @Test
    @DisplayName("generar(): 'hasta' anterior a 'desde' es un rango inválido")
    void rangoInvalido() {
        UsuariosDePrueba.iniciarComoGerente();
        assertEquals("Rango de fechas inválido.", assertThrows(NegocioException.class,
                () -> reportes.generar("Ventas por sede", null, "20/10/2026", "01/10/2026")).getMessage());
    }

    @Test
    @DisplayName("generar(): una fecha mal escrita se rechaza con el formato esperado")
    void fechaMalEscrita() {
        UsuariosDePrueba.iniciarComoGerente();
        assertTrue(assertThrows(NegocioException.class,
                () -> reportes.generar("Bordados realizados", null, "2026-10-01", "")).getMessage().contains("dd/mm/aaaa"));
    }
}
