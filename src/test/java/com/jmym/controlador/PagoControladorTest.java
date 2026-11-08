package com.jmym.controlador;

import com.jmym.modelo.Orden;
import com.jmym.modelo.Pago;
import com.jmym.util.Formato;
import com.jmym.util.NegocioException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase probada: {@link PagoControlador} (RF-006).
 * Cubre las reglas de registrar() que se validan antes de guardar en la base de datos
 * y el texto del comprobante. Los casos que insertan el pago están en PagoControladorIT.
 */
@DisplayName("PagoControlador - reglas del abono y comprobante")
class PagoControladorTest {

    private final PagoControlador pagos = new PagoControlador();
    private final String hoy = Formato.hoy();

    @AfterEach
    void limpiar() { Sesion.cerrar(); }

    private String mensaje(Orden o, String valor, String fecha, String medio) {
        return assertThrows(NegocioException.class, () -> pagos.registrar(o, valor, fecha, medio)).getMessage();
    }

    @Test
    @DisplayName("Solo el Vendedor puede registrar pagos")
    void soloVendedor() {
        UsuariosDePrueba.iniciarComoGerente();
        assertEquals("No tiene permisos para esta función.",
                mensaje(UsuariosDePrueba.ordenConSaldo(50000, 0), "10000", hoy, "Efectivo"));
    }

    @Test
    @DisplayName("El valor debe ser un número válido")
    void valorInvalido() {
        UsuariosDePrueba.iniciarComoVendedor();
        assertTrue(mensaje(UsuariosDePrueba.ordenConSaldo(50000, 0), "abc", hoy, "Efectivo").contains("Valor"));
    }

    @Test
    @DisplayName("El valor debe ser mayor a cero")
    void valorCero() {
        UsuariosDePrueba.iniciarComoVendedor();
        assertEquals("El valor del pago debe ser mayor a cero.",
                mensaje(UsuariosDePrueba.ordenConSaldo(50000, 0), "0", hoy, "Efectivo"));
    }

    @Test
    @DisplayName("El pago no puede superar el saldo pendiente")
    void superaElSaldo() {
        UsuariosDePrueba.iniciarComoVendedor();
        Orden o = UsuariosDePrueba.ordenConSaldo(50000, 30000);   // saldo 20.000
        assertEquals("El valor del pago supera el saldo pendiente.", mensaje(o, "20.001", hoy, "Efectivo"));
    }

    @Test
    @DisplayName("Hay que elegir el medio de pago")
    void sinMedio() {
        UsuariosDePrueba.iniciarComoVendedor();
        assertEquals("Seleccione el medio de pago.",
                mensaje(UsuariosDePrueba.ordenConSaldo(50000, 0), "10000", hoy, null));
    }

    @Test
    @DisplayName("La fecha es obligatoria y debe tener formato dd/mm/aaaa")
    void fechaObligatoriaYFormato() {
        UsuariosDePrueba.iniciarComoVendedor();
        Orden o = UsuariosDePrueba.ordenConSaldo(50000, 0);
        assertEquals("Ingrese la fecha del pago.", mensaje(o, "10000", "", "Efectivo"));
        assertTrue(mensaje(o, "10000", "2026-10-25", "Efectivo").contains("dd/mm/aaaa"));
    }

    @Test
    @DisplayName("La fecha del pago no puede ser futura")
    void fechaFutura() {
        UsuariosDePrueba.iniciarComoVendedor();
        String manana = Formato.aFecha(hoy).plusDays(1).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        assertEquals("La fecha del pago no puede ser futura.",
                mensaje(UsuariosDePrueba.ordenConSaldo(50000, 0), "10000", manana, "Efectivo"));
    }

    @Test
    @DisplayName("comprobante(): muestra abono, total y saldo pendiente")
    void comprobanteConSaldo() {
        Orden o = UsuariosDePrueba.ordenConSaldo(50000, 20000);   // ya incluye el abono: saldo 30.000
        Pago p = new Pago(7, 20000, "Transferencia", Timestamp.valueOf(LocalDate.of(2026, 10, 25).atTime(12, 0)));
        String texto = pagos.comprobante(o, p);
        assertTrue(texto.contains("COMPROBANTE DE PAGO"));
        assertTrue(texto.contains("Sede J.M.Y.M."));
        assertTrue(texto.contains("Ana Pérez"));
        assertTrue(texto.contains("Transferencia"));
        assertTrue(texto.contains("25/10/2026"));
        assertTrue(texto.contains(Formato.dinero(30000)));
        assertFalse(texto.contains("FACTURA CERRADA"));
    }

    @Test
    @DisplayName("comprobante(): con saldo cero indica que la factura está cerrada")
    void comprobanteFacturaCerrada() {
        Orden o = UsuariosDePrueba.ordenConSaldo(50000, 50000);
        Pago p = new Pago(7, 50000, "Efectivo", Timestamp.valueOf("2026-10-25 12:00:00"));
        assertTrue(pagos.comprobante(o, p).contains("FACTURA CERRADA"));
    }
}
