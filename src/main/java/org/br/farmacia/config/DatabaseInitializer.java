package org.br.farmacia.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

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
                System.err.println("Arquivo SQL não encontrado no classpath");
                return;
            }
            String scriptSQL = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            // Divide os comandos pelo ';'
            String[] comandos = scriptSQL.split(";");

            try (Statement stmt = connection.createStatement()) {
                for (String comando : comandos) {
                    comando = comando.trim();
                    if (!comando.isEmpty()) {
                        stmt.execute(comando + ";");
                    }
                }
            }

            sce.getServletContext().setAttribute("DBConnection", connection);
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
