package servicosTecnicos;

import dominio.Gestao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GestaoDAO {

    private Connection getConnection() {
        return Conexao.getConexao();
    }

    public void salvar(Gestao gestao) {
        String sql = "INSERT INTO Gestao (login, senha) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, gestao.getLogin());
            stmt.setString(2, gestao.getSenha());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    gestao.setIdGestao(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar gestão: " + e.getMessage(), e);
        }
    }

    public void atualizar(Gestao gestao) {
        String sql = "UPDATE Gestao SET login = ?, senha = ? WHERE idGestao = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, gestao.getLogin());
            stmt.setString(2, gestao.getSenha());
            stmt.setInt(3, gestao.getIdGestao());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar gestão: " + e.getMessage(), e);
        }
    }

    public void deletar(int idGestao) {
        String sql = "DELETE FROM Gestao WHERE idGestao = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idGestao);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar gestão: " + e.getMessage(), e);
        }
    }

    public boolean fazerLogin(String login, String senha) {
        String sql = "SELECT COUNT(*) FROM Gestao WHERE login = ? AND senha = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);
            stmt.setString(2, senha);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao fazer login: " + e.getMessage());
        }

        return false;
    }

    public Gestao buscarPorLogin(String login) {
        String sql = "SELECT * FROM Gestao WHERE login = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSet(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar gestão por login: " + e.getMessage(), e);
        }
        return null;
    }

    public Gestao autenticar(String login, String senha) {
        String sql = "SELECT * FROM Gestao WHERE login = ? AND senha = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);
            stmt.setString(2, senha);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSet(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao autenticar gestão: " + e.getMessage(), e);
        }
        return null;
    }

    public Gestao buscarPorId(int idGestao) {
        String sql = "SELECT * FROM Gestao WHERE idGestao = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idGestao);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSet(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar gestão por ID: " + e.getMessage(), e);
        }
        return null;
    }

    public List<Gestao> listarTodos() {
        String sql = "SELECT * FROM Gestao ORDER BY idGestao";
        List<Gestao> lista = new ArrayList<>();

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearResultSet(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar gestões: " + e.getMessage(), e);
        }

        return lista;
    }

    public boolean loginExiste(String login) {
        String sql = "SELECT COUNT(*) FROM Gestao WHERE login = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar login existente: " + e.getMessage(), e);
        }
        return false;
    }

    private Gestao mapearResultSet(ResultSet rs) throws SQLException {
        Gestao g = new Gestao();
        g.setIdGestao(rs.getInt("idGestao"));
        g.setLogin(rs.getString("login"));
        g.setSenha(rs.getString("senha"));
        return g;
    }
}
