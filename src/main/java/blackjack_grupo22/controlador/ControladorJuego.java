package blackjack_grupo22.controlador;

import blackjack_grupo22.modelo.EstadoRonda;
import blackjack_grupo22.modelo.Jugada;
import blackjack_grupo22.modelo.Juego;
import blackjack_grupo22.modelo.Mano;
import blackjack_grupo22.vista.VistaJuego;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * Controlador: atiende los eventos de la Vista, le pide operaciones al
 * Modelo y vuelve a dibujar la Vista con el estado que el Modelo reporta.
 * No contiene reglas del juego.
 */
public class ControladorJuego {

    private final Juego juego;
    private final VistaJuego vista;
    private final Stage ventana;

    public ControladorJuego(Juego juego, VistaJuego vista, Stage ventana) {
        this.juego = juego;
        this.vista = vista;
        this.ventana = ventana;
        conectarEventos();
    }

    /**
     * Deja la pantalla en el estado inicial (HU01).
     */
    public void iniciar() {
        actualizarVista();
    }

    /**
     * Asocia cada botón con el método que lo atiende.
     * "evento -> metodo()" significa: cuando ocurra el evento, ejecuta ese método.
     */
    private void conectarEventos() {
        vista.getBtnIniciar().setOnAction(evento -> alIniciarRonda());
        vista.getBtnPedir().setOnAction(evento -> alPedirCarta());
        vista.getBtnPlantarse().setOnAction(evento -> alPlantarse());
        vista.getBtnReglas().setOnAction(evento -> alVerReglas());
        vista.getBtnCerrar().setOnAction(evento -> alCerrarJuego());

        // La X de la ventana también pide confirmación (HU12).
        // consume() cancela el cierre cuando el usuario no confirma.
        ventana.setOnCloseRequest(evento -> {
            if (!confirmarCierre()) {
                evento.consume();
            }
        });
    }

    // ------------------------------------------------------------
    // Acciones del jugador
    // ------------------------------------------------------------

    private void alIniciarRonda() {
        String error = juego.iniciarRonda(vista.getTextoApuesta());
        if (error != null) {
            // Apuesta rechazada: se explica el problema y nada más cambia
            vista.mostrarMensaje(error);
            return;
        }
        vista.limpiarCampoApuesta();
        jugarComputadorSiCorresponde();
        actualizarVista();
    }

    private void alPedirCarta() {
        String error = juego.pedirCarta();
        if (error != null) {
            vista.mostrarMensaje(error);
            return;
        }
        jugarComputadorSiCorresponde();
        actualizarVista();
    }

    private void alPlantarse() {
        String error = juego.plantarse();
        if (error != null) {
            vista.mostrarMensaje(error);
            return;
        }
        jugarComputadorSiCorresponde();
        actualizarVista();
    }

    /**
     * Si el jugador terminó su turno (se plantó o llegó a 21), el computador
     * juega automáticamente, sin clics adicionales (HU06, 6.6).
     */
    private void jugarComputadorSiCorresponde() {
        if (juego.getEstado() == EstadoRonda.TURNO_COMPUTADOR) {
            juego.jugarTurnoComputador();
        }
    }

    // ------------------------------------------------------------
    // Reglas y cierre
    // ------------------------------------------------------------

    /**
     * Muestra las reglas en una ventana modal. Mientras está abierta no se
     * puede tocar la ventana principal, y al cerrarla no cambia nada del
     * modelo: la partida se conserva (HU11).
     */
    private void alVerReglas() {
        TextArea texto = new TextArea(textoDeReglas());
        texto.setEditable(false);
        texto.setWrapText(true);
        texto.setPrefSize(560, 420);

        Alert ventanaReglas = new Alert(Alert.AlertType.INFORMATION);
        ventanaReglas.initOwner(ventana);
        ventanaReglas.setTitle("Reglas del juego");
        ventanaReglas.setHeaderText("Blackjack: duelo contra el computador");
        ventanaReglas.getDialogPane().setContent(texto);
        ventanaReglas.showAndWait();
    }

    private void alCerrarJuego() {
        if (confirmarCierre()) {
            Platform.exit();
        }
    }

    /**
     * Pregunta si realmente se quiere cerrar. Devuelve true solo si el
     * usuario confirma. Si cancela, el juego sigue exactamente igual.
     */
    private boolean confirmarCierre() {
        ButtonType botonCerrar = new ButtonType("Cerrar juego", ButtonBar.ButtonData.OK_DONE);
        ButtonType botonCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.initOwner(ventana);
        alerta.setTitle("Cerrar juego");
        alerta.setHeaderText("¿Quieres cerrar el juego?");
        alerta.setContentText("Se perderá la partida actual.");
        alerta.getButtonTypes().setAll(botonCerrar, botonCancelar);

        Optional<ButtonType> respuesta = alerta.showAndWait();
        if (respuesta.isPresent() && respuesta.get() == botonCerrar) {
            return true;
        }
        return false;
    }

