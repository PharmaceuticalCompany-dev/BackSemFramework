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

        switch (funcionario.getCargo()) {
            case GERENTE:
                funcionario.setBonificacao(funcionario.getSalario() * 0.10);
                break;
            case ATENDENTE:
                funcionario.setBonificacao(funcionario.getSalario() * 0.02);
                break;
            case RH:
                funcionario.setBonificacao(funcionario.getSalario() * 0.05);
                break;
            case FINANCEIRO:
                funcionario.setBonificacao(funcionario.getSalario() * 0.04);
                break;
            case VENDEDOR:
                funcionario.setBonificacao(funcionario.getSalario() * 0.06);
                break;
            case ALMOXARIFE:
                funcionario.setBonificacao(funcionario.getSalario() * 0.03);
                break;
            case MOTORISTA:
                funcionario.setBonificacao(funcionario.getSalario() * 0.025);
                break;
            default:
                funcionario.setBonificacao(0);
                break;
        }

        calculaIRRF(funcionario);


    }

    public void calculaIRRF(Funcionario funcionario) {
        double salario = funcionario.getSalario();
        double aliquota = 0;
        double deducao = 0;

        if (salario <= 2428.80) {
            aliquota = 0;
            deducao = 0;
        } else if (salario <= 2826.65) {
            aliquota = 7.5;
            deducao = 182.16;
        } else if (salario <= 3751.05) {
            aliquota = 15.0;
            deducao = 394.16;
        } else if (salario <= 4664.68) {
            aliquota = 22.5;
            deducao = 675.49;
        } else {
            aliquota = 27.5;
            deducao = 908.75;
        }

        funcionario.setPercentualIrrf(aliquota);

        double valorIRRF = (salario * (aliquota / 100)) - deducao;
        if (valorIRRF < 0) {
            valorIRRF = 0;
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