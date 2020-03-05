package io.github.primepotato.jandas.utils;

import io.github.primepotato.jandas.Jandas;
import io.github.primepotato.jandas.dataframe.DataFrame;
import org.junit.Before;
import org.junit.Test;

public class DataFrameUtilsTest {

    DataFrame df;

    @Before
    public void setUp() throws Exception {
        df = Jandas.readCsv("src/test/resources/csv/party_constituency_vote_shares.csv");
    }

    @Test
    public void createEmptyDataFrameFromAnother() {
        DataFrame dfc = DataFrameUtils.createEmptyDataFrameFromAnother(df);
    }

    @Test
    public void recordToColumns() {
    }
}