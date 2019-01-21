package com.lchclearnet.fx.jandas;

import com.lchclearnet.jandas.dataframe.DataFrame;
import com.lchclearnet.jandas.io.DataFrameCsvReader;

public class ReadCsvToFrameDemo {
    public static void main(String[] args) {
        try {
            DataFrameCsvReader reader = new DataFrameCsvReader();
            reader.setDataFrameName("Spots");
            reader.setDir("\\\\filpr1\\#FILPR1\\ForexClear\\FOREXCLEAR ACTIVE\\SMART\\Prod Reports\\20181115");
            reader.setFilePattern("*_EOD_*_FXMD0001.csv");

            DataFrame DataFrame = reader.read();
            DataFrame.print(20);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
