package org.br.farmacia.repositories;

import org.br.farmacia.models.VendasProgramadas;

import javax.servlet.ServletContext;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VendasProgramadasRepository {

    private final Connection connection;

    public VendasProgramadasRepository(ServletContext context) {
        this.connection = (Connection) context.getAttribute("DBConnection");
    }

    public VendasProgramadas findById(int id) {
        VendasProgramadas vendasProgramadas = null;
        String sql = "SELECT ID, DATA_VENDA, VALOR_VENDA, CUSTO_PRODUTO, PRODUTO_ID, EMPRESA_ID FROM VENDAS_PROGRAMADAS WHERE ID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    vendasProgramadas = new VendasProgramadas();
                    vendasProgramadas.setId(rs.getInt("ID"));
                    vendasProgramadas.setDataVenda(rs.getDate("DATA_VENDA").toLocalDate());
                    vendasProgramadas.setValorVenda(rs.getDouble("VALOR_VENDA"));
                    vendasProgramadas.setCustoProduto(rs.getDouble("CUSTO_PRODUTO"));
                    vendasProgramadas.setProdutoId(rs.getInt("PRODUTO_ID"));
                    vendasProgramadas.setEmpresaId(rs.getInt("EMPRESA_ID"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar venda programada por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return vendasProgramadas;
    }


    public boolean save(VendasProgramadas vendasProgramadas) {
        String sql = "INSERT INTO VENDAS_PROGRAMADAS (DATA_VENDA, VALOR_VENDA, CUSTO_PRODUTO, PRODUTO_ID, EMPRESA_ID) VALUES (?, ?, ?, ?, ?)";
        int affectedRows = 0;
        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, Date.valueOf(vendasProgramadas.getDataVenda()));
            ps.setDouble(2, vendasProgramadas.getValorVenda());
            ps.setDouble(3, vendasProgramadas.getCustoProduto());
            ps.setInt(4, vendasProgramadas.getProdutoId());
            ps.setObject(5, vendasProgramadas.getEmpresaId());

            affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        vendasProgramadas.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao salvar venda programada: " + e.getMessage());
            e.printStackTrace();
        }
        return affectedRows > 0;
    }


    public boolean update(VendasProgramadas vendasProgramadas) {
        String sql = "UPDATE VENDAS_PROGRAMADAS SET DATA_VENDA, VALOR_VENDA, CUSTO_PRODUTO, PRODUTO_ID, EMPRESA_ID WHERE ID = ?";
        int affectedRows = 0;
        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(vendasProgramadas.getDataVenda()));
            ps.setDouble(2, vendasProgramadas.getValorVenda());
            ps.setDouble(3, vendasProgramadas.getCustoProduto());
            ps.setInt(4, vendasProgramadas.getProdutoId());
            ps.setInt(5, vendasProgramadas.getEmpresaId());
            affectedRows = ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar venda programada: " + e.getMessage());
            e.printStackTrace();
        }
        return affectedRows > 0;
    }


    public boolean delete(int id) {
        String sql = "DELETE FROM VENDAS_PROGRAMADAS WHERE ID = ?";
        int affectedRows = 0;
        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, id);
            affectedRows = ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao deletar venda programada: " + e.getMessage());
            e.printStackTrace();
        }
        return affectedRows > 0;
    }


    public List<VendasProgramadas> findAll() {
        List<VendasProgramadas> vendasProgramadas = new ArrayList<>();
        String sql = "SELECT ID, DATA_VENDA, VALOR_VENDA, CUSTO_PRODUTO, PRODUTO_ID, EMPRESA_ID FROM VENDAS_PROGRAMADAS";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    VendasProgramadas vendaProgramada = new VendasProgramadas();
                    vendaProgramada.setId(rs.getInt("ID"));
                    vendaProgramada.setDataVenda(rs.getDate("DATA_VENDA").toLocalDate());
                    vendaProgramada.setValorVenda(rs.getDouble("VALOR_VENDA"));
                    vendaProgramada.setCustoProduto(rs.getDouble("CUSTO_PRODUTO"));
                    vendaProgramada.setProdutoId(rs.getInt("PRODUTO_ID"));
                    vendaProgramada.setEmpresaId(rs.getInt("EMPRESA_ID"));
                    vendasProgramadas.add(vendaProgramada);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar todas as vendas programadas: " + e.getMessage());
            e.printStackTrace();
        }
        return vendasProgramadas;
    }
}