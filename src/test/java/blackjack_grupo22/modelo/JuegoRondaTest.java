package blackjack_grupo22.modelo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas de una ronda completa: reglas del computador, resultados,
 * liquidación del saldo, acciones fuera de turno y cartas sin repetir.
 * Las cartas de cada escenario se controlan con un mazo preparado.
 */
public class JuegoRondaTest {

    private Carta c(Valor valor, Palo palo) {
        return new Carta(valor, palo);
    }

    // Método auxiliar: crea un juego e inicia una ronda de 100 con las cartas indicadas
    private Juego iniciarConCartas(Carta[] cartas) {
        Juego juego = new Juego();
        String error = juego.iniciarRondaConMazo("100", new Mazo(cartas));
        assertNull(error);
        return juego;
    }

    // Método auxiliar: arma una mano a partir de un arreglo de cartas
    private Mano armarMano(Carta[] cartas) {
        Mano mano = new Mano();
        for (int i = 0; i < cartas.length; i++) {
            mano.agregarCarta(cartas[i]);
        }
        return mano;
    }

    // ------------------------------------------------------------
    // Resultados y pagos (saldo 1000, apuesta 100)
    // ------------------------------------------------------------

    @Test
    public void victoriaOrdinariaDevuelveLaApuestaYGanaOtraIgual() {
        // Jugador 18 contra computador 17
        Juego juego = iniciarConCartas(new Carta[]{
                c(Valor.DIEZ, Palo.CORAZONES), c(Valor.OCHO, Palo.PICAS),
                c(Valor.DIEZ, Palo.DIAMANTES), c(Valor.SIETE, Palo.TREBOLES)});
        juego.plantarse();
        juego.jugarTurnoComputador();

        assertEquals(Resultado.VICTORIA, juego.getResultado());
        assertEquals(200, juego.getPagoUltimaRonda());
        assertEquals(1100, juego.getSaldo());
        assertEquals(EstadoRonda.FINALIZADA, juego.getEstado());
    }

    @Test
    public void derrotaNoDevuelveNada() {
        // Jugador 17 contra computador 19
        Juego juego = iniciarConCartas(new Carta[]{
                c(Valor.DIEZ, Palo.CORAZONES), c(Valor.SIETE, Palo.PICAS),
                c(Valor.DIEZ, Palo.DIAMANTES), c(Valor.NUEVE, Palo.TREBOLES)});
        juego.plantarse();
        juego.jugarTurnoComputador();

        assertEquals(Resultado.DERROTA, juego.getResultado());
        assertEquals(0, juego.getPagoUltimaRonda());
        assertEquals(900, juego.getSaldo());
    }

    @Test
    public void empateDevuelveLaApuesta() {
        // 18 contra 18
        Juego juego = iniciarConCartas(new Carta[]{
                c(Valor.DIEZ, Palo.CORAZONES), c(Valor.OCHO, Palo.PICAS),
                c(Valor.DIEZ, Palo.DIAMANTES), c(Valor.OCHO, Palo.TREBOLES)});
        juego.plantarse();
        juego.jugarTurnoComputador();

        assertEquals(Resultado.EMPATE, juego.getResultado());
        assertEquals(100, juego.getPagoUltimaRonda());
        assertEquals(1000, juego.getSaldo());
    }

    @Test
    public void victoriaConBlackjackPagaDosYMediaVecesLaApuesta() {
        // Jugador As + K; el computador (9 + 7 = 16) no pide cartas
        Juego juego = iniciarConCartas(new Carta[]{
                c(Valor.AS, Palo.CORAZONES), c(Valor.K, Palo.PICAS),
                c(Valor.NUEVE, Palo.DIAMANTES), c(Valor.SIETE, Palo.TREBOLES)});

        // Con Blackjack el turno del computador comienza automáticamente
        assertEquals(EstadoRonda.TURNO_COMPUTADOR, juego.getEstado());
        juego.jugarTurnoComputador();

        assertEquals(Resultado.VICTORIA_BLACKJACK, juego.getResultado());
        assertEquals(250, juego.getPagoUltimaRonda());
        assertEquals(1150, juego.getSaldo());
        assertEquals(2, juego.getManoComputador().getCantidad());
    }

