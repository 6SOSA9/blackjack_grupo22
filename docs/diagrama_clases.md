# Diagrama de clases (UML)

```mermaid
classDiagram
    direction LR

    class Palo {
        <<enumeration>>
        CORAZONES
        DIAMANTES
        TREBOLES
        PICAS
        +getNombre() String
    }

    class Valor {
        <<enumeration>>
        AS
        DOS ... DIEZ
        J
        Q
        K
        +getPuntos() int
        +getCodigo() String
        +esFigura() boolean
        +valeDiez() boolean
    }

    class Carta {
        -valor Valor
        -palo Palo
        +getPuntos() int
        +esAs() boolean
        +getNombreImagen() String
        +equals(Object) boolean
    }

    class Nodo {
        -carta Carta
        -siguiente Nodo
        +getCarta() Carta
        +getSiguiente() Nodo
        +setSiguiente(Nodo)
    }

    class Mano {
        -primero Nodo
        -ultimo Nodo
        -cantidad int
        +agregarCarta(Carta)
        +obtenerCarta(int) Carta
        +calcularPuntaje() int
        +estaPasada() boolean
        +esBlackjack() boolean
        +esVeintiuno() boolean
        +esVeinteYMedio() boolean
        +determinarJugada() Jugada
        +vaciar()
    }

    class Mazo {
        -cartas Carta[52]
        -posicionSiguiente int
        +barajar()
        +repartir() Carta
        +cartasRestantes() int
    }

    class Jugada {
        <<enumeration>>
        BLACKJACK
        VEINTIUNO
        VEINTE_Y_MEDIO
        PASADO
        NORMAL
    }

    class Resultado {
        <<enumeration>>
        VICTORIA
        VICTORIA_BLACKJACK
        EMPATE
        DERROTA
    }

    class EstadoRonda {
        <<enumeration>>
        SIN_RONDA
        TURNO_JUGADOR
        TURNO_COMPUTADOR
        FINALIZADA
    }

    class Juego {
        -saldo int
        -apuesta int
        -estado EstadoRonda
        -resultado Resultado
        -mazo Mazo
        -manoJugador Mano
        -manoComputador Mano
        +validarApuesta(String) String
        +iniciarRonda(String) String
        +pedirCarta() String
        +plantarse() String
        +jugarTurnoComputador() String
        +determinarResultado(Mano, Mano) Resultado
        +rondaActiva() boolean
    }

    class VistaJuego {
        +mostrarSaldo(int)
        +mostrarManoJugador(Mano, String)
        +mostrarManoComputador(Mano, String)
        +mostrarMensaje(String)
        +actualizarControles(boolean, boolean)
    }

    class ControladorJuego {
        -juego Juego
        -vista VistaJuego
        +iniciar()
        -alIniciarRonda()
        -alPedirCarta()
        -alPlantarse()
        -alVerReglas()
        -alCerrarJuego()
        -actualizarVista()
    }

    class Main {
        +start(Stage)
    }

    Carta --> Valor
    Carta --> Palo
    Nodo --> Carta
    Nodo --> Nodo : siguiente
    Mano o-- Nodo : primero y ultimo
    Mazo *-- Carta : 52
    Mano ..> Jugada
    Juego *-- Mazo
    Juego *-- Mano : jugador y computador
    Juego --> EstadoRonda
    Juego --> Resultado
    ControladorJuego --> Juego
    ControladorJuego --> VistaJuego
    VistaJuego ..> Mano : solo lee
    Main ..> Juego
    Main ..> VistaJuego
    Main ..> ControladorJuego
```

## Paquetes (MVC)

| Capa | Clases |
|---|---|
| Modelo | Palo, Valor, Carta, Nodo, Mano, Mazo, Jugada, Resultado, EstadoRonda, Juego |
| Vista | VistaJuego |
| Controlador | ControladorJuego |