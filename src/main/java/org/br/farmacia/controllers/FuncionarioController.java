package org.br.farmacia.controllers;

import com.google.gson.*;
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

@WebServlet("/funcionarios")
public class FuncionarioController extends HttpServlet {

    private FuncionarioService funcionarioService;
    private Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        funcionarioService = new FuncionarioService(context);

        gson = new GsonBuilder().create();
    }

    private void setCorsHeaders(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        resp.setHeader("Access-Control-Max-Age", "3600");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCorsHeaders(resp);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        List<Funcionario> funcionarios = funcionarioService.listarFuncionarios();
        out.println(gson.toJson(funcionarios));
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCorsHeaders(resp);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        try {
            FuncionarioInput input = gson.fromJson(req.getReader(), FuncionarioInput.class);

            Funcionario novoFuncionario = new Funcionario(
                    input.id,
                    input.nome,
                    Genero.valueOf(input.genero.toUpperCase()),
                    Cargo.valueOf(input.cargo.toUpperCase()),
                    input.salario,
                    input.valeRefeicao,
                    input.valeAlimentacao,
                    input.planoSaude,
                    input.planoOdonto,
                    input.percentualIrrf,
                    input.percentualInss,
                    input.bonificacao // Pass bonificacao from input
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

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCorsHeaders(resp);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        try {
            // Assuming the ID is passed as a path parameter or query parameter,
            // or within the JSON body if it's for update.
            // Let's assume it's a query parameter for simplicity: /funcionarios?id=X
            String idParam = req.getParameter("id");
            if (idParam == null || idParam.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                JsonObject error = new JsonObject();
                error.addProperty("error", "ID do funcionário não fornecido para atualização.");
                out.println(gson.toJson(error));
                return;
            }
            int funcionarioId = Integer.parseInt(idParam);


            FuncionarioInput input = gson.fromJson(req.getReader(), FuncionarioInput.class);

            Funcionario funcionarioAtualizado = new Funcionario(
                    funcionarioId, // Use the ID from the path/query
                    input.nome,
                    Genero.valueOf(input.genero.toUpperCase()),
                    Cargo.valueOf(input.cargo.toUpperCase()),
                    input.salario,
                    input.valeRefeicao,
                    input.valeAlimentacao,
                    input.planoSaude,
                    input.planoOdonto,
                    input.percentualIrrf,
                    input.percentualInss,
                    input.bonificacao // Pass bonificacao from input
            );

            boolean success = funcionarioService.editarFuncionario(funcionarioId, funcionarioAtualizado);

            if (success) {
                JsonObject json = new JsonObject();
                json.addProperty("message", "Funcionário atualizado com sucesso");
                out.println(gson.toJson(json));
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                JsonObject error = new JsonObject();
                error.addProperty("error", "Funcionário com ID " + funcionarioId + " não encontrado.");
                out.println(gson.toJson(error));
            }

        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonObject error = new JsonObject();
            error.addProperty("error", "ID do funcionário inválido: " + e.getMessage());
            out.println(gson.toJson(error));
        }
        catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonObject error = new JsonObject();
            error.addProperty("error", "Erro ao atualizar funcionário: " + e.getMessage());
            out.println(gson.toJson(error));
        }

        out.flush();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCorsHeaders(resp);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        try {
            String idParam = req.getParameter("id");
            if (idParam == null || idParam.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                JsonObject error = new JsonObject();
                error.addProperty("error", "ID do funcionário não fornecido para exclusão.");
                out.println(gson.toJson(error));
                return;
            }
            int funcionarioId = Integer.parseInt(idParam);

            funcionarioService.removerFuncionario(funcionarioId);

            JsonObject json = new JsonObject();
            json.addProperty("message", "Funcionário removido com sucesso");
            out.println(gson.toJson(json));
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonObject error = new JsonObject();
            error.addProperty("error", "ID do funcionário inválido: " + e.getMessage());
            out.println(gson.toJson(error));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonObject error = new JsonObject();
            error.addProperty("error", "Erro ao remover funcionário: " + e.getMessage());
            out.println(gson.toJson(error));
        }

        out.flush();
    }


    private static class FuncionarioInput {
        int id;
        String nome;
        String genero;
        String cargo;
        double salario;
        double valeRefeicao;
        double valeAlimentacao;
        double planoSaude;
        double planoOdonto;
        double percentualIrrf;
        double percentualInss;
        double bonificacao; // Added bonificacao to input
    }
}