package web.api;

import dominio.Aluno;
import servicosTecnicos.AlunoDAO;
import com.google.gson.Gson;

import static spark.Spark.*;

public class AlunoController {

    private final AlunoDAO alunoDAO = new AlunoDAO();
    private final Gson gson = new Gson();

    public AlunoController() {
        setupEndpoints();
    }

    private void setupEndpoints() {

        final String PATH = "/alunos";

        post(PATH + "/login", (req, res) -> {

            Aluno reqAluno = gson.fromJson(req.body(), Aluno.class);
            Aluno aluno = alunoDAO.buscarPorEmail(reqAluno.getEmail());

            if (aluno == null) {
                res.status(401);
                return gson.toJson(new RespostaErro("Aluno não encontrado!"));
            }

            res.status(200);
            res.type("application/json");
            return gson.toJson(aluno);
        });

        post(PATH + "/cadastrar", (req, res) -> {

            Aluno aluno = gson.fromJson(req.body(), Aluno.class);

            if (alunoDAO.buscarPorEmail(aluno.getEmail()) != null) {
                res.status(409);
                return gson.toJson(new RespostaErro("Email já cadastrado!"));
            }

            alunoDAO.salvar(aluno);

            res.status(201);
            res.type("application/json");
            return gson.toJson(aluno);
        });
    }
}
