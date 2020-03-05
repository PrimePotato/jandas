package io.github.primepotato.jandas.io.containers.dynamic;

import io.github.primepotato.jandas.io.containers.ResultSetContainer;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DynamicResultSetContainer<T> extends ResultSetContainer {

    T createContainer();

    void insert(ResultSet rs, int col) throws SQLException;

}