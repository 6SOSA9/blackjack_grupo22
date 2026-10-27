# Casos de prueba

Todas las pruebas están en `src/test/java/blackjack_grupo22/modelo/` y se ejecutan con JUnit 5.
Resultado obtenido: todas aprobadas (ver `docs/pruebas_junit.png`).

| Caso | Entrada / acción | Resultado esperado | Obtenido | Prueba |
|---|---|---|---|---|
| Apuestas incorrectas | vacío, texto, 15, 0, -10, 10,5, 5, 1010 | Rechazo explicado; sin reparto ni descuento | Aprobó | `apuestasIncorrectasSonRechazadasConExplicacion` |
| Límite | Saldo 1000, apuesta 1000 | Se acepta; saldo 0 durante la ronda | Aprobó | `apuestaIgualAlSaldoEsValidaYDejaElSaldoEnCero` |
| Un as | As + 9; luego + 5 | 20 y luego 15 | Aprobó | `unAsSeAjustaSegunLasDemasCartas` |
| Varios ases | As+As+9; As+As+K; As+As+K+K | 21, 12 y 22 (solo la última se pasa) | Aprobó | `variosAsesSeAjustanUnoPorUno` |
| Blackjack | As + K (cualquier orden) | Blackjack | Aprobó | `blackjackSeReconoceEnCualquierOrden` |
| Veintiuno | 7 + 7 + 7 | Veintiuno, no Blackjack | Aprobó | `veintiunoDeTresCartasNoEsBlackjack` |
| Veinte y medio | K + 4, 4 + Q | Veinte y medio (14) | Aprobó | `veinteYMedioEsFiguraYCuatroEnCualquierOrden` |
| No es veinte y medio | 9 + 5, 10 + 4, K + 2 + 2 | Normal | Aprobó | `otrasManosQueSuman14NoSonVeinteYMedio` |
| Prioridad | Jugador 7+7+7; computador As + 10 | Gana el computador | Aprobó | `blackjackDelComputadorGanaAUnVeintiunoDeTresCartas` |
| Blackjack doble | Ambos As + K | Empate; se devuelve la apuesta | Aprobó | `blackjackDobleEsEmpate` |
| Jugador se pasa | K + 9 + 5 | Derrota; computador sin cartas | Aprobó | `jugadorQueSePasaPierdeYElComputadorNoJuega` |
| Umbral computador | 16 / 10+7 / As+6 | Con 16 pide; con 17 se planta | Aprobó | `computadorPideConDieciseisYSePasa`, `computadorSePlantaConDiecisiete`, `computadorSePlantaConAsMasSeisQueCuentaComoDiecisiete` |
| Veinte y medio del computador | K + 4 | Se planta | Aprobó | `computadorSePlantaConVeinteYMedio` |
| Comparación | 18 vs 18; 20 vs 22 | Empate; victoria | Aprobó | `dosManosDeDieciochoSinBlackjackEmpatan`, `veinteDelJugadorContraVeintidosDelComputadorEsVictoria` |
| Pagos (apuesta 100) | Los cuatro resultados | 900, 1000, 1100, 1150 | Aprobó | `JuegoRondaTest` (pagos) |
| Un solo pago | Repetir acciones al terminar | Saldo no cambia | Aprobó | `elSaldoSeActualizaUnaSolaVezPorRonda` |
| Fuera de turno | Pedir/plantarse sin ronda o dos veces | Rechazado; nada cambia | Aprobó | `accionesAntesDeLaRondaSonRechazadasYNoCambianNada`, `plantarseDosVecesHaceJugarAlComputadorUnaSolaVez` |
| Sin repetidas | 52 cartas; 300 rondas | Ninguna carta repetida | Aprobó | `repartirLas52CartasNuncaRepiteUnaCarta`, `nuncaHayCartasRepetidasEntreLasDosManos` |
| Continuidad | Terminar e iniciar otra ronda | Saldo conservado; mazo y manos nuevos | Aprobó | `nuevaRondaConservaElSaldoYRenuevaManosYMazo` |

## Pruebas manuales de la interfaz (HU11 y HU12)

| Acción | Resultado esperado | Obtenido |
|---|---|---|
| Ver reglas durante una ronda | La ventana se abre; al cerrarla todo sigue igual | Aprobado |
| Cerrar juego → Cancelar (antes de apostar) | Continúa abierto, nada cambia | Aprobado |
| Cerrar juego → Cancelar (turno del jugador) | Continúa abierto, nada cambia | Aprobado |
| Cerrar juego → Cancelar (tras un resultado) | Continúa abierto, nada cambia | Aprobado |
| X de la ventana → Cancelar | Continúa abierto | Aprobado |
| Confirmar cierre | La aplicación termina | Aprobado |
| Perder todo el saldo | Mensaje de que no se puede seguir; Reglas y Cerrar siguen activos | Aprobado |