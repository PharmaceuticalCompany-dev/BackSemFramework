package org.br.farmacia.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.br.farmacia.enums.TipoTransacao;
import org.br.farmacia.models.Caixa;
import org.br.farmacia.services.CaixaServices;
import org.br.farmacia.utilities.LocalDateTimeAdapter; // Você precisará criar esta classe auxiliar

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/caixa/*")
public class CaixaController extends HttpServlet {

    private CaixaServices caixaServices;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        caixaServices = new CaixaServices(context);
    }

    private void setCorsHeaders(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        resp.setHeader("Access-Control-Max-Age", "3600");
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setCorsHeaders(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCorsHeaders(resp);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        String pathInfo = req.getPathInfo();

        try {
            String empresaIdStr = req.getParameter("empresaId");
            if (empresaIdStr == null || empresaIdStr.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println(gson.toJson(new ErrorResponse("O parâmetro 'empresaId' é obrigatório.")));
                return;
            }
            int empresaId = Integer.parseInt(empresaIdStr);

            if (pathInfo != null && pathInfo.equals("/total")) {
                double total = caixaServices.getCaixaTotal(empresaId);
                JsonObject result = new JsonObject();
                result.addProperty("totalCaixa", total);
                out.println(gson.toJson(result));
            }
            else {
                List<Caixa> transacoes = caixaServices.listarTransacoes(empresaId);
                out.println(gson.toJson(transacoes));
            }

        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println(gson.toJson(new ErrorResponse("O 'empresaId' deve ser um número válido.")));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println(gson.toJson(new ErrorResponse("Erro ao processar a requisição: " + e.getMessage())));
        }

        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCorsHeaders(resp);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        String pathInfo = req.getPathInfo();

        try {
            if (pathInfo != null && pathInfo.equals("/pagar-salarios")) {
                handlePagarSalarios(req, resp, out);
            }
            else if (pathInfo != null && pathInfo.equals("/inicializar")) {
                handleInicializarCaixa(req, resp, out);
            }
            else {
                handleRegistrarTransacao(req, resp, out);
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println(gson.toJson(new ErrorResponse("Erro ao processar JSON: " + e.getMessage())));
        }

        out.flush();
    }

    private void handleRegistrarTransacao(HttpServletRequest req, HttpServletResponse resp, PrintWriter out) throws IOException {
        TransacaoInput input = gson.fromJson(req.getReader(), TransacaoInput.class);

        Caixa novaTransacao = caixaServices.registrarTransacao(
                input.tipo,
                input.valor,
                input.descricao,
                input.empresaId
        );

        if (novaTransacao != null) {
            resp.setStatus(HttpServletResponse.SC_CREATED);
            out.println(gson.toJson(novaTransacao));
        } else {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println(gson.toJson(new ErrorResponse("Falha ao registrar a transação.")));
        }
    }

    private void handlePagarSalarios(HttpServletRequest req, HttpServletResponse resp, PrintWriter out) throws IOException {
        JsonObject input = gson.fromJson(req.getReader(), JsonObject.class);
        int empresaId = input.get("empresaId").getAsInt();

        Caixa transacaoSalarios = caixaServices.registrarPagamentoSalarios(empresaId);

        if (transacaoSalarios != null) {
            resp.setStatus(HttpServletResponse.SC_CREATED);
            out.println(gson.toJson(transacaoSalarios));
        } else {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println(gson.toJson(new ErrorResponse("Nenhum salário a pagar ou falha ao registrar.")));
        }
    }

    private void handleInicializarCaixa(HttpServletRequest req, HttpServletResponse resp, PrintWriter out) throws IOException {
        InicializacaoInput input = gson.fromJson(req.getReader(), InicializacaoInput.class);

        caixaServices.inicializarCaixaEmpresa(input.empresaId, input.valorInicial);

        JsonObject result = new JsonObject();
        result.addProperty("message", "Caixa da empresa " + input.empresaId + " inicializado com sucesso.");
        out.println(gson.toJson(result));
    }

    private static class TransacaoInput {
        TipoTransacao tipo;
        double valor;
        String descricao;
        int empresaId;
    }

    private static class InicializacaoInput {
        int empresaId;
        double valorInicial;
    }

    private static class ErrorResponse {
        String error;
        ErrorResponse(String error) { this.error = error; }
    }
}