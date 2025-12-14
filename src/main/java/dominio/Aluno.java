package dominio;

public class Aluno {

    private Integer idAluno;
    private String nome;
    private String email;
    private Integer idCampus;

    public Aluno() {}

    public Aluno(Integer idAluno, String nome, String email, Integer idCampus) {
        this.idAluno = idAluno;
        this.nome = nome;
        this.email = email;
        this.idCampus = idCampus;
    }

    public Integer getIdAluno() {
        return idAluno;
    }

    public void setIdAluno(Integer idAluno) {
        this.idAluno = idAluno;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getIdCampus() {
        return idCampus;
    }

    public void setIdCampus(Integer idCampus) {
        this.idCampus = idCampus;
    }

    @Override
    public String toString() {
        return "Aluno{" +
                "idAluno=" + idAluno +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", idCampus=" + idCampus +
                '}';
    }
}
