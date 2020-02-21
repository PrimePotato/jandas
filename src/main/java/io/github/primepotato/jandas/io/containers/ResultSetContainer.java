package io.github.primepotato.jandas.io.containers;

import io.github.primepotato.jandas.column.Column;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetContainer {
    Column toColumn();

    void insert(ResultSet rs, int row, int col) throws SQLException;
}
