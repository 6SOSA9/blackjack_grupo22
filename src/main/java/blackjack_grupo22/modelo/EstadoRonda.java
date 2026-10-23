package blackjack_grupo22.modelo;

/**
 * Estados por los que pasa el juego. Con ellos el modelo sabe qué
 * acciones están permitidas en cada momento.
 */
public enum EstadoRonda {

    SIN_RONDA,          // todavía no se ha jugado ninguna ronda
    TURNO_JUGADOR,      // el jugador puede pedir carta o plantarse
    TURNO_COMPUTADOR,   // el jugador ya terminó; juega el computador
    FINALIZADA          // la ronda terminó y ya hay resultado
}
