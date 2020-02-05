package io.github.primepotato.jandas.io.csv;

import io.github.primepotato.jandas.io.csv.containers.FixedDataParser;
import org.junit.Test;

public class AbstractColumnDataContainerTest {

    @Test
    public void fixedColumnDataContainer(){
        FixedDataParser fix = new FixedDataParser("Colin", Double.class);
        fix.add("0.1123");
        fix.add("0.1223");
        fix.add("0.1423");
        fix.add("0.16523");
    }


}