package org.br.farmacia.repositories;

import org.br.farmacia.enums.TipoTransacao;
import org.br.farmacia.models.Caixa;
import javax.servlet.ServletContext;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CaixaRepository {

    private final Connection connection;

    public CaixaRepository(ServletContext context) {
        this.connection = (Connection) context.getAttribute("DBConnection");
    }

    public Caixa findById(int id) {
        String sql = "SELECT id, tipo, valor, data, descricao, empresa_id FROM TRANSACAO_FINANCEIRA WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToCaixa(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Caixa> findAll() {
        List<Caixa> lista = new ArrayList<>();
        String sql = "SELECT id, tipo, valor, data, descricao, empresa_id FROM TRANSACAO_FINANCEIRA ORDER BY data DESC";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                lista.add(mapResultSetToCaixa(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Caixa> findByEmpresaId(int empresaId) {
        List<Caixa> lista = new ArrayList<>();
        String sql = "SELECT id, tipo, valor, data, descricao, empresa_id FROM TRANSACAO_FINANCEIRA WHERE empresa_id = ? ORDER BY data DESC";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, empresaId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapResultSetToCaixa(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean save(Caixa caixa) {
        String sql = "INSERT INTO TRANSACAO_FINANCEIRA (tipo, valor, data, descricao, empresa_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, caixa.getTipo().name());
            ps.setDouble(2, caixa.getValor());
            ps.setTimestamp(3, Timestamp.valueOf(caixa.getData()));
            ps.setString(4, caixa.getDescricao());
            ps.setInt(5, caixa.getEmpresaId());

            int affected = ps.executeUpdate();

            if (affected == 1) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        caixa.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Caixa caixa) {
        String sql = "UPDATE TRANSACAO_FINANCEIRA SET tipo = ?, valor = ?, data = ?, descricao = ?, empresa_id = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, caixa.getTipo().name());
            ps.setDouble(2, caixa.getValor());
            ps.setTimestamp(3, Timestamp.valueOf(caixa.getData()));
            ps.setString(4, caixa.getDescricao());
            ps.setInt(5, caixa.getEmpresaId());
            ps.setInt(6, caixa.getId());

            int affected = ps.executeUpdate();
            return affected == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM TRANSACAO_FINANCEIRA WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            int affected = ps.executeUpdate();
            return affected == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Caixa mapResultSetToCaixa(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        TipoTransacao tipo = TipoTransacao.valueOf(rs.getString("tipo").toUpperCase());
        double valor = rs.getDouble("valor");
        LocalDateTime data = rs.getTimestamp("data").toLocalDateTime();
        String descricao = rs.getString("descricao");
        int empresaId = rs.getInt("empresa_id");

        return new Caixa(id, tipo, valor, data, descricao, empresaId);
    }
}