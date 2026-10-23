package blackjack_grupo22.modelo;

/**
 * Posibles resultados de una ronda, vistos desde el jugador.
 */
public enum Resultado {

    VICTORIA("Victoria"),
    VICTORIA_BLACKJACK("Victoria con Blackjack"),
    EMPATE("Empate"),
    DERROTA("Derrota");

    private final String nombre;

    Resultado(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}