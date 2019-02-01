package io.github.primepotato.jandas.dataframe;


import io.github.primepotato.jandas.column.Column;
import io.github.primepotato.jandas.column.DoubleColumn;
import io.github.primepotato.jandas.column.IntegerColumn;
import io.github.primepotato.jandas.column.StringColumn;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class DataFrameTest {

  DataFrame df1, df2;
  IntegerColumn ic1, ic2, ic3, ic4;
  DoubleColumn dbl1, dbl2, dbl3, dbl4;
  StringColumn sc1, sc2;

  @Before
  public void setUp() {

    String[] str1 = {"a", "b", "c"};
    sc1 = new StringColumn("str",true, str1);

    int[] data1 = {1, 2, 3};
    ic1 = new IntegerColumn("int1",true, data1);

    int[] data2 = {1, 2, 3};
    ic2 = new IntegerColumn("int2", true,data2);

    double[] dblData = {1., 2., 3.};
    dbl1 = new DoubleColumn("dbl1",true,dblData);

    List<Column> cols = new ArrayList<>();
    cols.add(sc1);
    cols.add(ic1);
    cols.add(ic2);
    cols.add(dbl1);

    df1 = new DataFrame("1", cols);

    String[] str2 = {"a", "b", "c","a", "b", "c","a", "b", "c","a", "b", "c"};
    sc2 = new StringColumn("str", true,str2);

    int[] data3 = {55, 25, 35, 455, 2, 3, 4, 2, 4, 23, 423, 234};
    ic3 = new IntegerColumn("int3", true,data3);

    int[] data4 = {155, 552, 355, 455, 2, 3, 4, 2, 4, 23, 423, 234};
    ic4 = new IntegerColumn("int4",true, data4);

    double[] dblData2 = {1., 2., 3., 45.5, 2., 3., .4, .2, .4, 2.3, 42.3, 2.34};
    dbl2 = new DoubleColumn("dbl2",true, dblData2);

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
  public void quickJoin() {

    List<String> jhs = new ArrayList<String>() {{
      add("str");
    }};
    DataFrame df = df1.quickJoin(jhs, df2);
    df.print(20);
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
  public void print() {
    df1.print(20);
    df2.print(20);
  }

  @Test
  public void resolveJoin() {

  }

}