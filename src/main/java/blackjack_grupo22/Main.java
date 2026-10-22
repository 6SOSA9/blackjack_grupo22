
package blackjack_grupo22;

import blackjack_grupo22.modelo.Carta;
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
        Carta c1 = new Carta(Valor.AS, Palo.CORAZONES);
        Carta c2 = new Carta(Valor.K, Palo.PICAS);
        System.out.println(c1 + " -> " + c1.getPuntos() + " puntos, imagen: " + c1.getNombreImagen());
        System.out.println(c2 + " -> " + c2.getPuntos() + " puntos, imagen: " + c2.getNombreImagen());
        System.out.println("¿K es figura? " + c2.getValor().esFigura());
        System.out.println("¿Son iguales? " + c1.equals(c2));

        Label mensaje = new Label("Blackjack - Grupo 22");
        StackPane raiz = new StackPane(mensaje);
        Scene escena = new Scene(raiz, 800, 600);
        ventanaPrincipal.setTitle("Blackjack: duelo contra el computador");
        ventanaPrincipal.setScene(escena);
        ventanaPrincipal.show();
    }
}