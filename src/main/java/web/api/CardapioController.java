package web.api;

import static spark.Spark.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dominio.Cardapio;
import servicosTecnicos.CardapioDAO;
import util.LocalDateAdapter;

import java.time.LocalDate;

public class CardapioController {

    private final Gson gson;
    private final CardapioDAO cardapioDAO;

    public CardapioController() {

        this.cardapioDAO = new CardapioDAO();
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

        setupEndpoints();
    }

    private void setupEndpoints() {

        final String PATH = "/cardapios";

        get(PATH, (req, res) -> {
            res.type("application/json");
            return gson.toJson(cardapioDAO.listarTodos());
        });

        get(PATH + "/data/:data", (req, res) -> {
            LocalDate data = LocalDate.parse(req.params(":data"));
            res.type("application/json");
            return gson.toJson(cardapioDAO.buscarPorData(data));
        });

        post(PATH, (req, res) -> {
            Cardapio c = gson.fromJson(req.body(), Cardapio.class);
            cardapioDAO.inserir(c);
            res.status(201);
            res.type("application/json");
            return gson.toJson(c);
        });
    }
}
