package blackjack_grupo22.modelo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas de la clase Mano: puntajes, manejo de ases y reconocimiento de jugadas.
 */
public class ManoTest {

    // Método auxiliar: crea una carta con menos escritura
    private Carta c(Valor valor, Palo palo) {
        return new Carta(valor, palo);
    }

    // Método auxiliar: arma una mano a partir de un arreglo de cartas
    private Mano armarMano(Carta[] cartas) {
        Mano mano = new Mano();
        for (int i = 0; i < cartas.length; i++) {
            mano.agregarCarta(cartas[i]);
        }
        return mano;
    }

    @Test
    public void manoVaciaTienePuntajeCero() {
        Mano mano = new Mano();
        assertEquals(0, mano.calcularPuntaje());
        assertEquals(0, mano.getCantidad());
        assertTrue(mano.estaVacia());
    }

    @Test
    public void unAsSeAjustaSegunLasDemasCartas() {
        Mano mano = new Mano();
        mano.agregarCarta(c(Valor.AS, Palo.CORAZONES));
        mano.agregarCarta(c(Valor.NUEVE, Palo.PICAS));
        assertEquals(20, mano.calcularPuntaje());

        mano.agregarCarta(c(Valor.CINCO, Palo.TREBOLES));
        assertEquals(15, mano.calcularPuntaje());
        assertFalse(mano.estaPasada());
    }

    @Test
    public void variosAsesSeAjustanUnoPorUno() {
        Mano dosAsesYNueve = armarMano(new Carta[]{
                c(Valor.AS, Palo.CORAZONES), c(Valor.AS, Palo.PICAS), c(Valor.NUEVE, Palo.TREBOLES)});
        assertEquals(21, dosAsesYNueve.calcularPuntaje());

        Mano dosAsesYK = armarMano(new Carta[]{
                c(Valor.AS, Palo.CORAZONES), c(Valor.AS, Palo.PICAS), c(Valor.K, Palo.TREBOLES)});
        assertEquals(12, dosAsesYK.calcularPuntaje());
        assertFalse(dosAsesYK.estaPasada());

        dosAsesYK.agregarCarta(c(Valor.K, Palo.DIAMANTES));
        assertEquals(22, dosAsesYK.calcularPuntaje());
        assertTrue(dosAsesYK.estaPasada());
    }

    @Test
    public void figurasValenDiezYElPaloNoCambiaElPuntaje() {
        assertEquals(10, armarMano(new Carta[]{c(Valor.J, Palo.CORAZONES)}).calcularPuntaje());
        assertEquals(10, armarMano(new Carta[]{c(Valor.Q, Palo.PICAS)}).calcularPuntaje());
        assertEquals(10, armarMano(new Carta[]{c(Valor.K, Palo.TREBOLES)}).calcularPuntaje());
        assertEquals(7, armarMano(new Carta[]{c(Valor.SIETE, Palo.DIAMANTES)}).calcularPuntaje());
        assertEquals(7, armarMano(new Carta[]{c(Valor.SIETE, Palo.PICAS)}).calcularPuntaje());
    }

    @Test
    public void blackjackSeReconoceEnCualquierOrden() {
        Mano asYRey = armarMano(new Carta[]{c(Valor.AS, Palo.CORAZONES), c(Valor.K, Palo.PICAS)});
        Mano diezYAs = armarMano(new Carta[]{c(Valor.DIEZ, Palo.TREBOLES), c(Valor.AS, Palo.DIAMANTES)});
        Mano reinaYAs = armarMano(new Carta[]{c(Valor.Q, Palo.PICAS), c(Valor.AS, Palo.CORAZONES)});

        assertTrue(asYRey.esBlackjack());
        assertTrue(diezYAs.esBlackjack());
        assertTrue(reinaYAs.esBlackjack());
        assertEquals(Jugada.BLACKJACK, asYRey.determinarJugada());
    }

    @Test
    public void veintiunoDeTresCartasNoEsBlackjack() {
        Mano sietes = armarMano(new Carta[]{
                c(Valor.SIETE, Palo.CORAZONES), c(Valor.SIETE, Palo.PICAS), c(Valor.SIETE, Palo.TREBOLES)});
        Mano asYCincos = armarMano(new Carta[]{
                c(Valor.AS, Palo.CORAZONES), c(Valor.CINCO, Palo.PICAS), c(Valor.CINCO, Palo.TREBOLES)});

        assertEquals(21, sietes.calcularPuntaje());
        assertFalse(sietes.esBlackjack());
        assertTrue(sietes.esVeintiuno());
        assertEquals(Jugada.VEINTIUNO, sietes.determinarJugada());

        assertEquals(21, asYCincos.calcularPuntaje());
        assertFalse(asYCincos.esBlackjack());
        assertEquals(Jugada.VEINTIUNO, asYCincos.determinarJugada());
    }

