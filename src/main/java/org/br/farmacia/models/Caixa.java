package org.br.farmacia.models;

import org.br.farmacia.enums.TipoTransacao;
import java.time.LocalDateTime;

public class Caixa {

    private int id;
    private TipoTransacao tipo;
    private double valor;
    private LocalDateTime data;
    private String descricao;
    private int empresaId;

    public Caixa() {
    }

    public Caixa(int id, TipoTransacao tipo, double valor, LocalDateTime data, String descricao, int empresaId) {
        this.id = id;
        this.tipo = tipo;
        this.valor = valor;
        this.data = data;
        this.descricao = descricao;
        this.empresaId = empresaId;
    }

    public Caixa(TipoTransacao tipo, double valor, LocalDateTime data, String descricao, int empresaId) {
        this.tipo = tipo;
        this.valor = valor;
        this.data = data;
        this.descricao = descricao;
        this.empresaId = empresaId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TipoTransacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoTransacao tipo) {
        this.tipo = tipo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(int empresaId) {
        this.empresaId = empresaId;
    }

    @Override
    public String toString() {
        return "Caixa{" +
                "id=" + id +
                ", tipo=" + tipo +
                ", valor=" + valor +
                ", data=" + data +
                ", descricao='" + descricao + '\'' +
                ", empresaId=" + empresaId +
                '}';
    }
}