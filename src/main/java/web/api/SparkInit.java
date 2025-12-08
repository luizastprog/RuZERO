package web.api;

import spark.Spark;
import static spark.Spark.*;
import com.google.gson.Gson;
import web.api.AlunoController;
import web.api.CampusController;
import web.api.CardapioController;
import web.api.EnderecoController;
import web.api.FeedbackController;
import web.api.GestaoController;
import web.api.NotaAlunosController;

public class SparkInit {

    public static void iniciar() {

        port(4567);

        staticFiles.location("/ui");

        before("/api/*", (req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            res.header("Access-Control-Allow-Headers", "Content-Type, Authorization, Accept, X-Requested-With");
            res.type("application/json");   // Apenas API retorna JSON
        });

        options("/*", (req, res) -> {
            String headers = req.headers("Access-Control-Request-Headers");
            if (headers != null) {
                res.header("Access-Control-Allow-Headers", headers);
            }

            String methods = req.headers("Access-Control-Request-Method");
            if (methods != null) {
                res.header("Access-Control-Allow-Methods", methods);
            }

            return "OK";
        });

        path("/api", () -> {

            new AlunoController();
            new CampusController();
            new CardapioController();
            new EnderecoController();
            new FeedbackController();
            new GestaoController();
            new NotaAlunosController();

        });

        notFound((req, res) -> {
            res.status(404);
            res.type("application/json");
            return new Gson().toJson(new RespostaErro("Rota não encontrada"));
        });

        awaitInitialization();
        System.out.println("🔥 Servidor Spark rodando em http://localhost:4567");
        System.out.println("➡ Arquivos estáticos: http://localhost:4567/index.html");
        System.out.println("➡ API disponível em /api/*");
    }
}
