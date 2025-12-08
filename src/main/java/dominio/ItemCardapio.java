package dominio;

public class ItemCardapio {

    private Integer idItemCardapio;

    public ItemCardapio() {
    }

    public ItemCardapio(Integer idItemCardapio) {
        this.idItemCardapio = idItemCardapio;
    }

    public Integer getIdItemCardapio() {
        return idItemCardapio;
    }

    public void setIdItemCardapio(Integer idItemCardapio) {
        this.idItemCardapio = idItemCardapio;
    }

    @Override
    public String toString() {
        return "ItemCardapio [id=" + idItemCardapio + "]";
    }
}