    @Test
    public void veinteYMedioEsFiguraYCuatroEnCualquierOrden() {
        Mano reyYCuatro = armarMano(new Carta[]{c(Valor.K, Palo.CORAZONES), c(Valor.CUATRO, Palo.PICAS)});
        Mano cuatroYReina = armarMano(new Carta[]{c(Valor.CUATRO, Palo.DIAMANTES), c(Valor.Q, Palo.TREBOLES)});
        Mano jotaYCuatro = armarMano(new Carta[]{c(Valor.J, Palo.PICAS), c(Valor.CUATRO, Palo.CORAZONES)});

        assertTrue(reyYCuatro.esVeinteYMedio());
        assertTrue(cuatroYReina.esVeinteYMedio());
        assertTrue(jotaYCuatro.esVeinteYMedio());
        assertEquals(14, reyYCuatro.calcularPuntaje());
        assertEquals(Jugada.VEINTE_Y_MEDIO, reyYCuatro.determinarJugada());
    }

    @Test
    public void otrasManosQueSuman14NoSonVeinteYMedio() {
        // 9 + 5 suma 14, pero no es figura + 4
        Mano nueveYCinco = armarMano(new Carta[]{c(Valor.NUEVE, Palo.CORAZONES), c(Valor.CINCO, Palo.PICAS)});
        // El 10 numérico no es una figura
        Mano diezYCuatro = armarMano(new Carta[]{c(Valor.DIEZ, Palo.CORAZONES), c(Valor.CUATRO, Palo.PICAS)});
        // Figura + 4 pero con tres cartas ya no es Veinte y medio
        Mano tresCartas = armarMano(new Carta[]{
                c(Valor.K, Palo.CORAZONES), c(Valor.DOS, Palo.PICAS), c(Valor.DOS, Palo.TREBOLES)});
        // Figura + otra carta que no es 4
        Mano reyYCinco = armarMano(new Carta[]{c(Valor.K, Palo.CORAZONES), c(Valor.CINCO, Palo.PICAS)});

        assertFalse(nueveYCinco.esVeinteYMedio());
        assertFalse(diezYCuatro.esVeinteYMedio());
        assertFalse(tresCartas.esVeinteYMedio());
        assertFalse(reyYCinco.esVeinteYMedio());
        assertEquals(Jugada.NORMAL, nueveYCinco.determinarJugada());
    }

    @Test
    public void pasarseSoloOcurreDespuesDeAjustarLosAses() {
        Mano reyNueveCinco = armarMano(new Carta[]{
                c(Valor.K, Palo.CORAZONES), c(Valor.NUEVE, Palo.PICAS), c(Valor.CINCO, Palo.TREBOLES)});
        assertEquals(24, reyNueveCinco.calcularPuntaje());
        assertTrue(reyNueveCinco.estaPasada());
        assertEquals(Jugada.PASADO, reyNueveCinco.determinarJugada());

        // As + K + 5 vale 16: el As se cuenta como 1 y no se pasa
        Mano conAs = armarMano(new Carta[]{
                c(Valor.AS, Palo.CORAZONES), c(Valor.K, Palo.PICAS), c(Valor.CINCO, Palo.TREBOLES)});
        assertEquals(16, conAs.calcularPuntaje());
        assertFalse(conAs.estaPasada());
        assertEquals(Jugada.NORMAL, conAs.determinarJugada());
    }

    @Test
    public void listaEnlazadaConservaElOrdenDeLasCartas() {
        Carta uno = c(Valor.AS, Palo.CORAZONES);
        Carta dos = c(Valor.SIETE, Palo.PICAS);
        Carta tres = c(Valor.K, Palo.TREBOLES);
        Mano mano = armarMano(new Carta[]{uno, dos, tres});

        assertEquals(3, mano.getCantidad());
        assertEquals(uno, mano.obtenerCarta(0));
        assertEquals(dos, mano.obtenerCarta(1));
        assertEquals(tres, mano.obtenerCarta(2));
    }

    @Test
    public void obtenerCartaFueraDeRangoLanzaExcepcion() {
        Mano mano = armarMano(new Carta[]{c(Valor.AS, Palo.CORAZONES)});

        assertThrows(IndexOutOfBoundsException.class, () -> mano.obtenerCarta(1));
        assertThrows(IndexOutOfBoundsException.class, () -> mano.obtenerCarta(-1));
    }

    @Test
    public void vaciarDejaLaManoSinCartas() {
        Mano mano = armarMano(new Carta[]{c(Valor.AS, Palo.CORAZONES), c(Valor.K, Palo.PICAS)});
        mano.vaciar();

        assertTrue(mano.estaVacia());
        assertEquals(0, mano.calcularPuntaje());

        // Se puede volver a usar después de vaciarla
        mano.agregarCarta(c(Valor.CINCO, Palo.PICAS));
        assertEquals(1, mano.getCantidad());
        assertEquals(5, mano.calcularPuntaje());
    }

    @Test
    public void asesTodosComoUnoCuandoNingunaCombinacionEvitaPasarse() {
        // As + As + K + K = 22: ambos ases valen 1 y aun así se pasa
        Mano mano = armarMano(new Carta[]{
                c(Valor.AS, Palo.CORAZONES), c(Valor.AS, Palo.PICAS),
                c(Valor.K, Palo.TREBOLES), c(Valor.K, Palo.DIAMANTES)});
        assertEquals(22, mano.calcularPuntaje());
        assertTrue(mano.estaPasada());
    }
}