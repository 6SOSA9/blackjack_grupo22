package blackjack_grupo22.modelo;

/**
 * Nodo de la lista enlazada que forma una mano.
 * Cada nodo guarda una carta y una referencia al nodo que sigue.
 * Si "siguiente" es null, este nodo es el último de la lista.
 */
public class Nodo {

    private Carta carta;
    private Nodo siguiente;

    public Nodo(Carta carta) {
        this.carta = carta;
        this.siguiente = null;   // al crearse, todavía no apunta a nadie
    }

    public Carta getCarta() {
        return carta;
    }

    public Nodo getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Nodo siguiente) {
        this.siguiente = siguiente;
    }
}
