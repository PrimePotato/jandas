package io.github.primepotato.jandas;

import io.github.primepotato.jandas.Jandas;
import io.github.primepotato.jandas.dataframe.DataFrame;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class JandasTest {

    private DataFrame dataFrame;

    @Before
    public void setUp() {

//        dataFrame = Jandas.readCsv("src/test/resources/csv/biostats.csv");

    }

    @Test
    public void readCsv() {

//        assert (dataFrame.wellFormed());
//        dataFrame.print();

        DataFrame df = Jandas.readCsv("src/test/resources/csv/biostats.csv", Arrays.asList("Name", "Sex", "Age"));
        df.print();
    }


}