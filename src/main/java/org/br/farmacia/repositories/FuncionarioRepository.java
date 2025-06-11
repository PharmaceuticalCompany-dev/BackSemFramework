package org.br.farmacia.repositories;

import org.br.farmacia.enums.Cargo;
import org.br.farmacia.enums.Genero;
import org.br.farmacia.models.Funcionario;
import org.br.farmacia.models.Setor; // Certifique-se de importar Setor se for usado

import javax.servlet.ServletContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioRepository {

    private final Connection connection;

    public FuncionarioRepository(ServletContext context) {
        this.connection = (Connection) context.getAttribute("DBConnection");
    }

    public Funcionario findById(int id) {
        // Incluindo os campos de benefícios na seleção
        String sql = "SELECT id, nome, idade, genero, cargo, salario, " +
                "vale_refeicao, vale_alimentacao, plano_saude, plano_odonto, id_setor " +
                "FROM funcionario WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToFuncionario(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Funcionario> findBySetorId(int setorId) {
        List<Funcionario> lista = new ArrayList<>();
        // Incluindo os campos de benefícios na seleção
        String sql = "SELECT id, nome, idade, genero, cargo, salario, " +
                "vale_refeicao, vale_alimentacao, plano_saude, plano_odonto, id_setor " +
                "FROM funcionario WHERE id_setor = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, setorId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapResultSetToFuncionario(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Funcionario> findAll() {
        List<Funcionario> lista = new ArrayList<>();
        // Incluindo os campos de benefícios na seleção
        String sql = "SELECT id, nome, idade, genero, cargo, salario, " +
                "vale_refeicao, vale_alimentacao, plano_saude, plano_odonto, id_setor " +
                "FROM funcionario";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                lista.add(mapResultSetToFuncionario(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean save(Funcionario funcionario) {
        // Adicionando os novos campos de benefícios na query INSERT
        String sql = "INSERT INTO funcionario (nome, idade, genero, cargo, salario, " +
                "vale_refeicao, vale_alimentacao, plano_saude, plano_odonto, id_setor) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, funcionario.getNome());
            ps.setInt(2, funcionario.getIdade());
            ps.setString(3, funcionario.getGenero().name());
            ps.setString(4, funcionario.getCargo().name());
            ps.setDouble(5, funcionario.getSalario());
            // Novos parâmetros para os benefícios
            ps.setDouble(6, funcionario.getValeRefeicao());
            ps.setDouble(7, funcionario.getValeAlimentacao());
            ps.setDouble(8, funcionario.getPlanoSaude());
            ps.setDouble(9, funcionario.getPlanoOdonto());
            // Adicionando o Setor ID se existir
            if (funcionario.getSetor() != null) {
                ps.setInt(10, funcionario.getSetor().getId()); // Supondo que Setor tenha um getId()
            } else {
                ps.setNull(10, Types.INTEGER); // Se o setor for opcional e puder ser nulo
            }

            int affected = ps.executeUpdate();

            // Se for um novo registro, e o ID é gerado automaticamente pelo banco, você pode recuperá-lo
            if (affected == 1) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        funcionario.setId(generatedKeys.getInt(1)); // Define o ID gerado no objeto Funcionario
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Funcionario funcionario) {
        // Adicionando os novos campos de benefícios na query UPDATE
        String sql = "UPDATE funcionario SET nome = ?, idade = ?, genero = ?, cargo = ?, salario = ?, " +
                "vale_refeicao = ?, vale_alimentacao = ?, plano_saude = ?, plano_odonto = ?, id_setor = ? " +
                "WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, funcionario.getNome());
            ps.setInt(2, funcionario.getIdade());
            ps.setString(3, funcionario.getGenero().name());
            ps.setString(4, funcionario.getCargo().name());
            ps.setDouble(5, funcionario.getSalario());
            // Novos parâmetros para os benefícios
            ps.setDouble(6, funcionario.getValeRefeicao());
            ps.setDouble(7, funcionario.getValeAlimentacao());
            ps.setDouble(8, funcionario.getPlanoSaude());
            ps.setDouble(9, funcionario.getPlanoOdonto());
            // Adicionando o Setor ID se existir
            if (funcionario.getSetor() != null) {
                ps.setInt(10, funcionario.getSetor().getId());
            } else {
                ps.setNull(10, Types.INTEGER);
            }
            ps.setInt(11, funcionario.getId()); // ID para a cláusula WHERE

            int affected = ps.executeUpdate();
            return affected == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM funcionario WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            int affected = ps.executeUpdate();
            return affected == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Funcionario mapResultSetToFuncionario(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nome = rs.getString("nome");
        int idade = rs.getInt("idade");
        Genero genero = Genero.valueOf(rs.getString("genero").toUpperCase());
        Cargo cargo = Cargo.valueOf(rs.getString("cargo").toUpperCase());
        double salario = rs.getDouble("salario");
        // Recuperando os novos campos de benefícios
        double valeRefeicao = rs.getDouble("vale_refeicao");
        double valeAlimentacao = rs.getDouble("vale_alimentacao");
        double planoSaude = rs.getDouble("plano_saude");
        double planoOdonto = rs.getDouble("plano_odonto");

        // Criando o objeto Funcionario usando o construtor que inclui os benefícios
        Funcionario funcionario = new Funcionario(nome, id, idade, genero, cargo,
                salario, valeRefeicao, valeAlimentacao,
                planoSaude, planoOdonto);

        // Se a classe Setor for importante e tiver um ID no banco de dados
        // Você precisará de um SetorRepository ou uma forma de carregar o objeto Setor
        // baseado no id_setor. Por simplicidade, vou adicionar um placeholder.
        int setorId = rs.getInt("id_setor");
        if (!rs.wasNull()) { // Verifica se o valor do ID do setor não era NULL no banco
            // Você precisaria de um SetorRepository para carregar o objeto Setor
            // Exemplo: Setor setor = new SetorRepository(connection).findById(setorId);
            // funcionario.setSetor(setor);
            // Por enquanto, apenas um placeholder para o conceito:
            // funcionario.setSetor(new Setor(setorId, "Nome do Setor")); // Assumindo construtor em Setor
        }
        return funcionario;
    }
}