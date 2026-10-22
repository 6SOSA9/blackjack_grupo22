
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
        // Caso "Un as": As + 9, y después se agrega un 5
        Mano mano1 = new Mano();
        mano1.agregarCarta(new Carta(Valor.AS, Palo.CORAZONES));
        mano1.agregarCarta(new Carta(Valor.NUEVE, Palo.PICAS));
        System.out.println("As + 9        -> " + mano1.calcularPuntaje() + " (esperado 20)");
        mano1.agregarCarta(new Carta(Valor.CINCO, Palo.TREBOLES));
        System.out.println("As + 9 + 5    -> " + mano1.calcularPuntaje() + " (esperado 15)");

        // Caso "Varios ases"
        Mano mano2 = new Mano();
        mano2.agregarCarta(new Carta(Valor.AS, Palo.CORAZONES));
        mano2.agregarCarta(new Carta(Valor.AS, Palo.PICAS));
        mano2.agregarCarta(new Carta(Valor.NUEVE, Palo.TREBOLES));
        System.out.println("As+As+9       -> " + mano2.calcularPuntaje() + " (esperado 21)");

        Mano mano3 = new Mano();
        mano3.agregarCarta(new Carta(Valor.AS, Palo.CORAZONES));
        mano3.agregarCarta(new Carta(Valor.AS, Palo.PICAS));
        mano3.agregarCarta(new Carta(Valor.K, Palo.TREBOLES));
        System.out.println("As+As+K       -> " + mano3.calcularPuntaje() + " (esperado 12)");
        mano3.agregarCarta(new Carta(Valor.K, Palo.DIAMANTES));
        System.out.println("As+As+K+K     -> " + mano3.calcularPuntaje() + " (esperado 22) pasada: " + mano3.estaPasada());

        // Otras manos de la tabla
        Mano mano4 = new Mano();
        mano4.agregarCarta(new Carta(Valor.AS, Palo.CORAZONES));
        mano4.agregarCarta(new Carta(Valor.K, Palo.PICAS));
        System.out.println("As + K        -> " + mano4.calcularPuntaje() + " (esperado 21)");

        Mano mano5 = new Mano();
        mano5.agregarCarta(new Carta(Valor.SIETE, Palo.CORAZONES));
        mano5.agregarCarta(new Carta(Valor.SIETE, Palo.PICAS));
        mano5.agregarCarta(new Carta(Valor.SIETE, Palo.TREBOLES));
        System.out.println("7 + 7 + 7     -> " + mano5.calcularPuntaje() + " (esperado 21)");

        Mano mano6 = new Mano();
        mano6.agregarCarta(new Carta(Valor.NUEVE, Palo.CORAZONES));
        mano6.agregarCarta(new Carta(Valor.CINCO, Palo.PICAS));
        System.out.println("9 + 5         -> " + mano6.calcularPuntaje() + " (esperado 14)");

        // Mano vacía y recorrido de la lista
        Mano vacia = new Mano();
        System.out.println("Mano vacía    -> " + vacia.calcularPuntaje() + " (esperado 0)");
        System.out.println("Mano 3 cartas: " + mano3 + " | cantidad: " + mano3.getCantidad());
        System.out.println("Segunda carta de mano3: " + mano3.obtenerCarta(1));

        Label mensaje = new Label("Blackjack - Grupo 22");
        StackPane raiz = new StackPane(mensaje);
        Scene escena = new Scene(raiz, 800, 600);
        ventanaPrincipal.setTitle("Blackjack: duelo contra el computador");
        ventanaPrincipal.setScene(escena);
        ventanaPrincipal.show();
    }
}