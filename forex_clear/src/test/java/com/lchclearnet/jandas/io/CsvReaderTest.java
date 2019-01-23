package com.lchclearnet.jandas.io;
import com.lchclearnet.jandas.column.DoubleColumn;
import com.lchclearnet.jandas.dataframe.DataFrame;

import java.io.File;
import org.junit.Test;

public class CsvReaderTest {

  @Test
  public void parse(){

    CsvReader cr = new CsvReader();
    File f = new File("Z:/data/FxReports/20181115/20181115_2359_EOD_22017_FXMD0001.csv");

    cr.parser.parse(f);
    DataFrame df = cr.dataFrame;

    DoubleColumn ask = (DoubleColumn)df.column("Ask");
    DoubleColumn bid = (DoubleColumn)df.column("Bid");
    DoubleColumn mid = (DoubleColumn)ask.plus(bid).scale(2);
    mid.name = "midDouble";
    df.addColumn(mid);

    df.print(20);

  }

}