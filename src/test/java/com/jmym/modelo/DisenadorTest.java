package com.jmym.modelo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Clase probada: {@link Disenador} (hija de Usuario). No ve precios y solo trabaja en producción. */
@DisplayName("Disenador - módulos y permisos")
class DisenadorTest {

    private final Disenador d = new Disenador("1", "Diego", "disenador@jmym.com");

    @Test
    @DisplayName("Solo accede a Producción e Historial")
    void accesos() {
        assertTrue(d.puedeAcceder("Producción"));
        assertTrue(d.puedeAcceder("Historial"));
        assertFalse(d.puedeAcceder("Pagos"));
        assertFalse(d.puedeAcceder("Reportes"));
        assertFalse(d.puedeAcceder("Usuarios"));
    }

    @Test
    @DisplayName("Su descripción incluye el rol con tilde")
    void descripcion() {
        assertEquals("Diego (Diseñador)", d.descripcion());
    }
}
