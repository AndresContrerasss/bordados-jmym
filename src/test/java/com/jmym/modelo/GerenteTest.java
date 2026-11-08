package com.jmym.modelo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Clase probada: {@link Gerente} (hija de Usuario). Es el único rol con acceso a Reportes y Usuarios. */
@DisplayName("Gerente - módulos y permisos")
class GerenteTest {

    private final Gerente gerente = new Gerente("1", "Gina", "gerente@jmym.com");

    @Test
    @DisplayName("Ve los 5 módulos de administración")
    void modulos() {
        assertArrayEquals(new String[]{"Inicio", "Sedes", "Usuarios", "Órdenes", "Reportes"}, gerente.getModulos());
    }

    @Test
    @DisplayName("No tiene Pagos, Producción ni Historial (son de Vendedor y Diseñador)")
    void modulosQueNoTiene() {
        assertFalse(gerente.puedeAcceder("Pagos"));
        assertFalse(gerente.puedeAcceder("Producción"));
        assertFalse(gerente.puedeAcceder("Historial"));
    }

    @Test
    @DisplayName("Su descripción incluye el rol")
    void descripcion() {
        assertEquals("Gina (Gerente)", gerente.descripcion());
    }

    @Test
    @DisplayName("Un usuario nuevo nace Activo y la clave se asigna aparte")
    void estadoInicial() {
        assertEquals("Activo", gerente.getEstado());
        assertNull(gerente.getContrasenaHash());
        gerente.setContrasenaHash("sal:hash");
        assertEquals("sal:hash", gerente.getContrasenaHash());
    }
}
