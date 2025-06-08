package org.br.farmacia.enums;

import org.br.farmacia.utilities.FormatarTexto;

public enum Genero {
    MASCULINO,
    FEMININO,
    OUTRO;

    @Override
    public String toString() {
        return FormatarTexto.capitalizar(name());
    }
}