package org.br.farmacia.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
            // Armazene a conexão no contexto para usar depois
            sce.getServletContext().setAttribute("DBConnection", connection);
            System.out.println("Conexão com banco iniciada!");
        } catch (ClassNotFoundException | SQLException e) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
