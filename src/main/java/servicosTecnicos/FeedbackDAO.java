// classe DAO responsável pelas operações de banco de dados da entidade Feedback.
// salva avaliações, realiza consultas, calcula médias e gera dados para relatórios

package servicosTecnicos;

import dominio.Feedback;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedbackDAO {

    private Connection getConnection() {
        return Conexao.getConexao();
    }

    public void salvar(Feedback feedback) {

        String sql = "INSERT INTO Feedback (nota, comentario, dataHora, idCampus, idAluno) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, feedback.getNota());
            stmt.setString(2, feedback.getComentario());

            LocalDateTime data = feedback.getDataHora() != null
                    ? feedback.getDataHora()
                    : LocalDateTime.now();

            stmt.setTimestamp(3, Timestamp.valueOf(data));
            stmt.setInt(4, feedback.getIdCampus());
            stmt.setInt(5, feedback.getIdAluno());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    feedback.setIdFeedback(rs.getInt(1));
                }
            }

            System.out.println("✔ Feedback salvo!");

        } catch (SQLException e) {
            System.err.println("ERRO ao salvar feedback!");
            e.printStackTrace();
        }
    }

    public List<Feedback> listarTodos() {

        String sql = "SELECT * FROM Feedback ORDER BY dataHora DESC";
        List<Feedback> lista = new ArrayList<>();

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearFeedback(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<Feedback> listarPorCampus(Integer idCampus) {

        String sql = "SELECT * FROM Feedback WHERE idCampus = ? ORDER BY dataHora DESC";
        List<Feedback> lista = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCampus);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(mapearFeedback(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    private Feedback mapearFeedback(ResultSet rs) throws SQLException {

        Feedback fb = new Feedback();

        fb.setIdFeedback(rs.getInt("idFeedback"));
        fb.setNota(rs.getString("nota"));
        fb.setComentario(rs.getString("comentario"));

        Timestamp ts = rs.getTimestamp("dataHora");
        fb.setDataHora(ts != null ? ts.toLocalDateTime() : LocalDateTime.now());

        fb.setIdCampus(rs.getInt("idCampus"));
        fb.setIdAluno(rs.getInt("idAluno"));

        return fb;
    }

    public List<Map<String, Object>> listarMediasPorCampus() {

        String sql = """
            SELECT c.idCampus, c.nome,
                   AVG(CAST(f.nota AS DECIMAL(10,2))) AS media
            FROM Campus c
            LEFT JOIN Feedback f ON f.idCampus = c.idCampus
            GROUP BY c.idCampus, c.nome
            ORDER BY c.idCampus
        """;

        List<Map<String, Object>> lista = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Map<String, Object> campus = new HashMap<>();
                campus.put("idCampus", rs.getInt("idCampus"));
                campus.put("nome", rs.getString("nome"));

                Map<String, Object> item = new HashMap<>();
                item.put("campus", campus);
                item.put("mediaNotas", rs.getDouble("media"));

                lista.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<Map<String, Object>> listarMediaNotasPorHorario(int idCampus) {

        String sql = """
            SELECT HOUR(dataHora) AS hora,
                   AVG(CAST(nota AS DECIMAL(10,2))) AS mediaNota
            FROM Feedback
            WHERE idCampus = ?
            GROUP BY HOUR(dataHora)
            ORDER BY hora
        """;

        List<Map<String, Object>> lista = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCampus);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> item = new HashMap<>();
                item.put("hora", rs.getInt("hora"));
                item.put("mediaNota", rs.getDouble("mediaNota"));
                lista.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}