    @Test
    public void blackjackDobleEsEmpate() {
        Juego juego = iniciarConCartas(new Carta[]{
                c(Valor.AS, Palo.CORAZONES), c(Valor.K, Palo.PICAS),
                c(Valor.AS, Palo.DIAMANTES), c(Valor.K, Palo.DIAMANTES)});
        juego.jugarTurnoComputador();

        assertEquals(Resultado.EMPATE, juego.getResultado());
        assertEquals(1000, juego.getSaldo());
    }

    @Test
    public void blackjackDelComputadorGanaAUnVeintiunoDeTresCartas() {
        // Jugador: 7 + 7 + 7 = 21. Computador: As + K
        Juego juego = iniciarConCartas(new Carta[]{
                c(Valor.SIETE, Palo.CORAZONES), c(Valor.SIETE, Palo.PICAS),
                c(Valor.SIETE, Palo.TREBOLES),
                c(Valor.AS, Palo.DIAMANTES), c(Valor.K, Palo.DIAMANTES)});

        assertEquals(EstadoRonda.TURNO_JUGADOR, juego.getEstado());
        juego.pedirCarta();
        // Con 21 el turno del jugador termina solo
        assertEquals(EstadoRonda.TURNO_COMPUTADOR, juego.getEstado());
        juego.jugarTurnoComputador();

        assertEquals(Resultado.DERROTA, juego.getResultado());
        assertEquals(900, juego.getSaldo());
    }

    @Test
    public void pagoDeBlackjackConApuestaDeDiezEsVeinticinco() {
        Juego juego = new Juego();
        juego.iniciarRondaConMazo("10", new Mazo(new Carta[]{
                c(Valor.AS, Palo.CORAZONES), c(Valor.K, Palo.PICAS),
                c(Valor.NUEVE, Palo.DIAMANTES), c(Valor.SIETE, Palo.TREBOLES)}));
        juego.jugarTurnoComputador();

        assertEquals(25, juego.getPagoUltimaRonda());
        assertEquals(1015, juego.getSaldo());
    }

    // ------------------------------------------------------------
    // Derrota por pasarse
    // ------------------------------------------------------------

    @Test
    public void jugadorQueSePasaPierdeYElComputadorNoJuega() {
        // K + 9 = 19; pide un 5 y llega a 24
        Juego juego = iniciarConCartas(new Carta[]{
                c(Valor.K, Palo.CORAZONES), c(Valor.NUEVE, Palo.PICAS),
                c(Valor.CINCO, Palo.TREBOLES)});
        juego.pedirCarta();

        assertEquals(24, juego.getManoJugador().calcularPuntaje());
        assertEquals(Resultado.DERROTA, juego.getResultado());
        assertEquals(900, juego.getSaldo());
        assertEquals(EstadoRonda.FINALIZADA, juego.getEstado());
        // El computador no recibió cartas
        assertTrue(juego.getManoComputador().estaVacia());
        assertNotNull(juego.jugarTurnoComputador());
        assertTrue(juego.getManoComputador().estaVacia());
    }

    // ------------------------------------------------------------
    // Reglas del computador
    // ------------------------------------------------------------

    @Test
    public void computadorPideConDieciseisYSePasa() {
        // Computador: 10 + 6 = 16 -> pide K -> 26
        Juego juego = iniciarConCartas(new Carta[]{
                c(Valor.DIEZ, Palo.CORAZONES), c(Valor.OCHO, Palo.PICAS),
                c(Valor.DIEZ, Palo.DIAMANTES), c(Valor.SEIS, Palo.TREBOLES),
                c(Valor.K, Palo.PICAS)});
        juego.plantarse();
        juego.jugarTurnoComputador();

        assertEquals(3, juego.getManoComputador().getCantidad());
        assertEquals(26, juego.getManoComputador().calcularPuntaje());
        assertEquals(Resultado.VICTORIA, juego.getResultado());
        assertEquals(1100, juego.getSaldo());
    }

