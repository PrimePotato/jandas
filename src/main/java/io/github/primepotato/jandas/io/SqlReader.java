package io.github.primepotato.jandas.io;
//package net.sqlitetutorial;

import io.github.primepotato.jandas.containers.dynamic.DoubleDynamicResultSetContainer;
import io.github.primepotato.jandas.containers.dynamic.DynamicResultSetContainer;
import io.github.primepotato.jandas.containers.dynamic.IntegerDynamicResultSetContainer;
import io.github.primepotato.jandas.containers.dynamic.StringDynamicResultSetContainer;
import io.github.primepotato.jandas.containers.fixed.DoubleResultSetContainer;
import io.github.primepotato.jandas.containers.fixed.IntResultSetContainer;
import io.github.primepotato.jandas.containers.fixed.ResultSetContainer;
import io.github.primepotato.jandas.containers.fixed.StringResultSetContainer;
import io.github.primepotato.jandas.dataframe.DataFrame;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


public class SqlReader {

    private Connection connection;

    public SqlReader() {
        this.connection = SQLiteJDBCDriverConnection.connect();

    }

    public static void main(String[] args) throws SQLException {
        SqlReader sqr = new SqlReader();
        Statement statement = sqr.connection.createStatement();

        ResultSet resultSet = statement.executeQuery("SELECT * FROM customers");
        DataFrame df = new DataFrame(resultSet);

    }


    private static Map<Integer, Function<Integer, ResultSetContainer>> fixedContainers = new HashMap<>();

    private static Map<Integer, Function<Integer, DynamicResultSetContainer>> dynamicContainers = new HashMap<>();

    static {

        fixedContainers.put(Types.VARCHAR, StringResultSetContainer::new);
        fixedContainers.put(Types.INTEGER, IntResultSetContainer::new);
        fixedContainers.put(Types.NUMERIC, DoubleResultSetContainer::new);


        dynamicContainers.put(Types.VARCHAR, x -> new StringDynamicResultSetContainer());
        dynamicContainers.put(Types.INTEGER, x -> new IntegerDynamicResultSetContainer());
        dynamicContainers.put(Types.NUMERIC, x -> new DoubleDynamicResultSetContainer());

    }

    public static List<DynamicResultSetContainer> resultSetToContainers(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        List<DynamicResultSetContainer> resultSetContainers = new ArrayList<>();

        int row = 0;
        while (resultSet.next()) {

            for (int col = 1; col <= metaData.getColumnCount(); ++col) {
                if (row == 0) {
                    resultSetContainers.add(dynamicContainers.get(metaData.getColumnType(col)).apply(0));
                }
                DynamicResultSetContainer dc = resultSetContainers.get(col - 1);
                dc.insert(resultSet, col);
            }

            ++row;
        }

        return resultSetContainers;
    }


    public static DataFrame resultSetToDataFrame(ResultSet resultSet) throws SQLException {
        List<DynamicResultSetContainer> rsc = resultSetToContainers(resultSet);
        return new DataFrame("", rsc.stream().map(DynamicResultSetContainer::toColumn).collect(Collectors.toList()));
    }


    public static List<ResultSetContainer> resultSetToContainers(ResultSet resultSet, int rowCount) throws SQLException {

        ResultSetMetaData metaData = resultSet.getMetaData();
        List<ResultSetContainer> resultSetContainers = new ArrayList<>();

        int row = 0;
        while (resultSet.next()) {
            for (int col = 1; col <= metaData.getColumnCount(); ++col) {
                if (row == 0) {
                    resultSetContainers.add(fixedContainers.get(metaData.getColumnType(col)).apply(rowCount));
                }
                ResultSetContainer dc = resultSetContainers.get(col - 1);
                dc.insert(resultSet, row, col);
            }
            ++row;
        }

        return resultSetContainers;

    }

    public static DataFrame resultSetToDataFrame(ResultSet resultSet, int rowCount) throws SQLException {
        List<ResultSetContainer> rsc = resultSetToContainers(resultSet, rowCount);
        return new DataFrame("", rsc.stream().map(ResultSetContainer::toColumn).collect(Collectors.toList()));
    }

}


class SQLiteJDBCDriverConnection {

    public static Connection connect() {
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:src/test/resources/sqlite/chinook.db";
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");
            return conn;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
}