package dominio;

import java.time.LocalDate;

public class Cardapio {

    private Integer idCardapio;
    private LocalDate data;
    private CardapioAluno cardapioAluno;
    private Gestao gestao;

    public Cardapio() {
    }

    public Cardapio(Integer idCardapio, LocalDate data,
                    CardapioAluno cardapioAluno, Gestao gestao) {
        this.idCardapio = idCardapio;
        this.data = data;
        this.cardapioAluno = cardapioAluno;
        this.gestao = gestao;
    }

    public Integer getIdCardapio() {
        return idCardapio;
    }

    public void setIdCardapio(Integer idCardapio) {
        this.idCardapio = idCardapio;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public CardapioAluno getCardapioAluno() {
        return cardapioAluno;
    }

    public void setCardapioAluno(CardapioAluno cardapioAluno) {
        this.cardapioAluno = cardapioAluno;
    }

    public Gestao getGestao() {
        return gestao;
    }

    public void setGestao(Gestao gestao) {
        this.gestao = gestao;
    }

    @Override
    public String toString() {
        return "Cardapio{" +
                "idCardapio=" + idCardapio +
                ", data=" + data +
                '}';
    }
}