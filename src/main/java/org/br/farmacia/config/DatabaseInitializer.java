package org.br.farmacia.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;

@WebListener
public class DatabaseInitializer implements ServletContextListener {

    private Connection connection;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Class.forName("org.postgresql.Driver");

            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:8010/farmacia",
                    "docker",
                    "docker"
            );

            InputStream is = getClass().getClassLoader().getResourceAsStream("db/scripts.sql");
            if (is == null) {
                System.err.println("Arquivo SQL não encontrado");
                return;
            }
            String scriptSQL = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            String[] comandos = scriptSQL.split(";");

            try (Statement stmt = connection.createStatement()) {
                for (String comando : comandos) {
                    comando = comando.trim();
                    if (!comando.isEmpty()) {
                        stmt.execute(comando + ";");
                    }
                }
            }

            // Checar se já existe empresa
            boolean empresaExiste = false;
            try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM EMPRESA");
                if (rs.next()) {
                    empresaExiste = rs.getInt(1) > 0;
                }
            }

            // Se não existir, inserir empresa padrão
            if (!empresaExiste) {
                try (Statement stmt = connection.createStatement()) {
                    String insertSql = "INSERT INTO EMPRESA (NOME, CAIXA_TOTAL) VALUES ('Farmácia Nacional', 200000.00)";
                    stmt.executeUpdate(insertSql);
                    System.out.println("Empresa padrão inserida no banco.");
                }
            }

            ServletContext context = sce.getServletContext();
            context.setAttribute("DBConnection", connection);

            System.out.println("Conexão com banco iniciada e script executado!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexão com banco encerrada!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
