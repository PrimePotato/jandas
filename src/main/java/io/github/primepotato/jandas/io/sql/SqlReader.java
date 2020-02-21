package io.github.primepotato.jandas.io.sql;

import io.github.primepotato.jandas.io.containers.ResultSetContainer;
import io.github.primepotato.jandas.io.containers.dynamic.DoubleDynamicResultSetContainer;
import io.github.primepotato.jandas.io.containers.dynamic.DynamicResultSetContainer;
import io.github.primepotato.jandas.io.containers.dynamic.IntegerDynamicResultSetContainer;
import io.github.primepotato.jandas.io.containers.dynamic.StringDynamicResultSetContainer;
import io.github.primepotato.jandas.io.containers.fixed.DoubleFixedResultSetContainer;
import io.github.primepotato.jandas.io.containers.fixed.FixedResultSetContainer;
import io.github.primepotato.jandas.io.containers.fixed.IntFixedResultSetContainer;
import io.github.primepotato.jandas.io.containers.fixed.StringFixedResultSetContainer;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public class SqlReader {

    private static Map<Integer, Function<Integer, FixedResultSetContainer>> fixedContainers = new HashMap<>();
    private static Map<Integer, Function<Integer, DynamicResultSetContainer>> dynamicContainers = new HashMap<>();

    static {

        fixedContainers.put(Types.VARCHAR, StringFixedResultSetContainer::new);
        fixedContainers.put(Types.INTEGER, IntFixedResultSetContainer::new);
        fixedContainers.put(Types.NUMERIC, DoubleFixedResultSetContainer::new);

        dynamicContainers.put(Types.VARCHAR, x -> new StringDynamicResultSetContainer());
        dynamicContainers.put(Types.INTEGER, x -> new IntegerDynamicResultSetContainer());
        dynamicContainers.put(Types.NUMERIC, x -> new DoubleDynamicResultSetContainer());

    }

    private Connection connection;

    public SqlReader(Connection connection) {
        this.connection = connection;
    }

    public static List<ResultSetContainer> resultSetToContainers(ResultSet resultSet) throws SQLException {
        return SqlReader.resultSetToContainers(resultSet, 0);
    }

    public static List<ResultSetContainer> resultSetToContainers(ResultSet resultSet, int rowCount) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        Map<Integer, ?> containerMap;
        List<ResultSetContainer> resultSetContainers = new ArrayList<>();

        if (rowCount == 0) {
            containerMap = dynamicContainers;
        } else {
            containerMap = fixedContainers;
        }

        int row = 0;
        while (resultSet.next()) {

            for (int col = 1; col <= metaData.getColumnCount(); ++col) {
                if (row == 0) {
                    Function<Integer, ResultSetContainer> f = (Function<Integer, ResultSetContainer>) (containerMap.get(metaData.getColumnType(col)));
                    resultSetContainers.add(f.apply(rowCount));
                }
                if (rowCount == 0) {
                    DynamicResultSetContainer dc = (DynamicResultSetContainer) resultSetContainers.get(col - 1);
                    dc.insert(resultSet, col);
                } else {
                    FixedResultSetContainer dc = (FixedResultSetContainer) resultSetContainers.get(col - 1);
                    dc.insert(resultSet, row, col);
                }
            }

            ++row;
        }

        return resultSetContainers;
    }

    public Connection getConnection() {
        return connection;
    }


}


