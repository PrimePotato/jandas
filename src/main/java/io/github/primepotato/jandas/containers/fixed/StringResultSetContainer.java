package io.github.primepotato.jandas.containers.fixed;

import io.github.primepotato.jandas.column.StringColumn;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StringResultSetContainer implements ResultSetContainer {

    String[] data;

    public StringResultSetContainer(int size) {
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
    public StringColumn toColumn() {
        return new StringColumn("", false,  data);
    }


}
