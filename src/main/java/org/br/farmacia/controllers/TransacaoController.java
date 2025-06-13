package org.br.farmacia.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.br.farmacia.models.Caixa;
import org.br.farmacia.enums.TipoTransacao;
import org.br.farmacia.services.CaixaServices;
import org.br.farmacia.utilities.LocalDateTimeAdapter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

@WebServlet("/caixa/transacao")
public class TransacaoController extends HttpServlet {

    private CaixaServices caixaServices;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

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

        TransacaoInput input = gson.fromJson(req.getReader(), TransacaoInput.class);

        Caixa novaTransacao = caixaServices.registrarTransacao(
                input.tipo,
                input.valor,
                input.descricao,
                input.empresaId
        );

        if (novaTransacao != null) {
            out.println(gson.toJson("Transação registrada com sucesso!"));
        } else {
            out.println(gson.toJson("Erro ao registrar a transação."));
        }
        out.flush();
    }

    private static class TransacaoInput {
        TipoTransacao tipo;
        double valor;
        String descricao;
        int empresaId;
    }
}
