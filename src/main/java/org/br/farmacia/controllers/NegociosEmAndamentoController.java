package org.br.farmacia.controllers;

import com.google.gson.Gson;
import org.br.farmacia.models.NegociosEmAndamento;
import org.br.farmacia.services.NegociosEmAndamentoService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/negocios")
public class NegociosEmAndamentoController extends HttpServlet {

    private NegociosEmAndamentoService service;
    private final Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        service = new NegociosEmAndamentoService(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        List<NegociosEmAndamento> lista = service.getAll();
        resp.getWriter().println(gson.toJson(lista));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        NegocioInput input = gson.fromJson(req.getReader(), NegocioInput.class);

        NegociosEmAndamento negocio = new NegociosEmAndamento();
        negocio.setTipo(input.tipo);
        negocio.setStatus(input.status);

        boolean sucesso = service.create(negocio);
        resp.setStatus(sucesso ? HttpServletResponse.SC_OK : HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        if (sucesso) {
            resp.getWriter().println("{\"id\":" + negocio.getId() + "}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        NegocioInput input = gson.fromJson(req.getReader(), NegocioInput.class);

        if (input.id == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        NegociosEmAndamento negocio = new NegociosEmAndamento();
        negocio.setId(input.id);
        negocio.setTipo(input.tipo);
        negocio.setStatus(input.status);

        boolean sucesso = service.update(negocio);
        resp.setStatus(sucesso ? HttpServletResponse.SC_OK : HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String idParam = req.getParameter("id");

        if (idParam == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            boolean sucesso = service.delete(id);
            resp.setStatus(sucesso ? HttpServletResponse.SC_OK : HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private static class NegocioInput {
        Integer id;
        String tipo;
        String status;
    }
}
