package servicosTecnicos;

import dominio.Campus;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CampusDAO {

    private Connection getConnection() {
        return Conexao.getConexao();
    }

    public void salvar(Campus campus) {
        String sql = "INSERT INTO Campus (nome) VALUES (?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, campus.getNome());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        campus.setIdCampus(rs.getInt(1));
                    }
                }
            }
            System.out.println("Campus salvo no banco de dados!");

        } catch (SQLException e) {
            System.err.println("ERRO ao salvar campus!");
            e.printStackTrace();
        }
    }

    public Campus buscarPorId(Integer id) {
        String sql = "SELECT * FROM Campus WHERE idCampus = ?";
        Campus campus = null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    campus = new Campus();
                    campus.setIdCampus(rs.getInt("idCampus"));
                    campus.setNome(rs.getString("nome"));
                }
            }
        } catch (SQLException e) {
            System.err.println("ERRO ao buscar campus por ID!");
            e.printStackTrace();
        }
        return campus;
    }


    public List<Campus> listarTodos() {
        String sql = "SELECT * FROM Campus ORDER BY idCampus";
        List<Campus> lista = new ArrayList<>();

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Campus campus = new Campus();
                campus.setIdCampus(rs.getInt("idCampus"));
                campus.setNome(rs.getString("nome"));
                lista.add(campus);
            }

        } catch (SQLException e) {
            System.err.println("ERRO ao listar campus!");
            e.printStackTrace();
        }
        return lista;
    }

    public void deletar(int id) {
        String sql = "DELETE FROM Campus WHERE idCampus = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Campus deletado do banco de dados!");

        } catch (SQLException e) {
            System.err.println("ERRO ao deletar campus!");
            e.printStackTrace();
        }
    }
}