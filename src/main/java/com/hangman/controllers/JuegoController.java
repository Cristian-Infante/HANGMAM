package com.hangman.controllers;

import com.hangman.services.Juego;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

@RestController
@RequestMapping("/Game")
public class JuegoController {
    private static final int MAX_JUGADORES = 5;
    private static ArrayList<Juego> juegos = new ArrayList<>();
    private static Timer temporizador;
    private static int indiceJugadorActual = 0;
    private static boolean juegoIniciado = false;

    private static long tiempoInicio;
    private static String ganador;
    private final int tiempoEspera = 30000;

    @PostMapping("/login")
    public String iniciarJuego(@RequestParam String nombreJugador) {
        // Verificar si el nombre de jugador ya está en uso
        if (juegos.stream().anyMatch(juego -> juego.getJugador().equals(nombreJugador))) {
            return "¡Lo siento! El nombre de jugador ya está en uso. Por favor, elige otro.";
        }

        if (!juegoIniciado && juegos.size() < MAX_JUGADORES) {
            if (juegos.isEmpty()) {
                // Iniciar temporizador si es el primer jugador
                iniciarTemporizador();
            }

            Juego juego = new Juego(nombreJugador);
            juegos.add(juego);

            System.out.println("Se ha conectado un nuevo jugador: " + juego.getJugador());

            if (juegos.size() == MAX_JUGADORES) {
                // Comenzar el juego cuando se alcanza el número máximo de jugadores
                temporizador.cancel();
                comenzarJuego();
            }

            return "!Bienvenid@ " + juego.getJugador() + "!";
        } else {
            return juegoIniciado ? "¡Lo siento! El juego ya ha comenzado." : "¡Lo siento! Ya se han alcanzado el número máximo de jugadores.";
        }
    }

    @GetMapping("/status")
    public String obtenerEstadoJuego() {

        if (!juegos.isEmpty()) {
            // Verificar si el juego ha comenzado
            if (!juegoIniciado) {
                // Calcular el tiempo restante para el inicio del juego
                long tiempoRestante = calcularTiempoRestante();
                return "El juego inicia en " + tiempoRestante + " segundos.";
            }
            return "El juego ha comenzado.\nEs el turno de " + juegos.get(indiceJugadorActual).getJugador() + ".";

        } else {
            return "No hay jugadores conectados.";
        }
    }

    @PostMapping("/guess")
    public String adivinarLetra(@RequestParam String nombreJugador, @RequestParam String intento) {
        // Buscar el juego del jugador en la lista de juegos
        Juego juego = buscarJuegoPorJugador(nombreJugador);

        if (juego != null) {
            // Verificar si el juego ha comenzado
            if (!juegoIniciado) {
                // Calcular el tiempo restante para el inicio del juego
                long tiempoRestante = calcularTiempoRestante();
                return "¡Espera a que el juego comience! Tiempo restante: " + tiempoRestante + " segundos.";
            }

            if (ganador != null) {
                return "¡El juego ha terminado! El ganador es " + ganador + ".";
            }

            // Verificar si es el turno del jugador actual
            if (juego.isPerdida()) {
                pasarAlSiguienteJugador();
                return "¡Lo siento, " + nombreJugador + "! Has perdido. Comienza una nueva ronda.";
            }
            else if (juego.isAdivinada()) {
                pasarAlSiguienteJugador();
                return "¡Felicidades, " + nombreJugador + "! Has ganado. Comienza una nueva ronda.";
            }
            else if (esTurnoDelJugador(juego)) {
                if (intento.length() == 1) {
                    char letra = intento.charAt(0);

                    if (juego.yaHaAdivinadoLetra(letra)) {
                        return "¡Ya has adivinado la letra " + letra + "! Inténtalo con otra letra.";
                    }

                    if (juego.yaHaUsadoLetra(letra)) {
                        return "¡Ya has usado la letra " + letra + "! Inténtalo con otra letra.";
                    }

                    juego.adivinarLetra(letra);
                } else if (intento.length() == juego.getLongitudPalabra()) {
                    juego.adivinarPalabra(intento);
                } else {
                    return "¡La longitud del intento no es válida!";
                }

                if (juego.isAdivinada() || juego.isPerdida()) {
                    // Reiniciar el juego para una nueva ronda
                    pasarAlSiguienteJugador(); // Cambiar al siguiente jugador en el turno

                    if (juego.isAdivinada()) {
                        ganador = nombreJugador;
                        juego.reiniciarJuego();
                        return "¡Felicidades, " + nombreJugador + "! Has ganado. Comienza una nueva ronda.";
                    } else {
                        return "¡Lo siento, " + nombreJugador + "! Has perdido. Comienza una nueva ronda.";
                    }
                } else {
                    pasarAlSiguienteJugador(); // Cambiar al siguiente jugador en el turno
                    return "Palabra actual: " + juego.getEstadoActualPalabra() +
                            "\nLongitud de la palabra: " + juego.getLongitudPalabra() +
                            "\nIntentos restantes: " + juego.getIntentos() +
                            "\nLetras usadas: " + juego.getLetrasUsadas();
                }
            } else {
                return "¡No es tu turno, " + nombreJugador + "! Espera tu turno para hacer una suposición.";
            }
        } else {
            return "¡Error! No se encontró el juego para el jugador " + nombreJugador;
        }
    }

    private boolean esTurnoDelJugador(Juego juego) {
        return juegos.indexOf(juego) == indiceJugadorActual;
    }

    private void pasarAlSiguienteJugador() {
        indiceJugadorActual = (indiceJugadorActual + 1) % juegos.size();
    }

    private Juego buscarJuegoPorJugador(String nombreJugador) {
        for (Juego juego : juegos) {
            if (juego.getJugador().equals(nombreJugador)) {
                return juego;
            }
        }
        return null;
    }

    private void iniciarTemporizador() {
        tiempoInicio = System.currentTimeMillis();  // Almacena el momento en que se inicia el temporizador
        temporizador = new Timer();
        temporizador.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!juegos.isEmpty()) {
                    // Iniciar el juego con los jugadores existentes cuando expire el temporizador
                    comenzarJuego();
                }
            }
        }, tiempoEspera); // 30 segundos
    }

    private long calcularTiempoRestante() {
        if (temporizador != null) {
            long tiempoProgramado = tiempoInicio + tiempoEspera;  // 30 segundos

            // Calcular la diferencia en milisegundos
            long tiempoRestante = tiempoProgramado - System.currentTimeMillis();

            return tiempoRestante / 1000;
        } else {
            // Temporizador no inicializado
            return 0;
        }
    }

    private void comenzarJuego() {
        juegoIniciado = true;
        System.out.println("\n¡Comienza el juego con " + juegos.size() + " jugadores!");
        System.out.println("La palabra es: " + juegos.get(0).getPalabra() + "\n");
    }
}