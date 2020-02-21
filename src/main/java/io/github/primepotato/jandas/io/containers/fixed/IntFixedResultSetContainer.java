package io.github.primepotato.jandas.io.containers.fixed;

import io.github.primepotato.jandas.column.impl.IntegerColumn;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IntFixedResultSetContainer implements FixedResultSetContainer {

    int[] data;

    public IntFixedResultSetContainer(int size) {
        this.data = createContainer(size);
    }

    @Override
    public int[] createContainer(int size) {
        return new int[size];
    }

    @Override
    public void insert(ResultSet rs, int row, int col) throws SQLException {
        data[row] = rs.getInt(col);
    }

    @Override
    public IntegerColumn toColumn() {
        return new IntegerColumn("", false, data);
    }
}


