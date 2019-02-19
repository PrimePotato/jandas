package io.github.primepotato.jandas.index.generation;

import io.github.primepotato.jandas.index.DoubleIndex;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class IndexGeneratorTest {

    @Before
    public void setUp() throws Exception {
        double[] data = {.1,.2,.4,.6,.5,.4,.3};
        DoubleIndex di = new DoubleIndex(data);
    }

    @Test
    public void nextIndex() {
    }


    @Test
    public void intIndexValue() {
    }

    @Test
    public void doubleIndexValue() {
        System.out.println(IndexGenerator.doubleIndexValue(100));
    }

    @Test
    public void indexValue() {
    }
}