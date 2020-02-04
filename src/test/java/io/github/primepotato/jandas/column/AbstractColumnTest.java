package io.github.primepotato.jandas.column;

import io.github.primepotato.jandas.Jandas;
import io.github.primepotato.jandas.dataframe.DataFrame;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractColumnTest {

    DataFrame dfFreshmen;

    @Before
    public void setUp() throws Exception {
        dfFreshmen = Jandas.readCsv("src/test/resources/csv/freshman_kgs.csv");
    }

    @Test
    public void unique() {
        Column col = dfFreshmen.column(0);
        col.unique();
    }

    @Test
    public void uniqueSet() {
    }

    @Test
    public void name() {
    }

    @Test
    public void cleanName() {
    }

    @Test
    public void index() {
    }

    @Test
    public void firstValue() {
    }
}