// classe DAO responsável pelas operações de banco de dados da entidade Aluno
// realiza cadastro, busca, listagem e verificação de alunos no banco

package servicosTecnicos;

import dominio.Aluno;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO {

    private Connection getConnection() {
        return Conexao.getConexao();
    }

    public void salvar(Aluno aluno) {

        String sql = "INSERT INTO Aluno (nome, email, idCampus) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, aluno.getNome());
            stmt.setString(2, aluno.getEmail());
            stmt.setInt(3, aluno.getIdCampus());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    aluno.setIdAluno(rs.getInt(1));
                }
            }

            System.out.println("Aluno cadastrado com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar aluno!");
            e.printStackTrace();
        }
    }

    public Aluno buscarPorEmail(String email) {

        String sql = "SELECT * FROM Aluno WHERE email = ?";
        Aluno aluno = null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                aluno = new Aluno();
                aluno.setIdAluno(rs.getInt("idAluno"));
                aluno.setNome(rs.getString("nome"));
                aluno.setEmail(rs.getString("email"));
                aluno.setIdCampus(rs.getInt("idCampus"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return aluno;
    }

    public List<Aluno> listarTodos() {

        List<Aluno> lista = new ArrayList<>();
        String sql = "SELECT * FROM Aluno";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Aluno aluno = new Aluno();
                aluno.setIdAluno(rs.getInt("idAluno"));
                aluno.setNome(rs.getString("nome"));
                aluno.setEmail(rs.getString("email"));
                aluno.setIdCampus(rs.getInt("idCampus"));

                lista.add(aluno);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}
