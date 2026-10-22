
package blackjack_grupo22;

import blackjack_grupo22.modelo.Mazo;
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
        Mazo mazo = new Mazo();
        System.out.println("Cartas en el mazo: " + mazo.cartasRestantes());

// Repartimos las 52 cartas y verificamos que ninguna se repita
        Carta[] repartidas = new Carta[52];
        int repetidas = 0;
        for (int i = 0; i < 52; i++) {
            repartidas[i] = mazo.repartir();
            // Comparamos la carta nueva con todas las anteriores
            for (int k = 0; k < i; k++) {
                if (repartidas[i].equals(repartidas[k])) {
                    repetidas++;
                }
            }
        }
        System.out.println("Cartas repartidas: 52, repetidas encontradas: " + repetidas);
        System.out.println("Cartas que quedan: " + mazo.cartasRestantes());
        System.out.println("Primeras 3 cartas: " + repartidas[0] + ", " + repartidas[1] + ", " + repartidas[2]);

        Label mensaje = new Label("Blackjack - Grupo 22");
        StackPane raiz = new StackPane(mensaje);
        Scene escena = new Scene(raiz, 800, 600);
        ventanaPrincipal.setTitle("Blackjack: duelo contra el computador");
        ventanaPrincipal.setScene(escena);
        ventanaPrincipal.show();
    }
}