package org.br.farmacia.repositories;

import org.br.farmacia.models.VendasProgramadas;
import org.br.farmacia.models.Produto;

import javax.servlet.ServletContext;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VendasProgramadasRepository {

    private final Connection connection;
    private final ProdutoRepository produtoRepository;

    public VendasProgramadasRepository(ServletContext context) {
        this.connection = (Connection) context.getAttribute("DBConnection");
        this.produtoRepository = new ProdutoRepository(context);
    }

    public VendasProgramadas findById(int id) {
        VendasProgramadas vendasProgramadas = null;
        String sql = "SELECT ID, DATA_VENDA, PRODUTO_ID, EMPRESA_ID FROM VENDAS_PROGRAMADAS WHERE ID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    vendasProgramadas = new VendasProgramadas();
                    vendasProgramadas.setId(rs.getInt("ID"));
                    vendasProgramadas.setDataVenda(rs.getDate("DATA_VENDA").toLocalDate());
                    vendasProgramadas.setProdutoId(rs.getInt("PRODUTO_ID"));
                    vendasProgramadas.setEmpresaId(rs.getInt("EMPRESA_ID"));

                    Produto produto = produtoRepository.findById(vendasProgramadas.getProdutoId());
                    if (produto != null) {
                        vendasProgramadas.setValorVendaCalculado(produto.getPrecoVenda());
                        vendasProgramadas.setCustoProdutoCalculado(produto.getPrecoCompra());
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar venda programada por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return vendasProgramadas;
    }

    public boolean save(VendasProgramadas vendasProgramadas) {
        String sql = "INSERT INTO VENDAS_PROGRAMADAS (DATA_VENDA, PRODUTO_ID, EMPRESA_ID) VALUES (?, ?, ?)";
        int affectedRows = 0;
        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, Date.valueOf(vendasProgramadas.getDataVenda()));
            ps.setInt(2, vendasProgramadas.getProdutoId());
            ps.setObject(3, vendasProgramadas.getEmpresaId());

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
        String sql = "UPDATE VENDAS_PROGRAMADAS SET DATA_VENDA = ?, PRODUTO_ID = ?, EMPRESA_ID = ? WHERE ID = ?";
        int affectedRows = 0;
        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(vendasProgramadas.getDataVenda()));
            ps.setInt(2, vendasProgramadas.getProdutoId());
            ps.setObject(3, vendasProgramadas.getEmpresaId());
            ps.setInt(4, vendasProgramadas.getId());
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
        String sql = "SELECT ID, DATA_VENDA, PRODUTO_ID, EMPRESA_ID FROM VENDAS_PROGRAMADAS";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    VendasProgramadas vendaProgramada = new VendasProgramadas();
                    vendaProgramada.setId(rs.getInt("ID"));
                    vendaProgramada.setDataVenda(rs.getDate("DATA_VENDA").toLocalDate());
                    vendaProgramada.setProdutoId(rs.getInt("PRODUTO_ID"));
                    vendaProgramada.setEmpresaId(rs.getInt("EMPRESA_ID"));

                    Produto produto = produtoRepository.findById(vendaProgramada.getProdutoId());
                    if (produto != null) {
                        vendaProgramada.setValorVendaCalculado(produto.getPrecoVenda());
                        vendaProgramada.setCustoProdutoCalculado(produto.getPrecoCompra());
                    }
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