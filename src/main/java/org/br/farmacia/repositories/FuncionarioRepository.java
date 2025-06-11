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
        String sql = "SELECT id, nome, data_Nascimento, genero, cargo, salario, " +
                "vale_refeicao, vale_alimentacao, plano_saude, plano_odonto, setor_id " +
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
        String sql = "SELECT id, nome, data_Nascimento, genero, cargo, salario, " +
                "vale_refeicao, vale_alimentacao, plano_saude, plano_odonto, setor_id " +
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
        String sql = "SELECT id, nome, data_Nascimento, genero, cargo, salario, " +
                "vale_refeicao, vale_alimentacao, plano_saude, plano_odonto, setor_id " +
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
        String sql = "INSERT INTO funcionario (nome, data_Nascimento, genero, cargo, salario, " +
                "vale_refeicao, vale_alimentacao, plano_saude, plano_odonto, setor_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, funcionario.getNome());
            ps.setDate(2, funcionario.getDataNascimento());
            ps.setString(3, funcionario.getGenero().name());
            ps.setString(4, funcionario.getCargo().name());
            ps.setDouble(5, funcionario.getSalario());
            ps.setDouble(6, funcionario.getValeRefeicao());
            ps.setDouble(7, funcionario.getValeAlimentacao());
            ps.setDouble(8, funcionario.getPlanoSaude());
            ps.setDouble(9, funcionario.getPlanoOdonto());
            if (funcionario.getSetor() != null) {
                ps.setInt(10, funcionario.getSetor().getId());
            } else {
                ps.setNull(10, Types.INTEGER);
            }

            int affected = ps.executeUpdate();

            if (affected == 1) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        funcionario.setId(generatedKeys.getInt(1));
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
        String sql = "UPDATE funcionario SET nome = ?, idade = ?, genero = ?, cargo = ?, salario = ?, " +
                "vale_refeicao = ?, vale_alimentacao = ?, plano_saude = ?, plano_odonto = ?, setor_id = ? " +
                "WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, funcionario.getNome());
            ps.setDate(2, funcionario.getDataNascimento());
            ps.setString(3, funcionario.getGenero().name());
            ps.setString(4, funcionario.getCargo().name());
            ps.setDouble(5, funcionario.getSalario());
            ps.setDouble(6, funcionario.getValeRefeicao());
            ps.setDouble(7, funcionario.getValeAlimentacao());
            ps.setDouble(8, funcionario.getPlanoSaude());
            ps.setDouble(9, funcionario.getPlanoOdonto());
            if (funcionario.getSetor() != null) {
                ps.setInt(10, funcionario.getSetor().getId());
            } else {
                ps.setNull(10, Types.INTEGER);
            }
            ps.setInt(11, funcionario.getId());

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
        Date data_Nascimento = rs.getDate("data_Nascimento");
        Genero genero = Genero.valueOf(rs.getString("genero").toUpperCase());
        Cargo cargo = Cargo.valueOf(rs.getString("cargo").toUpperCase());
        double salario = rs.getDouble("salario");
        double valeRefeicao = rs.getDouble("vale_refeicao");
        double valeAlimentacao = rs.getDouble("vale_alimentacao");
        double planoSaude = rs.getDouble("plano_saude");
        double planoOdonto = rs.getDouble("plano_odonto");

        Funcionario funcionario = new Funcionario(nome, id, data_Nascimento, genero, cargo,
                salario, valeRefeicao, valeAlimentacao,
                planoSaude, planoOdonto);

        int setorId = rs.getInt("id_setor");
        if (!rs.wasNull()) {

        }
        return funcionario;
    }
}