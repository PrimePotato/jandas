package io.github.primepotato.jandas.io.csv;

import io.github.primepotato.jandas.column.Column;
import io.github.primepotato.jandas.io.parsers.*;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.List;

public class FixedColumnDataContainer<T> extends ColumnDataContainer {

    public FixedColumnDataContainer(String name, AbstractParser<T> parser) {
        this.name = name;
        this.parser = parser;
        data = ColumnDataContainer.createNewContainer(parser.elementClass());
    }

    @Override
    public void add(String value) {
        data.add(parser.parse(value));
    }

}
