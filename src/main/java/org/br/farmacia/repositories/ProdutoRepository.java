package org.br.farmacia.repositories;

import org.br.farmacia.models.Produto;

import javax.servlet.ServletContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoRepository {

    private final Connection connection;

    public ProdutoRepository(ServletContext context) {
        this.connection = (Connection) context.getAttribute("DBConnection");
    }

    public Produto findById(int id) {
        String sql = "SELECT * FROM produto WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToProduto(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar produto por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<Produto> findAll() {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produto";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                lista.add(mapResultSetToProduto(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar todos os produtos: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    // --- MUDANÇA PRINCIPAL AQUI: O método 'save' agora retorna o Produto com o ID gerado ---
    public Produto save(Produto produto) {
        // Remove 'id' da query INSERT. O banco de dados irá auto-gerar.
        String sql = "INSERT INTO produto (nome, preco_compra, preco_venda, quantidade_estoque) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, produto.getNome());
            ps.setDouble(2, produto.getPrecoCompra());
            ps.setDouble(3, produto.getPrecoVenda());
            ps.setInt(4, produto.getQuantidadeEstoque());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 1) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        produto.setId(generatedId); // Define o ID gerado no objeto Produto
                        return produto; // Retorna o produto com o ID atualizado
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao salvar produto: " + e.getMessage());
            e.printStackTrace();
        }
        return null; // Retorna null em caso de falha
    }

    public boolean update(Produto produto) {
        String sql = "UPDATE produto SET nome = ?, preco_compra = ?, preco_venda = ?, quantidade_estoque = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, produto.getNome());
            ps.setDouble(2, produto.getPrecoCompra());
            ps.setDouble(3, produto.getPrecoVenda());
            ps.setInt(4, produto.getQuantidadeEstoque());
            ps.setInt(5, produto.getId());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar produto: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM produto WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("Erro ao deletar produto: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    private Produto mapResultSetToProduto(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nome = rs.getString("nome");
        double precoCompra = rs.getDouble("preco_compra");
        double precoVenda = rs.getDouble("preco_venda");
        int quantidadeEstoque = rs.getInt("quantidade_estoque");

        // Certifique-se de que o construtor de Produto em Produto.java aceita esses parâmetros
        return new Produto(id, nome, precoCompra, precoVenda, quantidadeEstoque);
    }
}