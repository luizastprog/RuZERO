// classe de domínio que representa uma avaliação (feedback)
// armazena nota, comentário, data/hora, aluno e campus associados

package dominio;

import java.time.LocalDateTime;

public class Feedback {

    private Integer idFeedback;
    private String nota;
    private String comentario;
    private LocalDateTime dataHora;
    private Integer idCampus;
    private Integer idAluno;

    public Feedback() {
        this.dataHora = LocalDateTime.now();
    }

    public Integer getIdFeedback() {
        return idFeedback;
    }

    public void setIdFeedback(Integer idFeedback) {
        this.idFeedback = idFeedback;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Integer getIdCampus() {
        return idCampus;
    }

    public void setIdCampus(Integer idCampus) {
        this.idCampus = idCampus;
    }

    public Integer getIdAluno() {
        return idAluno;
    }

    public void setIdAluno(Integer idAluno) {
        this.idAluno = idAluno;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "idFeedback=" + idFeedback +
                ", nota='" + nota + '\'' +
                ", comentario='" + comentario + '\'' +
                ", dataHora=" + dataHora +
                ", idCampus=" + idCampus +
                ", idAluno=" + idAluno +
                '}';
    }
}
