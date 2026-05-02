package project.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConfig {

    private static final String URL = "jdbc:mysql://localhost:3306/mydb";
    private static final String USER = "root";
    private static final String PASS = "1234";

    public static Connection getConnection() throws SQLException {

        Connection conn = DriverManager.getConnection(URL, USER, PASS);

        if (conn == null) {
            throw new SQLException("Konekcija sa bazom nije uspostavljena!");
        }

        return conn;
    }

}