package com.jmym.controlador;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de INTEGRACIÓN de pagos. Requieren base de datos Neon y una sesión iniciada,
 * por eso están deshabilitadas (@Disabled): no corren en la CI ni en 'mvn test' normal.
 * Sirven como guía de lo que falta por validar en el módulo de Pagos y Producción.
 * Responsable: Samir Correa Álvarez.
 */
@DisplayName("PagoControlador - integración (requiere BD)")
class PagoControladorIT {

    @Test
    @Disabled("Requiere base de datos Neon y sesión de Vendedor iniciada")
    @DisplayName("registrar(): un abono menor al saldo baja el saldo pendiente")
    void registrarAbonoParcial() {
        // TODO (con BD de prueba): iniciar sesión como Vendedor, crear una orden,
        //   registrar un abono y verificar que orden.getSaldo() disminuye.
    }

    @Test
    @Disabled("Requiere base de datos Neon y sesión de Vendedor iniciada")
    @DisplayName("registrar(): rechaza un pago mayor al saldo")
    void rechazaPagoMayorAlSaldo() {
        // TODO: verificar que registrar() lanza NegocioException cuando el valor supera el saldo.
    }

    @Test
    @Disabled("Requiere base de datos Neon y sesión de Vendedor iniciada")
    @DisplayName("una orden con saldo pendiente no se puede entregar")
    void noEntregarConSaldo() {
        // TODO: verificar contra la BD la regla de entrega (factura cerrada al pagar el total).
    }
}
