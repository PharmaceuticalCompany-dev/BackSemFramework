package org.br.farmacia.services;

import org.br.farmacia.models.Funcionario;
import org.br.farmacia.models.Setor;
import org.br.farmacia.repositories.SetorRepository;
import javax.servlet.ServletContext;
import java.util.Collections;
import java.util.List;


public class SetorService {

    private final SetorRepository setorRepository;
    private final FuncionarioService funcionarioService;

    public SetorService(ServletContext context) {
        this.setorRepository = new SetorRepository(context);
         this.funcionarioService = new FuncionarioService(context);
    }

    public boolean adicionarSetor(Setor setor) {
        if (setor != null) {
            return setorRepository.save(setor);
        }
        return false;
    }

    public void removerSetor(int id) {
        setorRepository.delete(id);
    }

    public boolean editarSetor(int id, Setor novoSetor) {
        Setor existente = setorRepository.findById(id);
        if (existente != null) {
            novoSetor.setId(id);
            return setorRepository.update(novoSetor);
        }
        return false;
    }

    public List<Setor> listarSetores() {
        List<Setor> setores = setorRepository.findAll();
        return setores;
    }

    public Setor buscarPorId(int id) {
        Setor setor = setorRepository.findById(id);
        if (setor != null) {
            List<Funcionario> funcionariosDoSetor = Collections.singletonList(funcionarioService.buscarPorId(setor.getId()));
            setor.setFuncionarios(funcionariosDoSetor);
        }
        return setor;
    }
}