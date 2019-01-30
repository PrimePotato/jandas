package io.github.primepotato.jandas.utils;

import io.github.primepotato.jandas.column.DoubleColumn;
import io.github.primepotato.jandas.dataframe.DataFrame;
import it.unimi.dsi.fastutil.ints.Int2DoubleOpenHashMap;
import org.ejml.equation.Equation;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

public class JandasTest {

  private DataFrame dataFrame;

  @Before
  public void setUp() {

    dataFrame =
        Jandas.readCsv("Z:\\data\\FxReports\\20181115\\20181115_2359_EOD_22017_FXMD0001.csv");

  }

  @Test
  public void readCsv() {

    DataFrame df;

    df = Jandas.readCsv("Z:\\data\\FxReports\\20181115\\20181115_2359_EOD_22017_FXMD0007.csv");
    assert (df.wellFormed());
    df.print(20);

  }

  @Test
  public void columnPlus() {

    DoubleColumn ask = (DoubleColumn) dataFrame.column("Ask");
    DoubleColumn bid = (DoubleColumn) dataFrame.column("Bid");
    DoubleColumn mid = new DoubleColumn("", true, new double[0]);

    Equation eq = new Equation();
    eq.alias(ask.getMatrix(), "a", bid.getMatrix(), "b", mid.getMatrix(), "m");

    eq.process("m = a-b");

    mid.name = "NewMid";
    dataFrame.addColumn(mid);
    dataFrame.print(22);
  }

  @Test
  public void columnScale() {

    DoubleColumn ask = (DoubleColumn) dataFrame.column("Ask");
    System.out.println(ask.scale(2));
  }

  @Test
  public void groupBy() {

    DataFrame df =
        Jandas.readCsv("Z:\\data\\FxReports\\20181115\\20181115_2359_EOD_22017_FXMD0007.csv");
    Map<String, Int2DoubleOpenHashMap> grp =
        df.groupBy(Arrays.asList("Scenario"), Arrays.asList("SpotRateShift_Core"));
    System.out.print(grp);
  }

  @Test
  public void quickJoin(){
    DataFrame df =
        Jandas.readCsv("Z:\\data\\FxReports\\20181115\\20181115_2359_EOD_22017_FXMD0007.csv");
    DataFrame dfJoin = dataFrame.quickJoin(Arrays.asList("CurrencyPair"), df);
    dfJoin.print(20);
  }

}