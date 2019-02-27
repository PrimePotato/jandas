package io.github.primepotato.jandas.utils;


import java.sql.SQLException;

@FunctionalInterface
public interface QuadFunction<T, U, S, V, R> {
    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argument
     * @param s the third function argument
     * @param v the fourth function argument
     * @return the function result
     */
    R apply(T t, U u, S s, V v) throws SQLException;

}


