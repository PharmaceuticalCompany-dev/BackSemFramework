package org.br.farmacia.services;

import org.br.farmacia.models.Caixa;

public class CaixaService {

    private Caixa caixa;

    public CaixaService(double valorInicial) {
        this.caixa = new Caixa(valorInicial);
    }

    public double getValorTotal() {
        return caixa.getValorTotal();
    }

    public void adicionarEntrada(double valor) {
        if (valor > 0) {
            caixa.setValorTotal(caixa.getValorTotal() + valor);
        }
    }

    public boolean registrarSaida(double valor) {
        if (valor > 0 && caixa.getValorTotal() >= valor) {
            caixa.setValorTotal(caixa.getValorTotal() - valor);
            return true;
        }
        return false;
    }

    public void resetarCaixa(double novoValor) {
        caixa.setValorTotal(novoValor);
    }
}
