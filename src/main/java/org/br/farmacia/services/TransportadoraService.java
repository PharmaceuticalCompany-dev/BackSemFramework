package org.br.farmacia.services;

import org.br.farmacia.models.Transportadora;

import java.util.ArrayList;
import java.util.List;

public class TransportadoraService {
    List<Transportadora> transportadoras;

    public TransportadoraService() {
        this.transportadoras = new ArrayList<>();
    }


    public void adicionarTransportadora(Transportadora transportadora) {
        if (transportadora != null) {
            transportadoras.add(transportadora);
        }
    }

    public void removerTransportadora(Transportadora transportadora) {
        if (transportadora != null) {
            transportadoras.remove(transportadora);
        }
    }

    public void editarTransportador(Transportadora transportadoraAtualizada) {
        if (transportadoraAtualizada != null) {
            for(int i = 0; i  < transportadoras.size(); i++){
                if(transportadoras.get(i).getNome().equals(transportadoraAtualizada.getNome())){
                    transportadoras.set(i, transportadoraAtualizada);
                    return;
                }
            }
        }
    }

    public List<Transportadora> listarTransportadoras() {
        return transportadoras;
    }
}
