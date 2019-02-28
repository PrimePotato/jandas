package io.github.primepotato.jandas.containers.dynamic;

import io.github.primepotato.jandas.column.Column;
import io.github.primepotato.jandas.containers.ResultSetContainer;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DynamicResultSetContainer extends ResultSetContainer {

    <T> T createContainer();

    void insert(ResultSet rs, int col) throws SQLException;

}