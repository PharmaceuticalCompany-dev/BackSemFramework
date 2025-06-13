package org.br.farmacia.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.br.farmacia.services.CaixaServices;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/caixa/inicializar")
public class InicializarCaixaController extends HttpServlet {

    private CaixaServices caixaServices;

    @Override
    public void init() throws ServletException {
        caixaServices = new CaixaServices(getServletContext());
    }

    private void setCorsHeaders(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        resp.setHeader("Access-Control-Max-Age", "3600");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCorsHeaders(resp);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        JsonObject input = new Gson().fromJson(req.getReader(), JsonObject.class);
        int empresaId = input.get("empresaId").getAsInt();
        double valorInicial = input.get("valorInicial").getAsDouble();

        caixaServices.inicializarCaixaEmpresa(empresaId, valorInicial);

        out.println(new Gson().toJson("Caixa da empresa inicializado com sucesso!"));
        out.flush();
    }
}
