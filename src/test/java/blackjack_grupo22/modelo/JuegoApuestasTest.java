package blackjack_grupo22.modelo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas de apuestas incorrectas, saldo insuficiente y descuento de la apuesta.
 */
public class JuegoApuestasTest {

    private Carta c(Valor valor, Palo palo) {
        return new Carta(valor, palo);
    }

    @Test
    public void estadoInicialTieneSaldoDeMilYSinRonda() {
        Juego juego = new Juego();

        assertEquals(1000, juego.getSaldo());
        assertEquals(0, juego.getApuesta());
        assertEquals(EstadoRonda.SIN_RONDA, juego.getEstado());
        assertTrue(juego.getManoJugador().estaVacia());
        assertTrue(juego.getManoComputador().estaVacia());
        assertFalse(juego.rondaActiva());
    }

    @Test
    public void apuestasIncorrectasSonRechazadasConExplicacion() {
        Juego juego = new Juego();
        // vacío, texto, no múltiplo, cero, negativo, decimales, mayor que el saldo, menor de 10
        String[] incorrectas = {"", "   ", "hola", "15", "0", "-10", "10,5", "10.5", "1010", "5", "1e3", "abc10"};

        for (int i = 0; i < incorrectas.length; i++) {
            String mensaje = juego.validarApuesta(incorrectas[i]);
            assertNotNull(mensaje, "Debió rechazarse: \"" + incorrectas[i] + "\"");
        }
    }

    @Test
    public void mensajesDeRechazoExplicanComoCorregir() {
        Juego juego = new Juego();

        assertEquals("Escribe una apuesta para comenzar.", juego.validarApuesta(""));
        assertEquals("La apuesta debe ser múltiplo de 10.", juego.validarApuesta("15"));
        assertEquals("La apuesta mínima es de 10 fichas.", juego.validarApuesta("5"));
        assertEquals("La apuesta debe ser mayor que cero.", juego.validarApuesta("0"));
        assertEquals("No tienes saldo suficiente. Tu saldo es de 1000 fichas.", juego.validarApuesta("1010"));
    }

    @Test
    public void unaApuestaRechazadaNoIniciaRondaNiCambiaNada() {
        Juego juego = new Juego();

        String mensaje = juego.iniciarRonda("15");

        assertNotNull(mensaje);
        assertEquals(1000, juego.getSaldo());
        assertEquals(0, juego.getApuesta());
        assertEquals(EstadoRonda.SIN_RONDA, juego.getEstado());
        assertTrue(juego.getManoJugador().estaVacia());
        assertTrue(juego.getManoComputador().estaVacia());
    }

    @Test
    public void apuestasValidasSonAceptadas() {
        Juego juego = new Juego();

        assertNull(juego.validarApuesta("10"));
        assertNull(juego.validarApuesta("100"));
        assertNull(juego.validarApuesta("  100  "));
        assertNull(juego.validarApuesta("1000"));   // igual al saldo
    }

    @Test
    public void validarUnaApuestaNoDescuentaElSaldo() {
        Juego juego = new Juego();

        juego.validarApuesta("100");

        assertEquals(1000, juego.getSaldo());
        assertEquals(0, juego.getApuesta());
    }

    @Test
    public void apuestaIgualAlSaldoEsValidaYDejaElSaldoEnCero() {
        Juego juego = new Juego();

        String mensaje = juego.iniciarRonda("1000");

        assertNull(mensaje);
        assertEquals(0, juego.getSaldo());
        assertEquals(1000, juego.getApuesta());
    }

    @Test
    public void laApuestaSeDescuentaUnaSolaVezAlIniciarLaRonda() {
        Juego juego = new Juego();
        Mazo mazo = new Mazo(new Carta[]{
                c(Valor.DIEZ, Palo.CORAZONES), c(Valor.OCHO, Palo.PICAS)});

        String primero = juego.iniciarRondaConMazo("100", mazo);
        assertNull(primero);
        assertEquals(900, juego.getSaldo());
        assertEquals(100, juego.getApuesta());
        assertEquals(EstadoRonda.TURNO_JUGADOR, juego.getEstado());
        assertEquals(2, juego.getManoJugador().getCantidad());

        // Activaciones repetidas: no se descuenta otra vez ni se reparte de nuevo
        String segundo = juego.iniciarRonda("100");
        assertEquals("Ya hay una ronda en juego.", segundo);
        assertEquals(900, juego.getSaldo());
        assertEquals(2, juego.getManoJugador().getCantidad());
    }

    @Test
    public void sinFichasSuficientesNoSePuedeIniciarOtraRonda() {
        Juego juego = new Juego();
        // El jugador apuesta todo y pierde: 17 contra 19
        juego.iniciarRondaConMazo("1000", new Mazo(new Carta[]{
                c(Valor.DIEZ, Palo.CORAZONES), c(Valor.SIETE, Palo.PICAS),
                c(Valor.DIEZ, Palo.DIAMANTES), c(Valor.NUEVE, Palo.TREBOLES)}));
        juego.plantarse();
        juego.jugarTurnoComputador();

        assertEquals(0, juego.getSaldo());
        assertFalse(juego.hayFichasSuficientes());
        assertEquals("No tienes fichas suficientes para otra ronda.", juego.validarApuesta("10"));
        assertNotNull(juego.iniciarRonda("10"));
        assertEquals(EstadoRonda.FINALIZADA, juego.getEstado());
    }
}