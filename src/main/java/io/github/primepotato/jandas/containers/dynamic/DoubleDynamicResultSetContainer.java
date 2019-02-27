package io.github.primepotato.jandas.containers.dynamic;

import io.github.primepotato.jandas.column.DoubleColumn;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DoubleDynamicResultSetContainer implements DynamicResultSetContainer {

    DoubleArrayList data;

    public DoubleDynamicResultSetContainer() {
        this.data = createContainer();
    }


    @Override
    public DoubleArrayList createContainer() {
        return new DoubleArrayList();
    }

    @Override
    public void insert(ResultSet rs, int col) throws SQLException {
        data.add(rs.getDouble(col));

    }

    @Override
    public DoubleColumn toColumn() {
        return new DoubleColumn("", false, data);
    }
}
