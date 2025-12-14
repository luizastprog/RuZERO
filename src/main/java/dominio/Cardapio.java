package dominio;

import java.time.LocalDate;

public class Cardapio {

    private Integer idCardapio;
    private LocalDate data;
    private Integer idGestao;

    public Cardapio() {
    }

    public Cardapio(Integer idCardapio, LocalDate data, Integer idGestao) {
        this.idCardapio = idCardapio;
        this.data = data;
        this.idGestao = idGestao;
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

    public Integer getIdGestao() {
        return idGestao;
    }

    public void setIdGestao(Integer idGestao) {
        this.idGestao = idGestao;
    }

    @Override
    public String toString() {
        return "Cardapio{" +
                "idCardapio=" + idCardapio +
                ", data=" + data +
                ", idGestao=" + idGestao +
                '}';
    }
}
