package dominio;

public class Aluno {

    private Integer idAluno;
    private String nome;
    private String email;
    private Campus campus;

    public Aluno() {}

    public Aluno(Integer idAluno, String nome, String email, Campus campus) {
        this.idAluno = idAluno;
        this.nome = nome;
        this.email = email;
        this.campus = campus;
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

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }

    @Override
    public String toString() {
        return "Aluno{" +
                "idAluno=" + idAluno +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", campus=" + (campus != null ? campus.getNome() : "null") +
                '}';
    }
}
