package io.github.primepotato.jandas.io.sql.containers.fixed;

import io.github.primepotato.jandas.column.impl.ObjectColumn;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StringFixedResultSetContainer implements FixedResultSetContainer {

    String[] data;

    public StringFixedResultSetContainer(int size) {
        this.data = createContainer(size);
    }

    @Override
    public String[] createContainer(int size) {
        return new String[size];
    }

    @Override
    public void insert(ResultSet rs, int row, int col) throws SQLException {
        data[row] = rs.getString(col);
    }

    @Override
    public ObjectColumn<String> toColumn() {
        return new ObjectColumn<> ("", false,  data, String.class);
    }


}
