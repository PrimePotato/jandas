package io.github.primepotato.jandas.io.csv;

import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.RowProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import io.github.primepotato.jandas.column.Column;
import io.github.primepotato.jandas.dataframe.DataFrame;
import io.github.primepotato.jandas.header.Header;
import io.github.primepotato.jandas.io.csv.containers.AbstractColumnDataContainer;
import io.github.primepotato.jandas.io.csv.containers.DynamicColumnDataContainer;
import io.github.primepotato.jandas.io.csv.containers.FixedColumnDataContainer;

import java.util.*;
import java.util.stream.Collectors;

public class CsvReader implements RowProcessor {

    private CsvParserSettings parserSettings;
    public CsvParser parser;
    String[] headers;
    Map<String, Class> dataTypes;

    private List<AbstractColumnDataContainer> pcds;

    public DataFrame dataFrame;
    public List<Column> columns;

    public CsvReader(List<String> selectedHeaders, Map<String, Class> dataTypes) {
        CsvParserSettings cps = createEmptyParserSettings();
        if (selectedHeaders != null) {
            cps.selectFields(selectedHeaders.toArray(new String[0]));
        }
        parserSettings = cps;
        cps.setProcessor(this);
        parser = new CsvParser(parserSettings);
        pcds = new ArrayList<>();
        this.dataTypes = (dataTypes==null)? new HashMap<>(): dataTypes;
    }

    public CsvReader(List<String> selectedHeaders){
        this(selectedHeaders, null);
    }

    public CsvReader() {
        this(null, null);
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
        dataFrame.header = new Header(headers);
        for (String h : headers){
            if (dataTypes.containsKey(h)) {
                pcds.add(new FixedColumnDataContainer(h, dataTypes.get(h)));
            } else {
                pcds.add(new DynamicColumnDataContainer(h));
            }
        }
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
        for (AbstractColumnDataContainer pcd : pcds) {
            columns.add(pcd.toColumn());
        }
    }
}
