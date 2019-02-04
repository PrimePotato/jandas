package io.github.primepotato.jandas.io;

import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import io.github.primepotato.jandas.dataframe.DataFrame;
import io.github.primepotato.jandas.dataframe.Record;

import java.io.*;

public class DataFrameCsvWriter {

    public static void toCsv(DataFrame df, String fPath){
        File file = new File(fPath);
        try (FileOutputStream csvResult = new FileOutputStream(file)) {
            Writer outputWriter = new OutputStreamWriter(csvResult);
            CsvWriter csvWriter = new CsvWriter(outputWriter, new CsvWriterSettings());
            csvWriter.writeHeaders(df.headers);

            for (Record rec : df){
                csvWriter.writeRow(rec.rowString());
            }

            csvWriter.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
