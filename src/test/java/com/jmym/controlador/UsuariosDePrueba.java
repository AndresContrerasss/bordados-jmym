package com.jmym.controlador;

import com.jmym.modelo.*;

/** Ayuda para las pruebas de controladores: crea usuarios con código e inicia sesión sin tocar la base de datos. */
final class UsuariosDePrueba {
    private UsuariosDePrueba() { }

    static Gerente gerente() { Gerente g = new Gerente("1000000001", "Gina Gerente", "gerente@jmym.com"); g.setCodUsuario(1); return g; }
    static Vendedor vendedor() { Vendedor v = new Vendedor("1000000002", "Víctor Vendedor", "vendedor@jmym.com"); v.setCodUsuario(2); v.setCodSede(1); return v; }
    static Disenador disenador() { Disenador d = new Disenador("1000000003", "Diego Diseñador", "disenador@jmym.com"); d.setCodUsuario(3); return d; }

    static Gerente iniciarComoGerente() { Gerente g = gerente(); Sesion.iniciar(g); return g; }
    static Vendedor iniciarComoVendedor() { Vendedor v = vendedor(); Sesion.iniciar(v); return v; }
    static Disenador iniciarComoDisenador() { Disenador d = disenador(); Sesion.iniciar(d); return d; }

    /** Orden con total y pagado ya definidos, lista para probar pagos. */
    static Orden ordenConSaldo(double total, double pagado) {
        Orden o = new Orden(new Cliente("1090000001", "Ana Pérez", "3001234567"));
        o.setCodOrden(7);
        o.setNombreSede("J.M.Y.M.");
        o.setTotal(total);
        o.setTotalPagado(pagado);
        return o;
    }
}
