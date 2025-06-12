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
        List<Produto> produtos = produtoService.listarProdutos();
        resp.getWriter().println(gson.toJson(produtos));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCorsHeaders(resp);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        ProdutoInput input = gson.fromJson(req.getReader(), ProdutoInput.class);
        Produto novoProduto = new Produto(input.nome, input.precoCompra, input.precoVenda, input.quantidadeEstoque);
        produtoService.adicionarProduto(novoProduto);

        out.println(gson.toJson("Produto cadastrado com sucesso!"));
        out.flush();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        ProdutoInput input = gson.fromJson(req.getReader(), ProdutoInput.class);
        Produto produto = new Produto(input.id, input.nome, input.precoCompra, input.precoVenda, input.quantidadeEstoque);
        produtoService.editarProduto(input.id, produto);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCorsHeaders(resp);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        int id = Integer.parseInt(req.getParameter("id"));
        produtoService.removerProduto(id);

        out.println(gson.toJson("Produto removido com sucesso!"));
        out.flush();
    }


    private static class ProdutoInput {
        int id;
        String nome;
        double precoCompra;
        double precoVenda;
        int quantidadeEstoque;
    }
}
