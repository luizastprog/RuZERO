// classe DAO responsável pelas operações de banco de dados relacionadas a endereços
// gerencia persistência e recuperação de dados de localização

package servicosTecnicos;

import dominio.Endereco;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnderecoDAO {

    private Connection getConnection() {
        return Conexao.getConexao();
    }

    public void inserir(Endereco endereco) {
        String sql = "INSERT INTO Endereco (rua, numero, bairro) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, endereco.getRua());
            stmt.setString(2, endereco.getNumero());
            stmt.setString(3, endereco.getBairro());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        endereco.setIdEndereco(rs.getInt(1));
                    }
                }
            }
            System.out.println("Endereço inserido no banco de dados!");

        } catch (SQLException e) {
            System.err.println("Erro ao inserir Endereco: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Endereco buscarPorId(int id) {
        String sql = "SELECT * FROM Endereco WHERE idEndereco = ?";
        Endereco endereco = null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    endereco = new Endereco(
                            rs.getInt("idEndereco"),
                            rs.getString("rua"),
                            rs.getString("numero"),
                            rs.getString("bairro")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar Endereco por ID: " + e.getMessage());
        }
        return endereco;
    }

    public List<Endereco> listarTodos() {
        String sql = "SELECT * FROM Endereco ORDER BY idEndereco";
        List<Endereco> enderecos = new ArrayList<>();

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Endereco endereco = new Endereco(
                        rs.getInt("idEndereco"),
                        rs.getString("rua"),
                        rs.getString("numero"),
                        rs.getString("bairro")
                );
                enderecos.add(endereco);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar todos os Enderecos: " + e.getMessage());
        }
        return enderecos;
    }

    public void atualizar(Endereco endereco) {
        String sql = "UPDATE Endereco SET rua = ?, numero = ?, bairro = ? WHERE idEndereco = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, endereco.getRua());
            stmt.setString(2, endereco.getNumero());
            stmt.setString(3, endereco.getBairro());
            stmt.setInt(4, endereco.getIdEndereco());

            stmt.executeUpdate();
            System.out.println("Endereço atualizado no banco de dados!");

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar Endereco: " + e.getMessage());
        }
    }

    public void deletar(int id) {
        String sql = "DELETE FROM Endereco WHERE idEndereco = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Endereço deletado do banco de dados!");

        } catch (SQLException e) {
            System.err.println("Erro ao deletar Endereco: " + e.getMessage());
        }
    }
}