package org.br.farmacia.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject; // Mantido, mas não usado diretamente para sucesso
import org.br.farmacia.models.Produto;
import org.br.farmacia.services.ProdutoService;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/produtos/*")
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
        resp.setCharacterEncoding("UTF-8");

        List<Produto> produtos = produtoService.listarProdutos();
        resp.getWriter().println(gson.toJson(produtos));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCorsHeaders(resp);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        req.setCharacterEncoding("UTF-8");

        try {
            ProdutoInput input = gson.fromJson(req.getReader(), ProdutoInput.class);

            Produto novoProduto = new Produto(input.nome, input.precoCompra, input.precoVenda, input.quantidadeEstoque);

            Produto produtoSalvo = produtoService.adicionarProduto(novoProduto);

            if (produtoSalvo != null) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
                out.println(gson.toJson(produtoSalvo));
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.println(gson.toJson("Erro ao cadastrar produto."));
            }
        } catch (Exception e) {
            System.err.println("Erro no doPost (adicionar produto): " + e.getMessage());
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println(gson.toJson("Erro na requisição: " + e.getMessage()));
        } finally {
            out.flush();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCorsHeaders(resp);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        req.setCharacterEncoding("UTF-8");

        try {
            String pathInfo = req.getPathInfo();
            int id = -1;
            if (pathInfo != null && pathInfo.length() > 1) {
                id = Integer.parseInt(pathInfo.substring(1));
            }

            if (id == -1) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println(gson.toJson("ID do produto não fornecido na URL."));
                return;
            }

            ProdutoInput input = gson.fromJson(req.getReader(), ProdutoInput.class);
            Produto produtoParaAtualizar = new Produto(id, input.nome, input.precoCompra, input.precoVenda, input.quantidadeEstoque);

            boolean sucesso = produtoService.editarProduto(id, produtoParaAtualizar);

            if (sucesso) {
                resp.setStatus(HttpServletResponse.SC_OK); // 200 OK
                out.println(gson.toJson("Produto editado com sucesso!"));
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.println(gson.toJson("Erro ao editar produto ou produto não encontrado."));
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println(gson.toJson("ID do produto inválido na URL."));
        } catch (Exception e) {
            System.err.println("Erro no doPut (editar produto): " + e.getMessage());
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println(gson.toJson("Erro na requisição: " + e.getMessage()));
        } finally {
            out.flush();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCorsHeaders(resp);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            String pathInfo = req.getPathInfo();
            int id = -1;
            if (pathInfo != null && pathInfo.length() > 1) {
                id = Integer.parseInt(pathInfo.substring(1)); // Remove a '/' inicial
            }

            if (id == -1) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println(gson.toJson("ID do produto não fornecido na URL."));
                return;
            }

            boolean sucesso = produtoService.removerProduto(id);

            if (sucesso) {
                resp.setStatus(HttpServletResponse.SC_OK); // 200 OK
                out.println(gson.toJson("Produto removido com sucesso!"));
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404 Not Found
                out.println(gson.toJson("Produto não encontrado para remoção."));
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println(gson.toJson("ID do produto inválido na URL."));
        } catch (Exception e) {
            System.err.println("Erro no doDelete (remover produto): " + e.getMessage());
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println(gson.toJson("Erro interno ao remover produto: " + e.getMessage()));
        } finally {
            out.flush();
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setCorsHeaders(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }


    private static class ProdutoInput {
        String nome;
        double precoCompra;
        double precoVenda;
        int quantidadeEstoque;
    }
}