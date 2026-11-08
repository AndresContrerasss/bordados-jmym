package com.jmym.controlador;

import com.jmym.modelo.Sede;
import com.jmym.modelo.Usuario;
import com.jmym.util.NegocioException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Clase probada: {@link UsuarioControlador} (RF-001). Permisos y validaciones previas a la base de datos. */
@DisplayName("UsuarioControlador - gestión de usuarios (solo Gerente)")
class UsuarioControladorTest {

    private final UsuarioControlador usuarios = new UsuarioControlador();
    private final Sede sede = sedeDePrueba();

    private static Sede sedeDePrueba() {
        Sede s = new Sede("J.M.Y.M.", "Calle 1 # 2-3", "3001234567");
        s.setCodSede(1);
        return s;
    }

    @AfterEach
    void limpiar() { Sesion.cerrar(); }

    private String mensaje(org.junit.jupiter.api.function.Executable accion) {
        return assertThrows(NegocioException.class, accion).getMessage();
    }

    @Test
    @DisplayName("listar() y guardar() rechazan a quien no es Gerente")
    void soloGerente() {
        UsuariosDePrueba.iniciarComoVendedor();
        assertThrows(NegocioException.class, usuarios::listar);
        assertEquals("No tiene permisos para esta función.", mensaje(
                () -> usuarios.guardar(0, "123", "Luis", "luis@jmym.com", "Clave123", "Vendedor", sede)));
    }

    @Test
    @DisplayName("guardar(): exige todos los campos obligatorios")
    void camposObligatorios() {
        UsuariosDePrueba.iniciarComoGerente();
        assertEquals("Complete todos los campos obligatorios.", mensaje(
                () -> usuarios.guardar(0, "", "Luis", "luis@jmym.com", "Clave123", "Vendedor", sede)));
        assertThrows(NegocioException.class, () -> usuarios.guardar(0, "123", "Luis", "luis@jmym.com", "Clave123", null, sede));
        assertThrows(NegocioException.class, () -> usuarios.guardar(0, "123", "Luis", "luis@jmym.com", "Clave123", "Vendedor", null));
    }

    @Test
    @DisplayName("guardar(): un usuario nuevo necesita contraseña")
    void nuevoSinClave() {
        UsuariosDePrueba.iniciarComoGerente();
        assertEquals("La contraseña es obligatoria para un usuario nuevo.", mensaje(
                () -> usuarios.guardar(0, "123", "Luis", "luis@jmym.com", "", "Vendedor", sede)));
    }

    @Test
    @DisplayName("guardar(): la contraseña debe tener al menos 6 caracteres")
    void claveCorta() {
        UsuariosDePrueba.iniciarComoGerente();
        assertEquals("La contraseña debe tener al menos 6 caracteres.", mensaje(
                () -> usuarios.guardar(0, "123", "Luis", "luis@jmym.com", "12345", "Vendedor", sede)));
    }

    @Test
    @DisplayName("guardar(): un rol que no existe es rechazado por la fábrica")
    void rolDesconocido() {
        UsuariosDePrueba.iniciarComoGerente();
        assertThrows(IllegalArgumentException.class,
                () -> usuarios.guardar(0, "123", "Luis", "luis@jmym.com", "Clave123", "Contador", sede));
    }

    @Test
    @DisplayName("cambiarEstado(): el Gerente no puede cambiar el estado de su propia cuenta")
    void noCambiaSuPropiaCuenta() {
        Usuario yo = UsuariosDePrueba.iniciarComoGerente();
        assertEquals("No puede cambiar el estado de su propia cuenta.", mensaje(() -> usuarios.cambiarEstado(yo, "Bloqueado")));
    }

    @Test
    @DisplayName("cambiarEstado(): solo el Gerente")
    void cambiarEstadoSoloGerente() {
        UsuariosDePrueba.iniciarComoVendedor();
        assertThrows(NegocioException.class, () -> usuarios.cambiarEstado(UsuariosDePrueba.disenador(), "Inactivo"));
    }
}
