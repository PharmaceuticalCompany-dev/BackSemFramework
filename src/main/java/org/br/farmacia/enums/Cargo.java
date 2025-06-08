package org.br.farmacia.enums;

import org.br.farmacia.utilities.FormatarTexto;

public enum Cargo {
    GERENTE,
    ATENDENTE,
    RH,
    FINANCEIRO,
    VENDEDOR,
    ALMOXARIFE,
    MOTORISTA;

    @Override
    public String toString() {
        return FormatarTexto.capitalizar(name());
    }
}
