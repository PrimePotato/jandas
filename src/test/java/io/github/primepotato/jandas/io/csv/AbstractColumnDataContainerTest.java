package io.github.primepotato.jandas.io.csv;

import io.github.primepotato.jandas.io.csv.containers.FixedAbstractColumnDataContainer;
import io.github.primepotato.jandas.io.parsers.DoubleParser;
import org.junit.Test;

public class AbstractColumnDataContainerTest {

    @Test
    public void fixedColumnDataContainer(){
        FixedAbstractColumnDataContainer<Double> fix = new FixedAbstractColumnDataContainer<>("Colin", new DoubleParser());
        fix.add("0.1123");
        fix.add("0.1223");
        fix.add("0.1423");
        fix.add("0.16523");
    }


}