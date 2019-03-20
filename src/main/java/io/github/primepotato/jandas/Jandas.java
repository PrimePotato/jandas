package io.github.primepotato.jandas;


import com.univocity.parsers.csv.CsvParserSettings;
import io.github.primepotato.jandas.dataframe.DataFrame;
import io.github.primepotato.jandas.io.csv.CsvReader;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.util.List;

public class Jandas {

    public static DataFrame readCsv(String path) {
        CsvReader cr = new CsvReader();
        File f = new File(path);
        cr.parser.parse(f);
        return cr.dataFrame;
    }

    public static DataFrame readCsv(String path, List<String> importHeaders) {

        CsvParserSettings cps = CsvReader.createEmptyParserSettings();
        cps.setHeaderExtractionEnabled(true);
        String[] flds = importHeaders.toArray(new String[0]);
        cps.selectFields(flds);
        CsvReader cr = new CsvReader(cps);
        File f = new File(path);
        cr.parser.parse(f);
        return cr.dataFrame;
    }

    public static DataFrame readCsv(String path, List<String> importHeaders, List<Pair<String, Class>> dataTypes) {

        CsvParserSettings cps = CsvReader.createEmptyParserSettings();
        cps.setHeaderExtractionEnabled(true);
        String[] flds = importHeaders.toArray(new String[0]);

        cps.selectFields(flds);
        CsvReader cr = new CsvReader(cps);
        File f = new File(path);
        cr.parser.parse(f);
        return cr.dataFrame;
    }

}
