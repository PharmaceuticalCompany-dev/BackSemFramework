package org.br.farmacia.services;

import org.br.farmacia.models.Caixa;
import org.br.farmacia.repositories.CaixaRepository;
import javax.servlet.ServletContext;
import java.util.List;

public class CaixaServices {

    private final CaixaRepository caixaRepository;

    public CaixaServices(ServletContext context) {
        this.caixaRepository = new CaixaRepository(context);
    }

    public boolean adicionarCaixa(Caixa caixa) {
        if (caixa != null) {
            // Pode adicionar validações ou regras de negócio aqui antes de salvar
            return caixaRepository.save(caixa);
        }
        return false;
    }

    public void removerCaixa(int id) {
        caixaRepository.delete(id);
    }

    public boolean editarCaixa(int id, Caixa novoCaixa) {
        Caixa existente = caixaRepository.findById(id);
        if (existente != null) {
            novoCaixa.setId(id); // Garante que o ID esteja setado para a atualização
            return caixaRepository.update(novoCaixa);
        }
        return false;
    }

    public List<Caixa> listarCaixas() {
        return caixaRepository.findAll();
    }

    public Caixa buscarPorId(int id) {
        return caixaRepository.findById(id);
    }

    // Métodos para operações de caixa (ex: registrar transações)
    public boolean registrarMovimentacaoEntrada(int caixaId, double valor) {
        Caixa caixa = caixaRepository.findById(caixaId);
        if (caixa != null) {
            caixa.adicionarFundos(valor); // Se for uma entrada
            // ou caixa.removerFundos(valor); se for uma saída, com validação de saldo
            return caixaRepository.update(caixa);
        }
        return false;
    }

    public boolean registrarMovimentacaoSaida(int caixaId, double valor) {
        Caixa caixa = caixaRepository.findById(caixaId);
        if (caixa != null) {
            caixa.removerFundos(valor);
            return caixaRepository.update(caixa);
        }
        return false;
    }
}