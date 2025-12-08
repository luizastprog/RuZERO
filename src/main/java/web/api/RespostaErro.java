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