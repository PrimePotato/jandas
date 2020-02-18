package io.github.primepotato.jandas.grouping;

import io.github.primepotato.jandas.Jandas;
import io.github.primepotato.jandas.dataframe.DataFrame;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DataFrameGroupByNewTest {
    DataFrame df;

    @Before
    public void setUp() throws Exception {
        df = Jandas.readCsv("src/test/resources/csv/freshman_kgs.csv");
    }

    @Test
    public void createIntMap() {

    }

    @Test
    public void groups() {
    }
}