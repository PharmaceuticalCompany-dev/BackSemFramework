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


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        List<Transportadora> transportadoras = transportadoraService.listarTransportadoras();
        out.println(gson.toJson(transportadoras));
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        try {
            TransportadoraInput input = gson.fromJson(req.getReader(), TransportadoraInput.class);

            Transportadora novaTransportadora = new Transportadora(
                    input.id,
                    input.nome,
                    new ArrayList<>(input.locaisAtendimento)
            );

            // 🚨 Aqui está o que estava faltando!
            novaTransportadora.setEmpresaId(input.empresaId);

            transportadoraService.adicionarTransportadora(novaTransportadora);

            JsonObject json = new JsonObject();
            json.addProperty("message", "Transportadora adicionada com sucesso");
            out.println(gson.toJson(json));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonObject error = new JsonObject();
            error.addProperty("error", "Erro ao adicionar transportadora: " + e.getMessage());
            out.println(gson.toJson(error));
        }

        out.flush();
    }


    private static class TransportadoraInput {
        int id;
        String nome;
        List<String> locaisAtendimento;
        Integer empresaId;
    }

}
