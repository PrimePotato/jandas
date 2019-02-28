package io.github.primepotato.jandas.io.sql.containers.fixed;

import io.github.primepotato.jandas.io.sql.containers.ResultSetContainer;

public interface FixedResultSetContainer extends ResultSetContainer {

    <T> T createContainer(int size);


}