    private String textoDeReglas() {
        return "OBJETIVO\n"
                + "Obtener un puntaje mayor que el computador sin superar 21. Se usa un mazo de 52 cartas "
                + "mezclado en cada ronda.\n\n"
                + "VALORES\n"
                + "Del 2 al 10 valen su número. J, Q y K valen 10. Los palos no cambian el puntaje.\n\n"
                + "ASES\n"
                + "Valen 11 u 1, lo que dé el mayor puntaje sin pasarse de 21. Si de ninguna forma se evita "
                + "pasarse, todos valen 1.\n\n"
                + "APUESTAS\n"
                + "Empiezas con 1.000 fichas. La apuesta debe ser un número entero, múltiplo de 10, de al menos 10 "
                + "y sin superar tu saldo. Se descuenta al iniciar la ronda y no cambia durante ella.\n\n"
                + "PAGOS (fichas que se acreditan al terminar)\n"
                + "Derrota: nada. Empate: se devuelve la apuesta. Victoria: se devuelve la apuesta y ganas otra "
                + "igual (2 veces la apuesta). Victoria con Blackjack: 2,5 veces la apuesta.\n\n"
                + "JUGADAS\n"
                + "Blackjack: un As y una carta de 10 (10, J, Q o K) en las dos cartas iniciales. Un 21 de tres o "
                + "más cartas no es Blackjack.\n"
                + "Veintiuno: 21 puntos sin ser Blackjack.\n"
                + "Veinte y medio: las dos primeras cartas son una figura (J, Q o K) y un 4, en cualquier orden. "
                + "Suma 14 y aún puedes pedir o plantarte. No paga extra.\n"
                + "Pasarse: más de 21 puntos después de ajustar los ases. Pierdes.\n\n"
                + "PRIORIDADES\n"
                + "Blackjack gana a toda mano que no sea Blackjack, incluso a un 21 de tres cartas. Dos Blackjack "
                + "empatan.\n"
                + "Veinte y medio gana a cualquier mano de 20 puntos o menos, pierde ante cualquier 21 (incluido "
                + "Blackjack) y empata con otro Veinte y medio.\n"
                + "Si no hay jugadas especiales, gana el mayor puntaje. Con puntajes iguales hay empate.\n\n"
                + "COMPUTADOR\n"
                + "Recibe dos cartas cuando te plantas o llegas a 21. Pide mientras tenga menos de 17 y se planta "
                + "con 17 o más (incluido 17 con un As contado como 11). Si tiene Veinte y medio, se planta. "
                + "Si supera 21, pierde. Sus decisiones dependen solo de su propia mano.\n\n"
                + "TURNOS\n"
                + "Si te pasas, pierdes de inmediato y el computador no juega. Si llegas a 21, tu turno termina "
                + "automáticamente.";
    }

    // ------------------------------------------------------------
    // Actualización de la Vista
    // ------------------------------------------------------------

    /**
     * Lee el estado del Modelo y dibuja la pantalla completa para que
     * siempre corresponda con él.
     */
    private void actualizarVista() {
        EstadoRonda estado = juego.getEstado();

        // Saldo y apuesta
        vista.mostrarSaldo(juego.getSaldo());
        if (estado == EstadoRonda.SIN_RONDA) {
            vista.mostrarApuesta("ninguna");
        } else {
            vista.mostrarApuesta(juego.getApuesta() + " fichas");
        }

        // Manos y puntajes
        Mano manoJugador = juego.getManoJugador();
        Mano manoComputador = juego.getManoComputador();
        vista.mostrarManoJugador(manoJugador, textoPuntaje(manoJugador));
        vista.mostrarManoComputador(manoComputador, textoPuntaje(manoComputador));

        // Botones habilitados según el estado
        boolean puedeJugar = (estado == EstadoRonda.TURNO_JUGADOR);
        boolean puedeApostar = !juego.rondaActiva() && juego.hayFichasSuficientes();
        vista.actualizarControles(puedeApostar, puedeJugar);

        // Mensajes
        if (estado == EstadoRonda.SIN_RONDA) {
            vista.mostrarMensaje("Ingresa una apuesta (múltiplo de 10, mínimo 10) y pulsa «Iniciar ronda».");
            vista.mostrarResultado("");
        } else if (estado == EstadoRonda.TURNO_JUGADOR) {
            String mensaje = "Tu turno: pide carta o plántate.";
            Jugada jugada = juego.getJugadaJugador();
            if (jugada == Jugada.VEINTE_Y_MEDIO) {
                mensaje = mensaje + "\n" + jugada.getMensaje();
            }
            vista.mostrarMensaje(mensaje);
            vista.mostrarResultado("");
        } else if (estado == EstadoRonda.TURNO_COMPUTADOR) {
            vista.mostrarMensaje("Turno del computador...");
            vista.mostrarResultado("");
        } else {
            mostrarFinDeRonda();
        }
    }

    /**
     * Mensajes al terminar la ronda: resultado, causa, pago y si se puede seguir.
     */
    private void mostrarFinDeRonda() {
        String resultado = juego.getResultado().getNombre().toUpperCase()
                + ": " + juego.getMensajeFinal()
                + "\nFichas acreditadas: " + juego.getPagoUltimaRonda();
        vista.mostrarResultado(resultado);

        if (juego.hayFichasSuficientes()) {
            vista.mostrarMensaje("Ronda terminada. Ingresa una nueva apuesta para seguir jugando.");
        } else {
            vista.mostrarMensaje("Tu saldo es menor de 10 fichas: no es posible continuar apostando.");
        }
    }

    /**
     * Texto del puntaje de una mano: "-" si está vacía; si no, el número
     * y, cuando hay una jugada especial, su nombre.
     */
    private String textoPuntaje(Mano mano) {
        if (mano.estaVacia()) {
            return "-";
        }
        String texto = String.valueOf(mano.calcularPuntaje());
        Jugada jugada = mano.determinarJugada();
        if (jugada != Jugada.NORMAL) {
            texto = texto + " (" + jugada.getNombre() + ")";
        }
        return texto;
    }
}