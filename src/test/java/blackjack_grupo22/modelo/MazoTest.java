package blackjack_grupo22.modelo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas de la clase Mazo: 52 cartas, reparto sin repetir y mazos controlados.
 */
public class MazoTest {

    @Test
    public void nuevoMazoTiene52Cartas() {
        Mazo mazo = new Mazo();
        assertEquals(52, mazo.cartasRestantes());
        assertTrue(mazo.hayCartas());
    }

    @Test
    public void repartirLas52CartasNuncaRepiteUnaCarta() {
        Mazo mazo = new Mazo();
        Carta[] repartidas = new Carta[52];

        for (int i = 0; i < 52; i++) {
            repartidas[i] = mazo.repartir();
            // Cada reparto reduce en uno las cartas restantes
            assertEquals(52 - (i + 1), mazo.cartasRestantes());
        }

        // Se compara cada carta con todas las demás
        for (int i = 0; i < 52; i++) {
            for (int k = i + 1; k < 52; k++) {
                assertFalse(repartidas[i].equals(repartidas[k]),
                        "Carta repetida: " + repartidas[i]);
            }
        }
        assertFalse(mazo.hayCartas());
    }

    @Test
    public void repartirConElMazoAgotadoLanzaExcepcion() {
        Mazo mazo = new Mazo();
        for (int i = 0; i < 52; i++) {
            mazo.repartir();
        }
        assertThrows(IllegalStateException.class, () -> mazo.repartir());
    }

    @Test
    public void barajarReiniciaElReparto() {
        Mazo mazo = new Mazo();
        for (int i = 0; i < 10; i++) {
            mazo.repartir();
        }
        assertEquals(42, mazo.cartasRestantes());

        mazo.barajar();
        assertEquals(52, mazo.cartasRestantes());
    }

    @Test
    public void dosMazosConLaMismaSemillaTienenElMismoOrden() {
        Mazo mazo1 = new Mazo(12345L);
        Mazo mazo2 = new Mazo(12345L);

        for (int i = 0; i < 52; i++) {
            assertEquals(mazo1.repartir(), mazo2.repartir());
        }
    }

    @Test
    public void mazoControladoReparteLasCartasIndicadasPrimero() {
        Carta primera = new Carta(Valor.AS, Palo.CORAZONES);
        Carta segunda = new Carta(Valor.K, Palo.PICAS);
        Mazo mazo = new Mazo(new Carta[]{primera, segunda});

        assertEquals(primera, mazo.repartir());
        assertEquals(segunda, mazo.repartir());
        assertEquals(50, mazo.cartasRestantes());
    }

    @Test
    public void mazoControladoRechazaCartasRepetidas() {
        Carta repetida = new Carta(Valor.AS, Palo.CORAZONES);
        Carta[] indicadas = new Carta[]{repetida, new Carta(Valor.AS, Palo.CORAZONES)};

        assertThrows(IllegalArgumentException.class, () -> new Mazo(indicadas));
    }
}