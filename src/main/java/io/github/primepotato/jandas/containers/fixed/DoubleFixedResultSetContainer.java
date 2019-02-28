package io.github.primepotato.jandas.containers.fixed;

import io.github.primepotato.jandas.column.DoubleColumn;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DoubleFixedResultSetContainer implements FixedResultSetContainer {

    double[] data;

    public DoubleFixedResultSetContainer(int size) {
        this.data = createContainer(size);
    }

    @Override
    public double[] createContainer(int size) {
        return new double[size];
    }

    @Override
    public void insert(ResultSet rs, int row, int col) throws SQLException {
        data[row] = rs.getDouble(col);
    }

    @Override
    public DoubleColumn toColumn() {
        return new DoubleColumn("", false, data);
    }
}
