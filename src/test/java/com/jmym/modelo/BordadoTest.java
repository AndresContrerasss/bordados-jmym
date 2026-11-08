package com.jmym.modelo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas del modelo Bordado (validaciones, subtotal y firma).
 * Responsable: módulo Órdenes / Clientes (Andrés Julián Contreras Tombe).
 */
@DisplayName("Bordado - validación y cálculos")
class BordadoTest {

    @Test
    @DisplayName("Crea un bordado válido y calcula el subtotal (cantidad x precio)")
    void subtotal() {
        Bordado b = new Bordado("Parche", "Azul", 3, 5000, "sin notas");
        assertEquals(15000.0, b.getSubtotal());
        assertEquals("Parche", b.getTipo());
        assertEquals("Azul", b.getColor());
    }

    @Test
    @DisplayName("Las notas nulas se guardan como cadena vacía")
    void notasNulas() {
        Bordado b = new Bordado("Presilla", "Rojo", 1, 1000, null);
        assertEquals("", b.getNotas());
    }

    @Test
    @DisplayName("firma(): identifica el bordado en minúsculas (tipo|color|cantidad|notas)")
    void firma() {
        Bordado b = new Bordado("Parche", "Azul", 2, 5000, "Logo");
        assertEquals("parche|azul|2|logo", b.firma());
    }

    @Test
    @DisplayName("Rechaza datos inválidos (tipo/color vacíos, cantidad o precio <= 0)")
    void datosInvalidos() {
        assertThrows(IllegalArgumentException.class, () -> new Bordado("", "Azul", 1, 1000, ""));
        assertThrows(IllegalArgumentException.class, () -> new Bordado("Parche", "", 1, 1000, ""));
        assertThrows(IllegalArgumentException.class, () -> new Bordado("Parche", "Azul", 0, 1000, ""));
        assertThrows(IllegalArgumentException.class, () -> new Bordado("Parche", "Azul", 1, 0, ""));
    }

    @Test
    @DisplayName("Rechaza textos que pasan del máximo permitido")
    void textosLargos() {
        assertThrows(IllegalArgumentException.class, () -> new Bordado("x".repeat(51), "Azul", 1, 1000, ""));
        assertThrows(IllegalArgumentException.class, () -> new Bordado("Parche", "c".repeat(31), 1, 1000, ""));
        assertThrows(IllegalArgumentException.class, () -> new Bordado("Parche", "Azul", 1, 1000, "n".repeat(251)));
    }
}
