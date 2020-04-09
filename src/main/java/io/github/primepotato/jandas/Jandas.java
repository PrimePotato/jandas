package io.github.primepotato.jandas;


import com.univocity.parsers.csv.CsvParserSettings;
import io.github.primepotato.jandas.dataframe.DataFrame;
import io.github.primepotato.jandas.io.csv.CsvReader;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.util.List;
import java.util.Map;

public class Jandas {

    public static DataFrame readCsv(String path) {
        return readCsv(path, null, null);
    }

    public static DataFrame readCsv(String path, List<String> importHeaders) {
        return readCsv(path, importHeaders, null);
    }

    public static DataFrame readCsv(String path, List<String> importHeaders, Map<String, Class> dataTypes) {

        CsvReader cr = new CsvReader(importHeaders, dataTypes);
        File f = new File(path);
        cr.parser.parse(f);
        return cr.getDataFrame();
    }

}
