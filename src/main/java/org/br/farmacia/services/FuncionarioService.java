package org.br.farmacia.services;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.br.farmacia.enums.Cargo;
import org.br.farmacia.enums.TipoSetor;
import org.br.farmacia.models.Funcionario;
import org.br.farmacia.models.Setor;
import org.br.farmacia.repositories.FuncionarioRepository;
import org.br.farmacia.repositories.SetorRepository;

import javax.servlet.ServletContext;

public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;

    //private final SetorRepository setorRepository;

    public FuncionarioService(ServletContext context) {
        this.funcionarioRepository = new FuncionarioRepository(context);
        //this.setorRepository = new SetorRepository((Connection) context);
    }

    public void inicializarFuncionario(Funcionario funcionario) {
        funcionario.setSetor(definirSetorPorCargo(funcionario.getCargo()));
        calcularEAtribuirBeneficiosPorCargo(funcionario);

        if (funcionario.getCargo() == Cargo.GERENTE) {
            funcionario.setBonificacao(funcionario.getSalario() * 0.10);
        } else {
           //TERMINAR DPS
        }
    }

    public boolean adicionarFuncionario(Funcionario funcionario) {
        if (funcionario != null) {
            inicializarFuncionario(funcionario);
            return funcionarioRepository.save(funcionario);
        }
        return false;
    }

    public void removerFuncionario(int id) {
        funcionarioRepository.delete(id);
    }

    public boolean editarFuncionario(int id, Funcionario novoFuncionario) {
        Funcionario existente = funcionarioRepository.findById(id);
        if (existente != null) {
            inicializarFuncionario(novoFuncionario);
            novoFuncionario.setId(id);
            return funcionarioRepository.update(novoFuncionario);
        }
        return false;
    }

    public List<Funcionario> listarFuncionarios() {
        return funcionarioRepository.findAll();
    }

    public Funcionario buscarPorId(int idEditar) {
        return funcionarioRepository.findById(idEditar);
    }


    private Setor setarTipoSetor(TipoSetor tipoSetor) {
        return new Setor(tipoSetor, new ArrayList<>());
    }

    private Setor definirSetorPorCargo(Cargo cargo) {
        TipoSetor tipoSetor;

        switch (cargo) {
            case GERENTE: tipoSetor = TipoSetor.GERENCIA_FILIAL; break;
            case ATENDENTE: tipoSetor = TipoSetor.ATENDIMENTO_CLIENTE; break;
            case RH: tipoSetor = TipoSetor.GESTAO_PESSOAS; break;
            case FINANCEIRO: tipoSetor = TipoSetor.FINANCEIRO; break;
            case VENDEDOR: tipoSetor = TipoSetor.VENDAS; break;
            case ALMOXARIFE: tipoSetor = TipoSetor.ALMOXARIFADO; break;
            case MOTORISTA: tipoSetor = TipoSetor.TRANSPORTADORAS; break;
            default: throw new IllegalArgumentException("Cargo inválido: " + cargo);
        }

        return setarTipoSetor(tipoSetor);
    }

    private void calcularEAtribuirBeneficiosPorCargo(Funcionario funcionario) {
        double valeRefeicao = 300;
        double valeAlimentacao = 300;
        double planoSaude = 3000;
        double planoOdonto = 3000;

        switch (funcionario.getCargo()) {
            case GERENTE:
                valeRefeicao *= 2;
                valeAlimentacao *= 2;
                planoSaude *= 1.5;
                planoOdonto *= 1.5;
                break;
            case VENDEDOR:
                valeRefeicao *= 1.5;
                valeAlimentacao *= 1.5;
                break;
            case FINANCEIRO:
            case RH:
                planoSaude *= 1.3;
                break;
            case MOTORISTA:
            case ALMOXARIFE:
                valeAlimentacao *= 1.2;
                break;
            case ATENDENTE:
                break;
        }

        funcionario.setValeRefeicao(valeRefeicao);
        funcionario.setValeAlimentacao(valeAlimentacao);
        funcionario.setPlanoSaude(planoSaude);
        funcionario.setPlanoOdonto(planoOdonto);
    }
}