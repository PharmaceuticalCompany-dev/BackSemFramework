package org.br.farmacia.controllers;

import org.br.farmacia.services.VendasProgramadasService;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/vendasProg/custo-anual-atual")
public class CustoAnualController extends HttpServlet {
    private VendasProgramadasService service;

    @Override
    public void init() {
        ServletContext context = getServletContext();
        service = new VendasProgramadasService(context);
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
        double custoAnualAtual = service.calcularCustoAnual();

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter writer = resp.getWriter();
        writer.print("{\"anoAtual\": " + java.time.LocalDate.now().getYear() + ", \"custoAnual\": " + custoAnualAtual + "}");
        writer.flush();
    }
}
