package web.api;

import static spark.Spark.*;
import com.google.gson.Gson;
import servicosTecnicos.CardapioDAO;
import dominio.Cardapio;
import java.time.LocalDate;
import java.util.List;

public class CardapioController {

    private final CardapioDAO cardapioDAO;
    private final Gson gson;

    public CardapioController() {
        this.cardapioDAO = new CardapioDAO();
        this.gson = new Gson();
        setupEndpoints();
    }

    private void setupEndpoints() {
        final String PATH = "/cardapios";

        get(PATH, (req, res) -> {
            try {
                List<Cardapio> cardapios = cardapioDAO.listarTodos();
                res.status(200);
                return gson.toJson(cardapios);
            } catch (Exception e) {
                res.status(500);
                return gson.toJson(new RespostaErro("Erro ao listar cardápios: " + e.getMessage()));
            }
        });

        get(PATH + "/:id", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params(":id"));
                Cardapio cardapio = cardapioDAO.buscarPorId(id);

                if (cardapio != null) {
                    res.status(200);
                    return gson.toJson(cardapio);
                } else {
                    res.status(404);
                    return gson.toJson(new RespostaErro("Cardápio não encontrado."));
                }
            } catch (NumberFormatException e) {
                res.status(400);
                return gson.toJson(new RespostaErro("ID de Cardápio inválido."));
            } catch (Exception e) {
                res.status(500);
                return gson.toJson(new RespostaErro("Erro ao buscar cardápio: " + e.getMessage()));
            }
        });

        get(PATH + "/data/:data", (req, res) -> {
            try {
                LocalDate data = LocalDate.parse(req.params(":data"));
                Cardapio cardapio = cardapioDAO.buscarPorData(data);

                if (cardapio != null) {
                    res.status(200);
                    return gson.toJson(cardapio);
                } else {
                    res.status(404);
                    return gson.toJson(new RespostaErro("Nenhum cardápio encontrado para esta data."));
                }
            } catch (Exception e) {
                res.status(500);
                return gson.toJson(new RespostaErro("Erro ao buscar cardápio: " + e.getMessage()));
            }
        });

        get(PATH + "/semana-atual", (req, res) -> {
            try {
                List<Cardapio> cardapios = cardapioDAO.buscarCardapioSemanaAtual();
                res.status(200);
                return gson.toJson(cardapios);
            } catch (Exception e) {
                res.status(500);
                return gson.toJson(new RespostaErro("Erro ao buscar cardápios da semana: " + e.getMessage()));
            }
        });

        get(PATH + "/campus/:campusId", (req, res) -> {
            try {
                int campusId = Integer.parseInt(req.params(":campusId"));
                List<Cardapio> cardapios = cardapioDAO.buscarPorCampus(campusId);

                if (cardapios != null && !cardapios.isEmpty()) {
                    res.status(200);
                    return gson.toJson(cardapios);
                } else {
                    res.status(404);
                    return gson.toJson(new RespostaErro("Nenhum cardápio encontrado para este campus."));
                }
            } catch (NumberFormatException e) {
                res.status(400);
                return gson.toJson(new RespostaErro("ID de Campus inválido."));
            } catch (Exception e) {
                res.status(500);
                return gson.toJson(new RespostaErro("Erro ao buscar cardápios: " + e.getMessage()));
            }
        });

        get(PATH + "/semana/:campusId", (req, res) -> {
            try {
                int campusId = Integer.parseInt(req.params(":campusId"));

                LocalDate hoje = LocalDate.now();
                int diaSemana = hoje.getDayOfWeek().getValue();
                LocalDate segunda = hoje.minusDays(diaSemana - 1);
                LocalDate sexta = segunda.plusDays(4);

                List<Cardapio> cardapios = cardapioDAO.buscarPorPeriodoECampus(segunda, sexta, campusId);

                if (cardapios != null && !cardapios.isEmpty()) {
                    res.status(200);
                    return gson.toJson(cardapios);
                } else {
                    res.status(404);
                    return gson.toJson(new RespostaErro("Nenhum cardápio encontrado para este campus na semana."));
                }
            } catch (NumberFormatException e) {
                res.status(400);
                return gson.toJson(new RespostaErro("ID de Campus inválido."));
            } catch (Exception e) {
                res.status(500);
                return gson.toJson(new RespostaErro("Erro ao buscar cardápio: " + e.getMessage()));
            }
        });

        get(PATH + "/periodo", (req, res) -> {
            try {
                String inicioStr = req.queryParams("inicio");
                String fimStr = req.queryParams("fim");
                String campusIdStr = req.queryParams("campusId");

                if (inicioStr == null || fimStr == null) {
                    res.status(400);
                    return gson.toJson(new RespostaErro("Parâmetros 'inicio' e 'fim' são obrigatórios."));
                }

                LocalDate inicio = LocalDate.parse(inicioStr);
                LocalDate fim = LocalDate.parse(fimStr);

                List<Cardapio> cardapios;

                if (campusIdStr != null) {
                    int campusId = Integer.parseInt(campusIdStr);
                    cardapios = cardapioDAO.buscarPorPeriodoECampus(inicio, fim, campusId);
                } else {
                    cardapios = cardapioDAO.buscarPorSemana(inicio, fim);
                }

                res.status(200);
                return gson.toJson(cardapios);
            } catch (Exception e) {
                res.status(500);
                return gson.toJson(new RespostaErro("Erro ao buscar cardápios: " + e.getMessage()));
            }
        });

        post(PATH, (req, res) -> {
            try {
                Cardapio novoCardapio = gson.fromJson(req.body(), Cardapio.class);

                if (novoCardapio.getData() == null) {
                    res.status(400);
                    return gson.toJson(new RespostaErro("Data é obrigatória."));
                }

                if (cardapioDAO.existeCardapioParaData(novoCardapio.getData())) {
                    res.status(409); // Conflict
                    return gson.toJson(new RespostaErro("Já existe cardápio para esta data."));
                }

                cardapioDAO.inserir(novoCardapio);
                res.status(201); // Created
                return gson.toJson(novoCardapio);
            } catch (Exception e) {
                res.status(400);
                return gson.toJson(new RespostaErro("Erro ao criar cardápio: " + e.getMessage()));
            }
        });

        put(PATH + "/:id", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params(":id"));
                Cardapio cardapioAtualizacao = gson.fromJson(req.body(), Cardapio.class);
                cardapioAtualizacao.setIdCardapio(id);

                if (cardapioDAO.buscarPorId(id) == null) {
                    res.status(404);
                    return gson.toJson(new RespostaErro("Cardápio não encontrado para atualização."));
                }

                cardapioDAO.atualizar(cardapioAtualizacao);
                res.status(200);
                return gson.toJson(cardapioAtualizacao);
            } catch (NumberFormatException e) {
                res.status(400);
                return gson.toJson(new RespostaErro("ID de Cardápio inválido."));
            } catch (Exception e) {
                res.status(500);
                return gson.toJson(new RespostaErro("Erro ao atualizar cardápio: " + e.getMessage()));
            }
        });

        delete(PATH + "/:id", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params(":id"));

                if (cardapioDAO.buscarPorId(id) == null) {
                    res.status(404);
                    return gson.toJson(new RespostaErro("Cardápio não encontrado."));
                }

                cardapioDAO.deletar(id);
                res.status(204); // No Content
                return "";
            } catch (NumberFormatException e) {
                res.status(400);
                return gson.toJson(new RespostaErro("ID de Cardápio inválido."));
            } catch (Exception e) {
                res.status(500);
                return gson.toJson(new RespostaErro("Erro ao deletar cardápio: " + e.getMessage()));
            }
        });
    }
}