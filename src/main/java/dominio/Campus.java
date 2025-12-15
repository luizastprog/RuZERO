// classe de domínio que representa um campus
// utilizada para identificar a qual campus alunos e feedbacks pertencem

package dominio;

public class Campus {

    private Integer idCampus;
    private String nome;

    public Campus() {
    }

    public Campus(Integer idCampus, String nome) {
        this.idCampus = idCampus;
        this.nome = nome;
    }

    public Campus(Integer idCampus) {
        this.idCampus = idCampus;
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

    @Override
    public String toString() {
        return "Campus{" +
                "idCampus=" + idCampus +
                ", nome='" + nome + '\'' +
                '}';
    }
}
