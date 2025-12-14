package servicosTecnicos;

import dominio.Cardapio;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CardapioDAO {

    private Connection getConnection() {
        return Conexao.getConexao();
    }

    public void inserir(Cardapio cardapio) {

        String sql = "INSERT INTO Cardapio (data, idGestao) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setDate(1, Date.valueOf(cardapio.getData()));
            stmt.setInt(2, cardapio.getIdGestao());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    cardapio.setIdCardapio(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Cardapio> listarTodos() {

        String sql = "SELECT * FROM Cardapio ORDER BY data DESC";
        List<Cardapio> lista = new ArrayList<>();

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public Cardapio buscarPorData(LocalDate data) {

        String sql = "SELECT * FROM Cardapio WHERE data = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(data));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSet(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Cardapio mapearResultSet(ResultSet rs) throws SQLException {

        Cardapio c = new Cardapio();

        c.setIdCardapio(rs.getInt("idCardapio"));
        c.setData(rs.getDate("data").toLocalDate());
        c.setIdGestao(rs.getInt("idGestao"));

        return c;
    }
}
