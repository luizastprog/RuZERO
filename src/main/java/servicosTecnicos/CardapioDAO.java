package servicosTecnicos;

import dominio.Cardapio;
import dominio.CardapioAluno;
import dominio.Gestao;
import dominio.ItemCardapio;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CardapioDAO {

    private Connection getConnection() {
        return Conexao.getConexao();
    }

    public void inserir(Cardapio cardapio) {
        String sql = "INSERT INTO cardapio (data, id_cardapio_aluno, id_gestao) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setDate(1, Date.valueOf(cardapio.getData()));
            stmt.setInt(2, cardapio.getCardapioAluno().getIdCardapioAluno());
            stmt.setInt(3, cardapio.getGestao().getIdGestao());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        cardapio.setIdCardapio(rs.getInt(1));
                    }
                }
            }
            System.out.println("Cardápio inserido no banco de dados!");

        } catch (SQLException e) {
            System.err.println("Erro ao inserir cardápio: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void atualizar(Cardapio cardapio) {
        String sql = "UPDATE cardapio SET data = ?, id_cardapio_aluno = ?, id_gestao = ? WHERE id_cardapio = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(cardapio.getData()));
            stmt.setInt(2, cardapio.getCardapioAluno().getIdCardapioAluno());
            stmt.setInt(3, cardapio.getGestao().getIdGestao());
            stmt.setInt(4, cardapio.getIdCardapio());

            stmt.executeUpdate();
            System.out.println("Cardápio atualizado no banco de dados!");

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar cardápio: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deletar(int idCardapio) {
        String sql = "DELETE FROM cardapio WHERE id_cardapio = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCardapio);
            stmt.executeUpdate();
            System.out.println("Cardápio deletado do banco de dados!");

        } catch (SQLException e) {
            System.err.println("Erro ao deletar cardápio: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Cardapio buscarPorId(int idCardapio) {
        String sql = "SELECT * FROM cardapio WHERE id_cardapio = ?";
        Cardapio cardapio = null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCardapio);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    cardapio = mapearResultSet(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar cardápio por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return cardapio;
    }

    public List<Cardapio> listarTodos() {
        String sql = "SELECT * FROM cardapio ORDER BY data DESC";
        List<Cardapio> cardapios = new ArrayList<>();

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                cardapios.add(mapearResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar todos os cardápios: " + e.getMessage());
            e.printStackTrace();
        }
        return cardapios;
    }

    public List<Cardapio> buscarPorSemana(LocalDate dataInicio, LocalDate dataFim) {
        String sql = "SELECT * FROM cardapio WHERE data BETWEEN ? AND ? ORDER BY data ASC";
        List<Cardapio> cardapios = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(dataInicio));
            stmt.setDate(2, Date.valueOf(dataFim));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cardapios.add(mapearResultSet(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar cardápios por semana: " + e.getMessage());
            e.printStackTrace();
        }
        return cardapios;
    }

    public Cardapio buscarPorData(LocalDate data) {
        String sql = "SELECT * FROM cardapio WHERE data = ?";
        Cardapio cardapio = null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(data));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    cardapio = mapearResultSet(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar cardápio por data: " + e.getMessage());
            e.printStackTrace();
        }
        return cardapio;
    }

    public List<Cardapio> buscarPorCampus(int idCampus) {
        String sql = "SELECT c.* FROM cardapio c " +
                "INNER JOIN gestao g ON c.id_gestao = g.id_gestao " +
                "WHERE g.id_campus = ? " +
                "ORDER BY c.data DESC";
        List<Cardapio> cardapios = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCampus);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cardapios.add(mapearResultSet(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar cardápios por campus: " + e.getMessage());
            e.printStackTrace();
        }
        return cardapios;
    }

    public List<Cardapio> buscarCardapioSemanaAtual() {
        LocalDate hoje = LocalDate.now();
        int diaSemana = hoje.getDayOfWeek().getValue();
        LocalDate segunda = hoje.minusDays(diaSemana - 1);
        LocalDate sexta = segunda.plusDays(4);

        return buscarPorSemana(segunda, sexta);
    }

    public List<Cardapio> buscarPorPeriodoECampus(LocalDate dataInicio, LocalDate dataFim, int idCampus) {
        String sql = "SELECT c.* FROM cardapio c " +
                "INNER JOIN gestao g ON c.id_gestao = g.id_gestao " +
                "WHERE c.data BETWEEN ? AND ? AND g.id_campus = ? " +
                "ORDER BY c.data ASC";
        List<Cardapio> cardapios = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(dataInicio));
            stmt.setDate(2, Date.valueOf(dataFim));
            stmt.setInt(3, idCampus);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cardapios.add(mapearResultSet(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar cardápios por período e campus: " + e.getMessage());
            e.printStackTrace();
        }
        return cardapios;
    }

    public boolean existeCardapioParaData(LocalDate data) {
        String sql = "SELECT COUNT(*) FROM cardapio WHERE data = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(data));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao verificar cardápio: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public List<ItemCardapio> buscarItensPorData(LocalDate data) {
        String sql = "SELECT ic.* FROM item_cardapio ic " +
                "INNER JOIN cardapio c ON ic.id_cardapio = c.id_cardapio " +
                "WHERE c.data = ? " +
                "ORDER BY ic.tipo_item";
        List<ItemCardapio> itens = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(data));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ItemCardapio item = new ItemCardapio();
                    item.setIdItemCardapio(rs.getInt("id_item_cardapio"));
                    itens.add(item);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar itens do cardápio: " + e.getMessage());
            e.printStackTrace();
        }
        return itens;
    }

    private Cardapio mapearResultSet(ResultSet rs) throws SQLException {
        Cardapio cardapio = new Cardapio();
        cardapio.setIdCardapio(rs.getInt("id_cardapio"));
        cardapio.setData(rs.getDate("data").toLocalDate());

        CardapioAluno cardapioAluno = new CardapioAluno();
        cardapioAluno.setIdCardapioAluno(rs.getInt("id_cardapio_aluno"));
        cardapio.setCardapioAluno(cardapioAluno);

        Gestao gestao = new Gestao();
        gestao.setIdGestao(rs.getInt("id_gestao"));
        cardapio.setGestao(gestao);

        return cardapio;
    }
}