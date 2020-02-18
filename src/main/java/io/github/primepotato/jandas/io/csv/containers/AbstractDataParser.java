package io.github.primepotato.jandas.io.csv.containers;

import io.github.primepotato.jandas.column.Column;
import io.github.primepotato.jandas.column.impl.DoubleColumn;
import io.github.primepotato.jandas.column.impl.IntegerColumn;
import io.github.primepotato.jandas.column.impl.ObjectColumn;
import io.github.primepotato.jandas.header.Heading;
import io.github.primepotato.jandas.io.parsers.AbstractParser;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.AbstractCollection;

public abstract class AbstractDataParser {

    public String name;
    public AbstractCollection data;
    public AbstractParser parser;

    public Class elementClass() {
        return parser.elementClass();
    }

    public Column toColumn(){
        if (elementClass().equals(Integer.class)){
            return new IntegerColumn(new Heading(name), false, (IntArrayList) data);
        } else if (elementClass().equals(Integer.class)){
            return new DoubleColumn(new Heading(name), false, (DoubleArrayList) data);
        } if (elementClass().equals(Integer.class)){
            return new ObjectColumn<>(new Heading(name), false, (ObjectArrayList) data, LocalTime.class);
        } if (elementClass().equals(Integer.class)){
            return new ObjectColumn<>(new Heading(name), false, (ObjectArrayList<LocalDate>) data, LocalDate.class);
        } if (elementClass().equals(Integer.class)){
            return new ObjectColumn<>(new Heading(name), false, (ObjectArrayList<String>) data, String.class);
        } else{
            return null;
        }
    }

    public abstract void add(String value);

    static <T extends AbstractCollection> T createNewContainer(Class cls) {

        if (cls.equals(Integer.class)) {
            return (T) new IntArrayList();
        } else if (cls.equals(Double.class)) {
            return (T) new DoubleArrayList();
        } else if (cls.equals(LocalDate.class)) {
            return (T) new ObjectArrayList<LocalDate>();
        } else if (cls.equals(LocalTime.class)) {
            return (T) new ObjectArrayList<LocalTime>();
        } else if (cls.equals(String.class)) {
            return (T) new ObjectArrayList<String>();
        }
        return null;
    }

}
