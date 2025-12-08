package web.api;

import dominio.Aluno;
import servicosTecnicos.AlunoDAO;
import com.google.gson.Gson;

import static spark.Spark.*;

public class AlunoController {

    private final AlunoDAO alunoDAO;
    private final Gson gson;

    public AlunoController() {
        this.alunoDAO = new AlunoDAO();
        this.gson = new Gson();
        setupEndpoints();
    }

    private void setupEndpoints() {

        final String PATH = "/alunos";

        post(PATH + "/login", (req, res) -> {
            Aluno alunoReq = gson.fromJson(req.body(), Aluno.class);

            Aluno aluno = alunoDAO.buscarPorEmail(alunoReq.getEmail());

            if (aluno != null) {
                res.status(200);
                return gson.toJson(aluno);
            } else {
                res.status(401);
                return gson.toJson(new RespostaErro("Email não encontrado!"));
            }
        });

        post(PATH + "/cadastrar", (req, res) -> {
            try {
                Aluno novoAluno = gson.fromJson(req.body(), Aluno.class);
                alunoDAO.salvar(novoAluno);

                res.status(201);
                return gson.toJson(novoAluno);
            } catch (Exception e) {
                res.status(400);
                return gson.toJson(new RespostaErro("Erro ao cadastrar aluno: " + e.getMessage()));
            }
        });
    }
}
