package org.br.farmacia.services;

import org.br.farmacia.models.NegociosEmAndamento;
import org.br.farmacia.repositories.NegociosEmAndamentoRepository;

import javax.servlet.ServletContext;
import java.util.List;

public class NegociosEmAndamentoService {

    private final NegociosEmAndamentoRepository repository;

    public NegociosEmAndamentoService(ServletContext context) {
        this.repository = new NegociosEmAndamentoRepository(context);
    }

    public NegociosEmAndamento getById(int id) {
        return repository.findById(id);
    }

    public List<NegociosEmAndamento> getAll() {
        return repository.findAll();
    }

    public boolean adicionarNegocio(NegociosEmAndamento negocio) {
        if(negocio != null) {
            return repository.save(negocio);
        }
        return false;
    }

    public boolean update(Integer id, NegociosEmAndamento negocio) {
        return repository.update(negocio);
    }

    public boolean delete(int id) {
        return repository.delete(id);
    }
}
