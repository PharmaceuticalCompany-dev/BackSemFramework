package org.br.farmacia.models;

public class Caixa {
    private int id; // Adicionando um ID para persistência no banco de dados
    private double valorTotal;

    // Construtor para criação de um novo Caixa (sem ID, que será gerado pelo DB)
    public Caixa(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    // Construtor para carregar um Caixa do banco de dados (com ID)
    public Caixa(int id, double valorTotal) {
        this.id = id;
        this.valorTotal = valorTotal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public void adicionarFundos(double valor) {
        if (valor > 0) {
            this.valorTotal += valor;
        }
    }

    public boolean removerFundos(double valor) {
        if (valor > 0 && this.valorTotal >= valor) {
            this.valorTotal -= valor;
            return true;
        }
        return false;
    }
}