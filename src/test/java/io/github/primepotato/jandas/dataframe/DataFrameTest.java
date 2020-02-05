package io.github.primepotato.jandas.dataframe;


import io.github.primepotato.jandas.Jandas;
import io.github.primepotato.jandas.column.Column;
import io.github.primepotato.jandas.column.impl.DoubleColumn;
import io.github.primepotato.jandas.column.impl.IntegerColumn;
import io.github.primepotato.jandas.column.impl.ObjectColumn;
import io.github.primepotato.jandas.index.meta.JoinType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class DataFrameTest {

    private DataFrame df1, df2;

    @Before
    public void setUp() {

        String[] str1 = {"a", "b", "c"};
        ObjectColumn<String> sc1 = new ObjectColumn<>("str", true, str1, String.class);

        int[] data1 = {1, 2, 3};
        IntegerColumn ic1 = new IntegerColumn("int1", true, data1);

        int[] data2 = {1, 2, 3};
        IntegerColumn ic2 = new IntegerColumn("int2", true, data2);

        double[] dblData = {1.1, 2.1, 3.1};
        DoubleColumn dbl1 = new DoubleColumn("dbl1", true, dblData);

        List<Column> cols = new ArrayList<>();
        cols.add(sc1);
        cols.add(ic1);
        cols.add(ic2);
        cols.add(dbl1);

        df1 = new DataFrame("1", cols);

        String[] str2 = {"a", "b", "c", "a", "b", "c", "a", "b", "c", "a", "b", "c"};
        ObjectColumn<String> sc2 = new ObjectColumn<>("str", true, str2, String.class);

        int[] data3 = {55, 25, 35, 455, 2, 3, 4, 2, 4, 23, 423, 234};
        IntegerColumn ic3 = new IntegerColumn("int3", true, data3);

        int[] data4 = {155, 552, 355, 455, 2, 3, 4, 2, 4, 23, 423, 234};
        IntegerColumn ic4 = new IntegerColumn("int4", true, data4);

        double[] dblData2 = {1., 2., 3., 45.5, 2., 3., .4, .2, .4, 2.3, 42.3, 2.34};
        DoubleColumn dbl2 = new DoubleColumn("dbl2", true, dblData2);

        List<Column> cols2 = new ArrayList<>();
        cols2.add(sc2);
        cols2.add(ic3);
        cols2.add(ic4);
        cols2.add(dbl2);

        df2 = new DataFrame("2", cols2);
    }

    @Test
    public void groupBy() {

        List<String> gby = new ArrayList<String>() {{
            add("int1");
            add("int2");
        }};
        List<String> agg = new ArrayList<String>() {{
            add("dbl1");
        }};
        System.out.println(df1.groupBy(gby, agg));
    }

    @Test
    public void join() {

        List<String> jhs = new ArrayList<String>() {{
            add("str");
        }};
        DataFrame df = df1.join(jhs, df2, JoinType.INNER);
        df.print();
    }

    @Test
    public void getColumn() {

        List<String> jhs = new ArrayList<String>() {{
            add("str");
        }};

        List<Column> cols2 = df2.getColumns(jhs, Column.class);

    }

    @Test
    public void getColumns() {

    }

    @Test
    public void buildMetaIndex() {

    }

    @Test
    public void columnCount() {

    }

    @Test
    public void name() {

    }

    @Test
    public void rowCount() {

    }

    @Test
    public void getString() {

    }

    @Test
    public void column() {

    }

    @Test
    public void toMap() {
        Map m = df1.toMap();
        System.out.println(m);
    }

    @Test
    public void print() {
        df1.print();
    }

    @Test
    public void resolveJoin() {

    }

    @Test
    public void toCsv() {
        df1.toCsv("src/test/resources/toCsvTest.csv");
        DataFrame dfs = Jandas.readCsv("src/test/resources/toCsvTest.csv");
        Assert.assertEquals(df1, dfs);
    }
}