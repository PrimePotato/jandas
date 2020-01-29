package io.github.primepotato.jandas.io.sql.connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteJDBCDriverConnection {

    public static Connection connect(String url) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");
            return conn;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
}
