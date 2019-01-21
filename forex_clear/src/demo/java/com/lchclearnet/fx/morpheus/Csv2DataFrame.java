package com.lchclearnet.fx.morpheus;

import com.zavtech.morpheus.frame.DataFrame;

import java.nio.file.Paths;

public class Csv2DataFrame {
    public static void main(String[] args) {
        try {
            DataFrame<Object, String> df = DataFrame.read().csv(options -> {
                options.setFile(Paths.get("D:/Mkt/PROD/20180911", "20180911_2359_FRPT0001.csv").toFile());
            }).rows().select(row -> {
                String member = row.getValue("Member");
                return !member.startsWith("Record Count");
            });


            df.tail(10).out().print();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
