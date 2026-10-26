package blackjack_grupo22;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Clase principal de la aplicación.
 * Por ahora muestra una ventana de prueba; en el Paso 9 conectaremos
 * la Vista, el Modelo y el Controlador.
 */
public class Main extends Application {

    @Override
    public void start(Stage ventanaPrincipal) {
        Label mensaje = new Label("Blackjack - Grupo 22");
        StackPane raiz = new StackPane(mensaje);
        Scene escena = new Scene(raiz, 800, 600);

        ventanaPrincipal.setTitle("Blackjack: duelo contra el computador");
        ventanaPrincipal.setScene(escena);
        ventanaPrincipal.show();
    }
}