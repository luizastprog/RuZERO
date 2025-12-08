package dominio;

public class Campus {

    private Integer idCampus;
    private String nome;
    private Endereco endereco;
    public Campus() {
    }

    public Campus(Integer idCampus, String nome, Endereco endereco) {
        this.idCampus = idCampus;
        this.nome = nome;
        this.endereco = endereco;
    }

    public Integer getIdCampus() {
        return idCampus;
    }

    public void setIdCampus(Integer idCampus) {
        this.idCampus = idCampus;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Endereco getEndereco() {this.endereco = endereco;
        return null;
    }
    public void setEndereco(Endereco endereco) {this.endereco = endereco; }

    @Override
    public String toString() {
        return "Campus{" +
                "idCampus=" + idCampus +
                ", nome='" + nome +
                ", endereco=" + endereco +
                '}';
    }
}