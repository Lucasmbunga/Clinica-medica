package ao.clinica.medica.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    // Configuração da URL de conexão para o banco de dados criado (clinica_medica_db)
    private static final String URL = "jdbc:mysql://localhost:3306/clinica_medica?useTimezone=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        try {
            // Carrega explicitamente o Driver do MySQL Connector/J configurado no pom.xml
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("O Driver do MySQL (Connector/J) não foi encontrado no projeto. Verifica o teu pom.xml.", e);
        }
    }
}