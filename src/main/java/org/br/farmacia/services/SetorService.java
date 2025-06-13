package org.br.farmacia.services;

import org.br.farmacia.models.Setor;
import org.br.farmacia.repositories.SetorRepository;
import org.br.farmacia.database.ConexaoBanco; // Importar sua classe de conexão

import javax.servlet.ServletContext;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SetorService {

    private final SetorRepository setorRepository;

    public SetorService(ServletContext context) {
        this.setorRepository = new SetorRepository(context);
    }

    public Setor buscarPorId(int id) {
        return setorRepository.findById(id);
    }

    public List<Setor> listarSetores() {
        return setorRepository.findAll();
    }

}