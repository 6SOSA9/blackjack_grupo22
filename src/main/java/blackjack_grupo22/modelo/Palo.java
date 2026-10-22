package blackjack_grupo22.modelo;

/**
 * Representa los cuatro palos de la baraja francesa.
 * Los palos NO modifican el puntaje en Blackjack; solo sirven para
 * identificar cada carta de forma única (por ejemplo, para elegir la imagen).
 */
public enum Palo {

    CORAZONES("Corazones"),
    DIAMANTES("Diamantes"),
    TREBOLES("Tréboles"),
    PICAS("Picas");

    // Nombre legible para mostrar en mensajes al usuario
    private final String nombre;

    // Constructor del enum: se ejecuta una vez por cada valor de arriba
    Palo(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}
