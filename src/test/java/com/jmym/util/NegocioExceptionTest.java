package com.jmym.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase probada: {@link NegocioException} (util).
 * Capa 1 del proyecto: es la base de todos los mensajes de error que ve el usuario.
 */
@DisplayName("NegocioException - errores que se muestran al usuario")
class NegocioExceptionTest {

    @Test
    @DisplayName("Conserva el mensaje que se le pasa")
    void conservaElMensaje() {
        assertEquals("Credenciales inválidas.", new NegocioException("Credenciales inválidas.").getMessage());
    }

    @Test
    @DisplayName("Es una excepción 'unchecked' (RuntimeException): no exige throws")
    void esUnchecked() {
        assertInstanceOf(RuntimeException.class, new NegocioException("x"));
    }
}
