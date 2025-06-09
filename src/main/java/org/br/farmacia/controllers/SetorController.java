package org.br.farmacia.controllers;

import org.br.farmacia.models.Funcionario;
import org.br.farmacia.models.Setor;

import java.util.List;

public class SetorController {
    private Setor setor;

    public SetorController(Setor setor) {
        this.setor = setor;
    }

    void adicionaFuncionariosSetor(Funcionario func) {
         setor.getFuncionarios().add(func);
    }

    public List<Funcionario> listarFuncionariosSetor() {
        return setor.getFuncionarios();
    }
}
