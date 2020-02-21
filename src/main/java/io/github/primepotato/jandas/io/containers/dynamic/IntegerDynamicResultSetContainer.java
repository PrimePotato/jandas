package io.github.primepotato.jandas.io.containers.dynamic;

import io.github.primepotato.jandas.column.impl.IntegerColumn;
import io.github.primepotato.jandas.header.Heading;
import it.unimi.dsi.fastutil.ints.IntArrayList;


import java.sql.ResultSet;
import java.sql.SQLException;

public class IntegerDynamicResultSetContainer implements DynamicResultSetContainer {

    IntArrayList data;

    public IntegerDynamicResultSetContainer() {
        this.data = createContainer();
    }

    @Override
    public IntArrayList createContainer() {
        return new IntArrayList();
    }

    @Override
    public void insert(ResultSet rs, int col) throws SQLException {
        data.add(rs.getInt(col));

    }

    @Override
    public IntegerColumn toColumn() {
        return new IntegerColumn(new Heading(""), false, data);
    }

    @Override
    public void insert(ResultSet rs, int row, int col) throws SQLException {
        this.insert(rs, col);
    }
}
