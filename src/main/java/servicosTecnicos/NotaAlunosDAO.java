package servicosTecnicos;

import dominio.NotaAlunos;
import dominio.Campus;
import dominio.Feedback;
import dominio.Gestao;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotaAlunosDAO {

    private final FeedbackDAO feedbackDAO = new FeedbackDAO();
    private final CampusDAO campusDAO = new CampusDAO();
    private final GestaoDAO gestaoDAO = new GestaoDAO();

    private Connection getConnection() {
        return Conexao.getConexao();
    }

    public void salvar(NotaAlunos notaAlunos) {
        String sql = "INSERT INTO nota_alunos (media_notas, total_avaliacao, feedback_id_feedback, " +
                "campus_id_campus, gestao_id_gestao) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setDouble(1, notaAlunos.getMediaNotas());
            stmt.setString(2, notaAlunos.getTotalAvaliacao());
            stmt.setInt(3, notaAlunos.getFeedback().getIdFeedback());
            stmt.setInt(4, notaAlunos.getCampus().getIdCampus());
            stmt.setInt(5, notaAlunos.getGestao().getIdGestao());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        notaAlunos.setIdNotaAlunos(rs.getInt(1));
                    }
                }
            }
            System.out.println("Relatório salvo no banco de dados!");

        } catch (SQLException e) {
            System.err.println("ERRO ao salvar relatório!");
            e.printStackTrace();
        }
    }

    public NotaAlunos buscarPorId(Integer id) {
        String sql = "SELECT * FROM nota_alunos WHERE id_nota_alunos = ?";
        NotaAlunos notaAlunos = null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    notaAlunos = mapearNotaAlunos(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("ERRO ao buscar relatório por ID!");
            e.printStackTrace();
        }
        return notaAlunos;
    }

    public List<NotaAlunos> listarTodos() {
        String sql = "SELECT * FROM nota_alunos ORDER BY id_nota_alunos DESC";
        List<NotaAlunos> lista = new ArrayList<>();

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearNotaAlunos(rs));
            }

        } catch (SQLException e) {
            System.err.println("ERRO ao listar relatórios!");
            e.printStackTrace();
        }
        return lista;
    }

    public List<NotaAlunos> listarPorCampus(Integer campusId) {
        String sql = "SELECT * FROM nota_alunos WHERE campus_id_campus = ? ORDER BY id_nota_alunos DESC";
        List<NotaAlunos> lista = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, campusId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearNotaAlunos(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("ERRO ao listar relatórios por campus!");
            e.printStackTrace();
        }
        return lista;
    }

    public void atualizar(NotaAlunos notaAlunos) {
        String sql = "UPDATE nota_alunos SET media_notas = ?, total_avaliacao = ?, feedback_id_feedback = ?, campus_id_campus = ?, gestao_id_gestao = ? WHERE id_nota_alunos = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, notaAlunos.getMediaNotas());
            stmt.setString(2, notaAlunos.getTotalAvaliacao());
            stmt.setInt(3, notaAlunos.getFeedback().getIdFeedback());
            stmt.setInt(4, notaAlunos.getCampus().getIdCampus());
            stmt.setInt(5, notaAlunos.getGestao().getIdGestao());
            stmt.setInt(6, notaAlunos.getIdNotaAlunos());

            stmt.executeUpdate();
            System.out.println("Relatório atualizado no banco de dados!");

        } catch (SQLException e) {
            System.err.println("ERRO ao atualizar relatório!");
            e.printStackTrace();
        }
    }

    public void deletar(int id) {
        String sql = "DELETE FROM nota_alunos WHERE id_nota_alunos = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Relatório deletado do banco de dados!");

        } catch (SQLException e) {
            System.err.println("ERRO ao deletar relatório!");
            e.printStackTrace();
        }
    }

    public List<NotaAlunos> listarMediaPorCampus() {
        String sql = "SELECT c.id_campus, c.nome, AVG(CAST(f.nota AS SIGNED)) AS media_notas " +
                "FROM campus c " +
                "LEFT JOIN feedback f ON c.id_campus = f.campus_id_campus " +
                "GROUP BY c.id_campus, c.nome";
        List<NotaAlunos> lista = new ArrayList<>();

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Campus campus = new Campus();
                campus.setIdCampus(rs.getInt("id_campus"));
                campus.setNome(rs.getString("nome"));

                NotaAlunos nota = new NotaAlunos();
                nota.setCampus(campus);
                nota.setMediaNotas(rs.getDouble("media_notas"));

                lista.add(nota);
            }
        } catch (SQLException e) {
            System.err.println("ERRO ao listar média de notas por campus!");
            e.printStackTrace();
        }
        return lista;
    }

    private NotaAlunos mapearNotaAlunos(ResultSet rs) throws SQLException {
        NotaAlunos notaAlunos = new NotaAlunos();
        notaAlunos.setIdNotaAlunos(rs.getInt("id_nota_alunos"));
        notaAlunos.setMediaNotas(rs.getDouble("media_notas"));
        notaAlunos.setTotalAvaliacao(rs.getString("total_avaliacao"));

        Feedback feedback = feedbackDAO.buscarPorId(rs.getInt("feedback_id_feedback"));
        Campus campus = campusDAO.buscarPorId(rs.getInt("campus_id_campus"));
        Gestao gestao = gestaoDAO.buscarPorId(rs.getInt("gestao_id_gestao"));

        notaAlunos.setFeedback(feedback);
        notaAlunos.setCampus(campus);
        notaAlunos.setGestao(gestao);

        return notaAlunos;
    }
}