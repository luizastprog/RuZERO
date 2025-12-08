package servicosTecnicos;

import dominio.Feedback;
import dominio.Campus;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FeedbackDAO {

    private final CampusDAO campusDAO = new CampusDAO();

    private Connection getConnection() {
        return Conexao.getConexao();
    }

    public void salvar(Feedback feedback) {

        String sql = "INSERT INTO feedback (nota, comentario, data_hora, campus_id_campus, aluno_matricula) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, feedback.getNota());
            stmt.setString(2, feedback.getComentario());

            LocalDateTime data = (feedback.getDataHora() == null)
                    ? LocalDateTime.now()
                    : feedback.getDataHora();

            stmt.setTimestamp(3, Timestamp.valueOf(data));
            stmt.setInt(4, feedback.getCampus().getIdCampus());
            stmt.setInt(5, feedback.getAlunoMatricula());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    feedback.setIdFeedback(rs.getInt(1));
                }
            }

            System.out.println("✔ Feedback salvo!");

        } catch (SQLException e) {
            System.err.println("❌ ERRO ao salvar feedback!");
            e.printStackTrace();
        }
    }

    public Feedback buscarPorId(Integer id) {

        String sql = "SELECT * FROM feedback WHERE id_feedback = ?";
        Feedback fb = null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    fb = mapearFeedback(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ ERRO ao buscar feedback por ID!");
            e.printStackTrace();
        }

        return fb;
    }

    public List<Feedback> listarTodos() {

        String sql = "SELECT * FROM feedback ORDER BY data_hora DESC";
        List<Feedback> lista = new ArrayList<>();

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearFeedback(rs));
            }

        } catch (SQLException e) {
            System.err.println("❌ ERRO ao listar feedbacks!");
            e.printStackTrace();
        }

        return lista;
    }

    public List<Feedback> listarPorCampus(Integer campusId) {

        String sql = "SELECT * FROM feedback WHERE campus_id_campus = ? ORDER BY data_hora DESC";
        List<Feedback> lista = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, campusId);
            ResultSet rs = stmt.executeQuery();

            Campus campus = campusDAO.buscarPorId(campusId);

            while (rs.next()) {
                Feedback fb = mapearFeedback(rs);
                fb.setCampus(campus);
                lista.add(fb);
            }

        } catch (SQLException e) {
            System.err.println("❌ ERRO ao listar feedbacks por campus!");
            e.printStackTrace();
        }

        return lista;
    }

    public void atualizar(Feedback fb) {

        String sql = "UPDATE feedback SET nota = ?, comentario = ?, campus_id_campus = ?, aluno_matricula = ? " +
                "WHERE id_feedback = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fb.getNota());
            stmt.setString(2, fb.getComentario());
            stmt.setInt(3, fb.getCampus().getIdCampus());
            stmt.setInt(4, fb.getAlunoMatricula());
            stmt.setInt(5, fb.getIdFeedback());

            stmt.executeUpdate();
            System.out.println("✔ Feedback atualizado!");

        } catch (SQLException e) {
            System.err.println("❌ ERRO ao atualizar feedback!");
            e.printStackTrace();
        }
    }

    public void deletar(int id) {

        String sql = "DELETE FROM feedback WHERE id_feedback = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

            System.out.println("✔ Feedback deletado!");

        } catch (SQLException e) {
            System.err.println("❌ ERRO ao deletar feedback!");
            e.printStackTrace();
        }
    }

    private Feedback mapearFeedback(ResultSet rs) throws SQLException {

        Feedback fb = new Feedback();

        fb.setIdFeedback(rs.getInt("id_feedback"));
        fb.setNota(rs.getString("nota"));
        fb.setComentario(rs.getString("comentario"));

        Timestamp ts = rs.getTimestamp("data_hora");
        fb.setDataHora(ts != null ? ts.toLocalDateTime() : LocalDateTime.now());

        fb.setAlunoMatricula(rs.getInt("aluno_matricula"));

        Campus campus = campusDAO.buscarPorId(rs.getInt("campus_id_campus"));
        fb.setCampus(campus);

        return fb;
    }
}
