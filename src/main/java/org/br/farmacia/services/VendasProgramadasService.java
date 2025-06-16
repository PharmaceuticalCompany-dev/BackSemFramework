package org.br.farmacia.services;

import org.br.farmacia.models.VendasProgramadas;
import org.br.farmacia.models.Produto;
import org.br.farmacia.repositories.ProdutoRepository;
import org.br.farmacia.repositories.VendasProgramadasRepository;

import javax.servlet.ServletContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VendasProgramadasService {
    List<VendasProgramadas> vendasProgramadas;

    private VendasProgramadasRepository vendasProgramadasRepository;
    private ProdutoRepository produtoRepository;

    public VendasProgramadasService(ServletContext context) {
        this.vendasProgramadasRepository = new VendasProgramadasRepository(context);
        this.produtoRepository = new ProdutoRepository(context);
    }

    public boolean adicionarVendaProgramada(VendasProgramadas vendaProgramada) {
        if(vendaProgramada != null) {
            return  vendasProgramadasRepository.save(vendaProgramada);
        }
        return false;
    }

    public double somarVendasDoMes(int mes, int ano) {
        double soma = 0.0;

        for (VendasProgramadas venda : vendasProgramadasRepository.findAll()) {
            LocalDate data = venda.getDataVenda();
            if (data.getMonthValue() == mes && data.getYear() == ano) {
                soma += venda.getValorVendaCalculado();
            }
        }

        return soma;
    }

    public double somarVendasMesAtual() {
        LocalDate hoje = LocalDate.now();
        return somarVendasDoMes(hoje.getMonthValue(), hoje.getYear());
    }


    public double estimarLucroAnualPorMediaMensal() {
        LocalDate hoje = LocalDate.now();
        int anoAtual = hoje.getYear();
        int mesAtual = hoje.getMonthValue();

        double somaLucroMesesPassados = 0.0;

        for (int mes = 1; mes <= mesAtual; mes++) {
            somaLucroMesesPassados += somarVendasDoMes(mes, anoAtual);
        }

        double mediaMensal = somaLucroMesesPassados / mesAtual;
        double estimativaLucroAnual = mediaMensal * 12;

        return estimativaLucroAnual;
    }





    public boolean removerVendaProgramada(int id) {
        return vendasProgramadasRepository.delete(id);
    }

    public boolean editarVendaProgramada(int id, VendasProgramadas vendaProgramada) {
        VendasProgramadas existente = vendasProgramadasRepository.findById(id);

        if (existente != null) {
            existente.setDataVenda(vendaProgramada.getDataVenda());
            existente.setProdutoId(vendaProgramada.getProdutoId());
            existente.setEmpresaId(vendaProgramada.getEmpresaId());

            Produto produto = produtoRepository.findById(existente.getProdutoId());
            if (produto != null) {
                existente.setValorVendaCalculado(produto.getPrecoVenda());
                existente.setCustoProdutoCalculado(produto.getPrecoCompra());
            }

            return vendasProgramadasRepository.update(existente);
        }

        return false;
    }

    public List<VendasProgramadas> listar() {
        vendasProgramadas = vendasProgramadasRepository.findAll();
        return vendasProgramadas;
    }

    public double calcularCustoAnual() {
        int anoAtual = LocalDate.now().getYear();
        double custoTotal = 0.0;
        for (VendasProgramadas v : listar()) {
            if (v.getAno() == anoAtual) {
                custoTotal += v.getCustoProdutoCalculado();
            }
        }
        return custoTotal;
    }


    public double calcularRendimentoAnual() {
        int anoAtual = LocalDate.now().getYear();
        double rendimentoTotal = 0.0;
        for (VendasProgramadas v : listar()) {
            if (v.getAno() == anoAtual) {
                rendimentoTotal += v.getValorVendaCalculado();
            }
        }
        return rendimentoTotal;
    }

}