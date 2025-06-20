package org.br.farmacia.services;

import org.br.farmacia.models.Transportadora;
import org.br.farmacia.repositories.TransportadoraRepository;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;

public class TransportadoraService {
    List<Transportadora> transportadoras;

    private final TransportadoraRepository transportadoraRepository;

    public TransportadoraService(ServletContext context) {
        this.transportadoras = new ArrayList<>();
        this.transportadoraRepository = new TransportadoraRepository(context);
    }


    public boolean adicionarTransportadora(Transportadora transportadora) {
        if (transportadora != null) {
            return transportadoraRepository.save(transportadora);
        }
        return false;
    }

    public boolean removerTransportadora(int id) {
       return transportadoraRepository.delete(id);
    }

    public boolean editarTransportadora(int id, Transportadora transportadora) {
        Transportadora existente = transportadoraRepository.findById(id);

        if (existente != null) {
            existente.setNome(transportadora.getNome());
            existente.setContato(transportadora.getContato());
            existente.setTelefone(transportadora.getTelefone());
            existente.setRegiao(transportadora.getRegiao());

            return transportadoraRepository.update(existente);
        }

        return false;
    }



    public List<Transportadora> listarTransportadoras() {
        transportadoras = transportadoraRepository.findAll();
        return transportadoras;
    }

}
