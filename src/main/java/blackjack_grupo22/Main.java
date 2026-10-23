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

        // A. Victoria ordinaria: jugador 18, computador 17
        System.out.println("--- A. Victoria ordinaria (18 contra 17) ---");
        Juego a = new Juego();
        a.iniciarRondaConMazo("100", new Mazo(new Carta[]{
                carta(Valor.DIEZ, Palo.CORAZONES), carta(Valor.OCHO, Palo.PICAS),
                carta(Valor.DIEZ, Palo.DIAMANTES), carta(Valor.SIETE, Palo.TREBOLES)}));
        a.plantarse();
        a.jugarTurnoComputador();
        mostrarFinal(a);

        // B. Derrota: jugador 17, computador 19
        System.out.println("--- B. Derrota (17 contra 19) ---");
        Juego b = new Juego();
        b.iniciarRondaConMazo("100", new Mazo(new Carta[]{
                carta(Valor.DIEZ, Palo.CORAZONES), carta(Valor.SIETE, Palo.PICAS),
                carta(Valor.DIEZ, Palo.DIAMANTES), carta(Valor.NUEVE, Palo.TREBOLES)}));
        b.plantarse();
        b.jugarTurnoComputador();
        mostrarFinal(b);

        // C. Empate: 18 contra 18
        System.out.println("--- C. Empate (18 contra 18) ---");
        Juego c = new Juego();
        c.iniciarRondaConMazo("100", new Mazo(new Carta[]{
                carta(Valor.DIEZ, Palo.CORAZONES), carta(Valor.OCHO, Palo.PICAS),
                carta(Valor.DIEZ, Palo.DIAMANTES), carta(Valor.OCHO, Palo.TREBOLES)}));
        c.plantarse();
        c.jugarTurnoComputador();
        mostrarFinal(c);

        // D. Victoria con Blackjack (el computador no pide aunque tenga 16)
        System.out.println("--- D. Victoria con Blackjack ---");
        Juego d = new Juego();
        d.iniciarRondaConMazo("100", new Mazo(new Carta[]{
                carta(Valor.AS, Palo.CORAZONES), carta(Valor.K, Palo.PICAS),
                carta(Valor.NUEVE, Palo.DIAMANTES), carta(Valor.SIETE, Palo.TREBOLES)}));
        d.jugarTurnoComputador();
        mostrarFinal(d);

        // E. Blackjack doble: empate
        System.out.println("--- E. Blackjack doble ---");
        Juego e = new Juego();
        e.iniciarRondaConMazo("100", new Mazo(new Carta[]{
                carta(Valor.AS, Palo.CORAZONES), carta(Valor.K, Palo.PICAS),
                carta(Valor.AS, Palo.DIAMANTES), carta(Valor.K, Palo.DIAMANTES)}));
        e.jugarTurnoComputador();
        mostrarFinal(e);

        // F. Prioridad: jugador 7+7+7 (21), computador con Blackjack
        System.out.println("--- F. Prioridad del Blackjack del computador ---");
        Juego f = new Juego();
        f.iniciarRondaConMazo("100", new Mazo(new Carta[]{
                carta(Valor.SIETE, Palo.CORAZONES), carta(Valor.SIETE, Palo.PICAS),
                carta(Valor.SIETE, Palo.TREBOLES),
                carta(Valor.AS, Palo.DIAMANTES), carta(Valor.K, Palo.DIAMANTES)}));
        f.pedirCarta();
        f.jugarTurnoComputador();
        mostrarFinal(f);

        // G. El jugador se pasa: el computador no juega
        System.out.println("--- G. El jugador se pasa ---");
        Juego g = new Juego();
        g.iniciarRondaConMazo("100", new Mazo(new Carta[]{
                carta(Valor.K, Palo.CORAZONES), carta(Valor.NUEVE, Palo.PICAS),
                carta(Valor.CINCO, Palo.TREBOLES)}));
        g.pedirCarta();
        mostrarFinal(g);

        // H. El computador pide con 16 y se pasa
        System.out.println("--- H. El computador pide con 16 y se pasa ---");
        Juego h = new Juego();
        h.iniciarRondaConMazo("100", new Mazo(new Carta[]{
                carta(Valor.DIEZ, Palo.CORAZONES), carta(Valor.OCHO, Palo.PICAS),
                carta(Valor.DIEZ, Palo.DIAMANTES), carta(Valor.SEIS, Palo.TREBOLES),
                carta(Valor.K, Palo.PICAS)}));
        h.plantarse();
        h.jugarTurnoComputador();
        mostrarFinal(h);

        // I. El computador se planta con As + 6 (17)
        System.out.println("--- I. El computador se planta con As + 6 ---");
        Juego i = new Juego();
        i.iniciarRondaConMazo("100", new Mazo(new Carta[]{
                carta(Valor.DIEZ, Palo.CORAZONES), carta(Valor.OCHO, Palo.PICAS),
                carta(Valor.AS, Palo.DIAMANTES), carta(Valor.SEIS, Palo.TREBOLES)}));
        i.plantarse();
        i.jugarTurnoComputador();
        mostrarFinal(i);

        // J. Veinte y medio (14) le gana a 19
        System.out.println("--- J. Veinte y medio contra 19 ---");
        Juego j = new Juego();
        j.iniciarRondaConMazo("100", new Mazo(new Carta[]{
                carta(Valor.K, Palo.CORAZONES), carta(Valor.CUATRO, Palo.PICAS),
                carta(Valor.DIEZ, Palo.DIAMANTES), carta(Valor.NUEVE, Palo.TREBOLES)}));
        j.plantarse();
        j.jugarTurnoComputador();
        mostrarFinal(j);

        // K. Acciones fuera de turno y repetidas
        System.out.println("--- K. Acciones fuera de turno ---");
        Juego k = new Juego();
        System.out.println("Pedir antes de la ronda: " + k.pedirCarta());
        System.out.println("Plantarse antes de la ronda: " + k.plantarse());
        System.out.println("Turno computador antes de tiempo: " + k.jugarTurnoComputador());
        k.iniciarRondaConMazo("100", new Mazo(new Carta[]{
                carta(Valor.DIEZ, Palo.CORAZONES), carta(Valor.OCHO, Palo.PICAS),
                carta(Valor.DIEZ, Palo.DIAMANTES), carta(Valor.SIETE, Palo.TREBOLES)}));
        System.out.println("Plantarse (1.ª vez): " + k.plantarse());
        System.out.println("Plantarse (2.ª vez): " + k.plantarse());
        System.out.println("Pedir carta después de plantarse: " + k.pedirCarta());
        System.out.println("Turno del computador (1.ª vez): " + k.jugarTurnoComputador());
        System.out.println("Turno del computador (2.ª vez): " + k.jugarTurnoComputador());
        System.out.println("Saldo final: " + k.getSaldo() + " (se pagó una sola vez)");

        Label mensaje = new Label("Blackjack - Grupo 22");
        StackPane raiz = new StackPane(mensaje);
        Scene escena = new Scene(raiz, 800, 600);
        ventanaPrincipal.setTitle("Blackjack: duelo contra el computador");
        ventanaPrincipal.setScene(escena);
        ventanaPrincipal.show();
    }

    // Método auxiliar temporal: escribe menos al crear cartas
    private Carta carta(Valor valor, Palo palo) {
        return new Carta(valor, palo);
    }

    // Método auxiliar temporal: imprime cómo terminó la ronda
    private void mostrarFinal(Juego juego) {
        System.out.println("Jugador: " + juego.getManoJugador() + " -> "
                + juego.getManoJugador().calcularPuntaje());
        System.out.println("Computador: " + juego.getManoComputador() + " -> "
                + juego.getManoComputador().calcularPuntaje());
        System.out.println("Estado: " + juego.getEstado()
                + " | Resultado: " + juego.getResultado().getNombre()
                + " | Pago: " + juego.getPagoUltimaRonda()
                + " | Saldo: " + juego.getSaldo());
        System.out.println("Mensaje: " + juego.getMensajeFinal());
    }
}