package org.br.farmacia.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.br.farmacia.models.Transportadora;
import org.br.farmacia.services.TransportadoraService;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/transportadoras")
public class TransportadoraController extends HttpServlet {
    private TransportadoraService transportadoraService;
    private Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        transportadoraService = new TransportadoraService(servletContext);
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

        List<Transportadora> transportadoras = transportadoraService.listarTransportadoras();
        out.println(gson.toJson(transportadoras));
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setCorsHeaders(resp);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        TransportadoraInput input = gson.fromJson(req.getReader(), TransportadoraInput.class);

        Transportadora novaTransportadora = new Transportadora(
                input.id,
                input.nome,
                input.contato,
                input.telefone,
                input.regiao
        );



        transportadoraService.adicionarTransportadora(novaTransportadora);

        JsonObject json = new JsonObject();
        json.addProperty("message", "Transportadora adicionada com sucesso");
        out.println(gson.toJson(json));

        out.flush();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setCorsHeaders(resp);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        TransportadoraInput input = gson.fromJson(req.getReader(), TransportadoraInput.class);

        Transportadora transportadoraAtualizada = new Transportadora(
                input.id,
                input.nome,
                input.contato,
                input.telefone,
                input.regiao
        );


        boolean sucesso = transportadoraService.editarTransportadora(input.id, transportadoraAtualizada);

        JsonObject json = new JsonObject();
        if (sucesso) {
            json.addProperty("message", "Transportadora atualizada com sucesso");
        } else {
            json.addProperty("message", "Transportadora não encontrada");
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

        out.println(gson.toJson(json));
        out.flush();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setCorsHeaders(resp);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        TransportadoraInput input = gson.fromJson(req.getReader(), TransportadoraInput.class);

        boolean sucesso = transportadoraService.removerTransportadora(input.id);

        JsonObject json = new JsonObject();
        if (sucesso) {
            json.addProperty("message", "Transportadora deletada com sucesso");
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            json.addProperty("message", "Transportadora não encontrada");
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

        out.println(gson.toJson(json));

        out.flush();


    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setCorsHeaders(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }


    private static class TransportadoraInput {
        int id;
        String nome;
        String contato;
        String telefone;
        String regiao;
        Integer empresaId;
    }

}
