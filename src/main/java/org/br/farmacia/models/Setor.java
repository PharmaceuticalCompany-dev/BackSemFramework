package org.br.farmacia.models;

import org.br.farmacia.enums.TipoSetor;
import java.util.List;
import java.util.ArrayList;

public class Setor {
    private int id;
    private TipoSetor tipoSetor;
    private List<Funcionario> funcionarios;

    public Setor(TipoSetor tipoSetor, List<Funcionario> funcionarios) {
        this.tipoSetor = tipoSetor;
        this.funcionarios = funcionarios;
        this.funcionarios = new ArrayList<>();
    }

    public Setor(int id, TipoSetor tipoSetor, List<Funcionario> funcionarios) {
        this.id = id;
        this.tipoSetor = tipoSetor;
        this.funcionarios = funcionarios;
    }

    public Setor() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TipoSetor getTipoSetor() {
        return tipoSetor;
    }

    public void setTipoSetor(TipoSetor tipoSetor) {
        this.tipoSetor = tipoSetor;
    }

    public List<Funcionario> getFuncionarios() {
        return funcionarios;
    }

    public void setFuncionarios(List<Funcionario> funcionarios) {
        this.funcionarios = funcionarios;
    }
}