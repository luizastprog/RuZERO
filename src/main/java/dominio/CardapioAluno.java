package dominio;

public class CardapioAluno {

    private Integer idCardapioAluno;

    public CardapioAluno() {}

    public CardapioAluno(Integer idCardapioAluno) {
        this.idCardapioAluno = idCardapioAluno;
    }

    public Integer getIdCardapioAluno() {
        return idCardapioAluno;
    }

    public void setIdCardapioAluno(Integer idCardapioAluno) {
        this.idCardapioAluno = idCardapioAluno;
    }

    @Override
    public String toString() {
        return "CardapioAluno [id=" + idCardapioAluno + "]";
    }
}