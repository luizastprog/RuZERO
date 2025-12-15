// classe de domínio que representa um item do cardápio
// armazena o nome do item, tipo e o cardápio ao qual pertence


package dominio;

public class ItemCardapio {

    private Integer idItemCardapio;
    private Integer idCardapio;
    private String tipoItem;
    private String nome;

    public ItemCardapio() {}

    public Integer getIdItemCardapio() {
        return idItemCardapio;
    }

    public void setIdItemCardapio(Integer idItemCardapio) {
        this.idItemCardapio = idItemCardapio;
    }

    public Integer getIdCardapio() {
        return idCardapio;
    }

    public void setIdCardapio(Integer idCardapio) {
        this.idCardapio = idCardapio;
    }

    public String getTipoItem() {
        return tipoItem;
    }

    public void setTipoItem(String tipoItem) {
        this.tipoItem = tipoItem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
