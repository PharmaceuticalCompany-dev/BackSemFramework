package org.br.farmacia.services;

import org.br.farmacia.models.Empresa;
import org.br.farmacia.repositories.EmpresaRepository;

import java.util.List;

public class EmpresaService {

    private EmpresaRepository repository;

    public EmpresaService(EmpresaRepository repository) {
        this.repository = repository;
    }

    public List<Empresa> listarEmpresas() {
        return repository.findAll();
    }
}
