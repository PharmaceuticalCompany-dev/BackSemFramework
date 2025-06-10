package org.br.farmacia.services;

import org.br.farmacia.models.VendasProgramadas;
import org.br.farmacia.repositories.FuncionarioRepository;
import org.br.farmacia.repositories.VendasProgramadasRepository;

import javax.servlet.ServletContext;
import java.util.List;

public class VendasProgramadasService {
    List<VendasProgramadas> vendasProgramadas;

    private VendasProgramadasRepository vendasProgramadasRepository;

    public VendasProgramadasService(ServletContext context) {
        this.vendasProgramadasRepository = new VendasProgramadasRepository(context);
    }

    public boolean adicionarVendaProgramada(VendasProgramadas vendaProgramada) {
        if(vendaProgramada != null) {
            return  vendasProgramadasRepository.save(vendaProgramada);
        }
        return false;
    }

    public void removerVendaProgramada(int id) {vendasProgramadasRepository.delete(id);}

    public boolean editarVendaProgramada(int id, VendasProgramadas vendaProgramada) {
        VendasProgramadas existente = vendasProgramadasRepository.findById(id);

        if (existente != null) {
            existente.setDataVenda(vendaProgramada.getDataVenda());
            existente.setValorVenda(vendaProgramada.getValorVenda());
            existente.setCustoProduto(vendaProgramada.getCustoProduto());
            existente.setProdutoId(vendaProgramada.getProdutoId());

            return vendasProgramadasRepository.update(existente);
        }

        return false;
    }

    public List<VendasProgramadas> listar() {
        vendasProgramadas = vendasProgramadasRepository.findAll();
        return vendasProgramadas;
    }
}
