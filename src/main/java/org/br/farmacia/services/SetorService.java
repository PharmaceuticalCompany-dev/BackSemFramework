package org.br.farmacia.services;

import java.util.ArrayList;
import java.util.List;

import org.br.farmacia.enums.TipoSetor;
import org.br.farmacia.models.Funcionario;
import org.br.farmacia.models.Setor;

public class SetorService {

    private List<Setor> setores;

    public SetorService() {
        this.setores = new ArrayList<>();
    }

    public void adicionarSetor(Setor setor) {
        if (setor != null && buscarPorTipo(setor.getTipoSetor()) == null) {
            setores.add(setor);
        }
    }

    public void editarSetor(Setor setorAtualizado) {
        if (setorAtualizado != null) {
            for (int i = 0; i < setores.size(); i++) {
                Setor setorExistente = setores.get(i);
                if (setorExistente.getTipoSetor().equals(setorAtualizado.getTipoSetor())) {
                    setores.set(i, setorAtualizado);
                    return;
                }
            }
        }
    }


    public List<Setor> listarSetores() {
        return setores;
    }

    public Setor buscarPorTipo(TipoSetor tipo) {
        for (Setor s : setores) {
            if (s.getTipoSetor().equals(tipo)) {
                return s;
            }
        }
        return null;
    }

    public void adicionarFuncionarioAoSetor(Funcionario funcionario) {
        if (funcionario == null || funcionario.getSetor() == null) {
            return;
        }

        TipoSetor tipo = funcionario.getSetor().getTipoSetor();
        Setor setorEncontrado = buscarPorTipo(tipo);

        if (setorEncontrado == null) {
            Setor novoSetor = new Setor(tipo, new ArrayList<>());
            novoSetor.adicionarFuncionario(funcionario);
            setores.add(novoSetor);
        } else {
            setorEncontrado.adicionarFuncionario(funcionario);
        }
    }

    public int contarTotalFuncionarios() {
        int total = 0;
        for (Setor setor : setores) {
            total += setor.getQuantidadeFuncionarios();
        }
        return total;
    }
}