    @Test
    public void computadorSePlantaConDiecisiete() {
        // Computador: 10 + 7 = 17; la siguiente carta del mazo no se reparte
        Juego juego = iniciarConCartas(new Carta[]{
                c(Valor.DIEZ, Palo.CORAZONES), c(Valor.OCHO, Palo.PICAS),
                c(Valor.DIEZ, Palo.DIAMANTES), c(Valor.SIETE, Palo.TREBOLES),
                c(Valor.DOS, Palo.PICAS)});
        juego.plantarse();
        juego.jugarTurnoComputador();

        assertEquals(2, juego.getManoComputador().getCantidad());
        assertEquals(17, juego.getManoComputador().calcularPuntaje());
        assertEquals(48, juego.cartasRestantesEnMazo());
    }

    @Test
    public void computadorSePlantaConAsMasSeisQueCuentaComoDiecisiete() {
        Juego juego = iniciarConCartas(new Carta[]{
                c(Valor.DIEZ, Palo.CORAZONES), c(Valor.OCHO, Palo.PICAS),
                c(Valor.AS, Palo.DIAMANTES), c(Valor.SEIS, Palo.TREBOLES),
                c(Valor.DOS, Palo.PICAS)});
        juego.plantarse();
        juego.jugarTurnoComputador();

        assertEquals(2, juego.getManoComputador().getCantidad());
        assertEquals(17, juego.getManoComputador().calcularPuntaje());
        assertEquals(Resultado.VICTORIA, juego.getResultado());
    }

    @Test
    public void computadorSigueSacandoCartasHastaLlegarADiecisieteOMas() {
        // Computador: 10 + 2 = 12 -> pide 3 (15) -> pide 2 (17) -> se planta
        Juego juego = iniciarConCartas(new Carta[]{
                c(Valor.DIEZ, Palo.CORAZONES), c(Valor.OCHO, Palo.PICAS),
                c(Valor.DIEZ, Palo.DIAMANTES), c(Valor.DOS, Palo.TREBOLES),
                c(Valor.TRES, Palo.DIAMANTES), c(Valor.DOS, Palo.DIAMANTES)});
        juego.plantarse();
        juego.jugarTurnoComputador();

        assertEquals(4, juego.getManoComputador().getCantidad());
        assertEquals(17, juego.getManoComputador().calcularPuntaje());
    }

    @Test
    public void computadorSePlantaConVeinteYMedio() {
        // Computador: K + 4 = 14 (Veinte y medio). No pide, aunque tenga menos de 17
        Juego juego = iniciarConCartas(new Carta[]{
                c(Valor.DIEZ, Palo.CORAZONES), c(Valor.NUEVE, Palo.PICAS),
                c(Valor.K, Palo.DIAMANTES), c(Valor.CUATRO, Palo.TREBOLES),
                c(Valor.CINCO, Palo.PICAS)});
        juego.plantarse();
        juego.jugarTurnoComputador();

        assertEquals(2, juego.getManoComputador().getCantidad());
        assertEquals(14, juego.getManoComputador().calcularPuntaje());
        assertTrue(juego.getManoComputador().esVeinteYMedio());
        assertEquals(48, juego.cartasRestantesEnMazo());
        // Veinte y medio del computador le gana al 19 del jugador
        assertEquals(Resultado.DERROTA, juego.getResultado());
        assertEquals(900, juego.getSaldo());
    }

    // ------------------------------------------------------------
    // Veinte y medio contra otras manos
    // ------------------------------------------------------------

    @Test
    public void veinteYMedioDelJugadorGanaAUnDiecinueve() {
        Juego juego = iniciarConCartas(new Carta[]{
                c(Valor.K, Palo.CORAZONES), c(Valor.CUATRO, Palo.PICAS),
                c(Valor.DIEZ, Palo.DIAMANTES), c(Valor.NUEVE, Palo.TREBOLES)});
        assertEquals(Jugada.VEINTE_Y_MEDIO, juego.getJugadaJugador());
        juego.plantarse();
        juego.jugarTurnoComputador();

        assertEquals(Resultado.VICTORIA, juego.getResultado());
        assertEquals(1100, juego.getSaldo());
    }

