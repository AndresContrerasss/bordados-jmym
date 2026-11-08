package com.jmym.modelo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase probada: {@link Persona} (abstracta), a través de Cliente y Vendedor que la heredan.
 * Cubre ENCAPSULACIÓN (los set validan) y POLIMORFISMO (toString usa descripcion()).
 */
@DisplayName("Persona - identificación, nombre y descripción")
class PersonaTest {

    @Test
    @DisplayName("Persona es abstracta: no se puede instanciar directamente")
    void esAbstracta() {
        assertTrue(java.lang.reflect.Modifier.isAbstract(Persona.class.getModifiers()));
    }

    @Test
    @DisplayName("La identificación acepta hasta 12 dígitos")
    void identificacionValida() {
        Cliente c = new Cliente("1090000001", "Ana", "3001234567");
        assertEquals("1090000001", c.getIdentificacion());
        assertDoesNotThrow(() -> c.setIdentificacion("123456789012"));      // 12 dígitos: límite
    }

    @Test
    @DisplayName("La identificación rechaza letras, vacío y más de 12 dígitos")
    void identificacionInvalida() {
        Cliente c = new Cliente("1", "Ana", "3001234567");
        assertThrows(IllegalArgumentException.class, () -> c.setIdentificacion("12ab"));
        assertThrows(IllegalArgumentException.class, () -> c.setIdentificacion(" 123 "));   // los espacios no son dígitos
        assertThrows(IllegalArgumentException.class, () -> c.setIdentificacion(""));
        assertThrows(IllegalArgumentException.class, () -> c.setIdentificacion("1234567890123")); // 13
    }

    @Test
    @DisplayName("El nombre es obligatorio, se recorta y admite máximo 100 caracteres")
    void nombre() {
        Cliente c = new Cliente("1", "  Ana Pérez  ", "3001234567");
        assertEquals("Ana Pérez", c.getNombre());
        assertThrows(IllegalArgumentException.class, () -> c.setNombre("   "));
        assertThrows(IllegalArgumentException.class, () -> c.setNombre(null));
        assertThrows(IllegalArgumentException.class, () -> c.setNombre("a".repeat(101)));
        assertDoesNotThrow(() -> c.setNombre("a".repeat(100)));
    }

    @Test
    @DisplayName("toString() devuelve la descripción propia de cada clase hija (polimorfismo)")
    void toStringPolimorfico() {
        Persona cliente = new Cliente("1090000001", "Ana", "3001234567");
        Persona vendedor = new Vendedor("1", "Luis", "luis@jmym.com");
        assertEquals("Ana (C.C. 1090000001)", cliente.toString());
        assertEquals("Luis (Vendedor)", vendedor.toString());
    }
}
