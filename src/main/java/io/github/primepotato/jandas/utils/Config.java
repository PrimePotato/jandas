package io.github.primepotato.jandas.utils;

public interface Config {
    boolean has(String... keys);

    <T> T get(String... keys);
}
