package web.api;

import static spark.Spark.*;
import com.google.gson.Gson;
import servicosTecnicos.CampusDAO;
import dominio.Campus;
import java.util.List;
import com.google.gson.GsonBuilder;


public class CampusController {

    private final CampusDAO campusDAO;
    private final Gson gson;

    public CampusController() {
        this.campusDAO = new CampusDAO();
        this.gson = new GsonBuilder().create();
        setupEndpoints();
    }

    private void setupEndpoints() {
        final String PATH = "/campus";

        get(PATH, (req, res) -> {
            try {
                List<Campus> listaCampi = campusDAO.listarTodos();
                res.status(200);
                return gson.toJson(listaCampi);
            } catch (Exception e) {
                res.status(500);
                return gson.toJson(new RespostaErro("Erro ao listar Campi: " + e.getMessage()));
            }
        });

        get(PATH + "/:id", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params(":id"));
                Campus campus = campusDAO.buscarPorId(id);
                if (campus != null) {
                    res.status(200);
                    return gson.toJson(campus);
                } else {
                    res.status(404);
                    return gson.toJson(new RespostaErro("Campus não encontrado."));
                }
            } catch (NumberFormatException e) {
                res.status(400);
                return gson.toJson(new RespostaErro("ID de Campus inválido."));
            }
        });

        post(PATH, (req, res) -> {
            try {
                Campus novoCampus = gson.fromJson(req.body(), Campus.class);
                campusDAO.salvar(novoCampus);
                res.status(201);
                return gson.toJson(novoCampus);
            } catch (Exception e) {
                res.status(400);
                return gson.toJson(new RespostaErro("Erro ao cadastrar Campus: " + e.getMessage()));
            }
        });

        delete(PATH + "/:id", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params(":id"));
                campusDAO.deletar(id);
                res.status(204); // No Content
                return "";
            } catch (Exception e) {
                res.status(500);
                return gson.toJson(new RespostaErro("Erro ao deletar Campus: " + e.getMessage()));
            }
        });
    }
}