package io.github.primepotato.jandas.containers.dynamic;

import io.github.primepotato.jandas.column.StringColumn;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StringDynamicResultSetContainer implements DynamicResultSetContainer {

    ObjectArrayList<String> data;

    public StringDynamicResultSetContainer() {
        this.data = createContainer();
    }

    @Override
    public ObjectArrayList<String> createContainer() {
        return new ObjectArrayList();
    }

    @Override
    public void insert(ResultSet rs, int col) throws SQLException {
        data.add(rs.getString(col));

    }

    @Override
    public StringColumn toColumn() {
        return new StringColumn("", false, data);
    }

    @Override
    public void insert(ResultSet rs, int row, int col) throws SQLException {
        this.insert(rs, col);
    }
}
