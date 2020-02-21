package io.github.primepotato.jandas.io.parsers;

import io.github.primepotato.jandas.io.parsers.impl.*;
import org.junit.Before;
import org.junit.Test;

public class AbstractParserTest {
    DateParser dp = new DateParser();
    DateTimeParser dtp = new DateTimeParser();
    DoubleParser dblp = new DoubleParser();
    EnumParser ep = new EnumParser();
    IntParser ip = new IntParser();
    LongParser lp = new LongParser();
    StringParser sp = new StringParser();
    TimeParser tp = new TimeParser();

    @Before
    public void setUp() throws Exception {
        dp = new DateParser();
        dtp = new DateTimeParser();
        dblp = new DoubleParser();
        ep = new EnumParser<>();
        ip = new IntParser();
        lp = new LongParser();
        sp = new StringParser();
        tp = new TimeParser();
    }

    @Test
    public void remove() {
        AbstractParser.remove("", '.');
    }

    @Test
    public void isMissing() {
        dp.isMissing("");
    }

    @Test
    public void parseByte() {
    }

    @Test
    public void parseInt() {
    }

    @Test
    public void parseShort() {
    }

    @Test
    public void parseLong() {
    }

    @Test
    public void parseDouble() {
    }

    @Test
    public void parseFloat() {
    }
}