package blackjack_grupo22.vista;

import blackjack_grupo22.modelo.Carta;
import blackjack_grupo22.modelo.Mano;
import blackjack_grupo22.modelo.Palo;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.InputStream;

/**
 * Construye la interfaz gráfica con código (sin archivos FXML).
 * Solo presenta información y recoge las acciones del usuario.
 */
public class VistaJuego {

    private static final double ANCHO_CARTA = 80;
    private static final double ALTO_CARTA = 112;

    private final BorderPane raiz;

    private final Label lblSaldo;
    private final Label lblApuesta;
    private final Label lblPuntajeJugador;
    private final Label lblPuntajeComputador;
    private final Label lblMensaje;
    private final Label lblResultado;

    private final HBox cartasJugador;
    private final HBox cartasComputador;

    private final TextField txtApuesta;
    private final Button btnIniciar;
    private final Button btnPedir;
    private final Button btnPlantarse;
    private final Button btnReglas;
    private final Button btnCerrar;

    public VistaJuego() {
        // ---------- Parte superior: título, saldo y apuesta ----------
        Label titulo = new Label("BLACKJACK");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titulo.setTextFill(Color.WHITE);

        lblSaldo = crearEtiquetaInfo();
        lblApuesta = crearEtiquetaInfo();

        HBox barraInfo = new HBox(40, lblSaldo, lblApuesta);
        barraInfo.setAlignment(Pos.CENTER);

        VBox superior = new VBox(8, titulo, barraInfo);
        superior.setAlignment(Pos.CENTER);
        superior.setPadding(new Insets(15));

        // ---------- Centro: zona del computador y zona del jugador ----------
        lblPuntajeComputador = crearEtiquetaPuntaje();
        cartasComputador = crearContenedorCartas();
        VBox zonaComputador = crearZona("COMPUTADOR", lblPuntajeComputador, cartasComputador);

        lblPuntajeJugador = crearEtiquetaPuntaje();
        cartasJugador = crearContenedorCartas();
        VBox zonaJugador = crearZona("JUGADOR", lblPuntajeJugador, cartasJugador);

        VBox centro = new VBox(20, zonaComputador, zonaJugador);
        centro.setPadding(new Insets(10, 30, 10, 30));

        // ---------- Parte inferior: mensajes y controles ----------
        lblMensaje = new Label();
        lblMensaje.setFont(Font.font("Arial", 16));
        lblMensaje.setTextFill(Color.WHITE);
        lblMensaje.setWrapText(true);

        lblResultado = new Label();
        lblResultado.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        lblResultado.setTextFill(Color.GOLD);
        lblResultado.setWrapText(true);

        Label etiquetaApuesta = new Label("Apuesta:");
        etiquetaApuesta.setTextFill(Color.WHITE);
        txtApuesta = new TextField();
        txtApuesta.setPromptText("Ej: 100");
        txtApuesta.setPrefWidth(100);

        btnIniciar = new Button("Iniciar ronda");
        btnPedir = new Button("Pedir carta");
        btnPlantarse = new Button("Plantarse");
        btnReglas = new Button("Ver reglas");
        btnCerrar = new Button("Cerrar juego");

        HBox barraControles = new HBox(12, etiquetaApuesta, txtApuesta, btnIniciar,
                btnPedir, btnPlantarse, btnReglas, btnCerrar);
        barraControles.setAlignment(Pos.CENTER);

        VBox inferior = new VBox(10, lblMensaje, lblResultado, barraControles);
        inferior.setPadding(new Insets(15, 30, 20, 30));
        inferior.setAlignment(Pos.CENTER);

        // ---------- Raíz: fondo verde de mesa de juego ----------
        raiz = new BorderPane();
        raiz.setStyle("-fx-background-color: #0b6623;");
        raiz.setTop(superior);
        raiz.setCenter(centro);
        raiz.setBottom(inferior);
    }

    // ------------------------------------------------------------
    // Métodos auxiliares de construcción
    // ------------------------------------------------------------

    private Label crearEtiquetaInfo() {
        Label etiqueta = new Label();
        etiqueta.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        etiqueta.setTextFill(Color.WHITE);
        return etiqueta;
    }

    private Label crearEtiquetaPuntaje() {
        Label etiqueta = new Label();
        etiqueta.setFont(Font.font("Arial", 15));
        etiqueta.setTextFill(Color.WHITE);
        return etiqueta;
    }

    private HBox crearContenedorCartas() {
        HBox contenedor = new HBox(8);
        contenedor.setAlignment(Pos.CENTER_LEFT);
        contenedor.setMinHeight(ALTO_CARTA + 10);
        return contenedor;
    }

    private VBox crearZona(String nombre, Label puntaje, HBox cartas) {
        Label titulo = new Label(nombre);
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.WHITE);

