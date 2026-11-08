package com.jmym.modelo;

import com.jmym.util.Constantes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Clase probada: {@link UsuarioFactory}. Conecta Constantes.ROLES con las clases hijas de Usuario. */
@DisplayName("UsuarioFactory - un usuario por cada rol del negocio")
class UsuarioFactoryTest {

    @Test
    @DisplayName("Todos los roles de Constantes.ROLES se pueden crear y devuelven ese mismo rol")
    void cadaRolDelNegocioTieneClase() {
        for (String rol : Constantes.ROLES) {
            Usuario u = UsuarioFactory.crear(rol, "123", "Nombre", "n@jmym.com");
            assertEquals(rol, u.getRol(), "El rol creado debe coincidir con el pedido");
        }
    }

    @Test
    @DisplayName("Pasa identificación, nombre y correo al usuario creado")
    void pasaLosDatos() {
        Usuario u = UsuarioFactory.crear("Vendedor", "1090000001", "Luis", "LUIS@jmym.com");
        assertEquals("1090000001", u.getIdentificacion());
        assertEquals("Luis", u.getNombre());
        assertEquals("luis@jmym.com", u.getCorreo());
    }

    @Test
    @DisplayName("El mensaje de rol desconocido dice cuál fue")
    void rolDesconocido() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> UsuarioFactory.crear("Contador", "1", "X", "x@jmym.com"));
        assertTrue(e.getMessage().contains("Contador"));
    }

    @Test
    @DisplayName("Los datos inválidos siguen siendo rechazados por la clase hija")
    void datosInvalidos() {
        assertThrows(IllegalArgumentException.class, () -> UsuarioFactory.crear("Gerente", "abc", "X", "x@jmym.com"));
        assertThrows(IllegalArgumentException.class, () -> UsuarioFactory.crear("Gerente", "1", "X", "malcorreo"));
    }
}
