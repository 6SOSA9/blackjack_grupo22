# Blackjack - Duelo contra el computador

Mini proyecto 1 de FPOE (750014C).
Juego de 21 Blackjack hecho en Java y JavaFX, con patrón MVC.

## Tecnologías
- Java 27
- JavaFX 25
- Maven
- JUnit 5

## Cómo ejecutar
- Desde IntelliJ IDEA: ejecutar la clase `blackjack_grupo22.Launcher`.
- Desde terminal (con Maven instalado): `mvn javafx:run`.
- Pruebas: clic derecho sobre `src/test/java` → *Run 'All Tests'* (o `mvn test`).

## Integrantes
- LESDNY YAIR MORENO 202536905

## Estructura
src/main/java/blackjack_grupo22/
├── modelo/ Palo, Valor, Carta, Nodo, Mano, Mazo, Jugada, Resultado, EstadoRonda, Juego
├── vista/ VistaJuego
├── controlador/ ControladorJuego
├── Main.java
└── Launcher.java
src/main/resources/cartas/ 52 imágenes PNG
src/test/java/ pruebas JUnit del modelo
docs/ UML, casos de prueba y evidencias

## Decisiones de diseño
- **MVC:** el modelo no conoce JavaFX; la vista solo muestra; el controlador conecta eventos con el modelo.
- **Mazo:** arreglo de 52 posiciones, barajado con Fisher-Yates. Se reparte avanzando una posición, por lo que no hay cartas repetidas.
- **Mano:** lista enlazada propia (`Nodo` y `Mano`), sin `LinkedList`.
- **Estados:** `EstadoRonda` impide acciones fuera de turno y pagos repetidos.

## Reglas aclaradas con la profesora
- Veinte y medio: solo las dos primeras cartas, figura (J, Q, K) y un 4.
- El computador se planta con Veinte y medio.
- Blackjack: un As y una carta de valor 10 (10, J, Q o K).

## Documentación
- [Diagrama de clases](docs/diagrama_clases.md)
- [Casos de prueba](docs/casos_de_prueba.md)


## Avance del proyecto
- [x] Paso 1: configuración del proyecto
- [x] Paso 2: Palo, Valor y Carta
- [x] Paso 3: Mazo (arreglo de 52, barajar y repartir)
- [x] Paso 4: Nodo y Mano (lista enlazada, puntaje y ases)
- [x] Paso 5: Jugadas (Blackjack, Veintiuno, Veinte y medio, Pasarse)
- [x] Paso 6: Juego (apuestas, saldo, validación e inicio de ronda)
- [x] Paso 7: Juego (pedir, plantarse, computador, resultado y liquidación)
- [x] Paso 8: Pruebas JUnit del modelo
- [x] Paso 9: Vista, Controlador e imágenes de cartas
- [x] Paso 10: UML, casos de prueba y revisión final
- [x] Paso adicional: un logo =)