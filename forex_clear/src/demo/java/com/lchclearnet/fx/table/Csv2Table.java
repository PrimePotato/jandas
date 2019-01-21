package com.lchclearnet.fx.table;

import com.lchclearnet.table.Table;
import com.lchclearnet.table.io.csv.TableCsvReader;

public class Csv2Table {
    public static void main(String[] args) {
        try {
            TableCsvReader reader = new TableCsvReader();
            reader.setTableName("Spots");
            reader.setDir("\\\\filpr1\\#FILPR1\\ForexClear\\FOREXCLEAR ACTIVE\\SMART\\Prod Reports\\20181115");
            reader.setFilePattern("*_EOD_*_FXMD0001.csv");

            Table table = reader.read();
            System.out.println(table.printAll());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
