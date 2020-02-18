package io.github.primepotato.jandas.column;

import io.github.primepotato.jandas.Jandas;
import io.github.primepotato.jandas.dataframe.DataFrame;
import io.github.primepotato.jandas.io.parsers.StringParser;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class ColumnTest {

    Column strColumn;

    @Before
    public void setUp() throws Exception {
        DataFrame dfFreshmen = Jandas.readCsv("src/test/resources/csv/freshman_kgs.csv");
        strColumn = dfFreshmen.column("Sex");
    }

    @Test
    public void rebuildIndex() {
    }

    @Test
    public void unique() {
        strColumn.unique();
    }

    @Test
    public void uniqueSet() {
        strColumn.uniqueSet();
    }

    @Test
    public void cleanName() {
        strColumn.cleanName();
    }

    @Test
    public void subColumn() {
        strColumn.subColumn("Bob", new int[]{0,2,4});
    }

    @Test
    public void getString() {
        strColumn.getString(0);
    }

    @Test
    public void appendString() {
        strColumn.appendString("Bob", new StringParser());
    }

    @Test
    public void getObject() {
        strColumn.getObject(0);
    }

    @Test
    public void heading() {
        strColumn.getHeading();
    }

    @Test
    public void index() {
        strColumn.index();
    }

    @Test
    public void firstValue() {
        strColumn.firstValue();
    }

    @Test
    public void appendAll() {
        strColumn.appendAll(Arrays.asList("1","2","3","4"));
    }

    @Test
    public void newDataContainer() {
        strColumn.newDataContainer(10);
    }

    @Test
    public void equals() {
        strColumn.newDataContainer(10);
    }

    @Test
    public void createEmpty() {
    }


}