package io.github.primepotato.jandas.io;


import com.univocity.parsers.csv.CsvParserSettings;
import io.github.primepotato.jandas.dataframe.DataFrame;
import io.github.primepotato.jandas.io.csv.CsvReader;
import org.junit.Test;

import java.io.File;


public class CsvReaderTest {
git a
  @Test
  public void parse(){

    CsvReader cr = new CsvReader();
    File f = new File("src/test/resources/csv/biostats.csv");

    cr.parser.parse(f);
    DataFrame df = cr.dataFrame;
    df.print();

  }

  @Test
  public void parseSelected(){

    CsvParserSettings cps = CsvReader.createEmptyParserSettings();
    cps.setHeaderExtractionEnabled(true);

    String[] flds = {"Name","Sex","Age"};
    cps.selectFields(flds);
    File f = new File("src/test/resources/csv/biostats.csv");

//    CsvReader crf = new CsvReader(cps);
//    crf.parser.parse(f);
//
//    DataFrame df = crf.dataFrame;
//    df.print();

  }


}