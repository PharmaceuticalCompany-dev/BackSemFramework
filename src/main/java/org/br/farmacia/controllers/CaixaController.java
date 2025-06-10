package org.br.farmacia.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.br.farmacia.models.Caixa;
import org.br.farmacia.services.CaixaServices;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/caixas")
public class CaixaController extends HttpServlet {

    private CaixaServices caixaService;
    private Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        caixaService = new CaixaServices(context);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        String idParam = req.getParameter("id");
        if (idParam != null) {
            try {
                int id = Integer.parseInt(idParam);
                Caixa caixa = caixaService.buscarPorId(id);
                if (caixa != null) {
                    out.println(gson.toJson(caixa));
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    JsonObject error = new JsonObject();
                    error.addProperty("error", "Caixa not found");
                    out.println(gson.toJson(error));
                }
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                JsonObject error = new JsonObject();
                error.addProperty("error", "Invalid ID format");
                out.println(gson.toJson(error));
            }
        } else {
            List<Caixa> caixas = caixaService.listarCaixas();
            out.println(gson.toJson(caixas));
        }
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        try {
            CaixaInput input = gson.fromJson(req.getReader(), CaixaInput.class);

            Caixa novoCaixa = new Caixa(input.valorTotal);

            if (caixaService.adicionarCaixa(novoCaixa)) {
                JsonObject json = new JsonObject();
                json.addProperty("message", "Caixa adicionado com sucesso");
                out.println(gson.toJson(json));
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                JsonObject error = new JsonObject();
                error.addProperty("error", "Falha ao adicionar caixa");
                out.println(gson.toJson(error));
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonObject error = new JsonObject();
            error.addProperty("error", "Erro ao adicionar caixa: " + e.getMessage());
            out.println(gson.toJson(error));
        }
        out.flush();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        try {
            CaixaInput input = gson.fromJson(req.getReader(), CaixaInput.class);
            int id = input.id; // Assumindo que o ID é enviado no corpo da requisição para atualização

            Caixa caixaAtualizado = new Caixa(id, input.valorTotal);

            if (caixaService.editarCaixa(id, caixaAtualizado)) {
                JsonObject json = new JsonObject();
                json.addProperty("message", "Caixa atualizado com sucesso");
                out.println(gson.toJson(json));
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                JsonObject error = new JsonObject();
                error.addProperty("error", "Caixa não encontrado ou falha na atualização");
                out.println(gson.toJson(error));
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonObject error = new JsonObject();
            error.addProperty("error", "Erro ao atualizar caixa: " + e.getMessage());
            out.println(gson.toJson(error));
        }
        out.flush();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonObject error = new JsonObject();
            error.addProperty("error", "ID do caixa é necessário para exclusão.");
            out.println(gson.toJson(error));
            out.flush();
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            caixaService.removerCaixa(id);
            JsonObject json = new JsonObject();
            json.addProperty("message", "Caixa removido com sucesso");
            out.println(gson.toJson(json));
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonObject error = new JsonObject();
            error.addProperty("error", "Formato de ID inválido.");
            out.println(gson.toJson(error));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonObject error = new JsonObject();
            error.addProperty("error", "Erro ao remover caixa: " + e.getMessage());
            out.println(gson.toJson(error));
        }
        out.flush();
    }

    private static class CaixaInput {
        int id; // Para updates e buscas específicas
        double valorTotal;
    }
}