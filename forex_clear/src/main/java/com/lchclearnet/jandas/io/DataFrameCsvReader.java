package com.lchclearnet.jandas.io;

import com.google.common.base.Strings;
import com.lchclearnet.fx.AppConfig;

import com.lchclearnet.jandas.column.Column;
import com.lchclearnet.jandas.column.ColumnParserFactory;
import com.lchclearnet.jandas.column.DoubleColumn;
import com.lchclearnet.jandas.dataframe.DataFrame;
import com.lchclearnet.jandas.column.parsers.AbstractParser;

import com.lchclearnet.utils.ArrayHelper;
import com.lchclearnet.utils.Config;
import com.lchclearnet.utils.FileHelper;
import com.lchclearnet.utils.Pair;
import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.RowProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import java.io.File;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scala.Tuple3;

public class DataFrameCsvReader implements RowProcessor {

  private static Logger logger = LogManager.getLogger();

  private CsvParserSettings parserSettings;
  private static double GROWTH_BASE = 2;

  private String dataFrameName;
  private boolean header;
  private String[] selectedHeaders;
  private String dir;
  private String filePattern;
  private String[] missingValueStrings;
  private Map<String, ?> columnParams;
  private List<AbstractCollection> dataContainers;
  //*************************************************************************************************************//
  //************************** Univocity CSV Parsing
  // ************************************************************//
  //*************************************************************************************************************//
  private String[] headerNames;
  private String[] columnNames;
  private int[] columnIndexes;
  private List<Column> columns;
  private List<Class> columnTypes;
  private AbstractParser[] parsers;
  private DataFrame dataFrame;

  public DataFrameCsvReader() {

    dataFrameName = "";
    header = true;
    missingValueStrings = null;

    parserSettings = new CsvParserSettings();
    parserSettings.getFormat().setDelimiter(',');
    parserSettings.setHeaderExtractionEnabled(header());
    parserSettings.setLineSeparatorDetectionEnabled(true);
    parserSettings.setProcessor(this);
    parserSettings.setIgnoreTrailingWhitespaces(true);
    parserSettings.setIgnoreLeadingWhitespaces(true);
    parserSettings.setMaxColumns(10000);
    parserSettings.setReadInputOnSeparateThread(false);

  }

  public static DataFrameCsvReader of(Config config, String... accessorProps) {

    DataFrameCsvReader dataFrameCsvReader = new DataFrameCsvReader();

    String[] props = new String[accessorProps.length + 1];
    System.arraycopy(accessorProps, 0, props, 0, accessorProps.length);

    //required fields
    props[accessorProps.length] = AppConfig.DIR;
    dataFrameCsvReader.dir = config.get(props);

    props[accessorProps.length] = AppConfig.FILE_PATTERN;
    dataFrameCsvReader.filePattern = config.get(props);

    //optional fields
    props[accessorProps.length] = AppConfig.TABLE_NAME;
    if (config.has(props)) {
      dataFrameCsvReader.dataFrameName = config.get(props);
    }

    props[accessorProps.length] = AppConfig.HEADER;
    if (config.has(props)) {
      dataFrameCsvReader.header = config.get(props);
    }

    props[accessorProps.length] = AppConfig.COLUMNS;
    if (config.has(props)) {
      dataFrameCsvReader.columnParams = config.get(props);
    }

    props[accessorProps.length] = AppConfig.PARSER_SETTINGS;
    if (config.has(props) && config.get(props) instanceof Map) {
      dataFrameCsvReader.selectedHeaders = getSelectedHeaders(config, props);
      configureParserSettings(dataFrameCsvReader, config, props);
    }

    return dataFrameCsvReader;
  }

  private static String[] getSelectedHeaders(Config config, String[] props) {

    Map<String, ?> parserSettings = config.get(props);
    return ((List<String>) parserSettings.get("selectFields")).stream().toArray(String[]::new);
  }

  private static void configureParserSettings(DataFrameCsvReader dataFrameCsvReader, Config config,
      String[] props) {

    String[] selectedFields = getSelectedHeaders(config, props);
    if (selectedFields != null) {
      dataFrameCsvReader.parserSettings.selectFields(selectedFields);
    }
  }

  public DataFrameCsvReader setParserSettings(CsvParserSettings parserSettings) {

    this.parserSettings = parserSettings;
    return this;
  }

  public DataFrameCsvReader setDataFrameName(String dataFrameName) {

    this.dataFrameName = dataFrameName;
    return this;
  }

  public DataFrameCsvReader setHeader(boolean header) {

    this.header = header;
    return this;
  }

  public DataFrameCsvReader setDir(String dir) {

    this.dir = dir;
    return this;
  }

  public DataFrameCsvReader setFilePattern(String filePattern) {

    this.filePattern = filePattern;
    return this;
  }

  public DataFrameCsvReader setMissingValueStrings(String[] missingValueStrings) {

    this.missingValueStrings = missingValueStrings;
    return this;
  }

  public void setColumnParams(Map<String, ?> columnParams) {

    this.columnParams = columnParams;
  }

  public File file() {

    Path parent = Paths.get(dir);
    Queue<Path> paths =
        FileHelper.find(parent, filePattern, new PriorityQueue<>(11, Collections.reverseOrder()));
    return paths.poll().toFile();
  }

