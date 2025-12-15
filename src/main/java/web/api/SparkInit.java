// classe responsável por configurar e iniciar o servidor Spark Java
// registra todos os controllers e inicializa o servidor

package web.api;

import static spark.Spark.*;
import com.google.gson.Gson;

public class SparkInit {

    public static void iniciar() {

        port(4567);

        String devPath = java.nio.file.Paths.get("src", "main", "resources", "ui").toAbsolutePath().toString();
        java.io.File devFolder = new java.io.File(devPath);

        if (devFolder.exists() && devFolder.isDirectory()) {
            System.out.println("⚙️  Dev mode: usando arquivos estáticos em " + devPath);
            staticFiles.externalLocation(devPath);
        } else {
            System.out.println("📦 Prod mode: usando arquivos estáticos do classpath /ui");
            staticFiles.location("/ui");
        }

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
            new ItemCardapioController();

        });

        notFound((req, res) -> {
            res.status(404);
            res.type("application/json");
            return new Gson().toJson(new RespostaErro("Rota não encontrada"));
        });
        before((req, res) -> {
            System.out.println("ROTA ACESSADA: " + req.requestMethod() + " " + req.pathInfo());
        });

        awaitInitialization();
        System.out.println("🔥 Servidor Spark rodando em http://localhost:4567");
        System.out.println("➡ Arquivos estáticos: http://localhost:4567/index.html");
        System.out.println("➡ API disponível em /api/*");
    }
}
