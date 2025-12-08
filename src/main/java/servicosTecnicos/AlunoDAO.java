package servicosTecnicos;

import dominio.Aluno;
import dominio.Campus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO {

    private final CampusDAO campusDAO = new CampusDAO();

    private Connection getConnection() {
        return Conexao.getConexao();
    }

    public void salvar(Aluno aluno) {

        Aluno existente = buscarPorEmail(aluno.getEmail());
        if (existente != null) {

            aluno.setIdAluno(existente.getIdAluno());
            aluno.setCampus(existente.getCampus());
            System.out.println("Aluno já existia — login realizado.");
            return;
        }

        String sql = "INSERT INTO Aluno (nome, email, Campus_idCampus) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, aluno.getNome());
            stmt.setString(2, aluno.getEmail());
            stmt.setInt(3, aluno.getCampus().getIdCampus());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    aluno.setIdAluno(rs.getInt(1));  // ID autogerado
                }
            }

            System.out.println("Aluno salvo (novo cadastro)!");

        } catch (SQLException e) {
            System.err.println("ERRO ao salvar aluno!");
            e.printStackTrace();
        }
    }


    public Aluno buscarPorId(Integer idAluno) {
        String sql = "SELECT * FROM Aluno WHERE idAluno = ?";
        Aluno aluno = null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idAluno);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                aluno = mapear(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aluno;
    }

    public Aluno buscarPorEmail(String email) {
        String sql = "SELECT * FROM Aluno WHERE email = ?";
        Aluno aluno = null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                aluno = mapear(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aluno;
    }

    public List<Aluno> listarTodos() {
        String sql = "SELECT * FROM Aluno ORDER BY nome";
        List<Aluno> lista = new ArrayList<>();

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private Aluno mapear(ResultSet rs) throws SQLException {
        Aluno aluno = new Aluno();
        aluno.setIdAluno(rs.getInt("idAluno"));
        aluno.setNome(rs.getString("nome"));
        aluno.setEmail(rs.getString("email"));

        int campusId = rs.getInt("Campus_idCampus");
        aluno.setCampus(campusDAO.buscarPorId(campusId));

        return aluno;
    }
}
