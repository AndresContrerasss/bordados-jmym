package com.jmym.modelo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas del modelo Pago (abono de una orden).
 * Responsable: módulo Pagos / Producción (Samir Correa Álvarez).
 */
@DisplayName("Pago - datos de un abono")
class PagoTest {

    @Test
    @DisplayName("Guarda y devuelve los datos del abono")
    void datosDelPago() {
        Timestamp fecha = Timestamp.valueOf("2026-10-25 12:00:00");
        Pago p = new Pago(55, 10000, "Efectivo", fecha);
        assertEquals(55, p.getCodOrden());
        assertEquals(10000.0, p.getValor());
        assertEquals("Efectivo", p.getMedio());
        assertEquals(fecha, p.getFecha());
    }

    @Test
    @DisplayName("El código arranca en 0 (aún no guardado) y luego se asigna")
    void codigoPago() {
        Pago p = new Pago(1, 5000, "Transferencia", Timestamp.valueOf("2026-10-25 12:00:00"));
        assertEquals(0, p.getCodPago());
        p.setCodPago(99);
        assertEquals(99, p.getCodPago());
    }
}
