package org.br.farmacia.services;

import org.br.farmacia.models.Setor;
import org.br.farmacia.repositories.SetorRepository;
import org.br.farmacia.database.ConexaoBanco; // Importar sua classe de conexão

import javax.servlet.ServletContext;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SetorService {

    private final SetorRepository setorRepository;
    private final ConexaoBanco conexaoBanco;
    public SetorService(ServletContext context) {
        this.conexaoBanco = (ConexaoBanco) context.getAttribute("DBConnectionProvider");
        Connection connection = null;
        connection = conexaoBanco.getConnection();
        this.setorRepository = new SetorRepository(connection);
    }


    public SetorService(Connection connection) {
        this.setorRepository = new SetorRepository(connection);
        this.conexaoBanco = null;
    }


    public boolean adicionarSetor(Setor setor) {

        Connection conn = null;
        try {
            conn = conexaoBanco.getConnection();
            SetorRepository repository = new SetorRepository(conn);
            return repository.save(setor);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Erro ao fechar conexão no adicionarSetor: " + e.getMessage());
                }
            }
        }
    }

    public Setor buscarPorId(int id) {
        Connection conn = null;
        try {
            conn = conexaoBanco.getConnection();
            SetorRepository repository = new SetorRepository(conn);
            return repository.findById(id);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Erro " + e.getMessage());
                }
            }
        }
    }

    public List<Setor> listarSetores() {
        Connection conn = null;
        try {
            conn = conexaoBanco.getConnection();
            SetorRepository repository = new SetorRepository(conn);
            return repository.findAll();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Erro " + e.getMessage());
                }
            }
        }
    }

    public boolean editarSetor(int id, Setor setorAtualizado) {
        Connection conn = null;
        try {
            conn = conexaoBanco.getConnection();
            SetorRepository repository = new SetorRepository(conn);
            setorAtualizado.setId(id);
            return repository.update(setorAtualizado);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println(" Erro " + e.getMessage());
                }
            }
        }
    }

    public boolean removerSetor(int id) {
        Connection conn = null;
        try {
            conn = conexaoBanco.getConnection();
            SetorRepository repository = new SetorRepository(conn);
            return repository.delete(id);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Erro " + e.getMessage());
                }
            }
        }
    }
}