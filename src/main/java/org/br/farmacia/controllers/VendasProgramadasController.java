package org.br.farmacia.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.br.farmacia.models.VendasProgramadas;
import org.br.farmacia.services.VendasProgramadasService;
import org.br.farmacia.util.LocalDateAdapter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/vendasProg")
public class VendasProgramadasController extends HttpServlet {
    private VendasProgramadasService vendasProgramadasService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        vendasProgramadasService = new VendasProgramadasService(servletContext);

        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
    }

    private void setCorsHeaders(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        resp.setHeader("Access-Control-Max-Age", "3600");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setCorsHeaders(resp);
        resp.setContentType("application/json");

        PrintWriter out = resp.getWriter();

        List<VendasProgramadas> vendasProgramadas = vendasProgramadasService.listar();
        out.println(gson.toJson(vendasProgramadas));
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setCorsHeaders(resp);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        VendasProgramadasInput input = gson.fromJson(req.getReader(), VendasProgramadasInput.class);

        VendasProgramadas novaVenda = new VendasProgramadas(
                input.id,
                LocalDate.parse(input.dataVenda, DateTimeFormatter.ISO_LOCAL_DATE),
                input.produtoId,
                input.quantidade,
                input.empresaId
        );

        vendasProgramadasService.adicionarVendaProgramada(novaVenda);

        JsonObject json = new JsonObject();
        json.addProperty("message", "Venda programada adicionada com sucesso!");
        resp.setStatus(HttpServletResponse.SC_CREATED);
        out.println(gson.toJson(json));

        out.flush();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setCorsHeaders(resp);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        VendasProgramadasInput input = gson.fromJson(req.getReader(), VendasProgramadasInput.class);

        if (input.id == 0) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonObject error = new JsonObject();
            error.addProperty("error", "ID da venda programada é obrigatório para atualização.");
            out.println(gson.toJson(error));
            return;
        }

        VendasProgramadas vendaParaAtualizar = new VendasProgramadas(
                input.id,
                LocalDate.parse(input.dataVenda, DateTimeFormatter.ISO_LOCAL_DATE),
                input.produtoId,
                input.quantidade,
                input.empresaId
        );

        if (vendasProgramadasService.editarVendaProgramada(input.id, vendaParaAtualizar)) {
            JsonObject json = new JsonObject();
            json.addProperty("message", "Venda programada atualizada com sucesso!");
            resp.setStatus(HttpServletResponse.SC_OK);
            out.println(gson.toJson(json));
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            JsonObject error = new JsonObject();
            error.addProperty("error", "Venda programada com ID " + input.id + " não encontrada.");
            out.println(gson.toJson(error));
        }
        out.flush();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setCorsHeaders(resp);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        String idParam = req.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonObject error = new JsonObject();
            error.addProperty("error", "ID da venda programada é obrigatório para deletar.");
            out.println(gson.toJson(error));
            out.flush();
            return;
        }

        int id = Integer.parseInt(idParam);
        if (vendasProgramadasService.removerVendaProgramada(id)) {
            JsonObject json = new JsonObject();
            json.addProperty("message", "Venda programada deletada com sucesso!");
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            out.println(gson.toJson(json));
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            JsonObject error = new JsonObject();
            error.addProperty("error", "Venda programada com ID " + id + " não encontrada.");
            out.println(gson.toJson(error));
        }
        out.flush();
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setCorsHeaders(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private static class VendasProgramadasInput {
        private int id;
        private String dataVenda;
        private int produtoId;
        private int quantidade;
        private Integer empresaId;
    }
}