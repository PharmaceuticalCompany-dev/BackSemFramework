package org.br.farmacia.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.br.farmacia.models.Caixa;
import org.br.farmacia.services.CaixaServices;
import org.br.farmacia.utilities.LocalDateTimeAdapter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/caixa/pagar-salarios")
public class PagarSalarioController extends HttpServlet {

    private CaixaServices caixaServices;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    @Override
    public void init() throws ServletException {
        caixaServices = new CaixaServices(getServletContext());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            JsonObject input = new Gson().fromJson(req.getReader(), JsonObject.class);
            int empresaId = input.get("empresaId").getAsInt();

            Caixa transacaoSalarios = caixaServices.registrarPagamentoSalarios(empresaId);

            if (transacaoSalarios != null) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
                gson.toJson(transacaoSalarios, resp.getWriter());
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                gson.toJson(new ErrorResponse("Nenhum salário a pagar ou falha ao registrar."), resp.getWriter());
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            gson.toJson(new ErrorResponse("Erro ao processar o pagamento de salários: " + e.getMessage()), resp.getWriter());
        }
    }

    private static class ErrorResponse {
        String error;

        ErrorResponse(String error) {
            this.error = error;
        }
    }
}
