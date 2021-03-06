package io.github.primepotato.jandas.io.csv.containers;

import io.github.primepotato.jandas.io.parsers.*;
import io.github.primepotato.jandas.io.parsers.impl.*;

@SuppressWarnings("unchecked")
public class FixedDataParser extends AbstractDataParser {

    public FixedDataParser(String name, Class cls) {
        this.name = name;
        parser = getParserGivenClass(cls);
        data = AbstractDataParser.createNewContainer(parser.elementClass());
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
