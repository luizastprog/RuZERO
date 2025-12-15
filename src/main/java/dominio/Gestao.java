// classe de domínio que representa um usuário da gestão
// contém dados de login utilizados para acesso administrativo ao sistema

package dominio;
public class Gestao {

    private Integer idGestao;
    private String login;
    private String senha;

    public Gestao() {
    }

    public Gestao(Integer idGestao, String login, String senha) {
        this.idGestao = idGestao;
        this.login = login;
        this.senha = senha;
    }

    public Integer getIdGestao() {
        return idGestao;
    }

    public void setIdGestao(Integer idGestao) {
        this.idGestao = idGestao;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String toString() {
        return "Gestao{" +
                "idGestao=" + idGestao +
                ", login='" + login + '\'' +
                '}';
    }
}