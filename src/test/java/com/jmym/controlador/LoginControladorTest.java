package com.jmym.controlador;

import com.jmym.util.NegocioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase probada: {@link LoginControlador} (RF-001).
 * Solo se prueban las validaciones que ocurren ANTES de consultar la base de datos.
 * El inicio de sesión completo (bloqueo a los 3 intentos) va en una prueba de integración.
 */
@DisplayName("LoginControlador - validaciones previas a la base de datos")
class LoginControladorTest {

    private final LoginControlador login = new LoginControlador();

    @Test
    @DisplayName("Correo vacío: pide correo y contraseña")
    void correoVacio() {
        NegocioException e = assertThrows(NegocioException.class, () -> login.iniciarSesion("  ", "Jmym2026"));
        assertEquals("Ingrese su correo y su contraseña.", e.getMessage());
    }

    @Test
    @DisplayName("Contraseña vacía: pide correo y contraseña")
    void claveVacia() {
        assertThrows(NegocioException.class, () -> login.iniciarSesion("vendedor@jmym.com", ""));
    }

    @Test
    @DisplayName("Ambos nulos: no revienta con NullPointerException")
    void nulos() {
        assertThrows(NegocioException.class, () -> login.iniciarSesion(null, null));
    }
}
