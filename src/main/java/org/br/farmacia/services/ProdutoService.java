package org.br.farmacia.services;

import org.br.farmacia.models.Funcionario;
import org.br.farmacia.models.Produto;

import java.util.ArrayList;
import java.util.List;

public class ProdutoService {
    private List<Produto> produtos;

    public ProdutoService() {
        this.produtos = new ArrayList<>();
    }

    public void adicionarProduto(Produto produto){
        if(produto != null){
            this.produtos.add(produto);
        }
    }

    public void removerProduto(Produto produto){
        if(produto != null){
            this.produtos.remove(produto);
        }
    }

    public void editarProduto(Produto produtoAtualizado){
        if(produtoAtualizado != null){
            for(int i = 0; i < this.produtos.size(); i++){
                if(this.produtos.get(i).getNome().equals(produtoAtualizado.getNome())){
                    this.produtos.set(i, produtoAtualizado);
                    return;
                }
            }
        }
    }

    public List<Produto> listarProdutos(){
        return produtos;
    }

    public int quantidadeTotalEstoque() {
        int total = 0;
        for(Produto produto : produtos) {
            total += produto.getQuantidadeEstoque();
        }
        return total;
    }
}
