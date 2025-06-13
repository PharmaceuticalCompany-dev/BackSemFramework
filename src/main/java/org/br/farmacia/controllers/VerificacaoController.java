package org.br.farmacia.controllers;

import org.br.farmacia.enums.Cargo;
import org.br.farmacia.services.security.CargoComPermissao;

public class VerificacaoController {
    private Cargo getCargoDoUsuarioLogado() {
        // Lógica real aqui: obter o cargo do token, sessão, etc.
        // Por exemplo, de um contexto de segurança após autenticação.
        // Por enquanto, vamos mockar um cargo para testar
        return Cargo.GERENTE; // Retorna o cargo do usuário logado
    }

    public void adicionarFuncionario(/* parâmetros do funcionário */) {
        Cargo cargoUsuario = getCargoDoUsuarioLogado();
        CargoComPermissao.verificaPermissoes permissoes = CargoComPermissao.getPermissao(cargoUsuario);

        if (permissoes != null && permissoes.podeGerenciarFuncionarios) {
            System.out.println("Permissão concedida para adicionar funcionário.");
            // Chamar FuncionarioService para adicionar o funcionário
        } else {
            System.out.println("Acesso negado: Você não tem permissão para adicionar funcionários.");
            // Retornar um erro HTTP 403 Forbidden para o cliente (se for um servidor HTTP)
        }
    }

    public void listarFuncionarios() {
        Cargo cargoUsuario = getCargoDoUsuarioLogado();
        CargoComPermissao.verificaPermissoes permissoes = CargoComPermissao.getPermissao(cargoUsuario);

        // Exemplo: Todos podem listar funcionários, mas apenas alguns podem ver salários
        if (permissoes != null) { // Se o cargo for válido
            System.out.println("Permissão concedida para listar funcionários.");
            // Chamar FuncionarioService para listar
        } else {
            System.out.println("Acesso negado: Cargo inválido.");
        }
    }
}
