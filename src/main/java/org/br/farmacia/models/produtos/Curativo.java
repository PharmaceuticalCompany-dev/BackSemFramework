package org.br.farmacia.models.produtos;

import org.br.farmacia.models.Produto;

public class Curativo extends Produto {

    public Curativo() {
    }

    @Override
    public void setPrecoCompra(double precoCompra) {
        super.setPrecoCompra(3);
    }

    @Override
    public void setPrecoVenda(double precoVenda) {
        super.setPrecoVenda(6);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
