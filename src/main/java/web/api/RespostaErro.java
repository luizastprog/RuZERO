// classe utilitária usada para padronizar respostas de erro da API
// Retorna mensagens de erro em formato JSON com status e descrição.


package web.api;

public class RespostaErro {
    private final String status = "error";
    private final String message;

    public RespostaErro(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}