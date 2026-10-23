package blackjack_grupo22;

import blackjack_grupo22.modelo.Carta;
import blackjack_grupo22.modelo.Juego;
import blackjack_grupo22.modelo.Mazo;
import blackjack_grupo22.modelo.Palo;
import blackjack_grupo22.modelo.Valor;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage ventanaPrincipal) {

        // 1. Validación de apuestas con saldo 1000
        System.out.println("--- Prueba de apuestas ---");
        Juego juego = new Juego();
        String[] pruebas = {"", "hola", "15", "0", "-10", "10,5", "10.5", "1010", "5", "  100  "};
        for (int i = 0; i < pruebas.length; i++) {
            String error = juego.validarApuesta(pruebas[i]);
            if (error == null) {
                System.out.println("\"" + pruebas[i] + "\" -> VÁLIDA");
            } else {
                System.out.println("\"" + pruebas[i] + "\" -> " + error);
            }
        }

        // 2. Ronda con Blackjack inicial (mazo controlado: As + K)
        System.out.println("--- Ronda con Blackjack inicial ---");
        Mazo mazoBlackjack = new Mazo(new Carta[]{
                new Carta(Valor.AS, Palo.CORAZONES),
                new Carta(Valor.K, Palo.PICAS)
        });
        String errorInicio = juego.iniciarRondaConMazo("100", mazoBlackjack);
        System.out.println("Error al iniciar: " + errorInicio);
        mostrarEstado(juego);
        System.out.println("Cartas en el mazo: " + juego.cartasRestantesEnMazo());
        System.out.println("Segundo intento: " + juego.iniciarRonda("100"));
        System.out.println("Saldo tras segundo intento: " + juego.getSaldo());

        // 3. Ronda con Veinte y medio (mazo controlado: K + 4)
        System.out.println("--- Ronda con Veinte y medio ---");
        Juego juego2 = new Juego();
        Mazo mazoVeinteYMedio = new Mazo(new Carta[]{
                new Carta(Valor.K, Palo.CORAZONES),
                new Carta(Valor.CUATRO, Palo.PICAS)
        });
        juego2.iniciarRondaConMazo("100", mazoVeinteYMedio);
        mostrarEstado(juego2);

        // 4. Una apuesta inválida no inicia la ronda
        System.out.println("--- Apuesta inválida no inicia ---");
        Juego juego3 = new Juego();
        System.out.println("Error: " + juego3.iniciarRonda("15"));
        mostrarEstado(juego3);

        // 5. Apuesta igual al saldo
        System.out.println("--- Apuesta igual al saldo ---");
        Juego juego4 = new Juego();
        juego4.iniciarRonda("1000");
        System.out.println("Saldo: " + juego4.getSaldo() + " | Apuesta: " + juego4.getApuesta());

        Label mensaje = new Label("Blackjack - Grupo 22");
        StackPane raiz = new StackPane(mensaje);
        Scene escena = new Scene(raiz, 800, 600);
        ventanaPrincipal.setTitle("Blackjack: duelo contra el computador");
        ventanaPrincipal.setScene(escena);
        ventanaPrincipal.show();
    }

    // Método auxiliar temporal: imprime el estado del juego
    private void mostrarEstado(Juego juego) {
        System.out.println("Saldo: " + juego.getSaldo() + " | Apuesta: " + juego.getApuesta()
                + " | Estado: " + juego.getEstado());
        System.out.println("Jugador: " + juego.getManoJugador() + " -> "
                + juego.getManoJugador().calcularPuntaje()
                + " (" + juego.getJugadaJugador().getNombre() + ")");
        System.out.println("Computador: " + juego.getManoComputador());
    }
}