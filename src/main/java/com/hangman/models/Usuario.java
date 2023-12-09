package com.hangman.models;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Usuario {
    String NombreJugador;
    @Setter
    int Intentos;

    public Usuario(String NombreJugador) {
        this.NombreJugador = NombreJugador;
        this.Intentos = 5;
    }
}
