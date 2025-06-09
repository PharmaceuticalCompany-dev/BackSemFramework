package org.br.farmacia.controllers;

import org.br.farmacia.models.Produto;

public class ProdutoController {
    private Produto produto;

    public ProdutoController(Produto produto) {
        this.produto = produto;
    }

    void atualizarProduto(int quantProduto) {
        produto.setQuantidadeEstoque(quantProduto);
    }

    double getLucroUnidade() {
        return produto.getPrecoVenda() - produto.getPrecoCompra();
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }
}
