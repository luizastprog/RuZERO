package web.api;

import static spark.Spark.*;
import com.google.gson.Gson;
import servicosTecnicos.NotaAlunosDAO;
import dominio.NotaAlunos;
import java.util.List;

public class NotaAlunosController {

    private final NotaAlunosDAO notaAlunosDAO;
    private final Gson gson;

    public NotaAlunosController() {
        this.notaAlunosDAO = new NotaAlunosDAO();
        this.gson = new Gson();
        setupEndpoints();
    }

    private void setupEndpoints() {
        final String PATH = "/relatorios";

        get(PATH + "/mapa", (req, res) -> {
            try {
                List<NotaAlunos> dadosMapa = notaAlunosDAO.listarMediaPorCampus();

                res.status(200);
                return gson.toJson(dadosMapa);
            } catch (Exception e) {
                res.status(500);
                return gson.toJson(new RespostaErro("Erro ao buscar dados do mapa: " + e.getMessage()));
            }
        });

        get(PATH + "/detalhes/:campusId", (req, res) -> {
            try {
                int campusId = Integer.parseInt(req.params(":campusId"));

                List<NotaAlunos> relatorio = notaAlunosDAO.listarPorCampus(campusId);

                res.status(200);
                return gson.toJson(relatorio);
            } catch (NumberFormatException e) {
                res.status(400);
                return gson.toJson(new RespostaErro("ID de Campus inválido."));
            } catch (Exception e) {
                res.status(500);
                return gson.toJson(new RespostaErro("Erro ao buscar relatório: " + e.getMessage()));
            }
        });

        post(PATH, (req, res) -> {
            try {
                NotaAlunos novoRelatorio = gson.fromJson(req.body(), NotaAlunos.class);
                notaAlunosDAO.salvar(novoRelatorio);
                res.status(201);
                return gson.toJson(novoRelatorio);
            } catch (Exception e) {
                res.status(400);
                return gson.toJson(new RespostaErro("Erro ao salvar relatório: " + e.getMessage()));
            }
        });
    }
}