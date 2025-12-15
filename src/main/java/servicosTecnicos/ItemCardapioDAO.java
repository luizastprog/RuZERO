// classe DAO responsável pelas operações de banco de dados dos itens de cardápio
// gerencia a criação e listagem de itens associados a cardápios


package servicosTecnicos;

import dominio.ItemCardapio;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemCardapioDAO {

    private Connection getConnection() {
        return Conexao.getConexao();
    }

    public void salvar(ItemCardapio item) {
        String sql = "INSERT INTO ItemCardapio (idCardapio, tipoItem, nome) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, item.getIdCardapio());
            stmt.setString(2, item.getTipoItem());
            stmt.setString(3, item.getNome());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                item.setIdItemCardapio(rs.getInt(1));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao salvar item de cardápio!");
            e.printStackTrace();
        }
    }

    public void atualizar(ItemCardapio item) {
        String sql = "UPDATE ItemCardapio SET nome=?, tipoItem=? WHERE idItemCardapio=?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, item.getNome());
            stmt.setString(2, item.getTipoItem());
            stmt.setInt(3, item.getIdItemCardapio());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar item de cardápio!");
            e.printStackTrace();
        }
    }

    public List<ItemCardapio> buscarPorCardapio(Integer idCardapio) {
        List<ItemCardapio> itens = new ArrayList<>();

        String sql = "SELECT * FROM ItemCardapio WHERE idCardapio = ? ORDER BY idItemCardapio ASC";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCardapio);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ItemCardapio item = new ItemCardapio();
                item.setIdItemCardapio(rs.getInt("idItemCardapio"));
                item.setIdCardapio(rs.getInt("idCardapio"));
                item.setTipoItem(rs.getString("tipoItem"));
                item.setNome(rs.getString("nome"));
                itens.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return itens;
    }
}
