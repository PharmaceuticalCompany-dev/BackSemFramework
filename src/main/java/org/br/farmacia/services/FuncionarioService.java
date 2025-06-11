package org.br.farmacia.services;

import java.util.ArrayList;
import java.util.List;

import org.br.farmacia.enums.Cargo;
import org.br.farmacia.enums.TipoSetor;
import org.br.farmacia.models.Funcionario;
import org.br.farmacia.models.Setor;
import org.br.farmacia.repositories.FuncionarioRepository;

import javax.servlet.ServletContext;

public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    // Opcionalmente, se você tiver um SetorRepository para carregar setores existentes do banco
    // private final SetorRepository setorRepository;

    public FuncionarioService(ServletContext context) {
        this.funcionarioRepository = new FuncionarioRepository(context);
        // this.setorRepository = new SetorRepository(context); // Se precisar
    }

    // Este método agora preenche os atributos de benefício diretamente no objeto Funcionario
    public void inicializarFuncionario(Funcionario funcionario) {
        // Define o setor
        funcionario.setSetor(definirSetorPorCargo(funcionario.getCargo()));
        // Calcula e define os benefícios diretamente no objeto Funcionario
        calcularEAtribuirBeneficiosPorCargo(funcionario);
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
            // A lógica de inicialização pode ser reaplicada ao novoFuncionario
            // para garantir que os benefícios e setor sejam calculados/atribuídos corretamente
            inicializarFuncionario(novoFuncionario);
            novoFuncionario.setId(id); // Garante que o ID do novoFuncionario seja o do existente
            return funcionarioRepository.update(novoFuncionario);
        }
        return false;
    }

    public List<Funcionario> listarFuncionarios() {
        return  funcionarioRepository.findAll();
    }

    public Funcionario buscarPorId(int idEditar) {
        return funcionarioRepository.findById(idEditar);
    }


    private Setor setarTipoSetor(TipoSetor tipoSetor) {
        // Se Setor for apenas um tipo e não precisar ser carregado do banco
        return new Setor(tipoSetor, new ArrayList<>());
        // Se Setor for uma entidade do banco de dados e você precisar buscar por tipo,
        // você precisaria de um SetorRepository aqui:
        // return setorRepository.findByTipoSetor(tipoSetor);
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

        // Retorna um novo objeto Setor. Se a classe Setor for uma entidade do banco,
        // talvez você precise carregar um Setor existente ou ter uma lógica de persistência para ele.
        return setarTipoSetor(tipoSetor);
    }

    // Este método agora recebe um objeto Funcionario e atribui os benefícios diretamente a ele
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
                // Não há modificadores específicos para atendente aqui
                break;
        }

        // Atribuir os valores calculados diretamente ao objeto Funcionario
        funcionario.setValeRefeicao(valeRefeicao);
        funcionario.setValeAlimentacao(valeAlimentacao);
        funcionario.setPlanoSaude(planoSaude);
        funcionario.setPlanoOdonto(planoOdonto);
    }
}