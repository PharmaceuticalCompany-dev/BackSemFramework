package org.br.farmacia.repositories;

import org.br.farmacia.models.Transportadora;
import javax.servlet.ServletContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransportadoraRepository {

    private final Connection connection;

    public TransportadoraRepository(ServletContext context) {
        this.connection = (Connection) context.getAttribute("DBConnection");
    }

    public Transportadora findById(int id) {
        Transportadora transportadora = null;
        String sql = "SELECT * FROM TRANSPORTADORA WHERE ID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToTransportadora(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean save(Transportadora transportadora) {
        String sql = "INSERT INTO TRANSPORTADORA (NOME, CONTATO, TELEFONE, REGIAO) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, transportadora.getNome());
            ps.setString(2, String.join(",", transportadora.getContato()));
            ps.setString(3, transportadora.getTelefone());
            ps.setString(4, transportadora.getRegiao());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Transportadora transportadora) {
        String sql = "UPDATE TRANSPORTADORA SET NOME = ?, CONTATO = ?, TELEFONE = ?, REGIAO = ? WHERE ID = ?";
        int affectedRows = 0;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, transportadora.getNome());
            ps.setString(2, String.join(",", transportadora.getContato()));
            ps.setString(3, transportadora.getTelefone());
            ps.setString(4, transportadora.getRegiao());
            ps.setInt(5, transportadora.getEmpresaId());
            ps.setInt(6, transportadora.getId());
            affectedRows = ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar transportadora: " + e.getMessage());
            e.printStackTrace();
        }
        return affectedRows > 0;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM TRANSPORTADORA WHERE ID = ?";
        int affectedRows = 0;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            affectedRows = ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao deletar transportadora: " + e.getMessage());
            e.printStackTrace();
        }
        return affectedRows > 0;
    }

    public List<Transportadora> findAll() {
        List<Transportadora> transportadoras = new ArrayList<>();
        String sql = "SELECT ID, NOME, CONTATO, TELEFONE, REGIAO FROM TRANSPORTADORA";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                transportadoras.add(mapResultSetToTransportadora(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transportadoras;
    }


    private Transportadora mapResultSetToTransportadora(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nome = rs.getString("nome");
        String contato  = rs.getString("contato");
        String telefone  = rs.getString("telefone");
        String regiao   = rs.getString("regiao");

        return new Transportadora(id, nome, contato, telefone, regiao);
    }

}