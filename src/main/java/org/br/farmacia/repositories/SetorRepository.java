package org.br.farmacia.repositories;

import org.br.farmacia.enums.TipoSetor;
import org.br.farmacia.models.Setor;
import org.br.farmacia.models.Funcionario; // Assuming Setor might need to interact with Funcionario
import javax.servlet.ServletContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SetorRepository {

    private final Connection connection;

    public SetorRepository(ServletContext context) {
        this.connection = (Connection) context.getAttribute("DBConnection");
    }

    public Setor findById(int id) {
        String sql = "SELECT * FROM setor WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToSetor(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Setor> findAll() {
        List<Setor> lista = new ArrayList<>();
        String sql = "SELECT * FROM setor";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                lista.add(mapResultSetToSetor(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean save(Setor setor) {
        String sql = "INSERT INTO setor (tipoSetor) VALUES (?)"; // Assuming tipoSetor is a String in DB
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, setor.getTipoSetor().name()); // Convert enum to string
            int affected = ps.executeUpdate();
            return affected == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Setor setor) {
        String sql = "UPDATE setor SET tipoSetor = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, setor.getTipoSetor().name());
            ps.setInt(2, setor.getId()); // Assuming Setor has an ID
            int affected = ps.executeUpdate();
            return affected == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM setor WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            int affected = ps.executeUpdate();
            return affected == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Setor mapResultSetToSetor(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        TipoSetor tipoSetor = TipoSetor.valueOf(rs.getString("tipoSetor").toUpperCase());
        // For the list of Funcionarios in Setor, you'd typically load them in a separate query or service layer
        // For simplicity, this example initializes an empty list.
        return new Setor(id, tipoSetor, new ArrayList<>());
    }
}