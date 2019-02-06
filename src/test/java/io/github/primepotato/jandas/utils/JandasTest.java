package io.github.primepotato.jandas.utils;

import io.github.primepotato.jandas.column.DoubleColumn;
import io.github.primepotato.jandas.dataframe.DataFrame;
import io.github.primepotato.jandas.io.DataFrameCsvWriter;
import org.ejml.equation.Equation;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class JandasTest {

    private DataFrame dataFrame;

    @Before
    public void setUp() {

        dataFrame = Jandas.readCsv("src/test/resources/SpotEg.csv");

    }

    @Test
    public void readCsv() {

        DataFrame df;

        df = Jandas.readCsv("src/test/resources/SpotEg.csv");
        assert (df.wellFormed());
        df.print(20);

    }



}