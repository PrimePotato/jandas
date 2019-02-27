package io.github.primepotato.jandas.containers.dynamic;

import io.github.primepotato.jandas.column.Column;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DynamicResultSetContainer {

    <T> T createContainer();

    void insert(ResultSet rs, int col) throws SQLException;

    Column toColumn();

}