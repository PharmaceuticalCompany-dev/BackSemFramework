package org.br.farmacia.controllers;

import java.util.ArrayList;
import java.util.List;

public class VendasProgramadas {
    private List<org.br.farmacia.models.VendasProgramadas> negocios;

    public VendasProgramadas() {
        this.negocios = new ArrayList<org.br.farmacia.models.VendasProgramadas>();
    }

    void adicionarVenda(org.br.farmacia.models.VendasProgramadas venda){//Adiciona mais uma venda programada
        this.negocios.add(venda);
    }

    public List<org.br.farmacia.models.VendasProgramadas> getVendas(){//Usado para listar as vendas
        return this.negocios;
    }
}
