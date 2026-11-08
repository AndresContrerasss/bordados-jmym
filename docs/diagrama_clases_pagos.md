# Diagramas de referencia

## Diagrama de clases (flujo del módulo Pagos, de la vista a la base de datos)
Muestra cómo se encadenan las capas en un módulo completo: `PanelPagos` (Vista) → `PagoControlador` → `PagoDAO` → `Pago`/`Orden` (Modelo).

```mermaid
classDiagram
    direction TB
    class Pago {
        -int codPago
        -int codOrden
        -double valor
        -String medio
        -Timestamp fecha
        +getValor() double
        +getMedio() String
    }
    class Orden {
        -int codOrden
        -double total
        -double totalPagado
        +getSaldo() double
        +calcularTotal() double
    }
    class BaseDAO {
        <<abstract>>
        #consultar(sql, mapeo, params)
        #ejecutar(sql, params)
        #transaccion(accion)
    }
    class PagoDAO {
        +listarPorOrden(int) ArrayList~Pago~
        +insertar(Pago, int codUsuario)
    }
    class PagoControlador {
        +buscarOrden(String) Orden
        +listarPagos(int) ArrayList~Pago~
        +registrar(Orden, String, String, String) Pago
        +comprobante(Orden, Pago) String
    }
    class PanelPagos {
        -cargar(String)
        -guardarPago()
        -verComprobante()
    }
    Orden "1" --> "0..*" Pago : recibe
    BaseDAO <|-- PagoDAO
    PagoDAO ..> Pago : crea y guarda
    PagoControlador ..> PagoDAO : usa
    PagoControlador ..> Orden : valida el saldo
    PanelPagos ..> PagoControlador : llama
```
