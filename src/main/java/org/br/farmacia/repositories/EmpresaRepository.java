package org.br.farmacia.repositories;

import org.br.farmacia.models.Empresa;

import javax.servlet.ServletContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpresaRepository {

    private final Connection connection;

    public EmpresaRepository(ServletContext context) {
        this.connection = (Connection) context.getAttribute("DBConnection");
    }

    public List<Empresa> findAll() {
        List<Empresa> empresas = new ArrayList<>();
        String sql = "SELECT ID, NOME, CAIXA_TOTAL FROM EMPRESA";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Empresa empresa = new Empresa();
                empresa.setId(rs.getInt("ID"));
                empresa.setNome(rs.getString("NOME"));
                empresa.setCaixaTotal(rs.getDouble("CAIXA_TOTAL"));

                empresas.add(empresa);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return empresas;
    }

    public Empresa findById(int id) {
        Empresa empresa = null;
        String sql = "SELECT ID, NOME, CAIXA_TOTAL FROM EMPRESA WHERE ID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                empresa = new Empresa();
                empresa.setId(rs.getInt("ID"));
                empresa.setNome(rs.getString("NOME"));
                empresa.setCaixaTotal(rs.getDouble("CAIXA_TOTAL"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return empresa;
    }

    public boolean save(Empresa empresa) {
        String sql = "INSERT INTO EMPRESA (NOME, CAIXA_TOTAL) VALUES (?, ?)";
        int affectedRows = 0;

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, empresa.getNome());
            ps.setDouble(2, empresa.getCaixaTotal());

            affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    empresa.setId(keys.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return affectedRows > 0;
    }

    public boolean update(Empresa empresa) {
        String sql = "UPDATE EMPRESA SET NOME = ?, CAIXA_TOTAL = ? WHERE ID = ?";
        int affectedRows = 0;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, empresa.getNome());
            ps.setDouble(2, empresa.getCaixaTotal());
            ps.setInt(3, empresa.getId());

            affectedRows = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return affectedRows > 0;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM EMPRESA WHERE ID = ?";
        int affectedRows = 0;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            affectedRows = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return affectedRows > 0;
    }
}
