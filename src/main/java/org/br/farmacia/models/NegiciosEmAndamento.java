package org.br.farmacia.models;

import org.br.farmacia.enums.TipoNegocio;

import java.util.List;

public class NegiciosEmAndamento {
    private int idNeg;
    private TipoNegocio tipo;
    private List<Funcionario> envolvidos;
    private String satus;

    public NegiciosEmAndamento(int idNeg, TipoNegocio tipo, List<Funcionario> envolvidos, String satus) {
        this.idNeg = idNeg;
        this.tipo = tipo;
        this.envolvidos = envolvidos;
        this.satus = satus;
    }

    public int getIdNeg() {
        return idNeg;
    }

    public void setIdNeg(int idNeg) {
        this.idNeg = idNeg;
    }

    public TipoNegocio getTipo() {
        return tipo;
    }

    public void setTipo(TipoNegocio tipo) {
        this.tipo = tipo;
    }

    public List<Funcionario> getEnvolvidos() {
        return envolvidos;
    }

    public void setEnvolvidos(List<Funcionario> envolvidos) {
        this.envolvidos = envolvidos;
    }

    public String getSatus() {
        return satus;
    }

    public void setSatus(String satus) {
        this.satus = satus;
    }
}

