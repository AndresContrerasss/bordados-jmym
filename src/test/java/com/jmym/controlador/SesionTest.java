package com.jmym.controlador;

import com.jmym.modelo.*;
import com.jmym.util.NegocioException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Clase probada: {@link Sesion}. Es la que decide qué rol puede usar cada función de los controladores. */
@DisplayName("Sesion - usuario activo y control de roles")
class SesionTest {

    @AfterEach
    void limpiar() { Sesion.cerrar(); }

    @Test
    @DisplayName("iniciar() guarda el usuario y cerrar() lo borra")
    void iniciarYCerrar() {
        Vendedor v = UsuariosDePrueba.vendedor();
        Sesion.iniciar(v);
        assertSame(v, Sesion.getUsuario());
        Sesion.cerrar();
        assertNull(Sesion.getUsuario());
    }

    @Test
    @DisplayName("como(): devuelve el usuario si tiene el rol pedido")
    void comoRolCorrecto() {
        Vendedor v = UsuariosDePrueba.iniciarComoVendedor();
        assertSame(v, Sesion.como(Vendedor.class));
    }

    @Test
    @DisplayName("como(): lanza 'No tiene permisos' si el rol es otro")
    void comoRolIncorrecto() {
        UsuariosDePrueba.iniciarComoVendedor();
        NegocioException e = assertThrows(NegocioException.class, () -> Sesion.como(Gerente.class));
        assertEquals("No tiene permisos para esta función.", e.getMessage());
    }

    @Test
    @DisplayName("como(): sin sesión iniciada también lanza 'No tiene permisos'")
    void comoSinSesion() {
        assertThrows(NegocioException.class, () -> Sesion.como(Vendedor.class));
    }

    @Test
    @DisplayName("como(Usuario.class) acepta cualquier rol (polimorfismo)")
    void comoUsuarioGenerico() {
        UsuariosDePrueba.iniciarComoDisenador();
        assertDoesNotThrow(() -> Sesion.como(Usuario.class));
    }
}
