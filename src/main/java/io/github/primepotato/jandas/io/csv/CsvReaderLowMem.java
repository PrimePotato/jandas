package io.github.primepotato.jandas.io.csv;

import com.univocity.parsers.common.AbstractParser;
import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.RowProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import io.github.primepotato.jandas.column.Column;
import io.github.primepotato.jandas.dataframe.DataFrame;
import io.github.primepotato.jandas.utils.ParserColumnData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvReaderLowMem implements RowProcessor {

  private CsvParserSettings parserSettings;
  public boolean header;
  private String[] missingValueStrings;
  public CsvParser parser;
  private int idx;
  private List<AbstractParser> cellParsers;
  String[] headers;

  private List<ParserColumnData> pcds;

  public DataFrame dataFrame;
  public List<Column> columns;

  public CsvReaderLowMem() {

    header = true;
    missingValueStrings = null;
    parserSettings = new CsvParserSettings();
    parserSettings.getFormat().setDelimiter(',');
    parserSettings.setHeaderExtractionEnabled(header);
    parserSettings.setLineSeparatorDetectionEnabled(true);
    parserSettings.setProcessor(this);
    parserSettings.setIgnoreTrailingWhitespaces(true);
    parserSettings.setIgnoreLeadingWhitespaces(true);
    parserSettings.setMaxColumns(1000);
    parserSettings.setReadInputOnSeparateThread(false);
    parser = new CsvParser(parserSettings);
    pcds = new ArrayList<>();
  }

  @Override
  public void processStarted(ParsingContext context) {
    idx = 0;
    columns = new ArrayList<>();
    headers = context.selectedHeaders();
    dataFrame = new DataFrame("", columns);
    dataFrame.headers = Arrays.asList(context.selectedHeaders());
    Arrays.stream(headers).forEach(x-> pcds.add(new ParserColumnData(x)));
  }

  @Override
  public void rowProcessed(String[] row, ParsingContext context) {
    if (row.length == headers.length) {
      for (int i = 0; i < row.length; i++) {
        pcds.get(i).add(row[i]);
      }
    }
    ++idx;
  }

  @Override
  public void processEnded(ParsingContext context) {
    for (ParserColumnData pcd: pcds){

      columns.add(pcd.toColumn());
    }
  }
}
