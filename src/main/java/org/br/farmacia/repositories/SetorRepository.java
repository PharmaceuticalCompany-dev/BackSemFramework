package org.br.farmacia.repositories;

import org.br.farmacia.enums.TipoSetor;
import org.br.farmacia.models.Setor;
import javax.servlet.ServletContext; // Se for uma aplicação web, caso contrário, ajuste a injeção da conexão
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SetorRepository {

    private final Connection connection;

    public SetorRepository(ServletContext context) {
        this.connection = (Connection) context.getAttribute("DBConnection");
    }

    public Setor findById(int id) {
        String sql = "SELECT id, nome FROM setor WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToSetor(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar setor por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<Setor> findAll() {
        List<Setor> lista = new ArrayList<>();
        String sql = "SELECT id, nome FROM setor";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                lista.add(mapResultSetToSetor(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar todos os setores: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    public boolean save(Setor setor) {
        String sql = "INSERT INTO setor (nome) VALUES (?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, setor.getTipoSetor().name());
            int affected = ps.executeUpdate();

            if (affected == 1) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    setor.setId(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao salvar setor: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Setor setor) {
        String sql = "UPDATE setor SET nome = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, setor.getTipoSetor().name());
            ps.setInt(2, setor.getId());
            int affected = ps.executeUpdate();
            return affected == 1;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar setor: " + e.getMessage());
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
            System.err.println("Erro ao deletar setor: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    private Setor mapResultSetToSetor(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nomeSetorString = rs.getString("nome");
        TipoSetor tipoSetor = null;
        try {
            tipoSetor = TipoSetor.valueOf(nomeSetorString.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.err.println("Erro: O valor '" + nomeSetorString + "' não é um TipoSetor válido. Definindo como null.");
        }
        return new Setor(id, tipoSetor, new ArrayList<>());
    }
}