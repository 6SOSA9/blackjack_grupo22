
package blackjack_grupo22;

import blackjack_grupo22.modelo.Carta;
import blackjack_grupo22.modelo.Mano;
import blackjack_grupo22.modelo.Palo;
import blackjack_grupo22.modelo.Valor;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Clase principal de la aplicación.
 * Hereda de Application, que es la clase base de toda aplicación JavaFX.
 * Por ahora solo muestra una ventana de prueba; más adelante aquí
 * conectaremos la Vista, el Modelo y el Controlador.
 */
public class Main extends Application {

    // JavaFX llama a este método automáticamente cuando arranca la aplicación.
    // "ventanaPrincipal" es la ventana que JavaFX crea para nosotros.
    @Override
    public void start(Stage ventanaPrincipal) {
        // Blackjack: As + K
        Mano m1 = new Mano();
        m1.agregarCarta(new Carta(Valor.AS, Palo.CORAZONES));
        m1.agregarCarta(new Carta(Valor.K, Palo.PICAS));
        mostrar("As + K", m1);

        // Blackjack: 10 + As (en otro orden)
        Mano m2 = new Mano();
        m2.agregarCarta(new Carta(Valor.DIEZ, Palo.TREBOLES));
        m2.agregarCarta(new Carta(Valor.AS, Palo.DIAMANTES));
        mostrar("10 + As", m2);

        // Veintiuno: 7 + 7 + 7 (tres cartas, no es Blackjack)
        Mano m3 = new Mano();
        m3.agregarCarta(new Carta(Valor.SIETE, Palo.CORAZONES));
        m3.agregarCarta(new Carta(Valor.SIETE, Palo.PICAS));
        m3.agregarCarta(new Carta(Valor.SIETE, Palo.TREBOLES));
        mostrar("7 + 7 + 7", m3);

        // Veintiuno: As + 5 + 5
        Mano m4 = new Mano();
        m4.agregarCarta(new Carta(Valor.AS, Palo.CORAZONES));
        m4.agregarCarta(new Carta(Valor.CINCO, Palo.PICAS));
        m4.agregarCarta(new Carta(Valor.CINCO, Palo.TREBOLES));
        mostrar("As + 5 + 5", m4);

        // Veinte y medio: K + 4
        Mano m5 = new Mano();
        m5.agregarCarta(new Carta(Valor.K, Palo.CORAZONES));
        m5.agregarCarta(new Carta(Valor.CUATRO, Palo.PICAS));
        mostrar("K + 4", m5);

        // Veinte y medio: 4 + Q (en otro orden)
        Mano m6 = new Mano();
        m6.agregarCarta(new Carta(Valor.CUATRO, Palo.DIAMANTES));
        m6.agregarCarta(new Carta(Valor.Q, Palo.TREBOLES));
        mostrar("4 + Q", m6);

        // Veinte y medio: 9 + 5 (caso de la tabla de la profesora)
        Mano m7 = new Mano();
        m7.agregarCarta(new Carta(Valor.NUEVE, Palo.CORAZONES));
        m7.agregarCarta(new Carta(Valor.CINCO, Palo.PICAS));
        mostrar("9 + 5", m7);

        // NO es Veinte y medio: suma 14 pero con tres cartas
        Mano m8 = new Mano();
        m8.agregarCarta(new Carta(Valor.NUEVE, Palo.CORAZONES));
        m8.agregarCarta(new Carta(Valor.TRES, Palo.PICAS));
        m8.agregarCarta(new Carta(Valor.DOS, Palo.TREBOLES));
        mostrar("9 + 3 + 2", m8);

        // Pasarse: K + 9 + 5
        Mano m9 = new Mano();
        m9.agregarCarta(new Carta(Valor.K, Palo.CORAZONES));
        m9.agregarCarta(new Carta(Valor.NUEVE, Palo.PICAS));
        m9.agregarCarta(new Carta(Valor.CINCO, Palo.TREBOLES));
        mostrar("K + 9 + 5", m9);

        // Pasarse con varios ases: As + As + K + K
        Mano m10 = new Mano();
        m10.agregarCarta(new Carta(Valor.AS, Palo.CORAZONES));
        m10.agregarCarta(new Carta(Valor.AS, Palo.PICAS));
        m10.agregarCarta(new Carta(Valor.K, Palo.TREBOLES));
        m10.agregarCarta(new Carta(Valor.K, Palo.DIAMANTES));
        mostrar("As + As + K + K", m10);

        // Normal: As + K + 5 (16, el As se ajusta a 1)
        Mano m11 = new Mano();
        m11.agregarCarta(new Carta(Valor.AS, Palo.CORAZONES));
        m11.agregarCarta(new Carta(Valor.K, Palo.PICAS));
        m11.agregarCarta(new Carta(Valor.CINCO, Palo.TREBOLES));
        mostrar("As + K + 5", m11);

        // Mano vacía
        mostrar("Mano vacía", new Mano());

        Label mensaje = new Label("Blackjack - Grupo 22");
        StackPane raiz = new StackPane(mensaje);
        Scene escena = new Scene(raiz, 800, 600);
        ventanaPrincipal.setTitle("Blackjack: duelo contra el computador");
        ventanaPrincipal.setScene(escena);
        ventanaPrincipal.show();
    }

    // Método auxiliar temporal: imprime el puntaje y la jugada de una mano
    private void mostrar(String titulo, Mano mano) {
        System.out.println(titulo + " -> puntaje " + mano.calcularPuntaje()
                + ", jugada: " + mano.determinarJugada().getNombre());
    }
}