package com.jmym.controlador;

import com.jmym.modelo.*;
import com.jmym.util.NegocioException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase probada: {@link OrdenControlador} (RF-003, RF-004 y RF-005).
 * Solo reglas que se resuelven antes de llegar a la base de datos: permisos por rol,
 * validaciones y secuencia de estados.
 */
@DisplayName("OrdenControlador - permisos por rol y reglas previas a la BD")
class OrdenControladorTest {

    private static final String SIN_PERMISOS = "No tiene permisos para esta función.";
    private final OrdenControlador ordenes = new OrdenControlador();

    @AfterEach
    void limpiar() { Sesion.cerrar(); }

    private static ArrayList<Bordado> unBordado() {
        ArrayList<Bordado> l = new ArrayList<>();
        l.add(new Bordado("Parche", "Azul", 2, 5000, ""));
        return l;
    }

    private static Orden ordenEn(String estado) {
        Orden o = new Orden(new Cliente("1090000001", "Ana", "3001234567"));
        o.setCodOrden(9);
        o.setEstado(estado);
        return o;
    }

    // ----------------------------------------------------------- Clientes
    @Test
    @DisplayName("buscarCliente(): rechaza cédulas con letras o de más de 12 dígitos")
    void buscarClienteInvalido() {
        assertThrows(NegocioException.class, () -> ordenes.buscarCliente("12ab"));
        assertThrows(NegocioException.class, () -> ordenes.buscarCliente("1234567890123"));
        assertThrows(NegocioException.class, () -> ordenes.buscarCliente(null));
    }

    @Test
    @DisplayName("corregirCliente(): solo el Gerente")
    void corregirClienteSoloGerente() {
        UsuariosDePrueba.iniciarComoVendedor();
        Cliente c = new Cliente("1090000001", "Ana", "3001234567");
        assertEquals(SIN_PERMISOS, assertThrows(NegocioException.class,
                () -> ordenes.corregirCliente(c, "1090000001", "Ana", "3001234567")).getMessage());
    }

    // ----------------------------------------------------------- Crear / modificar
    @Test
    @DisplayName("crearOrden(): solo el Vendedor")
    void crearSoloVendedor() {
        UsuariosDePrueba.iniciarComoGerente();
        assertEquals(SIN_PERMISOS, assertThrows(NegocioException.class,
                () -> ordenes.crearOrden(null, "1090000001", "Ana", "3001234567", unBordado())).getMessage());
    }

    @Test
    @DisplayName("crearOrden(): exige al menos un bordado")
    void crearSinBordados() {
        UsuariosDePrueba.iniciarComoVendedor();
        assertEquals("La orden debe tener al menos un bordado.", assertThrows(NegocioException.class,
                () -> ordenes.crearOrden(null, "1090000001", "Ana", "3001234567", new ArrayList<>())).getMessage());
    }

    @Test
    @DisplayName("modificarOrden(): exige al menos un bordado")
    void modificarSinBordados() {
        UsuariosDePrueba.iniciarComoVendedor();
        assertThrows(NegocioException.class, () -> ordenes.modificarOrden(9, "3001234567", new ArrayList<>()));
    }

    @Test
    @DisplayName("modificarOrden(): solo el Vendedor")
    void modificarSoloVendedor() {
        UsuariosDePrueba.iniciarComoDisenador();
        assertEquals(SIN_PERMISOS, assertThrows(NegocioException.class,
                () -> ordenes.modificarOrden(9, "3001234567", unBordado())).getMessage());
    }

    // ----------------------------------------------------------- Consultas
    @Test
    @DisplayName("listarDelVendedor(): solo el Vendedor")
    void listarDelVendedorSoloVendedor() {
        UsuariosDePrueba.iniciarComoGerente();
        assertThrows(NegocioException.class, () -> ordenes.listarDelVendedor("", null));
    }

    @Test
    @DisplayName("listarPanel(): el Vendedor no tiene acceso al panel de las tres sedes")
    void panelNoParaVendedor() {
        UsuariosDePrueba.iniciarComoVendedor();
        assertEquals(SIN_PERMISOS, assertThrows(NegocioException.class,
                () -> ordenes.listarPanel("", null, null)).getMessage());
    }

    @Test
    @DisplayName("listarHistorial(): el Gerente no tiene historial propio")
    void historialNoParaGerente() {
        UsuariosDePrueba.iniciarComoGerente();
        assertEquals(SIN_PERMISOS, assertThrows(NegocioException.class,
                () -> ordenes.listarHistorial("", null, null, null, null)).getMessage());
    }

    @Test
    @DisplayName("listarHistorial(): rechaza un rango donde 'hasta' es anterior a 'desde'")
    void historialRangoInvalido() {
        UsuariosDePrueba.iniciarComoVendedor();
        assertEquals("Rango de fechas inválido.", assertThrows(NegocioException.class,
                () -> ordenes.listarHistorial("", null, null, LocalDate.of(2026, 10, 20), LocalDate.of(2026, 10, 1))).getMessage());
    }

    @Test
    @DisplayName("historialDeLaOrden(): solo el Gerente (auditoría)")
    void auditoriaSoloGerente() {
        UsuariosDePrueba.iniciarComoVendedor();
        assertThrows(NegocioException.class, () -> ordenes.historialDeLaOrden(9));
    }

    // ----------------------------------------------------------- Estados
    @Test
    @DisplayName("avanzarEstado(): solo el Diseñador")
    void avanzarSoloDisenador() {
        UsuariosDePrueba.iniciarComoVendedor();
        assertEquals(SIN_PERMISOS, assertThrows(NegocioException.class,
                () -> ordenes.avanzarEstado(ordenEn("Pendiente"))).getMessage());
    }

    @Test
    @DisplayName("avanzarEstado(): el Diseñador no puede marcar una orden como Entregada")
    void disenadorNoEntrega() {
        UsuariosDePrueba.iniciarComoDisenador();
        // "Listo para Entrega" -> el siguiente sería "Entregada", que solo hace el Vendedor
        assertEquals("No es posible cambiar el estado sin cumplir la secuencia establecida.",
                assertThrows(NegocioException.class, () -> ordenes.avanzarEstado(ordenEn("Listo para Entrega"))).getMessage());
    }

    @Test
    @DisplayName("avanzarEstado(): una orden Entregada ya no avanza")
    void entregadaNoAvanza() {
        UsuariosDePrueba.iniciarComoDisenador();
        assertThrows(NegocioException.class, () -> ordenes.avanzarEstado(ordenEn("Entregada")));
    }

    @Test
    @DisplayName("entregar(): solo el Vendedor")
    void entregarSoloVendedor() {
        UsuariosDePrueba.iniciarComoDisenador();
        assertThrows(NegocioException.class, () -> ordenes.entregar(ordenEn("Listo para Entrega")));
    }

    @Test
    @DisplayName("eliminar(): solo el Gerente")
    void eliminarSoloGerente() {
        UsuariosDePrueba.iniciarComoVendedor();
        assertEquals(SIN_PERMISOS, assertThrows(NegocioException.class,
                () -> ordenes.eliminar(ordenEn("Pendiente"))).getMessage());
    }
}
