// classe DAO responsável pelo acesso aos dados de NotaAlunos
// realiza operações de persistência e consulta dos relatórios
// de avaliações, incluindo:
// - salvamento de médias de notas,
// - listagem geral de relatórios,
// - listagem por campus,
// - cálculo da média de notas por campus.
// é utilizada principalmente para geração de relatórios
// e visualizações administrativas

package servicosTecnicos;

import dominio.NotaAlunos;
import dominio.Campus;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotaAlunosDAO {

    private Connection getConnection() {
        return Conexao.getConexao();
    }

    public void salvar(NotaAlunos notaAlunos) {

        String sql = "INSERT INTO NotaAlunos " +
                "(mediaNotas, totalAvaliacao, idFeedback, idCampus, idGestao) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setDouble(1, notaAlunos.getMediaNotas());
            stmt.setString(2, notaAlunos.getTotalAvaliacao());
            stmt.setInt(3, notaAlunos.getIdFeedback());
            stmt.setInt(4, notaAlunos.getIdCampus());
            stmt.setInt(5, notaAlunos.getIdGestao());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    notaAlunos.setIdNotaAlunos(rs.getInt(1));
                }
            }

            System.out.println("Relatório salvo no banco de dados!");

        } catch (SQLException e) {
            System.err.println("ERRO ao salvar relatório!");
            e.printStackTrace();
        }
    }

    public List<NotaAlunos> listarTodos() {

        String sql = "SELECT * FROM NotaAlunos ORDER BY idNotaAlunos DESC";
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

    public List<NotaAlunos> listarPorCampus(Integer idCampus) {

        String sql = "SELECT * FROM NotaAlunos WHERE idCampus = ? ORDER BY idNotaAlunos DESC";
        List<NotaAlunos> lista = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCampus);

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

    public List<NotaAlunos> listarMediaPorCampus() {

        String sql = "SELECT c.idCampus, c.nome, " +
                "AVG(CAST(f.nota AS SIGNED)) AS mediaNotas " +
                "FROM Campus c " +
                "LEFT JOIN Feedback f ON c.idCampus = f.idCampus " +
                "GROUP BY c.idCampus, c.nome";

        List<NotaAlunos> lista = new ArrayList<>();

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                NotaAlunos nota = new NotaAlunos();
                nota.setIdCampus(rs.getInt("id_campus"));
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

        notaAlunos.setIdNotaAlunos(rs.getInt("idNotaAlunos"));
        notaAlunos.setMediaNotas(rs.getDouble("mediaNotas"));
        notaAlunos.setTotalAvaliacao(rs.getString("totalAvaliacao"));
        notaAlunos.setIdFeedback(rs.getInt("idFeedback"));
        notaAlunos.setIdCampus(rs.getInt("idCampus"));
        notaAlunos.setIdGestao(rs.getInt("idGestao"));

        return notaAlunos;
    }
}
