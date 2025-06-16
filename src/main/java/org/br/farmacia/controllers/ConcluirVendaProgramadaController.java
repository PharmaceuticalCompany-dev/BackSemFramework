package org.br.farmacia.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.br.farmacia.services.VendasProgramadasService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/vendasProg/concluir")
public class ConcluirVendaProgramadaController extends HttpServlet {

    private VendasProgramadasService service;

    @Override
    public void init() {
        service = new VendasProgramadasService(getServletContext());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(req.getReader(), JsonObject.class);

        if (jsonObject == null || !jsonObject.has("id")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Parâmetro 'id' é obrigatório\"}");
            return;
        }

        int id = jsonObject.get("id").getAsInt();

        boolean sucesso = service.concluirVendaProgramada(id);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter writer = resp.getWriter();
        writer.print("{\"concluida\": " + sucesso + "}");
        writer.flush();
    }

}

