package org.br.farmacia.models;

import java.time.LocalDate;

public class VendasProgramadas {
    private int id;
    private LocalDate dataVenda;
    private int produtoId;
    private Integer empresaId;

    private double valorVendaCalculado;
    private double custoProdutoCalculado;

    public VendasProgramadas() {
    }

    public VendasProgramadas(int id, LocalDate dataVenda, int produtoId, Integer empresaId) {
        this.id = id;
        this.dataVenda = dataVenda;
        this.produtoId = produtoId;
        this.empresaId = empresaId;
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
        this.valorVendaCalculado = valorVendaCalculado;
    }

    public double getCustoProdutoCalculado() {
        return custoProdutoCalculado;
    }

    public void setCustoProdutoCalculado(double custoProdutoCalculado) {
        this.custoProdutoCalculado = custoProdutoCalculado;
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