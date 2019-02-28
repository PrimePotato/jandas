package io.github.primepotato.jandas.containers.fixed;

import io.github.primepotato.jandas.column.Column;
import io.github.primepotato.jandas.containers.ResultSetContainer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

public interface FixedResultSetContainer extends ResultSetContainer {

    <T> T createContainer(int size);


}