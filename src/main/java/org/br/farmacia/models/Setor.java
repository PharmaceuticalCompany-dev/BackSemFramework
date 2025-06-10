package org.br.farmacia.models;

import org.br.farmacia.enums.TipoSetor;
import java.util.List;
import java.util.ArrayList; // Added for the constructor

public class Setor {
    private int id; // Assuming an ID for database operations
    private TipoSetor tipoSetor;
    private List<Funcionario> funcionarios; // As per UML

    // Constructor for creating new Setor objects (without ID from DB)
    public Setor(TipoSetor tipoSetor, List<Funcionario> funcionarios) {
        this.tipoSetor = tipoSetor;
        this.funcionarios = funcionarios;
    }

    // Constructor for loading from DB (with ID)
    public Setor(int id, TipoSetor tipoSetor, List<Funcionario> funcionarios) {
        this.id = id;
        this.tipoSetor = tipoSetor;
        this.funcionarios = funcionarios;
    }

    // Getters and Setters
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

    // You might also want to override toString(), equals(), and hashCode()
}