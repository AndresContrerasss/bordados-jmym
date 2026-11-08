package com.jmym.modelo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas del modelo Orden: total, saldo, avance de estado y regla de modificación.
 * Responsable: módulo Órdenes / Clientes (Andrés Julián Contreras Tombe).
 */
@DisplayName("Orden - total, saldo y estados")
class OrdenTest {

    private Orden ordenConBordados() {
        Orden o = new Orden(new Cliente("1090000001", "Ana Pérez", "3001234567"));
        o.getBordados().add(new Bordado("Parche", "Azul", 2, 5000, ""));   // 10.000
        o.getBordados().add(new Bordado("Presilla", "Rojo", 1, 3000, "")); // 3.000
        return o;
    }

    @Test
    @DisplayName("calcularTotal(): suma los subtotales de todos los bordados")
    void calcularTotal() {
        assertEquals(13000.0, ordenConBordados().calcularTotal());
    }

    @Test
    @DisplayName("getSaldo(): total menos lo pagado")
    void saldo() {
        Orden o = ordenConBordados();
        o.setTotal(13000);
        o.setTotalPagado(5000);
        assertEquals(8000.0, o.getSaldo());
        o.setTotalPagado(13000);
        assertEquals(0.0, o.getSaldo());
    }

    @Test
    @DisplayName("puedeModificarse(): solo mientras está Pendiente")
    void puedeModificarse() {
        Orden o = ordenConBordados();
        assertTrue(o.puedeModificarse());              // arranca en Pendiente
        o.setEstado("En Producción");
        assertFalse(o.puedeModificarse());
    }

    @Test
    @DisplayName("siguienteEstado(): avanza sin saltos y es null en el último")
    void siguienteEstado() {
        Orden o = ordenConBordados();
        assertEquals("En Producción", o.siguienteEstado());
        o.setEstado("Listo para Entrega");
        assertEquals("Entregada", o.siguienteEstado());
        o.setEstado("Entregada");
        assertNull(o.siguienteEstado());
    }

    @Test
    @DisplayName("getFechaCierre(): usa la entrega; si no hay, la fecha de finalización")
    void fechaCierre() {
        Orden o = ordenConBordados();
        assertNull(o.getFechaCierre());
        Timestamp finalizada = Timestamp.valueOf("2026-10-20 10:00:00");
        o.setFechaFinalizada(finalizada);
        assertEquals(finalizada, o.getFechaCierre());
        Timestamp entrega = Timestamp.valueOf("2026-10-22 15:00:00");
        o.setFechaEntrega(entrega);
        assertEquals(entrega, o.getFechaCierre(), "La entrega tiene prioridad");
    }
}
