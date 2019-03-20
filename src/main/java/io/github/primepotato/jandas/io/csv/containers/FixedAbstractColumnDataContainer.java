package io.github.primepotato.jandas.io.csv.containers;

import io.github.primepotato.jandas.io.parsers.*;

public class FixedAbstractColumnDataContainer<T> extends AbstractColumnDataContainer {

    public FixedAbstractColumnDataContainer(String name, AbstractParser<T> parser) {
        this.name = name;
        this.parser = parser;
        data = AbstractColumnDataContainer.createNewContainer(parser.elementClass());
    }

    @Override
    public void add(String value) {
        data.add(parser.parse(value));
    }

}
