package org.br.farmacia.models.produtos;

import org.br.farmacia.models.Produto;

public class Antiacido extends Produto {

    public Antiacido() {
    }

    @Override
    public void setPrecoCompra(double precoCompra) {
        super.setPrecoCompra(1.50);
    }

    @Override
    public void setPrecoVenda(double precoVenda) {
        super.setPrecoVenda(3);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
