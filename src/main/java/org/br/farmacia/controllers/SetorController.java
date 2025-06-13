package org.br.farmacia.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.br.farmacia.enums.TipoSetor;
import org.br.farmacia.models.Setor;
import org.br.farmacia.services.SetorService;
import org.br.farmacia.util.LocalDateAdapter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/setores")
public class SetorController extends HttpServlet {
    private SetorService setorService;
    private Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        setorService = new SetorService(getServletContext());

        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        String idParam = req.getParameter("id");

        if (idParam != null) {
            try {
                int id = Integer.parseInt(idParam);
                Setor setor = setorService.buscarPorId(id);
                if (setor != null) {
                    out.println(gson.toJson(setor));
                } else {
                    resp.setStatus(404);
                    out.println(gson.toJson(errorJson("Setor nao encontrado")));
                }
            } catch (NumberFormatException e) {
                resp.setStatus(400);
                out.println(gson.toJson(errorJson("ID invalido")));
            }
        } else {
            List<Setor> setores = setorService.listarSetores();
            out.println(gson.toJson(setores));
        }
        out.flush();
    }

    private JsonObject errorJson(String msg) {
        JsonObject json = new JsonObject();
        json.addProperty("error", msg);
        return json;
    }
}
