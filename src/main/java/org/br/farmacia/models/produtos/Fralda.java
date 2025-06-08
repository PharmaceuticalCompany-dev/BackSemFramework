package org.br.farmacia.models.produtos;

import org.br.farmacia.models.Produto;

public class Fralda extends Produto {

    public Fralda() {
    }

    @Override
    public void setPrecoCompra(double precoCompra) {
        super.setPrecoCompra(20);
    }

    @Override
    public void setPrecoVenda(double precoVenda) {
        super.setPrecoVenda(40);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
