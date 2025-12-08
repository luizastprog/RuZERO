package web.api;

import static spark.Spark.*;
import com.google.gson.Gson;
import servicosTecnicos.FeedbackDAO;
import dominio.Feedback;
import java.util.List;

public class FeedbackController {

    private final FeedbackDAO feedbackDAO;
    private final Gson gson;

    public FeedbackController() {
        this.feedbackDAO = new FeedbackDAO();
        this.gson = new Gson();
        setupEndpoints();
    }

    private void setupEndpoints() {
        final String PATH = "/feedbacks";

        post(PATH, (req, res) -> {
            try {
                System.out.println("\n========== FEEDBACK RECEBIDO ==========");
                System.out.println("RAW JSON → " + req.body());

                Feedback novoFeedback = gson.fromJson(req.body(), Feedback.class);

                System.out.println("OBJETO CAMPUS → " + novoFeedback.getCampus());
                if (novoFeedback.getCampus() != null) {
                    System.out.println("ID DO CAMPUS → " + novoFeedback.getCampus().getIdCampus());
                } else {
                    System.out.println("ID DO CAMPUS → CAMPUS NULL");
                }

                System.out.println("ID DO ALUNO → " + novoFeedback.getAlunoMatricula());
                System.out.println("NOTA → " + novoFeedback.getNota());
                System.out.println("COMENTARIO → " + novoFeedback.getComentario());
                System.out.println("=========================================\n");

                feedbackDAO.salvar(novoFeedback);

                res.status(201);
                return gson.toJson(novoFeedback);

            } catch (Exception e) {
                res.status(400);
                return gson.toJson(new RespostaErro("Erro ao enviar avaliação: " + e.getMessage()));
            }
        });


        get(PATH, (req, res) -> {
            try {
                List<Feedback> feedbacks = feedbackDAO.listarTodos();
                res.status(200);
                return gson.toJson(feedbacks);
            } catch (Exception e) {
                res.status(500);
                return gson.toJson(new RespostaErro("Erro ao listar feedbacks: " + e.getMessage()));
            }
        });

        get(PATH + "/campus/:campusId", (req, res) -> {
            try {
                int campusId = Integer.parseInt(req.params(":campusId"));
                List<Feedback> feedbacks = feedbackDAO.listarPorCampus(campusId);
                res.status(200);
                return gson.toJson(feedbacks);
            } catch (NumberFormatException e) {
                res.status(400);
                return gson.toJson(new RespostaErro("ID de Campus inválido."));
            } catch (Exception e) {
                res.status(500);
                return gson.toJson(new RespostaErro("Erro ao listar feedbacks por campus: " + e.getMessage()));
            }
        });
    }
}