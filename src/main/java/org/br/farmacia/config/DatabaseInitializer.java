package org.br.farmacia.config;

import org.br.farmacia.enums.TipoSetor;

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

            boolean empresaExiste = false;
            try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM EMPRESA");
                if (rs.next()) {
                    empresaExiste = rs.getInt(1) > 0;
                }
            }

            if (!empresaExiste) {
                try (Statement stmt = connection.createStatement()) {
                    String insertSql = "INSERT INTO EMPRESA (NOME, CAIXA_TOTAL) VALUES ('Farmácia Nacional', 200000.00)";
                    stmt.executeUpdate(insertSql);
                    System.out.println("Empresa padrão inserida no banco.");
                }
            }

            boolean setoresExistem = false;
            try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM SETOR");
                if (rs.next()) {
                    setoresExistem = rs.getInt(1) > 0;
                }
            }

            if (!setoresExistem) {
                try (Statement stmt = connection.createStatement()) {
                    for (TipoSetor setor : TipoSetor.values()) {
                        String insertSetor = "INSERT INTO SETOR (NOME) VALUES ('" + setor.toString() + "')";
                        stmt.executeUpdate(insertSetor);
                    }
                    System.out.println("Setores padrão inseridos no banco.");
                }
            }

            boolean produtosExistem = false;
            try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM PRODUTO");
                if (rs.next()) {
                    produtosExistem = rs.getInt(1) > 0;
                }
            }
            if (!produtosExistem) {
                try (Statement stmt = connection.createStatement()) {
                    String insertSql = "INSERT INTO PRODUTO (ID, NOME, PRECO_COMPRA, PRECO_VENDA, QUANTIDADE_ESTOQUE, EMPRESA_ID) VALUES (1, 'Vitamina D', 20.00, 39.00, 100, 1); " +
                            "INSERT INTO PRODUTO (ID, NOME, PRECO_COMPRA, PRECO_VENDA, QUANTIDADE_ESTOQUE, EMPRESA_ID) VALUES (2, 'Vitamina C', 15.00, 29.00, 200, 1); " +
                            "INSERT INTO PRODUTO (ID, NOME, PRECO_COMPRA, PRECO_VENDA, QUANTIDADE_ESTOQUE, EMPRESA_ID) VALUES (3, 'Multivitamínico', 30.00, 59.00, 150, 1); " +
                            "INSERT INTO PRODUTO (ID, NOME, PRECO_COMPRA, PRECO_VENDA, QUANTIDADE_ESTOQUE, EMPRESA_ID) VALUES (4, 'Creatina', 50.00, 90.00, 80, 1); " +
                            "INSERT INTO PRODUTO (ID, NOME, PRECO_COMPRA, PRECO_VENDA, QUANTIDADE_ESTOQUE, EMPRESA_ID) VALUES (5, 'Ômega 3', 35.00, 75.00, 120, 1); " +
                            "INSERT INTO PRODUTO (ID, NOME, PRECO_COMPRA, PRECO_VENDA, QUANTIDADE_ESTOQUE, EMPRESA_ID) VALUES (6, 'Proteína Whey', 120.00, 250.00, 50, 1); " +
                            "INSERT INTO PRODUTO (ID, NOME, PRECO_COMPRA, PRECO_VENDA, QUANTIDADE_ESTOQUE, EMPRESA_ID) VALUES (7, 'BCAA', 40.00, 80.00, 200, 1); " +
                            "INSERT INTO PRODUTO (ID, NOME, PRECO_COMPRA, PRECO_VENDA, QUANTIDADE_ESTOQUE, EMPRESA_ID) VALUES (8, 'ZMA', 25.00, 55.00, 130, 1); " +
                            "INSERT INTO PRODUTO (ID, NOME, PRECO_COMPRA, PRECO_VENDA, QUANTIDADE_ESTOQUE, EMPRESA_ID) VALUES (9, 'Colágeno', 40.00, 85.00, 160, 1); " +
                            "INSERT INTO PRODUTO (ID, NOME, PRECO_COMPRA, PRECO_VENDA, QUANTIDADE_ESTOQUE, EMPRESA_ID) VALUES (10, 'Vitamina B12', 10.00, 25.00, 300, 1);";

                    stmt.executeUpdate(insertSql);
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