    @Test
    public void veinteYMedioDelJugadorPierdeContraUnVeintiunoDelComputador() {
        // Computador: 7 + 7 = 14 -> pide 7 -> 21 (no es Blackjack)
        Juego juego = iniciarConCartas(new Carta[]{
                c(Valor.K, Palo.CORAZONES), c(Valor.CUATRO, Palo.PICAS),
                c(Valor.SIETE, Palo.DIAMANTES), c(Valor.SIETE, Palo.TREBOLES),
                c(Valor.SIETE, Palo.PICAS)});
        juego.plantarse();
        juego.jugarTurnoComputador();

        assertEquals(21, juego.getManoComputador().calcularPuntaje());
        assertEquals(Resultado.DERROTA, juego.getResultado());
        assertEquals(900, juego.getSaldo());
    }

    @Test
    public void veinteYMedioDelComputadorPierdeContraUnVeintiunoDelJugador() {
        // Jugador: 7 + 7 + 7 = 21. Computador: K + 4 (Veinte y medio)
        Juego juego = iniciarConCartas(new Carta[]{
                c(Valor.SIETE, Palo.CORAZONES), c(Valor.SIETE, Palo.PICAS),
                c(Valor.SIETE, Palo.TREBOLES),
                c(Valor.K, Palo.DIAMANTES), c(Valor.CUATRO, Palo.TREBOLES)});
        juego.pedirCarta();
        juego.jugarTurnoComputador();

        assertEquals(Resultado.VICTORIA, juego.getResultado());
        assertEquals(1100, juego.getSaldo());
    }

    @Test
    public void ambosConVeinteYMedioEmpatan() {
        Juego juego = iniciarConCartas(new Carta[]{
                c(Valor.K, Palo.CORAZONES), c(Valor.CUATRO, Palo.PICAS),
                c(Valor.Q, Palo.DIAMANTES), c(Valor.CUATRO, Palo.DIAMANTES)});
        juego.plantarse();
        juego.jugarTurnoComputador();

        assertEquals(Resultado.EMPATE, juego.getResultado());
        assertEquals(1000, juego.getSaldo());
    }

    // ------------------------------------------------------------
    // Comparación de manos (determinarResultado no modifica nada)
    // ------------------------------------------------------------

    @Test
    public void dosManosDeDieciochoSinBlackjackEmpatan() {
        Juego juego = new Juego();
        Mano jugador = armarMano(new Carta[]{c(Valor.DIEZ, Palo.CORAZONES), c(Valor.OCHO, Palo.PICAS)});
        Mano computador = armarMano(new Carta[]{c(Valor.DIEZ, Palo.DIAMANTES), c(Valor.OCHO, Palo.TREBOLES)});

        assertEquals(Resultado.EMPATE, juego.determinarResultado(jugador, computador));
    }

    @Test
    public void veinteDelJugadorContraVeintidosDelComputadorEsVictoria() {
        Juego juego = new Juego();
        Mano jugador = armarMano(new Carta[]{c(Valor.DIEZ, Palo.CORAZONES), c(Valor.DIEZ, Palo.PICAS)});
        Mano computador = armarMano(new Carta[]{
                c(Valor.DIEZ, Palo.DIAMANTES), c(Valor.SEIS, Palo.TREBOLES), c(Valor.SEIS, Palo.DIAMANTES)});

        assertEquals(22, computador.calcularPuntaje());
        assertEquals(Resultado.VICTORIA, juego.determinarResultado(jugador, computador));
    }

    // ------------------------------------------------------------
    // Acciones fuera de turno y saldo actualizado una sola vez
    // ------------------------------------------------------------

    @Test
    public void accionesAntesDeLaRondaSonRechazadasYNoCambianNada() {
        Juego juego = new Juego();

        assertNotNull(juego.pedirCarta());
        assertNotNull(juego.plantarse());
        assertNotNull(juego.jugarTurnoComputador());

        assertEquals(1000, juego.getSaldo());
        assertEquals(EstadoRonda.SIN_RONDA, juego.getEstado());
        assertTrue(juego.getManoJugador().estaVacia());
        assertTrue(juego.getManoComputador().estaVacia());
    }

