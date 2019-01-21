package com.lchclearnet.table.io.csv;

import com.google.common.base.Strings;
import com.lchclearnet.fx.AppConfig;
import com.lchclearnet.table.Column;
import com.lchclearnet.table.column.ColumnFactory;
import com.lchclearnet.table.Table;
import com.lchclearnet.table.column.parsers.AbstractParser;
import com.lchclearnet.utils.ArrayHelper;
import com.lchclearnet.utils.Config;
import com.lchclearnet.utils.FileHelper;
import com.lchclearnet.utils.Pair;
import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.RowProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.lang.reflect.Array;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.lchclearnet.table.column.ColumnType.SKIP;

public class TableCsvReader implements RowProcessor {

  private static Logger logger = LogManager.getLogger();

  private CsvParserSettings parserSettings;

  private String tableName;
  private boolean header;
  private String dir;
  private String filePattern;
  private String[] missingValueStrings;
  private Map<String, ?> columnParams;
  //*************************************************************************************************************//
  //************************** Univocity CSV Parsing
  // ************************************************************//
  //*************************************************************************************************************//
  private String[] headerNames;
  private String[] columnNames;
  private int[] columnIndexes;
  private Column[] columns;
  private AbstractParser[] parsers;
  private Table table;

  public TableCsvReader() {

    tableName = "";
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

  public static TableCsvReader of(Config config, String... accessorProps) {

    TableCsvReader tableCsvReader = new TableCsvReader();

    String[] props = new String[accessorProps.length + 1];
    System.arraycopy(accessorProps, 0, props, 0, accessorProps.length);

    //required fields
    props[accessorProps.length] = AppConfig.DIR;
    tableCsvReader.dir = config.get(props);

    props[accessorProps.length] = AppConfig.FILE_PATTERN;
    tableCsvReader.filePattern = config.get(props);

    //optional fields
    props[accessorProps.length] = AppConfig.TABLE_NAME;
    if (config.has(props)) {
      tableCsvReader.tableName = config.get(props);
    }

    props[accessorProps.length] = AppConfig.HEADER;
    if (config.has(props)) {
      tableCsvReader.header = config.get(props);
    }

    props[accessorProps.length] = AppConfig.COLUMNS;
    if (config.has(props)) {
      tableCsvReader.columnParams = config.get(props);
    }

    props[accessorProps.length] = AppConfig.PARSER_SETTINGS;
    if (config.has(props) && config.get(props) instanceof Map) {
      configureParserSettings(tableCsvReader, config, props);
    }

    return tableCsvReader;
  }

  private static void configureParserSettings(TableCsvReader tableCsvReader, Config config,
      String[] props) {
    //parser settings field
    Map<String, ?> parserSettings = config.get(props);
    List<String> selectedFields = (List<String>) parserSettings.get("selectFields");
    if (selectedFields != null) {
      tableCsvReader.parserSettings.selectFields(
          selectedFields.toArray(new String[selectedFields.size()]));
    }
    List<String> excludeFields = (List<String>) parserSettings.get("excludeFields");
    if (excludeFields != null) {
      tableCsvReader.parserSettings.excludeFields(
          excludeFields.toArray(new String[excludeFields.size()]));
    }
  }

  //*************************************************************************************************************//
  //************************** Accessors
  // ************************************************************************//
  //*************************************************************************************************************//
  public TableCsvReader setParserSettings(CsvParserSettings parserSettings) {

    this.parserSettings = parserSettings;
    return this;
  }

  public TableCsvReader setTableName(String tableName) {

    this.tableName = tableName;
    return this;
  }

  public TableCsvReader setHeader(boolean header) {

    this.header = header;
    return this;
  }

  public TableCsvReader setDir(String dir) {

    this.dir = dir;
    return this;
  }

  public TableCsvReader setFilePattern(String filePattern) {

    this.filePattern = filePattern;
    return this;
  }

  public TableCsvReader setMissingValueStrings(String[] missingValueStrings) {

    this.missingValueStrings = missingValueStrings;
    return this;
  }

  public void setColumnParams(Map<String, ?> columnParams) {

    this.columnParams = columnParams;
  }

  //*************************************************************************************************************//
  //************************** Csv Options
  // **********************************************************************//
  //*************************************************************************************************************//
  public File file() {

    Path parent = Paths.get(dir);
    Queue<Path> paths =
        FileHelper.find(parent, filePattern, new PriorityQueue<>(11, Collections.reverseOrder()));
    return paths.poll().toFile();
  }

  public String tableName() {

    return tableName;
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

  public Pair<Column, AbstractParser>[] columnAndParsers(String[] headerNames,
      Map<String, ?> columnParams) {

    final Pair<Column, AbstractParser>[] columnAndParsers =
        (Pair<Column, AbstractParser>[]) Array.newInstance(Pair.class, headerNames.length);

    if (columnParams != null) {

      //first we try matching a the exact column name
      Object parameters;
      String type;
      String[] params;
      for (int i = 0; i < headerNames.length; ++i) {
        parameters = columnParams.get(headerNames[i]);
        //first we try the to match the exact column name
        if (parameters != null) {
          if (parameters instanceof String) {
            type = (String) parameters;
            params = new String[0];
          } else {
            List<String> ps = (List) parameters;
            type = ps.get(0);
            params = new String[ps.size() - 1];
            for (int j = 0; j < params.length; ++j) {
              params[j] = ps.get(j + 1);
            }
          }
          columnAndParsers[i] = new Pair(ColumnFactory.createColumn(headerNames[i], type),
              ColumnFactory.columnParser(type, params));
        }
      }

      //then we try matching a the regular expression with the column name
      columnParams.forEach((colRegex, cParameters) -> {
        String cType, cName;
        String[] cParams;
        for (int k = 0; k < columnAndParsers.length; ++k) {
          if (columnAndParsers[k] != null) {
            continue;
          }
          cName = headerNames[k];
          if (cName.matches(colRegex)) {
            if (cParameters instanceof String) {
              cType = (String) cParameters;
              cParams = new String[0];
            } else {
              List<String> ps = (List) cParameters;
              cType = ps.get(0);
              cParams = new String[ps.size() - 1];
              for (int l = 0; l < cParams.length; ++l) {
                cParams[l] = ps.get(l + 1);
              }
            }
            columnAndParsers[k] = new Pair(ColumnFactory.createColumn(headerNames[k], cType),
                ColumnFactory.columnParser(cType, cParams));
          }
        }
      });
    }

    //finish with assigning default column name to empty header and a String Column Types to the
    // Rest
    for (int m = 0; m < columnAndParsers.length; ++m) {
      if (columnAndParsers[m] != null) {
        continue;
      }
      String columnName = headerNames[m];
      columnName = Strings.isNullOrEmpty(columnName) ? String.format("Column%d", m) : columnName;
      columnAndParsers[m] = new Pair(ColumnFactory.createColumn(columnName, "string"),
          ColumnFactory.columnParser("string"));
    }

    return Arrays.stream(columnAndParsers).filter(p -> p.first.type() != SKIP).toArray(Pair[]::new);
  }

  @Override
  public void processStarted(ParsingContext context) {

    headerNames = context.headers();
    Pair<Column, AbstractParser>[] columnAndParsers = columnAndParsers(headerNames, columnParams);
    columns = Arrays.stream(columnAndParsers).map(p -> p.first).toArray(Column[]::new);
    parsers = Arrays.stream(columnAndParsers).map(p -> p.second).toArray(AbstractParser[]::new);
    // getObject the column names without the skipped fields
    columnNames = Arrays.stream(columns).map(col -> col.name()).toArray(String[]::new);
    // getObject the index in the original table, which includes skipped fields
    columnIndexes = Arrays.stream(columnNames)
        .mapToInt(name -> ArrayHelper.firstIndexOf(name, headerNames))
        .toArray();
    table = Table.create(tableName()).addColumns(columns);
  }

  @Override
  public void rowProcessed(String[] row, ParsingContext context) {

    if (row.length == headerNames.length) {
      // for each column that we're including (not skipping)
      int cellIndex = 0;
      int columnIndex;
      for (int i = 0; i < columnIndexes.length; i++) {
        columnIndex = columnIndexes[i];
        Column<?> column = table.column(cellIndex);
        try {
          String value = row[columnIndex];
          column.appendCell(value, parsers[i]);
        } catch (Exception e) {
          throw new AddCellToColumnException(e, columnIndex, context.currentLine(), columnNames,
              row);
        }
        cellIndex++;
      }
    } else if (row.length < headerNames.length) {
      if (row.length == 1 && Strings.isNullOrEmpty(row[0])) {
        logger.warn("Invalid CSV file. Record[{}] is empty. Continuing.", context.currentLine());
      } else {
        logger.warn("Invalid CSV file. Record[{}] is too short.", context.currentLine());
        //throw new ColumnCellException(iobex, 0, rowNumber, columnNames, row);
      }
    } else {
      logger.warn("Invalid CSV file. Record[{}] is too long.", context.currentLine());
    }
  }

  @Override
  public void processEnded(ParsingContext context) {

  }

  //*************************************************************************************************************//
  //************************** Table loading
  // ********************************************************************//
  //*************************************************************************************************************//
  public Table read() {

    final CsvParser parser = new CsvParser(parserSettings);
    parser.parse(file());
    return table;
  }
}
