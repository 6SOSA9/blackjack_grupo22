package blackjack_grupo22.modelo;

/**
 * Representa una carta de la baraja: una combinación de valor y palo.
 * Es inmutable: sus atributos no cambian después de crearla.
 */
public class Carta {

    private final Valor valor;
    private final Palo palo;

    public Carta(Valor valor, Palo palo) {
        this.valor = valor;
        this.palo = palo;
    }

    public Valor getValor() {
        return valor;
    }

    public Palo getPalo() {
        return palo;
    }

    /**
     * Puntos base de la carta (el As devuelve 11).
     */
    public int getPuntos() {
        return valor.getPuntos();
    }

    public boolean esAs() {
        return valor == Valor.AS;
    }

    /**
     * Nombre del archivo de imagen que corresponde a esta carta.
     * Ejemplo: "AS_CORAZONES.png", "10_PICAS.png", "K_TREBOLES.png".
     * La Vista usará este nombre para cargar la imagen correcta.
     */
    public String getNombreImagen() {
        return valor.getCodigo() + "_" + palo.name() + ".png";
    }

    /**
     * Texto legible, por ejemplo "As de Corazones".
     */
    @Override
    public String toString() {
        return valor.getNombre() + " de " + palo.getNombre();
    }

    /**
     * Dos cartas son iguales si tienen el mismo valor Y el mismo palo.
     * Lo usaremos en las pruebas para comprobar que no hay cartas repetidas.
     */
    @Override
    public boolean equals(Object otro) {
        if (this == otro) {
            return true;
        }
        if (otro == null) {
            return false;
        }
        if (getClass() != otro.getClass()) {
            return false;
        }
        Carta otraCarta = (Carta) otro;
        if (this.valor == otraCarta.valor && this.palo == otraCarta.palo) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return valor.ordinal() * 4 + palo.ordinal();
    }
}