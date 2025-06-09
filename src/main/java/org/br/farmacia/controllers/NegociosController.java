package org.br.farmacia.controllers;

import org.br.farmacia.models.NegiciosEmAndamento;

import java.util.ArrayList;
import java.util.List;

public class NegociosController {
    private List<NegiciosEmAndamento> negocios;

    public NegociosController() {
        this.negocios = new ArrayList<NegiciosEmAndamento>();
    }

    void adicionarVenda(NegiciosEmAndamento negocios){//Adiciona mais uma venda programada
        this.negocios.add(negocios);
    }

    public List<NegiciosEmAndamento> getVendas(){//Usado para listar as vendas
        return this.negocios;
    }
}
