package io.github.primepotato.jandas.io.csv;

import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.RowProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import io.github.primepotato.jandas.column.Column;
import io.github.primepotato.jandas.dataframe.DataFrame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CsvReader implements RowProcessor {

    private CsvParserSettings parserSettings;
    public CsvParser parser;
    String[] headers;

    private List<DynamicColumnDataContainer> pcds;

    public DataFrame dataFrame;
    public List<Column> columns;

    public CsvReader(CsvParserSettings cps) {
        parserSettings = cps;
        cps.setProcessor(this);

        parser = new CsvParser(parserSettings);
        pcds = new ArrayList<>();
    }

    public CsvReader() {
        this(createEmptyParserSettings());
    }


    public static CsvParserSettings createEmptyParserSettings() {
        CsvParserSettings csp = new CsvParserSettings();
        csp.getFormat().setDelimiter(',');
        csp.setHeaderExtractionEnabled(true);
        csp.setLineSeparatorDetectionEnabled(true);
        csp.setIgnoreTrailingWhitespaces(true);
        csp.setIgnoreLeadingWhitespaces(true);
        csp.setMaxColumns(1000);
        csp.setReadInputOnSeparateThread(false);
        return csp;
    }

    @Override
    public void processStarted(ParsingContext context) {
        columns = new ArrayList<>();
        context.selectedHeaders();
        headers = context.selectedHeaders(); //TODO: univocity needs to run twice for this to work..... dodgy
        dataFrame = new DataFrame("", columns);
        dataFrame.headers = Arrays.stream(headers).collect(Collectors.toList());
        Arrays.stream(headers).forEach(x -> pcds.add(new DynamicColumnDataContainer(x)));
    }

    @Override
    public void rowProcessed(String[] row, ParsingContext context) {
        if (row.length == headers.length) {
            for (int i = 0; i < row.length; i++) {
                pcds.get(i).add(row[i]);
            }
        }
    }

    @Override
    public void processEnded(ParsingContext context) {
        for (DynamicColumnDataContainer pcd : pcds) {
            columns.add(pcd.toColumn());
        }
    }
}
