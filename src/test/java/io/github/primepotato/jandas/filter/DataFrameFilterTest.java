package io.github.primepotato.jandas.filter;


import io.github.primepotato.jandas.Jandas;
import io.github.primepotato.jandas.dataframe.DataFrame;
import io.github.primepotato.jandas.dataframe.RecordSet;
import org.junit.Before;
import org.junit.Test;
import java.util.function.Predicate;

import static org.junit.Assert.*;

public class DataFrameFilterTest {

    private DataFrame dfFreshmen;
    private DataFrameFilter dataFrameFilter;
    private Predicate<RecordSet> predicate;

    @Before
    public void setUp() throws Exception {
        dfFreshmen = Jandas.readCsv("src/test/resources/csv/freshman_kgs.csv");
        predicate = record -> record.getRowNumber()%10==0;
        dataFrameFilter = new DataFrameFilter(predicate);
    }

    @Test
    public void getPredicate() {
        assert dataFrameFilter.getPredicate() == predicate;
    }

    @Test
    public void apply() {
        DataFrame df = dataFrameFilter.apply(dfFreshmen);
        assertEquals(7, df.rowCount());
    }
}