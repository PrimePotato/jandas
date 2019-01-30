package io.github.primepotato.jandas.io;

import io.github.primepotato.jandas.column.DoubleColumn;
import io.github.primepotato.jandas.dataframe.DataFrame;
import org.junit.Test;

import java.io.File;

public class CsvReaderTest {

  @Test
  public void parse(){

    CsvReader cr = new CsvReader();
    File f = new File("Z:/data/FxReports/20181115/20181115_2359_EOD_22017_FXMD0001.csv");

    cr.parser.parse(f);
    DataFrame df = cr.dataFrame;

    DoubleColumn ask = (DoubleColumn)df.column("Ask");
    DoubleColumn bid = (DoubleColumn)df.column("Bid");
    DoubleColumn mid = (DoubleColumn)ask.plus(bid).scale(0.5);
    mid.name = "midDouble";
    df.addColumn(mid);

    df.print(20);

  }

}