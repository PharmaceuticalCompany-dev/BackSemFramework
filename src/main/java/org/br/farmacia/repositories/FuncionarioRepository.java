package org.br.farmacia.repositories;

import org.br.farmacia.enums.Cargo;
import org.br.farmacia.enums.Genero;
import org.br.farmacia.models.Funcionario;
import org.br.farmacia.models.Setor;

import javax.servlet.ServletContext;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioRepository {

    private final Connection connection;

    public FuncionarioRepository(ServletContext context) {
        this.connection = (Connection) context.getAttribute("DBConnection");
    }

    public Funcionario findById(int id) {
        String sql = "SELECT id, nome, dataNascimento, genero, cargo, salario, vale_refeicao, vale_alimentacao, plano_saude, plano_odonto, percentual_irrf, percentual_inss, bonificacao, setor_id FROM funcionario WHERE id = ?";
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
        String sql = "SELECT id, nome, dataNascimento, genero, cargo, salario, vale_refeicao, vale_alimentacao, plano_saude, plano_odonto, percentual_irrf, percentual_inss, bonificacao, setor_id FROM funcionario WHERE setor_id = ?";
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
        String sql = "SELECT id, nome, dataNascimento, genero, cargo, salario, vale_refeicao, vale_alimentacao, plano_saude, plano_odonto, percentual_irrf, percentual_inss, bonificacao, setor_id FROM funcionario";
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
        String sql = "INSERT INTO funcionario (nome, dataNascimento, genero, cargo, salario, setor_id, vale_refeicao, vale_alimentacao, plano_saude, plano_odonto, percentual_irrf, percentual_inss, bonificacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, funcionario.getNome());
            ps.setDate(2, Date.valueOf(funcionario.getDataNascimento()));
            ps.setString(3, funcionario.getGenero().name());
            ps.setString(4, funcionario.getCargo().name());
            ps.setDouble(5, funcionario.getSalario());
            if (funcionario.getSetor() != null && funcionario.getSetor().getId() > 0) {
                ps.setInt(6, funcionario.getSetor().getId());
            } else {
                ps.setNull(6, Types.INTEGER);
            }
            ps.setDouble(7, funcionario.getValeRefeicao());
            ps.setDouble(8, funcionario.getValeAlimentacao());
            ps.setDouble(9, funcionario.getPlanoSaude());
            ps.setDouble(10, funcionario.getPlanoOdonto());
            ps.setDouble(11, funcionario.getPercentualIRRF());
            ps.setDouble(12, funcionario.getPercentualINSS());
            ps.setDouble(13, funcionario.getBonificacao());
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
        String sql = "UPDATE funcionario SET nome = ?, dataNascimento = ?, genero = ?, cargo = ?, salario = ?, setor_id = ?, vale_refeicao = ?, vale_alimentacao = ?, plano_saude = ?, plano_odonto = ?, percentual_irrf = ?, percentual_inss = ?, bonificacao = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, funcionario.getNome());
            ps.setDate(2, Date.valueOf(funcionario.getDataNascimento()));
            ps.setString(3, funcionario.getGenero().name());
            ps.setString(4, funcionario.getCargo().name());
            ps.setDouble(5, funcionario.getSalario());
            if (funcionario.getSetor() != null && funcionario.getSetor().getId() > 0) {
                ps.setInt(6, funcionario.getSetor().getId());
            } else {
                ps.setNull(6, Types.INTEGER);
            }
            ps.setDouble(7, funcionario.getValeRefeicao());
            ps.setDouble(8, funcionario.getValeAlimentacao());
            ps.setDouble(9, funcionario.getPlanoSaude());
            ps.setDouble(10, funcionario.getPlanoOdonto());
            ps.setDouble(11, funcionario.getPercentualIRRF());
            ps.setDouble(12, funcionario.getPercentualINSS());
            ps.setDouble(13, funcionario.getBonificacao());
            ps.setInt(14, funcionario.getId());
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
        LocalDate dataNascimento = rs.getDate("dataNascimento").toLocalDate();
        Genero genero = Genero.valueOf(rs.getString("genero").toUpperCase());
        Cargo cargo = Cargo.valueOf(rs.getString("cargo").toUpperCase());
        double salario = rs.getDouble("salario");
        double valeRefeicao = rs.getDouble("vale_refeicao");
        double valeAlimentacao = rs.getDouble("vale_alimentacao");
        double planoSaude = rs.getDouble("plano_saude");
        double planoOdonto = rs.getDouble("plano_odonto");
        double percentualIrrf = rs.getDouble("percentual_irrf");
        double percentualInss = rs.getDouble("percentual_inss");
        double bonificacao = rs.getDouble("bonificacao");
        int setorId = rs.getInt("setor_id");

        Funcionario funcionario = new Funcionario(id, nome, dataNascimento, genero, salario, setorId, cargo, valeRefeicao, valeAlimentacao, planoSaude, planoOdonto,  percentualIrrf, percentualInss, bonificacao);
        funcionario.setValeRefeicao(valeRefeicao);
        funcionario.setValeAlimentacao(valeAlimentacao);
        funcionario.setPlanoSaude(planoSaude);
        funcionario.setPlanoOdonto(planoOdonto);
        funcionario.setPercentualIrrf(percentualIrrf);
        funcionario.setPercentualInss(percentualInss);
        funcionario.setBonificacao(bonificacao);

        if (!rs.wasNull()) {
            Setor setor = new Setor();
            setor.setId(setorId);
            funcionario.setSetor(setor);
        } else {
            funcionario.setSetor(null);
        }

        return funcionario;
    }
}
