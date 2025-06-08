package org.br.farmacia.interfaces;

public interface ICalculavel {
    default double calcular() {
        return  0.0;
    }
}