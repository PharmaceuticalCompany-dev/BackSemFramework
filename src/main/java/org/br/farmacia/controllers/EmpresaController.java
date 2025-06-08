package org.br.farmacia.controllers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.br.farmacia.models.Empresa;
import org.br.farmacia.models.Produto;
import org.br.farmacia.models.VendasProgramadas;
import org.br.farmacia.services.EmpresaService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/empresa")
public class EmpresaController extends HttpServlet {

    private EmpresaService empresaService;
    private Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        // Cria a empresa com listas vazias
        Empresa empresa = new Empresa(
                "Farmácia XYZ",
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        // Adiciona um produto na empresa
        empresa.getProdutos().add(new Produto(1,"Paracetamol", 1.0, 2.0, 3));

        // Cria o service passando a empresa
        empresaService = new EmpresaService(empresa);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        if ("lucro".equals(action)) {
            try {
                int ano = Integer.parseInt(req.getParameter("ano"));
                int mes = Integer.parseInt(req.getParameter("mes"));

                double lucro = empresaService.lucroMes(ano, mes);

                JsonObject json = new JsonObject();
                json.addProperty("ano", ano);
                json.addProperty("mes", mes);
                json.addProperty("lucro", lucro);

                out.println(gson.toJson(json));
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                JsonObject json = new JsonObject();
                json.addProperty("error", "Parâmetros 'ano' e 'mes' inválidos");
                out.println(gson.toJson(json));
            }
        } else if ("vendas".equals(action)) {
            List<VendasProgramadas> vendas = empresaService.getVendasProgramadas();
            out.println(gson.toJson(vendas));
        } else {
            JsonObject json = new JsonObject();
            json.addProperty("nome", empresaService.getEmpresa().getNome());
            out.println(gson.toJson(json));
        }
        out.flush();
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        try {
            ProdutoVendaInput input = gson.fromJson(req.getReader(), ProdutoVendaInput.class);

            Produto produto = empresaService.getEmpresa()
                    .getProdutos()
                    .stream()
                    .filter(p -> p.getId() == input.produtoId)
                    .findFirst()
                    .orElse(null);

            if (produto == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println("{\"error\":\"Produto não encontrado. ID: " + input.produtoId + "\"}");
            } else {
                empresaService.programarVenda(input.ano, input.mes, produto, input.quantidade);
                out.println("{\"message\":\"Venda programada com sucesso\"}");
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println("{\"error\":\"Erro ao processar requisição POST: " + e.getMessage() + "\"}");
        }
        out.flush();
    }

    private static class ProdutoVendaInput {
        int ano;
        int mes;
        int produtoId;
        int quantidade;
    }
}
