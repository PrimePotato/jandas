package io.github.primepotato.jandas.io;


import io.github.primepotato.jandas.dataframe.DataFrame;
import io.github.primepotato.jandas.io.CsvReaderLowMem;
import org.junit.Test;

import java.io.File;

public class CsvReaderLowMemTest {

  @Test
  public void parse(){

    CsvReaderLowMem cr = new CsvReaderLowMem ();
    File f = new File("Z:/data/FxReports/20181115/20181115_2359_FRPT0001.csv");

    cr.parser.parse(f);
    DataFrame df = cr.dataFrame;
    df.print(20);

  }

}