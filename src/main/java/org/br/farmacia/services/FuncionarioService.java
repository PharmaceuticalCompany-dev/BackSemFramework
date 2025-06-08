package org.br.farmacia.services;

import java.util.ArrayList;
import java.util.List;

import org.br.farmacia.enums.Cargo;
import org.br.farmacia.enums.TipoSetor;
import org.br.farmacia.models.Beneficios;
import org.br.farmacia.models.Funcionario;
import org.br.farmacia.models.Setor;

public class FuncionarioService {

    private List<Funcionario> funcionarios;

    public FuncionarioService(){
        this.funcionarios = new ArrayList<>();
    }

    public void inicializarFuncionario(Funcionario funcionario) {
        funcionario.setSetor(definirSetorPorCargo(funcionario.getCargo()));
        funcionario.setBeneficios(calcularBeneficiosPorCargo(funcionario.getCargo()));
    }

    public void adicionarFuncionario(Funcionario funcionario) {
        if (funcionario != null) {
            inicializarFuncionario(funcionario);
            funcionarios.add(funcionario);
        }
    }

    public void removerFuncionario(int id) {
        funcionarios.removeIf(f -> f.getId() == id);
    }

    public void editarFuncionario(int id, Funcionario novoFuncionario) {
        for (int i = 0; i < funcionarios.size(); i++) {
            if (funcionarios.get(i).getId() == id) {
                inicializarFuncionario(novoFuncionario); // atualiza setor e benefícios
                funcionarios.set(i, novoFuncionario);
                return;
            }
        }
    }

    public List<Funcionario> listarFuncionarios() {
        return funcionarios;
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
            default: throw new IllegalArgumentException("Cargo inválido");
        }

        return setarTipoSetor(tipoSetor);
    }

    private Beneficios calcularBeneficiosPorCargo(Cargo cargo) {
        double valeRefeicao = 300, valeAlimentacao = 300, planoSaude = 3000, planoOdonto = 3000;

        switch (cargo) {
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
                // sem alteração
                break;
        }

        return new Beneficios(valeRefeicao, valeAlimentacao, planoSaude, planoOdonto);
    }
}
