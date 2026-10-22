package blackjack_grupo22.modelo;

import java.util.Random;

/**
 * Representa el mazo de 52 cartas sin comodines.
 * Se guarda en un arreglo de 52 posiciones.
 * Para repartir no se borran cartas: se avanza una posición en el arreglo,
 * de modo que una misma carta nunca se reparte dos veces.
 */
public class Mazo {

    // Cantidad total de cartas de la baraja francesa sin comodines
    public static final int TOTAL_CARTAS = 52;

    // Arreglo de 52 posiciones donde viven las cartas
    private Carta[] cartas;

    // Posición de la próxima carta que se va a repartir (0 = la primera)
    private int posicionSiguiente;

    // Generador de números aleatorios para barajar
    private Random generador;

    /**
     * Crea un mazo completo (52 cartas ordenadas) y lo baraja.
     */
    public Mazo() {
        this.generador = new Random();
        this.cartas = new Carta[TOTAL_CARTAS];
        construirMazoOrdenado();
        barajar();
    }

    /**
     * Constructor que recibe una "semilla" para el generador aleatorio.
     * Con la misma semilla la mezcla siempre es igual, lo que sirve
     * para hacer pruebas repetibles. En el juego normal NO se usa.
     */
    public Mazo(long semilla) {
        this.generador = new Random(semilla);
        this.cartas = new Carta[TOTAL_CARTAS];
        construirMazoOrdenado();
        barajar();
    }

    /**
     * Llena el arreglo con las 52 combinaciones de palo y valor.
     * Recorre los 4 palos y, dentro de cada uno, los 13 valores.
     */
    private void construirMazoOrdenado() {
        Palo[] palos = Palo.values();
        Valor[] valores = Valor.values();

        int indice = 0;
        for (int p = 0; p < palos.length; p++) {
            for (int v = 0; v < valores.length; v++) {
                cartas[indice] = new Carta(valores[v], palos[p]);
                indice++;
            }
        }
    }

    /**
     * Baraja el mazo con el algoritmo Fisher-Yates y reinicia el reparto.
     *
     * Funcionamiento: se recorre el arreglo desde la última posición hasta
     * la 1. En cada paso se elige al azar una posición entre 0 y la actual
     * (inclusive) y se intercambian las dos cartas. Lo que queda a la
     * derecha ya está "fijo" y no se vuelve a tocar.
     */
    public void barajar() {
        for (int i = TOTAL_CARTAS - 1; i > 0; i--) {
            // nextInt(i + 1) devuelve un número entre 0 e i, ambos incluidos
            int j = generador.nextInt(i + 1);

            // Intercambio de las cartas en las posiciones i y j
            Carta auxiliar = cartas[i];
            cartas[i] = cartas[j];
            cartas[j] = auxiliar;
        }
        // Después de barajar, el reparto vuelve a empezar desde la primera carta
        posicionSiguiente = 0;
    }

    /**
     * Reparte la siguiente carta del mazo y avanza la posición.
     * Lanza una excepción si ya no quedan cartas (en Blackjack con dos
     * participantes esto casi nunca ocurre, pero se protege igual).
     */
    public Carta repartir() {
        if (posicionSiguiente >= TOTAL_CARTAS) {
            throw new IllegalStateException("El mazo no tiene más cartas para repartir.");
        }
        Carta carta = cartas[posicionSiguiente];
        posicionSiguiente++;
        return carta;
    }

    /**
     * Cantidad de cartas que todavía no se han repartido.
     */
    public int cartasRestantes() {
        return TOTAL_CARTAS - posicionSiguiente;
    }

    /**
     * Indica si aún quedan cartas por repartir.
     */
    public boolean hayCartas() {
        if (posicionSiguiente < TOTAL_CARTAS) {
            return true;
        }
        return false;
    }
}
