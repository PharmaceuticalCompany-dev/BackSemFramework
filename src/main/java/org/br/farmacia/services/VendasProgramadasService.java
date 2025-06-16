package org.br.farmacia.services;

import org.br.farmacia.models.VendasProgramadas;
import org.br.farmacia.models.Produto;
import org.br.farmacia.repositories.ProdutoRepository;
import org.br.farmacia.repositories.VendasProgramadasRepository;

import javax.servlet.ServletContext;
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

    public List<VendasProgramadas> buscarPorMesEAno(int mes, int ano) {
        List<VendasProgramadas> resultado = new ArrayList<>();
        for (VendasProgramadas v : listar()) {
            if (v.getMes() == mes && v.getAno() == ano) {
                resultado.add(v);
            }
        }
        return resultado;
    }

    public double calcularLucroAnual(int ano) {
        double lucroTotal = 0.0;
        for (VendasProgramadas v : listar()) {
            if (v.getAno() == ano) {
                lucroTotal += v.getLucro();
            }
        }
        return lucroTotal;
    }

    public double calcularCustoAnual(int ano) {
        double custoTotal = 0.0;
        for (VendasProgramadas v : listar()) {
            if (v.getAno() == ano) {
                custoTotal += v.getCustoProdutoCalculado();
            }
        }
        return custoTotal;
    }

    public double calcularRendimentoAnual(int ano) {
        double rendimentoTotal = 0.0;
        for (VendasProgramadas v : listar()) {
            if (v.getAno() == ano) {
                rendimentoTotal += v.getValorVendaCalculado();
            }
        }
        return rendimentoTotal;
    }
}