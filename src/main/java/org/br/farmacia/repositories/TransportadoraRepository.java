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
        String sql = "SELECT ID, NOME, LOCAIS_ATENDIMENTO, EMPRESA_ID FROM TRANSPORTADORA WHERE ID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                transportadora = new Transportadora();
                transportadora.setId(rs.getInt("ID"));
                transportadora.setNome(rs.getString("NOME"));

                String locaisString = rs.getString("LOCAIS_ATENDIMENTO");
                ArrayList<String> locaisAtendimento = new ArrayList<>();
                if (locaisString != null && !locaisString.trim().isEmpty()) {
                    locaisAtendimento.addAll(Arrays.asList(locaisString.split(",")));
                }
                transportadora.setLocaisAtendimento(locaisAtendimento);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar transportadora por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return transportadora;
    }

    public boolean save(Transportadora transportadora) {
        String sql = "INSERT INTO TRANSPORTADORA (NOME, LOCAIS_ATENDIMENTO, EMPRESA_ID) VALUES (?, ?, ?)";
        int affectedRows = 0;
        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, transportadora.getNome());
            ps.setString(2, String.join(",", transportadora.getLocaisAtendimento()));
            ps.setObject(3, transportadora.getEmpresaId());

            affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        transportadora.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao salvar transportadora: " + e.getMessage());
            e.printStackTrace();
        }
        return affectedRows > 0;
    }

    public boolean update(Transportadora transportadora) {
        String sql = "UPDATE TRANSPORTADORA SET NOME = ?, LOCAIS_ATENDIMENTO = ? WHERE ID = ?";
        int affectedRows = 0;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, transportadora.getNome());
            ps.setString(2, String.join(",", transportadora.getLocaisAtendimento()));
            ps.setInt(3, transportadora.getId());
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
        String sql = "SELECT ID, NOME, LOCAIS_ATENDIMENTO, EMPRESA_ID FROM TRANSPORTADORA";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Transportadora transportadora = new Transportadora();
                transportadora.setId(rs.getInt("ID"));
                transportadora.setNome(rs.getString("NOME"));

                String locaisString = rs.getString("LOCAIS_ATENDIMENTO");
                ArrayList<String> locaisAtendimento = new ArrayList<>();
                if (locaisString != null && !locaisString.trim().isEmpty()) {
                    locaisAtendimento.addAll(Arrays.asList(locaisString.split(",")));
                }
                transportadora.setLocaisAtendimento(locaisAtendimento);

                transportadoras.add(transportadora);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar todas as transportadoras: " + e.getMessage());
            e.printStackTrace();
        }
        return transportadoras;
    }

}