    @Test
    public void plantarseDosVecesHaceJugarAlComputadorUnaSolaVez() {
        Juego juego = iniciarConCartas(new Carta[]{
                c(Valor.DIEZ, Palo.CORAZONES), c(Valor.OCHO, Palo.PICAS),
                c(Valor.DIEZ, Palo.DIAMANTES), c(Valor.SIETE, Palo.TREBOLES)});

        assertNull(juego.plantarse());
        assertNotNull(juego.plantarse());          // segunda vez: ignorada
        assertNotNull(juego.pedirCarta());         // ya no es su turno
        assertEquals(2, juego.getManoJugador().getCantidad());

        assertNull(juego.jugarTurnoComputador());
        assertNotNull(juego.jugarTurnoComputador());   // segunda vez: ignorada
        assertEquals(2, juego.getManoComputador().getCantidad());
    }

    @Test
    public void elSaldoSeActualizaUnaSolaVezPorRonda() {
        Juego juego = iniciarConCartas(new Carta[]{
                c(Valor.DIEZ, Palo.CORAZONES), c(Valor.OCHO, Palo.PICAS),
                c(Valor.DIEZ, Palo.DIAMANTES), c(Valor.SIETE, Palo.TREBOLES)});
        juego.plantarse();
        juego.jugarTurnoComputador();
        assertEquals(1100, juego.getSaldo());

        // Repetir acciones después del resultado no genera pagos ni descuentos
        juego.jugarTurnoComputador();
        juego.pedirCarta();
        juego.plantarse();
        juego.getResultado();
        juego.getMensajeFinal();

        assertEquals(1100, juego.getSaldo());
        assertEquals(EstadoRonda.FINALIZADA, juego.getEstado());
        assertEquals(2, juego.getManoJugador().getCantidad());
    }

    // ------------------------------------------------------------
    // Continuidad entre rondas y cartas sin repetir
    // ------------------------------------------------------------

    @Test
    public void nuevaRondaConservaElSaldoYRenuevaManosYMazo() {
        Juego juego = iniciarConCartas(new Carta[]{
                c(Valor.DIEZ, Palo.CORAZONES), c(Valor.OCHO, Palo.PICAS),
                c(Valor.DIEZ, Palo.DIAMANTES), c(Valor.SIETE, Palo.TREBOLES)});
        juego.plantarse();
        juego.jugarTurnoComputador();
        assertEquals(1100, juego.getSaldo());

        String error = juego.iniciarRonda("100");

        assertNull(error);
        assertEquals(1000, juego.getSaldo());          // 1100 - 100, no se reinician las 1000
        assertEquals(100, juego.getApuesta());
        assertEquals(2, juego.getManoJugador().getCantidad());
        assertTrue(juego.getManoComputador().estaVacia());
        assertEquals(50, juego.cartasRestantesEnMazo());   // mazo completo menos 2 cartas
        assertNull(juego.getResultado());
    }

    @Test
    public void nuncaHayCartasRepetidasEntreLasDosManos() {
        // Se juegan muchas rondas con mazo mezclado y se revisa cada una
        for (int ronda = 0; ronda < 300; ronda++) {
            Juego juego = new Juego();
            juego.iniciarRonda("10");
            if (juego.getEstado() == EstadoRonda.TURNO_JUGADOR) {
                juego.plantarse();
            }
            juego.jugarTurnoComputador();

            Mano jugador = juego.getManoJugador();
            Mano computador = juego.getManoComputador();
            int total = jugador.getCantidad() + computador.getCantidad();
            Carta[] todas = new Carta[total];
            int indice = 0;
            for (int i = 0; i < jugador.getCantidad(); i++) {
                todas[indice] = jugador.obtenerCarta(i);
                indice++;
            }
            for (int i = 0; i < computador.getCantidad(); i++) {
                todas[indice] = computador.obtenerCarta(i);
                indice++;
            }

            for (int i = 0; i < total; i++) {
                for (int k = i + 1; k < total; k++) {
                    assertFalse(todas[i].equals(todas[k]),
                            "Carta repetida en la ronda " + ronda + ": " + todas[i]);
                }
            }
        }
    }
}