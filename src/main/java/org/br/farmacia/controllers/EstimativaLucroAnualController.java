package org.br.farmacia.controllers;

import org.br.farmacia.services.VendasProgramadasService;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/vendasProg/estimativa-lucro-anual")
public class EstimativaLucroAnualController extends HttpServlet {
    private VendasProgramadasService service;

    @Override
    public void init() {
        ServletContext context = getServletContext();
        service = new VendasProgramadasService(context);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        double estimativa = service.estimarLucroAnualPorMediaMensal();

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter writer = resp.getWriter();
        writer.print("{\"estimativaLucroAnual\": " + estimativa + "}");
        writer.flush();
    }
}
