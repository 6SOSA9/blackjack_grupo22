
package blackjack_grupo22;

import javafx.application.Application;

/**
 * Clase de arranque.
 * Existe solo porque, cuando el proyecto no usa módulos de Java,
 * IntelliJ muestra el error "faltan componentes de JavaFX" si se ejecuta
 * directamente una clase que hereda de Application.
 * Esta clase NO hereda de Application, así que evita ese problema
 * y simplemente le pide a JavaFX que inicie Main.
 */
public class Launcher {

    static void main(String[] args) {
        Application.launch(Main.class, args);
    }
}