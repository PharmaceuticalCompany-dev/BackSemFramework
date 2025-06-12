package org.br.farmacia.services;

import org.br.farmacia.models.Funcionario;
import org.br.farmacia.models.Produto;
import org.br.farmacia.repositories.ProdutoRepository;

import javax.servlet.ServletContext;
import java.util.List;

public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ServletContext context) {
        this.produtoRepository = new ProdutoRepository(context);
    }

    public boolean adicionarProduto(Produto produto) {
        if (produto != null) {
            return produtoRepository.save(produto);
        }
        return false;
    }

    public boolean removerProduto(int id) {
        return produtoRepository.delete(id);
    }

    public boolean editarProduto(int id, Produto novoProduto) {
        Produto existente = produtoRepository.findById(id);
        if (existente != null) {
            novoProduto.setId(id);
            return produtoRepository.update(novoProduto);
        }
        return false;
    }

    public List<Produto> listarProdutos() {
        return produtoRepository.findAll();
    }

    public int quantidadeTotalEstoque() {
        List<Produto> produtos = listarProdutos();
        return produtos.stream()
                .mapToInt(Produto::getQuantidadeEstoque)
                .sum();
    }
}
