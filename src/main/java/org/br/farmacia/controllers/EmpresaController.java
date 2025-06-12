package org.br.farmacia.controllers;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.br.farmacia.models.Empresa;
import org.br.farmacia.repositories.EmpresaRepository;
import org.br.farmacia.services.EmpresaService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/empresa")
public class EmpresaController extends HttpServlet {

    private EmpresaService empresaService;
    private Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        EmpresaRepository empresaRepository = new EmpresaRepository(context);
        this.empresaService = new EmpresaService(empresaRepository);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        List<Empresa> empresas = empresaService.listarEmpresas();
        out.println(gson.toJson(empresas));

        out.flush();
    }
}
