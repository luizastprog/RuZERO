package dominio;

import java.time.LocalDateTime;

public class Feedback {

    private Integer idFeedback;
    private String nota;
    private String comentario;
    private transient LocalDateTime dataHora;
    private Campus campus;
    private Integer alunoMatricula;

    public Feedback() {
        this.dataHora = LocalDateTime.now();
    }

    public Feedback(Integer idFeedback, String nota, String comentario,
                    Campus campus, Integer alunoMatricula) {
        this.idFeedback = idFeedback;
        this.nota = nota;
        this.comentario = comentario;
        this.campus = campus;
        this.alunoMatricula = alunoMatricula;
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

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }

    public Integer getAlunoMatricula() {
        return alunoMatricula;
    }

    public void setAlunoMatricula(Integer alunoMatricula) {
        this.alunoMatricula = alunoMatricula;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "idFeedback=" + idFeedback +
                ", nota='" + nota + '\'' +
                ", comentario='" + comentario + '\'' +
                ", dataHora=" + dataHora +
                ", alunoMatricula=" + alunoMatricula +
                '}';
    }
}
