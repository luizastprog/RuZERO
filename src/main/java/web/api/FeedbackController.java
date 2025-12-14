package web.api;

import static spark.Spark.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dominio.Feedback;
import servicosTecnicos.FeedbackDAO;
import util.LocalDateTimeAdapter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class FeedbackController {

    private final FeedbackDAO feedbackDAO;
    private final Gson gson;

    public FeedbackController() {

        this.feedbackDAO = new FeedbackDAO();
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        setupEndpoints();
    }

    private void setupEndpoints() {

        final String PATH = "/feedbacks";

        post(PATH, (req, res) -> {

            try {
                Feedback fb = gson.fromJson(req.body(), Feedback.class);

                if (fb.getIdCampus() == null || fb.getIdAluno() == null) {
                    res.status(400);
                    return gson.toJson(
                            new RespostaErro("Campus ou aluno inválido.")
                    );
                }

                feedbackDAO.salvar(fb);

                res.status(201);
                res.type("application/json");
                return gson.toJson(fb);

            } catch (Exception e) {
                res.status(400);
                return gson.toJson(
                        new RespostaErro("Erro ao enviar feedback: " + e.getMessage())
                );
            }
        });

        get(PATH, (req, res) -> gson.toJson(feedbackDAO.listarTodos()));

        get(PATH + "/campus/:campusId", (req, res) -> {
            int idCampus = Integer.parseInt(req.params("campusId"));
            return gson.toJson(feedbackDAO.listarPorCampus(idCampus));
        });

        get(PATH + "/medias/mapa", (req, res) ->
                gson.toJson(feedbackDAO.listarMediasPorCampus())
        );

        get(PATH + "/horarios/media/:campusId", (req, res) -> {

            int idCampus = Integer.parseInt(req.params("campusId"));
            List<Map<String, Object>> dados =
                    feedbackDAO.listarMediaNotasPorHorario(idCampus);

            return gson.toJson(dados);
        });
    }
}
