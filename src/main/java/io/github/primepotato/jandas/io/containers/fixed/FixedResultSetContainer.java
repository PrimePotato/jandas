package io.github.primepotato.jandas.io.containers.fixed;

import io.github.primepotato.jandas.io.containers.ResultSetContainer;

public interface FixedResultSetContainer extends ResultSetContainer {

    <T> T createContainer(int size);


}