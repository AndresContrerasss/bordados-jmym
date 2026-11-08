package com.jmym.modelo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Cliente y, de paso, de la clase abstracta Persona (encapsulación).
 * Responsable: módulo Órdenes / Clientes (Andrés Julián Contreras Tombe).
 */
@DisplayName("Cliente / Persona - validación de datos")
class ClienteTest {

    @Test
    @DisplayName("Crea un cliente válido y arma su descripción")
    void clienteValido() {
        Cliente c = new Cliente("1090000001", "Ana Pérez", "3001234567");
        assertEquals("1090000001", c.getCedula());
        assertEquals("1090000001", c.getIdentificacion());   // cédula == identificación
        assertEquals("Ana Pérez (C.C. 1090000001)", c.descripcion());
    }

    @Test
    @DisplayName("La cédula debe ser solo números (máximo 12)")
    void cedulaInvalida() {
        assertThrows(IllegalArgumentException.class, () -> new Cliente("12a", "Ana", "3001234567"));
        assertThrows(IllegalArgumentException.class, () -> new Cliente("", "Ana", "3001234567"));
        assertThrows(IllegalArgumentException.class, () -> new Cliente("1234567890123", "Ana", "3001234567")); // 13 dígitos
    }

    @Test
    @DisplayName("El nombre es obligatorio y el teléfono solo números (máximo 10)")
    void nombreYTelefono() {
        assertThrows(IllegalArgumentException.class, () -> new Cliente("1090000001", "  ", "3001234567"));
        assertThrows(IllegalArgumentException.class, () -> new Cliente("1090000001", "Ana", "300-123"));
        assertThrows(IllegalArgumentException.class, () -> new Cliente("1090000001", "Ana", "30012345678")); // 11 dígitos
    }
}
