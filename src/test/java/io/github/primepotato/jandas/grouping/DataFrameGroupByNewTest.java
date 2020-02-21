package io.github.primepotato.jandas.grouping;

import io.github.primepotato.jandas.Jandas;
import io.github.primepotato.jandas.dataframe.DataFrame;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DataFrameGroupByNewTest {
    DataFrame df;
    DataFrameGroupByNew<String> dfg;

    @Before
    public void setUp() throws Exception {
        df = Jandas.readCsv("src/test/resources/csv/freshman_kgs.csv");
        dfg = new DataFrameGroupByNew<>(x -> x.getString("Sex"), df, String.class);
    }

    @Test
    public void getGroup() {
        DataFrame d = dfg.getGroup("M");
    }
}