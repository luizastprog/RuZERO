package dominio;

public class NotaAlunos {

    private Integer idNotaAlunos;
    private Double mediaNotas;
    private String totalAvaliacao;
    private Feedback feedback;
    private Campus campus;
    private Gestao gestao;

    public NotaAlunos() {
    }

    public NotaAlunos(Integer idNotaAlunos, Double mediaNotas,
                      String totalAvaliacao, Feedback feedback,
                      Campus campus, Gestao gestao) {
        this.idNotaAlunos = idNotaAlunos;
        this.mediaNotas = mediaNotas;
        this.totalAvaliacao = totalAvaliacao;
        this.feedback = feedback;
        this.campus = campus;
        this.gestao = gestao;
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

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }

    public Gestao getGestao() {
        return gestao;
    }

    public void setGestao(Gestao gestao) {
        this.gestao = gestao;
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