        VBox zona = new VBox(6, titulo, puntaje, cartas);
        zona.setPadding(new Insets(10));
        zona.setStyle("-fx-border-color: white; -fx-border-radius: 8; -fx-background-color: rgba(0,0,0,0.15); -fx-background-radius: 8;");
        return zona;
    }

    // ------------------------------------------------------------
    // Dibujo de las cartas
    // ------------------------------------------------------------

    /**
     * Crea el elemento gráfico de una carta. Primero intenta cargar su
     * imagen desde resources/cartas; si el archivo no existe, la dibuja.
     * La imagen se coloca sobre un fondo blanco con borde, para que las
     * partes transparentes del PNG no dejen ver la mesa verde.
     */
    private Node crearImagenCarta(Carta carta) {
        String ruta = "/cartas/" + carta.getNombreImagen();
        InputStream flujo = getClass().getResourceAsStream(ruta);

        if (flujo != null) {
            Image imagen = new Image(flujo);
            ImageView vistaImagen = new ImageView(imagen);
            vistaImagen.setFitWidth(ANCHO_CARTA);
            vistaImagen.setPreserveRatio(true);
            vistaImagen.setSmooth(true);

            // Fondo blanco con esquinas redondeadas, del tamaño de la carta
            double altoImagen = ANCHO_CARTA * imagen.getHeight() / imagen.getWidth();
            Rectangle fondo = new Rectangle(ANCHO_CARTA, altoImagen);
            fondo.setArcWidth(12);
            fondo.setArcHeight(12);
            fondo.setFill(Color.WHITE);
            fondo.setStroke(Color.BLACK);

            return new StackPane(fondo, vistaImagen);
        }
        return crearCartaDibujada(carta);
    }

    /**
     * Carta de respaldo: rectángulo blanco con el valor y el símbolo del palo.
     */
    private Node crearCartaDibujada(Carta carta) {
        Rectangle fondo = new Rectangle(ANCHO_CARTA, ALTO_CARTA);
        fondo.setArcWidth(12);
        fondo.setArcHeight(12);
        fondo.setFill(Color.WHITE);
        fondo.setStroke(Color.BLACK);

        Label texto = new Label(carta.getValor().getNombre() + "\n" + simboloDelPalo(carta.getPalo()));
        texto.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        texto.setStyle("-fx-text-alignment: center;");
        if (carta.getPalo() == Palo.CORAZONES || carta.getPalo() == Palo.DIAMANTES) {
            texto.setTextFill(Color.RED);
        } else {
            texto.setTextFill(Color.BLACK);
        }

        return new StackPane(fondo, texto);
    }

    private String simboloDelPalo(Palo palo) {
        if (palo == Palo.CORAZONES) {
            return "♥";
        }
        if (palo == Palo.DIAMANTES) {
            return "♦";
        }
        if (palo == Palo.TREBOLES) {
            return "♣";
        }
        return "♠";
    }

    // ------------------------------------------------------------
    // Métodos que el Controlador usa para actualizar la pantalla
    // ------------------------------------------------------------

    public void mostrarSaldo(int saldo) {
        lblSaldo.setText("Saldo: " + saldo + " fichas");
    }

    public void mostrarApuesta(String texto) {
        lblApuesta.setText("Apuesta vigente: " + texto);
    }

    public void mostrarManoJugador(Mano mano, String puntaje) {
        mostrarMano(cartasJugador, mano, "Sin cartas");
        lblPuntajeJugador.setText("Puntaje: " + puntaje);
    }

    public void mostrarManoComputador(Mano mano, String puntaje) {
        mostrarMano(cartasComputador, mano, "El computador espera su turno");
        lblPuntajeComputador.setText("Puntaje: " + puntaje);
    }

    private void mostrarMano(HBox contenedor, Mano mano, String textoVacio) {
        contenedor.getChildren().clear();

        if (mano.estaVacia()) {
            Label aviso = new Label(textoVacio);
            aviso.setTextFill(Color.LIGHTGRAY);
            contenedor.getChildren().add(aviso);
            return;
        }

        for (int i = 0; i < mano.getCantidad(); i++) {
            contenedor.getChildren().add(crearImagenCarta(mano.obtenerCarta(i)));
        }
    }

    public void mostrarMensaje(String texto) {
        lblMensaje.setText(texto);
    }

    public void mostrarResultado(String texto) {
        lblResultado.setText(texto);
    }

    /**
     * Habilita o deshabilita los controles según el estado del juego.
     * El campo de apuesta y "Iniciar ronda" van juntos; "Pedir carta" y
     * "Plantarse" también. "Ver reglas" y "Cerrar juego" siempre están activos.
     */
    public void actualizarControles(boolean puedeApostar, boolean puedeJugar) {
        txtApuesta.setDisable(!puedeApostar);
        btnIniciar.setDisable(!puedeApostar);
        btnPedir.setDisable(!puedeJugar);
        btnPlantarse.setDisable(!puedeJugar);
    }

    public void limpiarCampoApuesta() {
        txtApuesta.clear();
    }

    // ------------------------------------------------------------
    // Acceso a los elementos que recogen acciones
    // ------------------------------------------------------------

    public BorderPane getRaiz() {
        return raiz;
    }

    public String getTextoApuesta() {
        return txtApuesta.getText();
    }

    public Button getBtnIniciar() {
        return btnIniciar;
    }

    public Button getBtnPedir() {
        return btnPedir;
    }

    public Button getBtnPlantarse() {
        return btnPlantarse;
    }

    public Button getBtnReglas() {
        return btnReglas;
    }

    public Button getBtnCerrar() {
        return btnCerrar;
    }
}