package io.github.primepotato.jandas.io.csv.containers;

import io.github.primepotato.jandas.io.parsers.*;
import io.github.primepotato.jandas.io.parsers.impl.*;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class DynamicDataParser extends AbstractDataParser {

    private List<AbstractParser> parserCascade;
    private int parserPos;

    public DynamicDataParser(String name) {

        this.name = name;
        defaultParsers();
        setParser();
    }

    @Override
    public Class elementClass() {
        return parser.elementClass();
    }

    private boolean setParser() {

        try {
            parser = parserCascade.get(parserPos);
            if (data == null) {
                data = AbstractDataParser.createNewContainer(parser.elementClass());
            } else if (data.size() == 0) {
                data = AbstractDataParser.createNewContainer(parser.elementClass());
            } else {
                Class cls = parser.elementClass();
                AbstractCollection newData = AbstractDataParser.createNewContainer(parser.elementClass());
                switch (cls.getSimpleName()) {
                    case "Double":
                        for (Object d : data) {
                            newData.add(((Integer) d).doubleValue());
                        }
                        break;
                    case "LocalDate":
                        return false;
                    case "LocalTime":
                        return false;
                    default:
                        for (Object d : data) {
                            newData.add(d.toString());
                        }
                }
                data = newData;
            }
            return true;
        } catch (Exception ignored) {
            return false;
        }

    }

    @Override
    public void add(String value) {

        try {
            data.add(parser.parse(value));
        } catch (Exception e) {
            if (++parserPos < parserCascade.size()) {
                if (setParser()) {
                    add(value);
                } else {
                    setParser();
                    add(value);
                }
            } else {
                throw new RuntimeException("BOBLBISDFVNDAL");
            }
        }
    }

    private void defaultParsers() {

        parserCascade = new ArrayList<AbstractParser>() {{
            add(new IntParser());
            add(new DoubleParser());
            add(new DateParser());
            add(new TimeParser());
            add(new StringParser());
        }};
    }


}
