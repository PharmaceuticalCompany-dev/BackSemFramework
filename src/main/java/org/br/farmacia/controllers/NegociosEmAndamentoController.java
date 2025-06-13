package org.br.farmacia.controllers;

import com.google.gson.Gson;
import org.br.farmacia.models.NegociosEmAndamento;
import org.br.farmacia.services.NegociosEmAndamentoService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/negocios")
public class NegociosEmAndamentoController extends HttpServlet {

    private NegociosEmAndamentoService service;
    private final Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        service = new NegociosEmAndamentoService(getServletContext());
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
        List<NegociosEmAndamento> lista = service.getAll();
        resp.getWriter().println(gson.toJson(lista));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCorsHeaders(resp);
        resp.setContentType("application/json");
        NegocioInput input = gson.fromJson(req.getReader(), NegocioInput.class);
        PrintWriter out = resp.getWriter();

        NegociosEmAndamento negocio = new NegociosEmAndamento();
        negocio.setTipo(input.tipo);
        negocio.setStatus(input.status);
        service.adicionarNegocio(negocio);

        out.println(gson.toJson("Negocio em andamento cadastrado com sucesso!"));
        out.flush();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        NegocioInput input = gson.fromJson(req.getReader(), NegocioInput.class);
        NegociosEmAndamento negocio = new NegociosEmAndamento(input.id, input.tipo, input.status);
        service.update(input.id, negocio);
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCorsHeaders(resp);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        int id = Integer.parseInt(req.getParameter("id"));
        service.delete(id);

        out.println(gson.toJson("Negócio removido com sucesso!"));
        out.flush();
    }


    private static class NegocioInput {
        Integer id;
        String tipo;
        String status;
    }
}
