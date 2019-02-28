package io.github.primepotato.jandas.io.csv;


import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.RowProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import io.github.primepotato.jandas.column.*;
import io.github.primepotato.jandas.dataframe.DataFrame;
import io.github.primepotato.jandas.io.parsers.*;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.Map.Entry;

public class CsvReader implements RowProcessor {

  private CsvParserSettings parserSettings;
  public boolean header;
  private String[] missingValueStrings;
  public CsvParser parser;
  private int idx;
  private List<AbstractParser> cellParsers;
  String[] headers;

  public DataFrame dataFrame;

  Map<String, IntArrayList> parsedIntData;
  Map<String, DoubleArrayList> parsedDoubleData;
  Map<String, ObjectArrayList<LocalDate>> parsedDateData;
  Map<String, ObjectArrayList<LocalTime>> parsedTimeData;
  Map<String, ObjectArrayList<String>> parsedStringData;

  List<Column> columns;

  public CsvReader() {

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

    parsedStringData = new HashMap<>();
    parsedTimeData = new HashMap<>();
    parsedDateData = new HashMap<>();
    parsedDoubleData = new HashMap<>();
    parsedIntData = new HashMap<>();
    parsedStringData = new HashMap<>();

    cellParsers();
  }

  private void cellParsers() {

    cellParsers = new ArrayList<AbstractParser>() {{
      add(new IntParser());
      add(new DoubleParser());
      add(new DateParser());
      add(new TimeParser());
    }};
  }

  public AbstractParser parseColumn(String name, ObjectArrayList<String> colData) {

    for (AbstractParser par : cellParsers) {
      try {
        Map<String, ? extends AbstractCollection> container = getDataContainer(par.elementClass());
        container.put(name, attemptParse(colData, par));
        return par;
      } catch (Exception ignored) {
      }
    }
    return null;
  }

  public AbstractParser parseValue(String colName, String val, int parserPosition) {

    try {
      AbstractParser par = cellParsers.get(parserPosition);
      Map<String, ? extends AbstractCollection> container = getDataContainer(par.elementClass());
      AbstractCollection d = container.getOrDefault(colName, getNewContainer(par.elementClass()));
      d.add(par.parse(val));
      return par;
    } catch (Exception ignored) {
      if (parserPosition + 1 < cellParsers.size()) {
        return parseValue(colName, val, parserPosition + 1);
      }
    }
    return null;
  }

  private <T extends AbstractCollection> T attemptParse(ObjectArrayList<String> colData,
      AbstractParser parser) {

    Class cls = parser.elementClass();
    AbstractCollection container = getNewContainer(cls);
    for (String s : colData) {
      container.add(parser.parse(s));
    }
    return (T) container;
  }

  private Map<String, ? extends AbstractCollection> getDataContainer(Class cls) {

    if (cls.getSimpleName().equals("Integer")) {
      return parsedIntData;
    } else if (cls.getSimpleName().equals("Double")) {
      return parsedDoubleData;
    } else if (cls.getSimpleName().equals("LocalDate")) {
      return parsedDateData;
    } else if (cls.getSimpleName().equals("LocalTime")) {
      return parsedTimeData;
    } else if (cls.getSimpleName().equals("String")) {
      return parsedStringData;
    }
    return null;
  }

  private <T extends Object> T getNewContainer(Class cls) {

    if (cls.getSimpleName().equals("Integer")) {
      return (T) new IntArrayList();
    } else if (cls.getSimpleName().equals("Double")) {
      return (T) new DoubleArrayList();
    } else if (cls.getSimpleName().equals("LocalDate")) {
      return (T) new ObjectArrayList<LocalDate>();
    } else if (cls.getSimpleName().equals("LocalTime")) {
      return (T) new ObjectArrayList<LocalTime>();
    } else if (cls.getSimpleName().equals("String")) {
      return (T) new ObjectArrayList<String>();
    }
    return null;
  }

  @Override
  public void processStarted(ParsingContext context) {

    idx = 0;
    columns = new ArrayList<>();
    headers = context.selectedHeaders();
    dataFrame = new DataFrame("", columns);
    dataFrame.headers = new ArrayList<>();
  }

  @Override
  public void rowProcessed(String[] row, ParsingContext context) {

    if (row.length == headers.length) {
      for (int i = 0; i < row.length; i++) {
        if (parsedStringData.containsKey(headers[i])) {
          ObjectArrayList<String> cd = parsedStringData.get(headers[i]);
          cd.add(row[i]);
        } else {
          ObjectArrayList<String> cd = new ObjectArrayList<>();
          cd.add(row[i]);
          parsedStringData.put(headers[i], cd);
        }
      }
    }
    idx++;
  }

  private List<Column> emptyColumnList(int x) {

    List<Column> columns = new ArrayList<>();
    for (int i = 0; i < x; i++) {
      columns.add(null);
    }
    return columns;
  }

  public Set<String> getStringColumns() {

    Set<String> hdrs = new HashSet<>(parsedStringData.keySet());
    hdrs.removeAll(parsedIntData.keySet());
    hdrs.removeAll(parsedDoubleData.keySet());
    hdrs.removeAll(parsedDateData.keySet());
    hdrs.removeAll(parsedTimeData.keySet());
    return hdrs;
  }

  @Override
  public void processEnded(ParsingContext context) {

    for (Entry<String, ObjectArrayList<String>> colData : parsedStringData.entrySet()) {
      parseColumn(colData.getKey(), colData.getValue());
    }

    Map<String, Column> colMap = new LinkedHashMap<>();

    parsedIntData.entrySet()
        .stream()
        .forEach(e -> colMap.put(e.getKey(), new IntegerColumn(e.getKey(), false, e.getValue())));

    parsedDoubleData.entrySet()
        .stream()
        .forEach(e -> colMap.put(e.getKey(), new DoubleColumn(e.getKey(), false, e.getValue())));

    parsedDateData.entrySet()
        .stream()
        .forEach(e -> colMap.put(e.getKey(), new DateColumn(e.getKey(), false, e.getValue())));

    parsedTimeData.entrySet()
        .stream()
        .forEach(e -> colMap.put(e.getKey(), new TimeColumn(e.getKey(), false, e.getValue())));

    Set<String> stringCols = getStringColumns();
    for (String s : stringCols) {
      colMap.put(s, new StringColumn(s, false, parsedStringData.get(s)));
    }

    for (String h : headers) {
      dataFrame.headers.add(h);
      columns.add(colMap.get(h));
    }

  }

}
