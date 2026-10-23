package blackjack_grupo22.modelo;

/**
 * Clase central del modelo. Administra el saldo, la apuesta, el mazo,
 * las manos y el estado de la ronda. No sabe nada de JavaFX: la Vista
 * y el Controlador le piden operaciones y le consultan datos.
 */
public class Juego {

    public static final int SALDO_INICIAL = 1000;
    public static final int APUESTA_MINIMA = 10;
    public static final int MULTIPLO_APUESTA = 10;

    private int saldo;                    // fichas disponibles del jugador
    private int apuesta;                  // apuesta de la ronda actual (0 si no hay)
    private EstadoRonda estado;           // en qué momento está el juego
    private Mazo mazo;                    // mazo compartido por ambos participantes
    private final Mano manoJugador;
    private final Mano manoComputador;

    /**
     * Crea un juego nuevo: saldo de 1.000, sin apuesta y sin ronda.
     * Las dos manos existen pero están vacías.
     */
    public Juego() {
        this.saldo = SALDO_INICIAL;
        this.apuesta = 0;
        this.estado = EstadoRonda.SIN_RONDA;
        this.mazo = new Mazo();
        this.manoJugador = new Mano();
        this.manoComputador = new Mano();
    }

    // ------------------------------------------------------------
    // Validación de la apuesta (HU02)
    // ------------------------------------------------------------

    /**
     * Revisa el texto escrito por el usuario.
     * Devuelve null si la apuesta es válida; si no, devuelve un mensaje
     * que explica cómo corregirla. Este método NO modifica el saldo
     * ni reparte cartas: solo valida.
     */
    public String validarApuesta(String texto) {
        // Sin fichas suficientes no se puede apostar nada
        if (saldo < APUESTA_MINIMA) {
            return "No tienes fichas suficientes para otra ronda.";
        }

        if (texto == null) {
            texto = "";
        }
        String limpio = texto.trim();   // quita espacios al inicio y al final

        if (limpio.isEmpty()) {
            return "Escribe una apuesta para comenzar.";
        }

        if (tieneFormatoDecimal(limpio)) {
            return "La apuesta debe ser un número entero, sin decimales.";
        }

        // Intentamos convertir el texto en un número entero
        int valor;
        try {
            valor = Integer.parseInt(limpio);
        } catch (NumberFormatException e) {
            return "La apuesta debe ser un número, sin letras ni símbolos.";
        }

        if (valor <= 0) {
            return "La apuesta debe ser mayor que cero.";
        }
        if (valor < APUESTA_MINIMA) {
            return "La apuesta mínima es de " + APUESTA_MINIMA + " fichas.";
        }
        if (valor % MULTIPLO_APUESTA != 0) {
            return "La apuesta debe ser múltiplo de " + MULTIPLO_APUESTA + ".";
        }
        if (valor > saldo) {
            return "No tienes saldo suficiente. Tu saldo es de " + saldo + " fichas.";
        }

        return null;   // todo en orden: la apuesta es válida
    }

    /**
     * Indica si el texto parece un número decimal, como "10,5" o "10.5":
     * solo dígitos, un signo "-" opcional al inicio y exactamente
     * un separador decimal (punto o coma).
     */
    private boolean tieneFormatoDecimal(String texto) {
        int separadores = 0;
        int digitos = 0;

        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (c >= '0' && c <= '9') {
                digitos++;
            } else if (c == '.' || c == ',') {
                separadores++;
            } else if (c == '-' && i == 0) {
                // el signo negativo al inicio es aceptable en este análisis
            } else {
                return false;   // hay otro símbolo: no es un decimal
            }
        }
        return separadores == 1 && digitos > 0;
    }

    // ------------------------------------------------------------
    // Inicio de ronda (HU03)
    // ------------------------------------------------------------

    /**
     * Inicia una ronda con un mazo nuevo y mezclado (juego normal).
     * Devuelve null si todo salió bien, o el mensaje de error.
     */
    public String iniciarRonda(String textoApuesta) {
        return iniciarRondaConMazo(textoApuesta, new Mazo());
    }

    /**
     * Inicia una ronda usando el mazo que se reciba. El juego normal usa
     * iniciarRonda(); este método existe para que las pruebas puedan
     * entregar un mazo con cartas conocidas.
     *
     * Orden del reparto: jugador, jugador, computador, computador.
     * En este momento solo se reparten las dos del jugador; las del
     * computador se reparten cuando comience su turno.
     */
    public String iniciarRondaConMazo(String textoApuesta, Mazo mazoDeLaRonda) {
        // No se puede iniciar otra ronda mientras la actual esté activa
        if (rondaActiva()) {
            return "Ya hay una ronda en juego.";
        }

        // Se valida la apuesta; si falla, no se toca nada más
        String error = validarApuesta(textoApuesta);
        if (error != null) {
            return error;
        }

        int valor = Integer.parseInt(textoApuesta.trim());

        // La apuesta se descuenta UNA sola vez y queda fija en la ronda
        saldo = saldo - valor;
        apuesta = valor;

        // Mazo completo y manos vacías para la nueva ronda
        mazo = mazoDeLaRonda;
        manoJugador.vaciar();
        manoComputador.vaciar();

        // El jugador recibe sus dos cartas
        manoJugador.agregarCarta(mazo.repartir());
        manoJugador.agregarCarta(mazo.repartir());

        // Con 21 (o Blackjack) el turno del jugador termina automáticamente
        if (manoJugador.calcularPuntaje() == 21) {
            estado = EstadoRonda.TURNO_COMPUTADOR;
        } else {
            estado = EstadoRonda.TURNO_JUGADOR;
        }

        return null;
    }

    // ------------------------------------------------------------
    // Consultas (las usarán el Controlador y la Vista)
    // ------------------------------------------------------------

    /**
     * Una ronda está activa mientras sea turno del jugador o del computador.
     */
    public boolean rondaActiva() {
        if (estado == EstadoRonda.TURNO_JUGADOR || estado == EstadoRonda.TURNO_COMPUTADOR) {
            return true;
        }
        return false;
    }

    /**
     * Indica si el saldo alcanza para apostar al menos la apuesta mínima.
     */
    public boolean hayFichasSuficientes() {
        if (saldo >= APUESTA_MINIMA) {
            return true;
        }
        return false;
    }

    public int getSaldo() {
        return saldo;
    }

    public int getApuesta() {
        return apuesta;
    }

    public EstadoRonda getEstado() {
        return estado;
    }

    public Mano getManoJugador() {
        return manoJugador;
    }

    public Mano getManoComputador() {
        return manoComputador;
    }

    public jugada getJugadaJugador() {
        return manoJugador.determinarJugada();
    }

    public int cartasRestantesEnMazo() {
        return mazo.cartasRestantes();
    }
}