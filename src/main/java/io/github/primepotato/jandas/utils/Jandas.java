package io.github.primepotato.jandas.utils;


import io.github.primepotato.jandas.dataframe.DataFrame;
import io.github.primepotato.jandas.io.CsvReader;

import java.io.File;

public class Jandas {

  public static DataFrame readCsv(String path){
    CsvReader cr = new CsvReader();
    File f = new File(path);
    cr.parser.parse(f);
    return cr.dataFrame;
  }

}
