package web.api;

import static spark.Spark.*;
import com.google.gson.Gson;
import servicosTecnicos.GestaoDAO;
import dominio.Gestao;
import com.google.gson.GsonBuilder;

public class GestaoController {

    private final GestaoDAO gestaoDAO;
    private final Gson gson;

    public GestaoController() {
        this.gestaoDAO = new GestaoDAO();
        this.gson = new GsonBuilder().create();
        setupEndpoints();
    }

    private void setupEndpoints() {
        final String PATH = "/gestao";

        post(PATH + "/login", (req, res) -> {
            try {
                Gestao gestaoLogin = gson.fromJson(req.body(), Gestao.class);

                boolean sucesso = gestaoDAO.fazerLogin(gestaoLogin.getLogin(), gestaoLogin.getSenha());

                if (sucesso) {
                    res.status(200);
                    Gestao gestorCompleto = gestaoDAO.buscarPorLogin(gestaoLogin.getLogin());
                    return gson.toJson(gestorCompleto);
                } else {
                    res.status(401); // Não Autorizado
                    return gson.toJson(new RespostaErro("Login ou senha incorretos!"));
                }
            } catch (Exception e) {
                res.status(400);
                return gson.toJson(new RespostaErro("Erro na requisição de login: " + e.getMessage()));
            }
        });

        post(PATH, (req, res) -> {
            try {
                Gestao novoGestor = gson.fromJson(req.body(), Gestao.class);
                gestaoDAO.salvar(novoGestor);
                res.status(201); // Criado
                return gson.toJson(novoGestor);
            } catch (Exception e) {
                res.status(400);
                return gson.toJson(new RespostaErro("Erro ao cadastrar gestor: " + e.getMessage()));
            }
        });
    }
}