package dominio;

public class NotaAlunos {

    private Integer idNotaAlunos;
    private Double mediaNotas;
    private String totalAvaliacao;
    private Integer idFeedback;
    private Integer idCampus;
    private Integer idGestao;

    public NotaAlunos() {
    }

    public NotaAlunos(Integer idNotaAlunos, Double mediaNotas,
                      String totalAvaliacao, Integer idFeedback,
                      Integer idCampus, Integer idGestao) {
        this.idNotaAlunos = idNotaAlunos;
        this.mediaNotas = mediaNotas;
        this.totalAvaliacao = totalAvaliacao;
        this.idFeedback = idFeedback;
        this.idCampus = idCampus;
        this.idGestao = idGestao;
    }

    public Integer getIdNotaAlunos() {
        return idNotaAlunos;
    }

    public void setIdNotaAlunos(Integer idNotaAlunos) {
        this.idNotaAlunos = idNotaAlunos;
    }

    public Double getMediaNotas() {
        return mediaNotas;
    }

    public void setMediaNotas(Double mediaNotas) {
        this.mediaNotas = mediaNotas;
    }

    public String getTotalAvaliacao() {
        return totalAvaliacao;
    }

    public void setTotalAvaliacao(String totalAvaliacao) {
        this.totalAvaliacao = totalAvaliacao;
    }

    public Integer getIdFeedback() { return idFeedback; }

    public void setIdFeedback(Integer idFeedback) {
        this.idFeedback = idFeedback;
    }

    public Integer getIdCampus() { return idCampus; }

    public void setIdCampus(Integer idCampus) {
        this.idCampus = idCampus;
    }

    public Integer getIdGestao() {
        return idGestao;
    }

    public void setIdGestao(Integer idGestao) {
        this.idGestao = idGestao;
    }

    @Override
    public String toString() {
        return "NotaAlunos{" +
                "idNotaAlunos=" + idNotaAlunos +
                ", mediaNotas=" + mediaNotas +
                ", totalAvaliacao='" + totalAvaliacao + '\'' +
                '}';
    }
}