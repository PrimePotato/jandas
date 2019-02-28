package io.github.primepotato.jandas.io.sql.containers.dynamic;

import io.github.primepotato.jandas.io.sql.containers.ResultSetContainer;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DynamicResultSetContainer extends ResultSetContainer {

    <T> T createContainer();

    void insert(ResultSet rs, int col) throws SQLException;

}