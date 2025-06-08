package org.br.farmacia.models;

public class Produto {
    private int id;
    private String nome;
    private double precoCompra;
    private double precoVenda;
    private int quantidadeEstoque;


    public Produto() {
    }

    public Produto(int id, String nome, double precoCompra, double precoVenda, int quantidadeEstoque) {
        this.id = id;
        this.nome = nome;
        this.precoCompra = precoCompra;
        this.precoVenda = precoVenda;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public Produto(String nome, double precoCompra, double precoVenda, int quantidadeEstoque) {
        this.nome = nome;
        this.precoCompra = precoCompra;
        this.precoVenda = precoVenda;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPrecoCompra() {
        return precoCompra;
    }

    public void setPrecoCompra(double precoCompra) {
        this.precoCompra = precoCompra;
    }

    public double getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(double precoVenda) {
        this.precoVenda = precoVenda;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    @Override
    public String toString() {
        return "ID: " + id +
                "\nNome: " + nome +
                "\nPreço compra: " + precoCompra +
                "\nPreço venda: " + precoVenda +
                "\nQuantidade estoque: " + quantidadeEstoque;
    }
}
