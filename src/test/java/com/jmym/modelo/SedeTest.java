package com.jmym.modelo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas del modelo Sede (validación e identidad por código).
 * Responsable: módulo Sedes (Sebastián Lasso).
 */
@DisplayName("Sede - validación e identidad")
class SedeTest {

    @Test
    @DisplayName("Crea una sede válida; toString muestra el nombre")
    void sedeValida() {
        Sede s = new Sede("J.M.Y.M.", "Calle 1 # 2-3", "6020000");
        assertEquals("J.M.Y.M.", s.getNombre());
        assertEquals("J.M.Y.M.", s.toString());
        assertEquals("Activa", s.getEstado());
    }

    @Test
    @DisplayName("Rechaza nombre o dirección vacíos y teléfono no numérico")
    void sedeInvalida() {
        assertThrows(IllegalArgumentException.class, () -> new Sede("", "Calle 1", "6020000"));
        assertThrows(IllegalArgumentException.class, () -> new Sede("Lobo", "  ", "6020000"));
        assertThrows(IllegalArgumentException.class, () -> new Sede("Lobo", "Calle 1", "602-00"));
    }

    @Test
    @DisplayName("Dos sedes con el mismo código son iguales (equals/hashCode)")
    void identidadPorCodigo() {
        Sede a = new Sede("The Police", "Cra 5", "6021111");
        Sede b = new Sede("Lobo", "Cra 9", "6022222");
        a.setCodSede(7);
        b.setCodSede(7);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        b.setCodSede(8);
        assertNotEquals(a, b);
    }
}
