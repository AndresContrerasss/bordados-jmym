package com.jmym.controlador;

import com.jmym.modelo.Sede;
import com.jmym.util.NegocioException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Clase probada: {@link SedeControlador} (RF-002). Permisos y validación de datos antes de la base de datos. */
@DisplayName("SedeControlador - gestión de sedes (solo Gerente)")
class SedeControladorTest {

    private final SedeControlador sedes = new SedeControlador();

    @AfterEach
    void limpiar() { Sesion.cerrar(); }

    @Test
    @DisplayName("guardar(): rechaza a quien no es Gerente")
    void guardarSoloGerente() {
        UsuariosDePrueba.iniciarComoVendedor();
        assertEquals("No tiene permisos para esta función.", assertThrows(NegocioException.class,
                () -> sedes.guardar(0, "Nueva", "Calle 5", "3001234567")).getMessage());
    }

    @Test
    @DisplayName("guardar(): valida nombre, dirección y teléfono de la sede")
    void guardarValidaDatos() {
        UsuariosDePrueba.iniciarComoGerente();
        assertThrows(IllegalArgumentException.class, () -> sedes.guardar(0, "", "Calle 5", "3001234567"));
        assertThrows(IllegalArgumentException.class, () -> sedes.guardar(0, "Nueva", "", "3001234567"));
        assertThrows(IllegalArgumentException.class, () -> sedes.guardar(0, "Nueva", "Calle 5", "abc"));
    }

    @Test
    @DisplayName("cambiarEstado(): rechaza a quien no es Gerente")
    void cambiarEstadoSoloGerente() {
        UsuariosDePrueba.iniciarComoDisenador();
        Sede s = new Sede("Lobo", "Calle 9", "3001234567");
        assertThrows(NegocioException.class, () -> sedes.cambiarEstado(s, "Inactiva"));
    }
}
