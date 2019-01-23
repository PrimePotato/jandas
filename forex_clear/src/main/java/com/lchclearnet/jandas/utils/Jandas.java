package com.lchclearnet.jandas.utils;

import com.lchclearnet.jandas.dataframe.DataFrame;
import com.lchclearnet.jandas.io.CsvReader;
import java.io.File;

public class Jandas {

  public static DataFrame readCsv(String path){
    CsvReader cr = new CsvReader();
    File f = new File(path);
    cr.parser.parse(f);
    return cr.dataFrame;
  }

}
