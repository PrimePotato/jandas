package io.github.primepotato.jandas.io;

import io.github.primepotato.jandas.column.DoubleColumn;
import io.github.primepotato.jandas.dataframe.DataFrame;
import org.junit.Test;

import java.io.File;

public class CsvReaderTest {

  @Test
  public void parse(){

    CsvReader cr = new CsvReader();
    File f = new File("src/test/resources/SpotEg.csv");

    cr.parser.parse(f);
    DataFrame df = cr.dataFrame;

    DoubleColumn ask = df.column("Ask");
    DoubleColumn bid = df.column("Bid");
    DoubleColumn mid = (DoubleColumn)ask.plus(bid).scale(0.5);
    mid.name = "midDouble";
    df.addColumn(mid);

    df.print(20);

  }

}