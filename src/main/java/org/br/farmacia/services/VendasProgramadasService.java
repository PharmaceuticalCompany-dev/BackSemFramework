package org.br.farmacia.services;

import org.br.farmacia.models.VendasProgramadas;

import java.util.ArrayList;
import java.util.List;

public class VendasProgramadasService {

    private List<VendasProgramadas> vendas;

    public VendasProgramadasService() {
        this.vendas = new ArrayList<>();
    }

    public void adicionarVenda(VendasProgramadas venda) {
        if (venda != null) {
            vendas.add(venda);
        }
    }

    public void removerVenda(VendasProgramadas venda) {
        vendas.remove(venda);
    }

    public List<VendasProgramadas> listarVendas() {
        return new ArrayList<>(vendas);
    }

    public List<VendasProgramadas> buscarPorAno(int ano) {
        List<VendasProgramadas> resultado = new ArrayList<>();
        for (VendasProgramadas venda : vendas) {
            if (venda.getAno() == ano) {
                resultado.add(venda);
            }
        }
        return resultado;
    }

    public List<VendasProgramadas> buscarPorMesEAno(int mes, int ano) {
        List<VendasProgramadas> resultado = new ArrayList<>();
        for (VendasProgramadas venda : vendas) {
            if (venda.getAno() == ano && venda.getMes() == mes) {
                resultado.add(venda);
            }
        }
        return resultado;
    }

    public double calcularLucroTotal() {
        double total = 0;
        for (VendasProgramadas venda : vendas) {
            total += venda.getLucro();
        }
        return total;
    }

    public double calcularLucroPorAno(int ano) {
        double total = 0;
        for (VendasProgramadas venda : vendas) {
            if (venda.getAno() == ano) {
                total += venda.getLucro();
            }
        }
        return total;
    }

    public double calcularLucroPorMesEAno(int mes, int ano) {
        double total = 0;
        for (VendasProgramadas venda : vendas) {
            if (venda.getAno() == ano && venda.getMes() == mes) {
                total += venda.getLucro();
            }
        }
        return total;
    }
}
