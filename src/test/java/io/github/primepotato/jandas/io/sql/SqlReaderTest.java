package io.github.primepotato.jandas.io.sql;

import io.github.primepotato.jandas.containers.ResultSetContainer;
import io.github.primepotato.jandas.io.sql.connections.SQLiteJDBCDriverConnection;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.Assert.*;

public class SqlReaderTest {

    SqlReader sqr;
    ResultSet resultSet;
    Statement statement;
    String sql;

    @Before
    public void setUp() throws Exception {
        String url = "jdbc:sqlite:src/test/resources/sqlite/chinook.db";
        Connection con = SQLiteJDBCDriverConnection.connect(url);
        sqr = new SqlReader(con);
        sql = "SELECT * FROM customers";
        statement = sqr.getConnection().createStatement();
        resultSet = statement.executeQuery(sql);
    }

    @Test
    public void resultSetToContainers() throws Exception {
        List<ResultSetContainer> dynamic  = SqlReader.resultSetToContainers(resultSet, 0);

        this.setUp();
        List<ResultSetContainer> fixed  = SqlReader.resultSetToContainers(resultSet, 59);
    }

    @Test
    public void resultSetToDataFrame() {
    }
}