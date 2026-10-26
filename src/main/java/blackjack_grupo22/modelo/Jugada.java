package blackjack_grupo22.modelo;


public enum Jugada {

    BLACKJACK("Blackjack", "¡Blackjack! As y carta de diez en las dos primeras cartas."),
    VEINTIUNO("Veintiuno", "¡Veintiuno! Puntaje de 21 sin ser Blackjack."),
    VEINTE_Y_MEDIO("Veinte y medio", "¡Veinte y medio! Una figura y un 4 en las dos primeras cartas."),
    PASADO("Pasarse", "Te pasaste de 21."),
    NORMAL("Normal", "");

    private final String nombre;    // nombre corto de la jugada
    private final String mensaje;   // mensaje que se mostrará al usuario

    Jugada(String nombre, String mensaje) {
        this.nombre = nombre;
        this.mensaje = mensaje;
    }

    public String getNombre() {
        return nombre;
    }

    public String getMensaje() {
        return mensaje;
    }
}
