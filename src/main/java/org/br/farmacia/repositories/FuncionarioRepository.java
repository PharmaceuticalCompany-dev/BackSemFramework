package org.br.farmacia.repositories;

import org.br.farmacia.enums.Cargo;
import org.br.farmacia.enums.Genero;
import org.br.farmacia.models.Funcionario;

import javax.servlet.ServletContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioRepository {

    private final Connection connection;


    //Aqui chama esse DBConnection que foi declarado la no inicializador do banco de dados
    //o ServletContext representa a aplicação web em tempo de execução. Cada aplicação web
    //tem um único ServletContext, e este é gerado quando a aplicação é implantada no servidor.
    public FuncionarioRepository(ServletContext context) {
        this.connection = (Connection) context.getAttribute("DBConnection");
    }

    public Funcionario findById(int id) {
        String sql = "SELECT * FROM funcionario WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            //esse ps insere na primeira ? da string
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            //Verifica se há resultado, e transforma a linha do banco em objeto Funcionario.
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
        String sql = "SELECT * FROM funcionario WHERE id_setor = ?";
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
        String sql = "SELECT * FROM funcionario";
        //statement sem parametro para consulta simples
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
        String sql = "INSERT INTO funcionario (nome, idade, genero, cargo, salario) VALUES (?, ?, ?, ?, ?)\n";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, funcionario.getNome());
            ps.setInt(2, funcionario.getIdade());
            ps.setString(3, funcionario.getGenero().name());
            ps.setString(4, funcionario.getCargo().name());
            ps.setDouble(5, funcionario.getSalario());
            int affected = ps.executeUpdate();
            //Ele retorna um int indicando quantas linhas foram afetadas pela execução desse comando
            return affected == 1;
            //Aqui, o metodo retorna true se exatamente uma linha foi afetada, indicando sucesso para essa operação específica
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Funcionario funcionario) {
        String sql = "UPDATE funcionario SET nome = ?, idade = ?, genero = ?, cargo = ?, salario = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, funcionario.getNome());
            ps.setInt(2, funcionario.getIdade());
            ps.setString(3, funcionario.getGenero().name());
            ps.setString(4, funcionario.getCargo().name());
            ps.setDouble(5, funcionario.getSalario());
            ps.setInt(6, funcionario.getId());
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

        return new Funcionario(nome, id, idade, genero, cargo, salario);
    }
}
