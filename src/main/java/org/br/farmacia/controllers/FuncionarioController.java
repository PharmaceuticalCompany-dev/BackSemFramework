package org.br.farmacia.controllers;

//Gson é uma biblioteca do Google para converter objetos Java em JSON e vice-versa
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.br.farmacia.enums.Cargo;
import org.br.farmacia.enums.Genero;
import org.br.farmacia.models.Funcionario;
import org.br.farmacia.services.FuncionarioService;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

//WebServlet é uma anotação para mapear a classe a uma URL específica.
@WebServlet("/funcionarios")
//HttpServlet é a classe base para implementar servlets que respondem a requisições HTTP.
//ServletContext fornece contexto da aplicação para compartilhar informações (como a conexão do banco).
public class FuncionarioController extends HttpServlet {

    private FuncionarioService funcionarioService;
    private Gson gson = new Gson();

    //init() é chamado pelo servidor uma vez quando o servlet é criado.
    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        funcionarioService = new FuncionarioService(context);

        //Funcionario exemplo = new Funcionario("João da Silva", 5, 35, Genero.MASCULINO, Cargo.GERENTE, 5000);
        //funcionarioService.adicionarFuncionario(exemplo);
    }



    //quando recebe uma requisição get ele chama esse metodo.
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        List<Funcionario> funcionarios = funcionarioService.listarFuncionarios();
        out.println(gson.toJson(funcionarios));
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        try {
            FuncionarioInput input = gson.fromJson(req.getReader(), FuncionarioInput.class);

            Funcionario novoFuncionario = new Funcionario(
                    input.nome,
                    input.id,
                    input.idade,
                    Genero.valueOf(input.genero.toUpperCase()),
                    Cargo.valueOf(input.cargo.toUpperCase()),
                    input.salario
            );

            funcionarioService.adicionarFuncionario(novoFuncionario);

            JsonObject json = new JsonObject();
            json.addProperty("message", "Funcionário adicionado com sucesso");
            out.println(gson.toJson(json));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonObject error = new JsonObject();
            error.addProperty("error", "Erro ao adicionar funcionário: " + e.getMessage());
            out.println(gson.toJson(error));
        }

        out.flush();
    }


    private static class FuncionarioInput {
        int id;
        String nome;
        int idade;
        String genero;
        String cargo;
        double salario;
    }
}
