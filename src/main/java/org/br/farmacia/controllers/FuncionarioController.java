package org.br.farmacia.controllers;

import com.google.gson.*;
import org.br.farmacia.enums.Cargo;
import org.br.farmacia.enums.Genero;
import org.br.farmacia.models.Funcionario;
import org.br.farmacia.services.FuncionarioService;
import org.br.farmacia.util.LocalDateAdapter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;



@WebServlet("/funcionarios")
public class FuncionarioController extends HttpServlet {

    private FuncionarioService funcionarioService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        funcionarioService = new FuncionarioService(context);

        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
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
        List<Funcionario> funcionarios = funcionarioService.listarFuncionarios();
        resp.getWriter().println(gson.toJson(funcionarios));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCorsHeaders(resp);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        FuncionarioInput input = gson.fromJson(req.getReader(), FuncionarioInput.class);
        Genero generoEnum = input.genero != null ? Genero.valueOf(input.genero.toUpperCase()) : null;
        Cargo cargoEnum = input.cargo != null ? Cargo.valueOf(input.cargo.toUpperCase()) : null;

        Funcionario novoFuncionario = new Funcionario(
                input.id,
                input.nome,
                LocalDate.parse(input.dataNascimento, DateTimeFormatter.ISO_LOCAL_DATE),
                generoEnum,
                input.salario,
                input.setor_id,
                cargoEnum
        );
        funcionarioService.adicionarFuncionario(novoFuncionario);

        out.println(gson.toJson("Funcionario cadastrado com sucesso!"));
        out.flush();
    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCorsHeaders(resp);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        FuncionarioInput input = gson.fromJson(req.getReader(), FuncionarioInput.class);
        Genero generoEnum = input.genero != null ? Genero.valueOf(input.genero.toUpperCase()) : null;
        Cargo cargoEnum = input.cargo != null ? Cargo.valueOf(input.cargo.toUpperCase()) : null;

        Funcionario novoFuncionario = new Funcionario(
                input.id,
                input.nome,
                LocalDate.parse(input.dataNascimento, DateTimeFormatter.ISO_LOCAL_DATE),
                generoEnum,
                input.salario,
                input.setor_id,
                cargoEnum
        );
        funcionarioService.editarFuncionario(input.id,  novoFuncionario);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCorsHeaders(resp);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        int id = Integer.parseInt(req.getParameter("id"));
        funcionarioService.removerFuncionario(id);

        out.println(gson.toJson("Funcionario deletado com sucesso!"));
        out.flush();
    }

    private static class FuncionarioInput {
        int id;
        String nome;
        String dataNascimento;
        String genero;
        String cargo;
        int setor_id;
        double salario;
        double valeRefeicao;
        double valeAlimentacao;
        double planoSaude;
        double planoOdonto;
        double percentualIRRF;
        double percentualINSS;
        double bonificacao;
    }







    /*
    {

    JSON PRA TESTAR A API
  "id": 1,
  "nome": "Danilo",
  "dataNascimento": "2004-07-11",
  "genero": "MASCULINO",
  "cargo": "GERENTE",
  "setor_id": 1,
  "salario": 2400.00
}

     */
}
