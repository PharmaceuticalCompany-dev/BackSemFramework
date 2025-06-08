package org.br.farmacia.services;

import org.br.farmacia.interfaces.ICalculavel;
import org.br.farmacia.models.*;

import java.time.LocalDate;
import java.util.*;

public class EmpresaService implements ICalculavel {

    private Empresa empresa;
    private Map<Integer, Map<Integer, Map<Produto, Integer>>> vendasMesAno;

    public EmpresaService(Empresa empresa) {
        this.empresa = empresa;
        this.vendasMesAno = new HashMap<>();
    }

    // Programa uma venda para um dado ano, mês, produto e quantidade
    public void programarVenda(int ano, int mes, Produto produto, int quantidade) {
        vendasMesAno
                .computeIfAbsent(ano, y -> new HashMap<>())
                .computeIfAbsent(mes, x -> new HashMap<>())
                .merge(produto, quantidade, Integer::sum);
    }

    // Calcula o lucro do mês (vendas - custos)
    public double lucroMes(int ano, int mes) {
        double vendas = 0;
        double custos = 0;

        Map<Produto, Integer> vendasDoMes = vendasMesAno
                .getOrDefault(ano, Collections.emptyMap())
                .getOrDefault(mes, Collections.emptyMap());

        for (Map.Entry<Produto, Integer> entry : vendasDoMes.entrySet()) {
            Produto produto = entry.getKey();
            int quantidade = entry.getValue();

            vendas += produto.getPrecoVenda() * quantidade;
            custos += produto.getPrecoCompra() * quantidade;
        }

        return vendas - custos;
    }

    // Calcula o lucro total do ano, somando todos os meses
    public double lucroAno(int ano) {
        double totalLucro = 0;
        Map<Integer, Map<Produto, Integer>> meses = vendasMesAno.getOrDefault(ano, Collections.emptyMap());

        for (int mes : meses.keySet()) {
            totalLucro += lucroMes(ano, mes);
        }

        return totalLucro;
    }

    // Atualiza o caixa da empresa com o lucro do mês atual e retorna novo valor do caixa
    @Override
    public double calcular() {
        int mes = LocalDate.now().getMonthValue();
        int ano = LocalDate.now().getYear();

        double lucro = lucroMes(ano, mes);
        double novoCaixa = empresa.getCaixaTotal() + lucro;
        empresa.setCaixaTotal(novoCaixa);

        return novoCaixa;
    }

    // Exibe resumo dos lucros mês a mês no ano informado
    public void exibirResumoAnuais(int ano) {
        System.out.println("Resumo de Lucros - Ano: " + ano);
        for (int mes = 1; mes <= 12; mes++) {
            double lucro = lucroMes(ano, mes);
            System.out.printf("Mês %02d: R$ %.2f%n", mes, lucro);
        }
    }

    // Constrói lista de vendas programadas para toda a empresa
    public List<VendasProgramadas> getVendasProgramadas() {
        List<VendasProgramadas> lista = new ArrayList<>();

        for (Map.Entry<Integer, Map<Integer, Map<Produto, Integer>>> anoEntry : vendasMesAno.entrySet()) {
            int ano = anoEntry.getKey();

            for (Map.Entry<Integer, Map<Produto, Integer>> mesEntry : anoEntry.getValue().entrySet()) {
                int mes = mesEntry.getKey();

                for (Map.Entry<Produto, Integer> venda : mesEntry.getValue().entrySet()) {
                    Produto produto = venda.getKey();
                    int quantidade = venda.getValue();

                    VendasProgramadas vp = new VendasProgramadas();
                    vp.setValorVenda(produto.getPrecoVenda() * quantidade);
                    vp.setCustoProduto(produto.getPrecoCompra() * quantidade);
                    vp.dataVenda = LocalDate.of(ano, mes, 1); // Dia fixo 1

                    lista.add(vp);
                }
            }
        }

        return lista;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Map<Integer, Map<Integer, Map<Produto, Integer>>> getVendasMesAno() {
        return vendasMesAno;
    }

    public void setVendasMesAno(Map<Integer, Map<Integer, Map<Produto, Integer>>> vendasMesAno) {
        this.vendasMesAno = vendasMesAno;
    }
}
