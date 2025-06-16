package org.br.farmacia.models;

import java.time.LocalDate;

public class VendasProgramadas {
    private int id;
    private LocalDate dataVenda;
    private int produtoId;
    private Integer empresaId;
    private int quantidade;
    private boolean concluida = false;

    private double valorVendaCalculado;
    private double custoProdutoCalculado;

    public VendasProgramadas() {
    }

    public boolean isConcluida() {
        return concluida;
    }

    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
    }

    public VendasProgramadas(int id, LocalDate dataVenda, int produtoId, int quantidade, boolean concluida, Integer empresaId) {
        this.id = id;
        this.dataVenda = dataVenda;
        this.produtoId = produtoId;
        this.empresaId = empresaId;
        this.quantidade = quantidade;
        this.concluida = concluida;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDate dataVenda) {
        this.dataVenda = dataVenda;
    }

    public double getValorVendaCalculado() {
        return valorVendaCalculado;
    }

    public void setValorVendaCalculado(double valorVendaCalculado) {
        this.valorVendaCalculado = valorVendaCalculado  * getQuantidade();
    }

    public double getCustoProdutoCalculado() {
        return custoProdutoCalculado;
    }

    public void setCustoProdutoCalculado(double custoProdutoCalculado) {
        this.custoProdutoCalculado = custoProdutoCalculado  * getQuantidade();
    }

    public double getLucro() {
        return valorVendaCalculado - custoProdutoCalculado;
    }

    public int getAno() {
        return dataVenda.getYear();
    }

    public int getMes() {
        return dataVenda.getMonthValue();
    }

    public int getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(int produtoId) {
        this.produtoId = produtoId;
    }

    public Integer getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Integer empresaId) {
        this.empresaId = empresaId;
    }
}