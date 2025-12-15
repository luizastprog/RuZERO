// controller responsável pelas operações relacionadas a endereços
// gerencia cadastro e consulta de endereços associados a entidades do sistema


package web.api;

import static spark.Spark.*;
import com.google.gson.Gson;
import dominio.Endereco;
import servicosTecnicos.EnderecoDAO;
import com.google.gson.GsonBuilder;

public class EnderecoController {

    private final EnderecoDAO enderecoDAO;
    private final Gson gson;

    public EnderecoController() {
        this.enderecoDAO = new EnderecoDAO();
        this.gson = new GsonBuilder().create();
        setupEndpoints();
    }

    private void setupEndpoints() {
        final String PATH = "/enderecos";

        get(PATH, (req, res) -> {
            try {
                return gson.toJson(enderecoDAO.listarTodos());
            } catch (Exception e) {
                res.status(500);
                return gson.toJson(new RespostaErro("Erro ao listar endereços: " + e.getMessage()));
            }
        });

        get(PATH + "/:id", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params(":id"));
                Endereco endereco = enderecoDAO.buscarPorId(id);
                if (endereco != null) {
                    return gson.toJson(endereco);
                } else {
                    res.status(404);
                    return gson.toJson(new RespostaErro("Endereço não encontrado"));
                }
            } catch (NumberFormatException e) {
                res.status(400);
                return gson.toJson(new RespostaErro("ID de Endereço inválido."));
            } catch (Exception e) {
                res.status(500);
                return gson.toJson(new RespostaErro("Erro ao buscar endereço: " + e.getMessage()));
            }
        });

        post(PATH, (req, res) -> {
            try {
                Endereco novoEndereco = gson.fromJson(req.body(), Endereco.class);
                enderecoDAO.inserir(novoEndereco); // AQUI: mudou de salvar() para inserir()
                res.status(201); // Created
                return gson.toJson(novoEndereco);
            } catch (Exception e) {
                res.status(400);
                return gson.toJson(new RespostaErro("Erro ao criar endereço: " + e.getMessage()));
            }
        });

        put(PATH + "/:id", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params(":id"));
                Endereco enderecoAtualizacao = gson.fromJson(req.body(), Endereco.class);
                enderecoAtualizacao.setIdEndereco(id);

                if (enderecoDAO.buscarPorId(id) == null) {
                    res.status(404);
                    return gson.toJson(new RespostaErro("Endereço não encontrado para atualização"));
                }

                enderecoDAO.atualizar(enderecoAtualizacao);
                res.status(200);
                return gson.toJson(enderecoAtualizacao);

            } catch (Exception e) {
                res.status(400);
                return gson.toJson(new RespostaErro("Erro ao atualizar endereço: " + e.getMessage()));
            }
        });

        delete(PATH + "/:id", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params(":id"));
                enderecoDAO.deletar(id);

                res.status(204); // No Content
                return "";
            } catch (Exception e) {
                res.status(500);
                return gson.toJson(new RespostaErro("Erro ao deletar endereço: " + e.getMessage()));
            }
        });
    }
}