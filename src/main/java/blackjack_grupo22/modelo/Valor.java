package blackjack_grupo22.modelo;

/**
 * Representa las 13 denominaciones de una carta.
 * Cada denominación guarda cuántos puntos vale.
 *
 * El As vale 11 aquí como valor base. Cuando una mano tenga varios ases,
 * la clase Mano decidirá si alguno debe contarse como 1 (para no pasarse de 21).
 */
public enum Valor {

    AS("As", 11, "AS"),
    DOS("2", 2, "2"),
    TRES("3", 3, "3"),
    CUATRO("4", 4, "4"),
    CINCO("5", 5, "5"),
    SEIS("6", 6, "6"),
    SIETE("7", 7, "7"),
    OCHO("8", 8, "8"),
    NUEVE("9", 9, "9"),
    DIEZ("10", 10, "10"),
    J("J", 10, "J"),
    Q("Q", 10, "Q"),
    K("K", 10, "K");

    private final String nombre;   // texto para mostrar: "As", "7", "K"...
    private final int puntos;      // puntos que aporta a la mano
    private final String codigo;   // texto corto para nombrar el archivo de imagen

    Valor(String nombre, int puntos, String codigo) {
        this.nombre = nombre;
        this.puntos = puntos;
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPuntos() {
        return puntos;
    }

    public String getCodigo() {
        return codigo;
    }

    /**
     * Indica si esta denominación es una figura (J, Q o K).
     * Lo necesitaremos para reconocer el "Veinte y medio" (figura + 4).
     */
    public boolean esFigura() {
        if (this == J || this == Q || this == K) {
            return true;
        }
        return false;
    }

    /**
     * Indica si esta denominación vale 10 puntos (10, J, Q o K).
     * Lo necesitaremos para reconocer el Blackjack (As + una carta de 10).
     */
    public boolean valeDiez() {
        if (puntos == 10) {
            return true;
        }
        return false;
    }
}
