package io.github.primepotato.jandas.containers.fixed;

import io.github.primepotato.jandas.column.Column;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

public interface ResultSetContainer {

    <T> T createContainer(int size);

    void insert(ResultSet rs, int row, int col) throws SQLException;

    Column toColumn();

}