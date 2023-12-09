package com.hangman.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hangman.models.Usuario;
import lombok.Getter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Juego {
    private static String palabraComun;  // Palabra común entre todos los juegos
    private Usuario usuario;
    @Getter
    private String palabra;
    @Getter
    private String estadoActualPalabra;
    private ArrayList<String> letrasUsadas;

    public Juego(String nombreJugador) {
        palabra = cargarPalabra();
        estadoActualPalabra = palabra.replaceAll("[a-zA-Z]", "_");
        this.usuario = new Usuario(nombreJugador);
    }

    public String cargarPalabra() {
        try {
            if (palabraComun == null) {
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonFile = "palabras.json";
                List<String> palabras = objectMapper.readValue(
                        getClass().getClassLoader().getResourceAsStream(jsonFile),
                        objectMapper.getTypeFactory().constructCollectionType(List.class, String.class)
                );

                // Seleccionar una palabra al azar
                Random random = new Random();
                palabraComun = palabras.get(random.nextInt(palabras.size()));
            }

            return palabraComun;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getLongitudPalabra() {
        return palabra.length();
    }

    public boolean isAdivinada() {
        return !estadoActualPalabra.contains("_");
    }

    public boolean isPerdida() {
        return usuario.getIntentos() == 0;
    }

    public void adivinarLetra(char letra) {
        if (letrasUsadas == null) {
            letrasUsadas = new ArrayList<>();
        }

        // Convertir la letra a minúscula antes de comparar
        char letraMinuscula = Character.toLowerCase(letra);

        boolean encontrada = false;
        for (int i = 0; i < palabra.length(); i++) {
            // Convertir la letra actual a minúscula para comparar
            char letraActual = Character.toLowerCase(palabra.charAt(i));

            if (letraActual == letraMinuscula) {
                encontrada = true;
                estadoActualPalabra = estadoActualPalabra.substring(0, i) + letra + estadoActualPalabra.substring(i + 1);
            }
        }
        if (!encontrada) {
            usuario.setIntentos(usuario.getIntentos() - 1);
        }
        letrasUsadas.add(String.valueOf(letra));
    }

    public void adivinarPalabra(String palabraAdivinada) {
        if (palabraAdivinada.equalsIgnoreCase(palabra)) {
            estadoActualPalabra = palabra;
            System.out.println("Palabra adivinada: " + palabraAdivinada + " Palabra: " + palabra);
        } else {
            usuario.setIntentos(usuario.getIntentos() - 1);
        }
    }

    public String getLetrasUsadas() {
        return letrasUsadas == null ? "" : letrasUsadas.toString();
    }

    public boolean yaHaAdivinadoLetra(char letra) {
        return estadoActualPalabra.indexOf(letra) != -1;
    }

    public boolean yaHaUsadoLetra(char letra) {
        return letrasUsadas != null && letrasUsadas.contains(String.valueOf(letra));
    }


    public String getJugador() {
        return this.usuario.getNombreJugador();
    }

    public int getIntentos() {
        return this.usuario.getIntentos();
    }



    public void reiniciarJuego() {
        // Restablecer el estado del juego para una nueva partida
        palabra = cargarPalabra();
        estadoActualPalabra = palabra.replaceAll("[a-zA-Z]", "_");
        usuario.setIntentos(5);
        letrasUsadas = new ArrayList<>();
    }
}