package io.github.primepotato.jandas.io.csv.containers;

import io.github.primepotato.jandas.io.parsers.*;

public class FixedColumnDataContainer extends AbstractColumnDataContainer {

    public FixedColumnDataContainer(String name, Class cls) {
        this.name = name;
        parser = getParserGivenClass(cls);
        data = AbstractColumnDataContainer.createNewContainer(parser.elementClass());
    }

    private AbstractParser getParserGivenClass(Class cls){
        switch (cls.getSimpleName())
        {
            case "Double":
                return new DoubleParser();
            case "Integer": case "int":
                return new IntParser();
            case "LocalDate":
                return new DateParser();
            case "LocalTime":
                return new TimeParser();
            default:
                return new StringParser();
        }

    }

    @Override
    public void add(String value) {
        data.add(parser.parse(value));
    }

}
