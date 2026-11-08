package com.jmym.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de las listas fijas del negocio (arreglos) y su búsqueda de posición.
 * Responsable: módulo Reportes (Sebastián Lasso).
 */
@DisplayName("Constantes - listas del negocio")
class ConstantesTest {

    @Test
    @DisplayName("posicion(): devuelve el índice del valor, o -1 si no existe")
    void posicion() {
        assertEquals(0, Constantes.posicion(Constantes.ESTADOS_ORDEN, "Pendiente"));
        assertEquals(3, Constantes.posicion(Constantes.ESTADOS_ORDEN, "Entregada"));
        assertEquals(-1, Constantes.posicion(Constantes.ESTADOS_ORDEN, "NoExiste"));
    }

    @Test
    @DisplayName("Las listas tienen los valores esperados del negocio")
    void listas() {
        assertArrayEquals(new String[]{"Gerente", "Vendedor", "Diseñador"}, Constantes.ROLES);
        assertEquals(4, Constantes.ESTADOS_ORDEN.length);
        assertEquals("Pendiente", Constantes.ESTADOS_ORDEN[0]);
        assertEquals("Entregada", Constantes.ESTADOS_ORDEN[Constantes.ESTADOS_ORDEN.length - 1]);
        assertArrayEquals(new String[]{"Efectivo", "Transferencia"}, Constantes.MEDIOS_PAGO);
    }

    @Test
    @DisplayName("Los límites de seguridad están en sus valores de negocio")
    void limites() {
        assertEquals(3, Constantes.MAX_INTENTOS);
        assertEquals(10, Constantes.MINUTOS_INACTIVIDAD);
    }
}