  public String dataFrameName() {

    return dataFrameName;
  }

  public boolean header() {

    return header;
  }

  private String[] headerNames(CsvParser parser) {

    String[] headerNames = parser.parseNext();

    // work around issue where Univocity returns null if a column has no header.
    for (int i = 0; i < headerNames.length; i++) {
      if (headerNames[i] == null) {
        headerNames[i] = "Column" + i;
      } else {
        headerNames[i] = headerNames[i].trim();
      }
    }

    return headerNames;
  }

  private Tuple3<String, Boolean, String[]> processParams(Object parameters) {

    List ps = (List) parameters;

    String type = (String) ps.get(0);
    Boolean indexed = (Boolean) ps.get(1);
    String[] params = new String[0];
    if (ps.size() > 2) {
      Object[] oParams = ps.subList(2, ps.size()).toArray();
      params = new String[oParams.length];
      System.arraycopy(oParams, 0, params, 0, oParams.length);
    }
    return new Tuple3(type, indexed, params);
  }

  private String matchRegex(String header, Collection<String> regex) {

    for (String r : regex) {
      if (header.matches(r)) {
        return r;
      }
    }
    return null;
  }

  public Pair<Column, AbstractParser>[] columnAndParsers(String[] headerNames,
      Map<String, ?> columnParams) {

    List<Pair<Column, AbstractParser>> cnp = new ArrayList<>();

    Object parameters;
    int headerCount = 0;
    String newName;
    for (String h : headerNames) {
      if (columnParams != null) {
        if (columnParams.containsKey(h)) {
          parameters = columnParams.get(h);
          Tuple3<String, Boolean, String[]> pars = processParams(parameters);
          cnp.add(ColumnParserFactory.createColumn(h, pars._1(), pars._2(), pars._3()));
        } else {
          String pKey = matchRegex(h, columnParams.keySet());
          if (pKey != null) {
            parameters = columnParams.get(pKey);
            Tuple3<String, Boolean, String[]> pars = processParams(parameters);
            cnp.add(ColumnParserFactory.createColumn(h, pars._1(), pars._2(), pars._3()));
          } else {
            newName = Strings.isNullOrEmpty(h) ? String.format("Column%d", headerCount) : h;
            cnp.add(createDefaultColumnParser(newName));
          }
        }
      } else {
        newName = Strings.isNullOrEmpty(h) ? String.format("Column%d", headerCount) : h;
        cnp.add(createDefaultColumnParser(newName));
      }
      headerCount++;
    }

    return (Pair<Column, AbstractParser>[]) cnp.stream().toArray(Pair[]::new);
  }

  private Pair<Column, AbstractParser> createDefaultColumnParser(String name) {

    return ColumnParserFactory.createColumn(name, "string", false);
  }

  @Override
  public void processStarted(ParsingContext context) {

    ///TODO:Ugly!!!
    if (this.selectedHeaders != null) {
      headerNames = this.selectedHeaders;
    } else {
      headerNames = context.selectedHeaders();
    }

    Pair<Column, AbstractParser>[] columnAndParsers = columnAndParsers(headerNames, columnParams);
    List<AbstractParser> pList =
        Arrays.stream(columnAndParsers).map(p -> p.second).collect(Collectors.toList());

    columns = Arrays.stream(columnAndParsers).map(p -> p.first).collect(Collectors.toList());
    parsers = pList.toArray(new AbstractParser[0]);
//    columnTypes = pList.stream().map(p -> p.elementClass()).collect(Collectors.toList());

    dataContainers = columns.stream().map(c->c.newDataContainer(0)).collect(Collectors.toList());

    columnNames = columns.stream().map(col -> col.name()).toArray(String[]::new);
    columnIndexes = Arrays.stream(columnNames)
        .mapToInt(name -> ArrayHelper.firstIndexOf(name, headerNames))
        .toArray();
    dataFrame = new DataFrame(dataFrameName, columns);
  }

  private static int arraySize(int x) {

    return (int) Math.ceil(Math.pow(GROWTH_BASE, Math.ceil(Math.log(x) / Math.log(GROWTH_BASE))));
  }

  @Override
  public void rowProcessed(String[] row, ParsingContext context) {

    if (row[0].length() > 10) {
      //TODO: Move somewhere more appropriate
      if (row[0].substring(0, 13).equals("Creation Date")) {
        return;
      }
    }

    for (int i = 0; i < row.length; i++) {
      try {
        dataContainers.get(i).add(parsers[i].parse(row[i]));
      } catch (Exception e){
        System.out.println(e);
      }
    }

  }

  @Override
  public void processEnded(ParsingContext context) {

    for (int i=0; i < columns.size(); i++) {

      try {
        Column col = columns.get(i);
        AbstractCollection dc = this.dataContainers.get(i);

        if (dc instanceof ObjectArrayList){
          Object[] vals =((ObjectArrayList) dc).elements();
        }

        col.appendAll(this.dataContainers.get(i));

        if (col instanceof DoubleColumn) {
          ((DoubleColumn) col).buildMatrix();
        }
      } catch (Exception  e) {
          System.out.println(1);
      }
    }

  }


  public DataFrame read() {

    final CsvParser parser = new CsvParser(parserSettings);
    parser.parse(file());
    return dataFrame;
  }
}
