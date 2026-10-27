package blackjack_grupo22;

import javafx.scene.image.Image;
import java.io.InputStream;
import blackjack_grupo22.controlador.ControladorJuego;
import blackjack_grupo22.modelo.Juego;
import blackjack_grupo22.vista.VistaJuego;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Clase principal. Aquí se crean las tres partes del patrón MVC
 * y se conectan entre sí:
 *   - Juego (Modelo): las reglas y los datos.
 *   - VistaJuego (Vista): lo que se ve en pantalla.
 *   - ControladorJuego (Controlador): conecta botones con el modelo.
 */
public class Main extends Application {

    @Override
    public void start(Stage ventanaPrincipal) {

        Juego modelo = new Juego();
        VistaJuego vista = new VistaJuego();
        ControladorJuego controlador = new ControladorJuego(modelo, vista, ventanaPrincipal);

        Scene escena = new Scene(vista.getRaiz(), 1000, 720);
        ventanaPrincipal.setTitle("Blackjack: duelo contra el computador");
        ventanaPrincipal.setScene(escena);

        // Deja la pantalla en su estado inicial (HU01) antes de mostrarla
        controlador.iniciar();
        ventanaPrincipal.show();

        InputStream flujoIcono = getClass().getResourceAsStream("/logo.png");
        if (flujoIcono != null) {
            ventanaPrincipal.getIcons().add(new Image(flujoIcono));
        }
    }
}