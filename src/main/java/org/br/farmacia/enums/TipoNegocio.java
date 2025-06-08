package org.br.farmacia.enums;

import org.br.farmacia.utilities.FormatarTexto;
public enum TipoNegocio {
    COMPRA,
    VENDA;

    @Override
    public String toString() {
        return FormatarTexto.capitalizar(name());
    }
}