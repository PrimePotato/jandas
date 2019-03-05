package io.github.primepotato.jandas.io.csv;

import io.github.primepotato.jandas.column.*;
import io.github.primepotato.jandas.io.parsers.*;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.List;

public class ParserColumnData {

    String name;
    List<AbstractParser> parserCascade;
    public AbstractParser parser;
    int parserPos;
    public AbstractCollection data;

    public ParserColumnData(String name) {

        this.name = name;
        defaultParsers();
        setParser(0);
    }

    public Class elementClass() {

        return parser.elementClass();
    }

    public Column toColumn() {

        switch (elementClass().getSimpleName()) {
            case "Integer":
                return new IntegerColumn(name, false, (IntArrayList) data);
            case "Double":
                return new DoubleColumn(name, false, (DoubleArrayList) data);
            case "LocalTime":
                return new TimeColumn(name, false, (ObjectArrayList<LocalTime>) data);
            case "LocalDate":
                return new DateColumn(name, false, (ObjectArrayList<LocalDate>) data);
            default:
                return new StringColumn(name, false, (ObjectArrayList<String>) data);
        }
    }

    private boolean setParser(int pos) {

        try {
            parser = parserCascade.get(parserPos);
            if (data == null) {
                data = createNewContainer(parser.elementClass());
            } else if (data.size() == 0) {
                data = createNewContainer(parser.elementClass());
            } else {
                Class cls = parser.elementClass();
                AbstractCollection newData = createNewContainer(parser.elementClass());
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

    public void add(String value) {

        try {
            data.add(parser.parse(value));
        } catch (Exception e) {
            if (++parserPos < parserCascade.size()) {
                if (setParser(parserPos)) {
                    add(value);
                } else {
                    setParser(++parserPos);
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

    public static <T extends AbstractCollection> T createNewContainer(Class cls) {

        if (cls.getSimpleName().equals("Integer")) {
            return (T) new IntArrayList();
        } else if (cls.getSimpleName().equals("Double")) {
            return (T) new DoubleArrayList();
        } else if (cls.getSimpleName().equals("LocalDate")) {
            return (T) new ObjectArrayList<LocalDate>();
        } else if (cls.getSimpleName().equals("LocalTime")) {
            return (T) new ObjectArrayList<LocalTime>();
        } else if (cls.getSimpleName().equals("String")) {
            return (T) new ObjectArrayList<String>();
        }
        return null;
    }

}
