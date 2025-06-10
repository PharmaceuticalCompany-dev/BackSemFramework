package org.br.farmacia.models;

import java.time.LocalDate;

public class VendasProgramadas {
    private int id;
    private LocalDate dataVenda;
    private double valorVenda;
    private double custoProduto;
    private int produtoId;
    private Integer empresaId;


    public VendasProgramadas() {
    }


    public VendasProgramadas(int id, LocalDate dataVenda, double valorVenda, double custoProduto, int produtoId, Integer empresaId) {
        this.id = id;
        this.dataVenda = dataVenda;
        this.valorVenda = valorVenda;
        this.custoProduto = custoProduto;
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

    public double getValorVenda() {
        return valorVenda;
    }

    public void setValorVenda(double valorVenda) {
        this.valorVenda = valorVenda;
    }

    public double getCustoProduto() {
        return custoProduto;
    }

    public void setCustoProduto(double custoProduto) {
        this.custoProduto = custoProduto;
    }

    public double getLucro() {
        return valorVenda - custoProduto;
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