package servicosTecnicos;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexao {

    private static final String URL =
            "jdbc:mysql://127.0.0.1:3306/RuZERO?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    private static final String USER = "root";
    private static final String PASSWORD = "marcela";

    public static Connection getConexao() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conectado ao banco de dados MySQL com sucesso!");
            return conn;

        } catch (Exception e) {
            System.out.println("ERRO AO CONECTAR NO BANCO!");
            System.out.println("Mensagem: " + e.getMessage());
            return null;
        }
    }
}
