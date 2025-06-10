package org.br.farmacia.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.br.farmacia.models.Produto;
import org.br.farmacia.services.ProdutoService;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/produtos")
public class ProdutoController extends HttpServlet {

    private ProdutoService produtoService;
    private final Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        produtoService = new ProdutoService(context);
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
        PrintWriter out = resp.getWriter();

        List<Produto> produtos = produtoService.listarProdutos();
        out.println(gson.toJson(produtos));
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCorsHeaders(resp);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        try {
            ProdutoInput input = gson.fromJson(req.getReader(), ProdutoInput.class);

            Produto novoProduto = new Produto(
                    input.nome,
                    input.precoCompra,
                    input.precoVenda,
                    input.quantidadeEstoque
            );

            boolean sucesso = produtoService.adicionarProduto(novoProduto);

            JsonObject json = new JsonObject();
            if (sucesso) {
                json.addProperty("message", "Produto adicionado com sucesso");
            } else {
                json.addProperty("message", "Falha ao adicionar produto");
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

            out.println(gson.toJson(json));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonObject error = new JsonObject();
            error.addProperty("error", "Erro ao adicionar produto: " + e.getMessage());
            out.println(gson.toJson(error));
        }

        out.flush();
    }

    private static class ProdutoInput {
        String nome;
        double precoCompra;
        double precoVenda;
        int quantidadeEstoque;
    }
}
