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
    private Resultado resultado;           // resultado de la última ronda (null si aún no termina)
    private String mensajeFinal;           // explicación de por qué se ganó, perdió o empató
    private int pagoUltimaRonda;           // fichas acreditadas al terminar la ronda
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
        this.resultado = null;
        this.mensajeFinal = "";
        this.pagoUltimaRonda = 0;
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
        // Se borra el resultado de la ronda anterior
        resultado = null;
        mensajeFinal = "";
        pagoUltimaRonda = 0;

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
    // Acciones del jugador (HU04 y HU05)
    // ------------------------------------------------------------

    /**
     * El jugador pide una carta. Solo se permite en TURNO_JUGADOR.
     * Devuelve null si la acción se hizo, o un mensaje si no estaba permitida.
     *
     * Después de repartir la carta:
     *  - si el jugador se pasó, pierde de inmediato (el computador no juega);
     *  - si llegó a 21, su turno termina y pasa al computador;
     *  - si no, sigue siendo su turno.
     */
    public String pedirCarta() {
        if (estado != EstadoRonda.TURNO_JUGADOR) {
            return "No es tu turno: no puedes pedir carta.";
        }

        manoJugador.agregarCarta(mazo.repartir());
        int puntaje = manoJugador.calcularPuntaje();

        if (puntaje > 21) {
            finalizarRonda(Resultado.DERROTA);
        } else if (puntaje == 21) {
            estado = EstadoRonda.TURNO_COMPUTADOR;
        }
        return null;
    }

    /**
     * El jugador se planta con su mano actual. Solo se permite en TURNO_JUGADOR.
     * Este método solo cambia el turno; el computador juega cuando se
     * llama a jugarTurnoComputador().
     */
    public String plantarse() {
        if (estado != EstadoRonda.TURNO_JUGADOR) {
            return "No es tu turno: no puedes plantarte.";
        }
        estado = EstadoRonda.TURNO_COMPUTADOR;
        return null;
    }

    // ------------------------------------------------------------
    // Turno del computador (HU06)
    // ------------------------------------------------------------

    /**
     * Ejecuta el turno completo del computador como una secuencia corta:
     *   1. Recibe sus dos primeras cartas.
     *   2. Si alguno de los dos tiene Blackjack, no pide más cartas
     *      (la ronda se resuelve con las dos cartas iniciales).
     *   3. Si no, pide mientras su puntaje sea menor que 17 y se planta
     *      con 17 o más. Un As contado como 11 también cuenta (As + 6 = 17).
     *   4. Se determina el resultado y se liquida la apuesta.
     *
     * Sus decisiones dependen solo de su propia mano, no de la del jugador.
     * Solo funciona en TURNO_COMPUTADOR, así que no puede ejecutarse dos veces.
     */
    public String jugarTurnoComputador() {
        if (estado != EstadoRonda.TURNO_COMPUTADOR) {
            return "No es el turno del computador.";
        }

        // 1. Dos cartas iniciales
        manoComputador.agregarCarta(mazo.repartir());
        manoComputador.agregarCarta(mazo.repartir());

        // 2. Si hay algún Blackjack, no se piden más cartas
        boolean hayBlackjack = manoJugador.esBlackjack() || manoComputador.esBlackjack();

        // 3. Regla de la banca: pedir mientras tenga menos de 17
        if (!hayBlackjack) {
            while (manoComputador.calcularPuntaje() < 17) {
                manoComputador.agregarCarta(mazo.repartir());
            }
        }

        // 4. Resultado y liquidación
        Resultado r = determinarResultado(manoJugador, manoComputador);
        finalizarRonda(r);
        return null;
    }

    // ------------------------------------------------------------
    // Resultado (HU08)
    // ------------------------------------------------------------

    /**
     * Compara las dos manos y devuelve el resultado desde el punto de vista
     * del jugador. No modifica nada: solo calcula. Por eso es público y se
     * puede probar con manos armadas a mano.
     *
     * Orden de las reglas:
     *   1. Quien se pasa pierde (primero se revisa al jugador).
     *   2. Blackjack: si ambos lo tienen, empate; si solo uno, gana ese.
     *   3. Veinte y medio: gana a cualquier mano de 20 o menos, pierde
     *      ante cualquier 21, y empata con otro Veinte y medio.
     *   4. Si no hay jugadas especiales, gana el mayor puntaje.
     */
    public Resultado determinarResultado(Mano jugador, Mano computador) {
        // 1. Pasarse
        if (jugador.estaPasada()) {
            return Resultado.DERROTA;
        }
        if (computador.estaPasada()) {
            return Resultado.VICTORIA;
        }

        // 2. Blackjack
        boolean jugadorBlackjack = jugador.esBlackjack();
        boolean computadorBlackjack = computador.esBlackjack();
        if (jugadorBlackjack && computadorBlackjack) {
            return Resultado.EMPATE;
        }
        if (jugadorBlackjack) {
            return Resultado.VICTORIA_BLACKJACK;
        }
        if (computadorBlackjack) {
            return Resultado.DERROTA;
        }

        // 3. Veinte y medio
        boolean jugadorVeinteYMedio = jugador.esVeinteYMedio();
        boolean computadorVeinteYMedio = computador.esVeinteYMedio();
        if (jugadorVeinteYMedio && computadorVeinteYMedio) {
            return Resultado.EMPATE;
        }
        if (jugadorVeinteYMedio) {
            if (computador.calcularPuntaje() == 21) {
                return Resultado.DERROTA;
            }
            return Resultado.VICTORIA;
        }
        if (computadorVeinteYMedio) {
            if (jugador.calcularPuntaje() == 21) {
                return Resultado.VICTORIA;
            }
            return Resultado.DERROTA;
        }

        // 4. Comparación normal de puntajes
        int puntajeJugador = jugador.calcularPuntaje();
        int puntajeComputador = computador.calcularPuntaje();
        if (puntajeJugador > puntajeComputador) {
            return Resultado.VICTORIA;
        }
        if (puntajeJugador < puntajeComputador) {
            return Resultado.DERROTA;
        }
        return Resultado.EMPATE;
    }

    /**
     * Construye el mensaje que explica la causa del resultado.
     * Usa las manos tal como quedaron al final de la ronda.
     */
    private String explicarResultado(Resultado r) {
        int puntajeJugador = manoJugador.calcularPuntaje();
        int puntajeComputador = manoComputador.calcularPuntaje();

        if (manoJugador.estaPasada()) {
            return "Te pasaste de 21 con " + puntajeJugador + ". Pierdes la ronda.";
        }
        if (manoComputador.estaPasada()) {
            return "El computador se pasó de 21 con " + puntajeComputador + ". ¡Ganaste!";
        }
        if (manoJugador.esBlackjack() && manoComputador.esBlackjack()) {
            return "Ambos tienen Blackjack. Empate.";
        }
        if (manoJugador.esBlackjack()) {
            return "¡Blackjack! Ganaste: el computador no tiene Blackjack.";
        }
        if (manoComputador.esBlackjack()) {
            return "El computador tiene Blackjack y supera tu mano.";
        }
        if (manoJugador.esVeinteYMedio() && manoComputador.esVeinteYMedio()) {
            return "Ambos tienen veinte y medio. Empate.";
        }
        if (manoJugador.esVeinteYMedio()) {
            if (r == Resultado.VICTORIA) {
                return "Tu veinte y medio vence al computador, que tiene " + puntajeComputador + " puntos.";
            }
            return "El computador tiene 21 y supera tu veinte y medio.";
        }
        if (manoComputador.esVeinteYMedio()) {
            if (r == Resultado.VICTORIA) {
                return "Tu 21 supera el veinte y medio del computador.";
            }
            return "El computador tiene veinte y medio y supera tu puntaje de " + puntajeJugador + ".";
        }

        // Comparación normal de puntajes
        if (r == Resultado.EMPATE) {
            return "Ambos tienen " + puntajeJugador + " puntos. Empate.";
        }
        if (r == Resultado.VICTORIA) {
            return "Ganaste: tus " + puntajeJugador + " puntos superan los " + puntajeComputador + " del computador.";
        }
        return "Perdiste: los " + puntajeComputador + " puntos del computador superan tus " + puntajeJugador + ".";
    }

    // ------------------------------------------------------------
    // Liquidación (HU09)
    // ------------------------------------------------------------

    /**
     * Calcula cuántas fichas se ACREDITAN según el resultado.
     * La apuesta ya se descontó al iniciar la ronda, por eso:
     *   - Derrota: 0 (la apuesta descontada se pierde).
     *   - Empate: se devuelve la apuesta.
     *   - Victoria: se devuelve la apuesta y se gana otra igual (2 veces).
     *   - Victoria con Blackjack: la apuesta más 1,5 veces (2,5 veces).
     * Como la apuesta es múltiplo de 10, apuesta * 5 / 2 siempre es exacto.
     */
    private int calcularPago(Resultado r) {
        if (r == Resultado.VICTORIA_BLACKJACK) {
            return apuesta * 5 / 2;
        }
        if (r == Resultado.VICTORIA) {
            return apuesta * 2;
        }
        if (r == Resultado.EMPATE) {
            return apuesta;
        }
        return 0;
    }

    /**
     * Cierra la ronda: guarda el resultado, acredita el pago y deja el
     * estado en FINALIZADA. Solo actúa si la ronda está activa, así que el
     * saldo se actualiza UNA sola vez por ronda aunque se repita la acción.
     */
    private void finalizarRonda(Resultado r) {
        if (!rondaActiva()) {
            return;
        }
        resultado = r;
        mensajeFinal = explicarResultado(r);
        pagoUltimaRonda = calcularPago(r);
        saldo = saldo + pagoUltimaRonda;
        estado = EstadoRonda.FINALIZADA;
    }

    // ------------------------------------------------------------
    // Consultas (las usarán el Controlador y la Vista)
    // ------------------------------------------------------------

    /**
     * Una ronda está activa mientras sea turno del jugador o del computador.
     */
    public Resultado getResultado() {
        return resultado;
    }

    public String getMensajeFinal() {
        return mensajeFinal;
    }

    public int getPagoUltimaRonda() {
        return pagoUltimaRonda;
    }
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