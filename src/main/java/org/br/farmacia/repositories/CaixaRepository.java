package org.br.farmacia.repositories;
import org.br.farmacia.models.Caixa;
import javax.servlet.ServletContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CaixaRepository {
    private Connection con;

    public CaixaRepository(ServletContext context) {
        this.con = (Connection) context.getAttribute("DBConnection");
    }

    public Caixa findById(int id) {
        String sql = "SELECT id, valorTotal from caixa where id = ?";
        try(PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return mapResultSetToCaixa(rs);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public List<Caixa> findAll() {
        List<Caixa> caixas = new ArrayList<>();
        String sql = "SELECT id, valorTotal from caixa";
        try (Statement stat = con.createStatement()){
           ResultSet rs = stat.executeQuery(sql);
           while (rs.next()){
               caixas.add(mapResultSetToCaixa(rs));
           }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return caixas;
    }

    public boolean save(Caixa caixa) {
        String sql = "INSERT INTO caixa (id, valorTotal) VALUES (?)";
        try (PreparedStatement ps = con.prepareStatement(sql)){
        ps.setDouble(1, caixa.getValorTotal());
        int affected = ps.executeUpdate();
        return affected == 1;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Caixa caixa) {
        String sql = "UPDATE caixa SET valorTotal = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, caixa.getValorTotal());
            ps.setInt(2, caixa.getId());
            int affected = ps.executeUpdate();
            return affected == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean delete(int id) {
        String sql = "DELETE FROM caixa WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
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
        double valorTotal = rs.getDouble("valorTotal");
        return new Caixa(id, valorTotal);
    }
}

