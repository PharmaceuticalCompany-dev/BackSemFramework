package org.br.farmacia.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.br.farmacia.services.CaixaServices;
import org.br.farmacia.utilities.LocalDateTimeAdapter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/caixa/total")
public class CaixaTotalController extends HttpServlet {

    private CaixaServices caixaServices;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    @Override
    public void init() throws ServletException {
        caixaServices = new CaixaServices(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            String empresaIdStr = req.getParameter("empresaId");
            if (empresaIdStr == null || empresaIdStr.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                gson.toJson(new ErrorResponse("O parâmetro 'empresaId' é obrigatório."), resp.getWriter());
                return;
            }

            int empresaId = Integer.parseInt(empresaIdStr);
            double total = caixaServices.getCaixaTotal(empresaId);

            JsonObject result = new JsonObject();
            result.addProperty("totalCaixa", total);
            gson.toJson(result, resp.getWriter());

        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            gson.toJson(new ErrorResponse("O 'empresaId' deve ser um número válido."), resp.getWriter());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            gson.toJson(new ErrorResponse("Erro ao processar a requisição: " + e.getMessage()), resp.getWriter());
        }
    }

    private static class ErrorResponse {
        String error;

        ErrorResponse(String error) {
            this.error = error;
        }
    }
}
