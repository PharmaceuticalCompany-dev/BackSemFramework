package org.br.farmacia.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonParseException;

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

        try {
            VendasProgramadasInput input = gson.fromJson(req.getReader(), VendasProgramadasInput.class);

            VendasProgramadas novaVenda = new VendasProgramadas(
                    input.id,
                    LocalDate.parse(input.dataVenda, DateTimeFormatter.ISO_LOCAL_DATE),
                    input.valorVenda,
                    input.custoProduto,
                    input.produtoId,
                    input.empresaId
            );

            vendasProgramadasService.adicionarVendaProgramada(novaVenda);

            JsonObject json = new JsonObject();
            json.addProperty("message", "Venda programada adicionada com sucesso!");
            resp.setStatus(HttpServletResponse.SC_CREATED);
            out.println(gson.toJson(json));

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonObject error = new JsonObject();
            error.addProperty("error", "Erro ao adicionar venda programada: " + e.getMessage());
            out.println(gson.toJson(error));
        }

        out.flush();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setCorsHeaders(resp);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        JsonObject json = new JsonObject();
        json.addProperty("message", "Método PUT não implementado ainda.");
        out.println(gson.toJson(json));
        out.flush();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setCorsHeaders(resp);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        JsonObject json = new JsonObject();
        json.addProperty("message", "Método DELETE não implementado ainda.");
        out.println(gson.toJson(json));
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
        private double valorVenda;
        private double custoProduto;
        private int produtoId;
        private Integer empresaId;
    }
}
