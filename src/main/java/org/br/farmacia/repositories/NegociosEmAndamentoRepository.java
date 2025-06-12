package org.br.farmacia.repositories;

import org.br.farmacia.models.NegociosEmAndamento;

import javax.servlet.ServletContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NegociosEmAndamentoRepository {

    private final Connection connection;

    public NegociosEmAndamentoRepository(ServletContext context) {
        this.connection = (Connection) context.getAttribute("DBConnection");
    }

    public NegociosEmAndamento findById(int id) {
        String sql = "SELECT * FROM negociosemandamento WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToNegocio(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<NegociosEmAndamento> findAll() {
        List<NegociosEmAndamento> lista = new ArrayList<>();
        String sql = "SELECT * FROM negociosemandamento";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                lista.add(mapResultSetToNegocio(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean save(NegociosEmAndamento negocio) {
        String sql = "INSERT INTO negociosemandamento (tipo, status) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, negocio.getTipo());
            ps.setString(2, negocio.getStatus());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                return false;
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    negocio.setId(generatedKeys.getInt(1));
                }
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(NegociosEmAndamento negocio) {
        String sql = "UPDATE negociosemandamento SET tipo = ?, status = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, negocio.getTipo());
            ps.setString(2, negocio.getStatus());
            ps.setInt(3, negocio.getId());

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM negociosemandamento WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private NegociosEmAndamento mapResultSetToNegocio(ResultSet rs) throws SQLException {
        NegociosEmAndamento negocio = new NegociosEmAndamento();
        negocio.setId(rs.getInt("id"));
        negocio.setTipo(rs.getString("tipo"));
        negocio.setStatus(rs.getString("status"));
        return negocio;
    }
}
