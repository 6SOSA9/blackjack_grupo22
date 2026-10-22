package blackjack_grupo22.modelo;

/**
 * Representa la mano de un participante (jugador o computador).
 * Está implementada como una lista enlazada simple de nodos.
 * También calcula el puntaje, ajustando el valor de los ases.
 */
public class Mano {

    // Primer nodo de la lista (null si la mano está vacía)
    private Nodo primero;

    // Último nodo de la lista: permite agregar al final sin recorrer
    private Nodo ultimo;

    // Cantidad de cartas que tiene la mano
    private int cantidad;

    /**
     * Crea una mano vacía.
     */
    public Mano() {
        this.primero = null;
        this.ultimo = null;
        this.cantidad = 0;
    }

    /**
     * Agrega una carta al final de la mano.
     */
    public void agregarCarta(Carta carta) {
        Nodo nuevo = new Nodo(carta);

        if (primero == null) {
            // Mano vacía: el nuevo nodo es a la vez el primero y el último
            primero = nuevo;
            ultimo = nuevo;
        } else {
            // El último actual apunta al nuevo, y el nuevo pasa a ser el último
            ultimo.setSiguiente(nuevo);
            ultimo = nuevo;
        }
        cantidad++;
    }

    /**
     * Cantidad de cartas de la mano.
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Indica si la mano no tiene cartas.
     */
    public boolean estaVacia() {
        if (cantidad == 0) {
            return true;
        }
        return false;
    }

    /**
     * Devuelve la carta que está en la posición indicada (0 = primera).
     * Recorre la lista nodo por nodo hasta llegar a esa posición.
     * La Vista usará este método para dibujar las cartas en orden.
     */
    public Carta obtenerCarta(int posicion) {
        if (posicion < 0 || posicion >= cantidad) {
            throw new IndexOutOfBoundsException("La mano no tiene una carta en la posición " + posicion);
        }

        Nodo actual = primero;
        int contador = 0;
        while (contador < posicion) {
            actual = actual.getSiguiente();
            contador++;
        }
        return actual.getCarta();
    }

    /**
     * Calcula el puntaje de la mano.
     *
     * Paso 1: se suman todas las cartas con el As valiendo 11.
     * Paso 2: mientras la suma supere 21 y todavía haya ases contados
     *         como 11, se convierte uno de ellos en 1 (restando 10).
     *
     * Resultado: el mayor puntaje posible sin pasarse de 21. Si aun
     * convirtiendo todos los ases la suma supera 21, el puntaje se queda
     * por encima de 21 y la mano está "pasada".
     */
    public int calcularPuntaje() {
        int suma = 0;
        int asesComoOnce = 0;

        // Recorremos la lista enlazada de principio a fin
        Nodo actual = primero;
        while (actual != null) {
            Carta carta = actual.getCarta();
            suma = suma + carta.getPuntos();
            if (carta.esAs()) {
                asesComoOnce++;
            }
            actual = actual.getSiguiente();
        }

        // Ajuste de ases: de 11 a 1 mientras sea necesario
        while (suma > 21 && asesComoOnce > 0) {
            suma = suma - 10;
            asesComoOnce--;
        }

        return suma;
    }

    /**
     * Indica si la mano se pasó de 21 después de ajustar los ases.
     */
    public boolean estaPasada() {
        if (calcularPuntaje() > 21) {
            return true;
        }
        return false;
    }

    /**
     * Vacía la mano (se usa al comenzar una nueva ronda).
     */
    public void vaciar() {
        primero = null;
        ultimo = null;
        cantidad = 0;
    }

    /**
     * Texto con todas las cartas, por ejemplo: "As de Corazones, 9 de Picas".
     */
    @Override
    public String toString() {
        if (cantidad == 0) {
            return "(mano vacía)";
        }

        String texto = "";
        Nodo actual = primero;
        while (actual != null) {
            texto = texto + actual.getCarta().toString();
            if (actual.getSiguiente() != null) {
                texto = texto + ", ";
            }
            actual = actual.getSiguiente();
        }
        return texto;
    }
}
