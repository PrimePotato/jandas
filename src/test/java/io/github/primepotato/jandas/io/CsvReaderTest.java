package io.github.primepotato.jandas.io;

import io.github.primepotato.jandas.column.DoubleColumn;
import io.github.primepotato.jandas.dataframe.DataFrame;
import org.junit.Test;

import java.io.File;

public class CsvReaderTest {

  @Test
  public void parse(){

    CsvReader cr = new CsvReader();
    File f = new File("src/test/resources/freshman_kgs.csv");

    cr.parser.parse(f);
    DataFrame df = cr.dataFrame;

    df.print(20);

  }

}