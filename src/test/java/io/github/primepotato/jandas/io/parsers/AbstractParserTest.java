package io.github.primepotato.jandas.io.parsers;


import lombok.val;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.junit.jupiter.api.Assertions;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

public class AbstractParserTest {
    List<? extends  AbstractParser> parsers;

    @Before
    public void setUp() throws Exception {

        val a = Class.forName("io.github.primepotato.jandas.io.parsers.AbstractParser");
        Reflections reflections = new Reflections(AbstractParser.class);
        val b = reflections.getSubTypesOf(AbstractParser.class);
        parsers = b.stream().map(x-> {
            try {
                return x.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
    }

    @Test
    public void remove() {
        parsers.forEach(x-> AbstractParser.remove("123141", '1'));
    }

    @Test
    public void isMissing() {

        parsers.forEach(x-> x.isMissing("123"));
    }

    @Test
    public void parseByte() {
        parsers.forEach(x->{
            if (x.elementClass().equals(Float.class))
                x.parseFloat("123.1");
            else
                Assertions.assertThrows(Exception.class, ()->x.parseByte("123.1"));
        });
    }

    @Test
    public void parseInt() {
        parsers.forEach(x->{
            if (x.elementClass().equals(Float.class))
                x.parseFloat("123.1");
            else
                Assertions.assertThrows(Exception.class, ()->x.parseInt("123.1"));
        });
    }

    @Test
    public void parseShort() {
        parsers.forEach(x->{
            if (x.elementClass().equals(Float.class))
                x.parseFloat("123.1");
            else
                Assertions.assertThrows(Exception.class, ()->x.parseShort("123.1"));
        });
    }

    @Test
    public void parseLong() {
        parsers.forEach(x->{
            if (x.elementClass().equals(Float.class))
                x.parseFloat("123.1");
            else
                Assertions.assertThrows(Exception.class, ()->x.parseLong("123.1"));
        });
    }

    @Test
    public void parseDouble() {
        parsers.forEach(x->{
            if (x.elementClass().equals(Float.class))
                x.parseFloat("123.1");
            else
                Assertions.assertThrows(Exception.class, ()->x.parseDouble("123.1"));
        });
    }

    @Test
    public void parseFloat() {
        parsers.forEach(x->{
                    if (x.elementClass().equals(Float.class))
                        x.parseFloat("123.1");
                    else
                        Assertions.assertThrows(Exception.class, ()->x.parseFloat("123.1"));
                });
    }
}