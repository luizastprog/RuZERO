package app;

import web.api.SparkInit;
import java.sql.Connection;
import servicosTecnicos.Conexao;


public class Main {
    public static void main(String[] args) {
        Connection c = Conexao.getConexao();
        System.out.println(c);

        SparkInit.iniciar();
    }
}