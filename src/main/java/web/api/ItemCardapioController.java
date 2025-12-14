package web.api;

import static spark.Spark.*;
import com.google.gson.Gson;
import dominio.ItemCardapio;
import servicosTecnicos.ItemCardapioDAO;

public class ItemCardapioController {

    private final ItemCardapioDAO dao = new ItemCardapioDAO();
    private final Gson gson = new Gson();

    public ItemCardapioController() {
        setupEndpoints();
    }

    private void setupEndpoints() {

        post("/itens-cardapio", (req, res) -> {
            try {
                res.type("application/json");
                ItemCardapio item = gson.fromJson(req.body(), ItemCardapio.class);

                if (item.getIdCardapio() == null) {
                    res.status(400);
                    return gson.toJson(new RespostaErro("idCardapio é obrigatório!"));
                }

                dao.salvar(item);
                res.status(201);
                return gson.toJson(item);

            } catch (Exception e) {
                res.status(500);
                return gson.toJson(new RespostaErro("Erro ao criar item: " + e.getMessage()));
            }
        });

        get("/itens-cardapio/:cardapioId", (req, res) -> {
            try {
                res.type("application/json");

                int idCardapio = Integer.parseInt(req.params(":cardapioId"));
                return gson.toJson(dao.buscarPorCardapio(idCardapio));

            } catch (NumberFormatException e) {
                res.status(400);
                return gson.toJson(new RespostaErro("ID inválido!"));
            } catch (Exception e) {
                res.status(500);
                return gson.toJson(new RespostaErro("Erro ao buscar itens: " + e.getMessage()));
            }
        });

        put("/itens-cardapio/:id", (req, res) -> {
            try {
                res.type("application/json");

                int id = Integer.parseInt(req.params(":id"));
                ItemCardapio item = gson.fromJson(req.body(), ItemCardapio.class);
                item.setIdItemCardapio(id);

                dao.atualizar(item);

                return gson.toJson(item);

            } catch (NumberFormatException e) {
                res.status(400);
                return gson.toJson(new RespostaErro("ID inválido!"));
            } catch (Exception e) {
                res.status(500);
                return gson.toJson(new RespostaErro("Erro ao atualizar item: " + e.getMessage()));
            }
        });
    }
}
