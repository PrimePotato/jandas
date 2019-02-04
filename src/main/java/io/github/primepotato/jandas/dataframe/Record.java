package io.github.primepotato.jandas.dataframe;


import io.github.primepotato.jandas.column.*;

import java.time.LocalDate;
import java.util.*;

public class Record implements Iterator<Record> {

  private final DataFrame dataFrame;
  private final String[] columnNames;
  private final Map<String, DateColumn> dateColumnMap =
      new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
  private final Map<String, DoubleColumn> doubleColumnMap =
      new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
  private final Map<String, IntegerColumn> intColumnMap =
      new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
  private final Map<String, StringColumn> stringColumnMap =
      new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
  private final Map<String, Column> columnMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
  private int rowNumber;

  public Record(DataFrame df) {

    this.dataFrame = df;
    columnNames = df.columns.stream().map(c -> c.name()).toArray(String[]::new);
    rowNumber = 0;
    for (Column column : df.columns) {
      if (column instanceof DoubleColumn) {
        doubleColumnMap.put(column.name(), (DoubleColumn) column);
      }
      if (column instanceof IntegerColumn) {
        intColumnMap.put(column.name(), (IntegerColumn) column);
      }
      if (column instanceof StringColumn) {
        stringColumnMap.put(column.name(), (StringColumn) column);
      }
      columnMap.put(column.name(), column);
    }
  }

  @Override
  public boolean hasNext() {

    return rowNumber < dataFrame.rowCount() - 1;
  }

  public String[] rowString(){
    String [] rs = new String[dataFrame.columnCount()];
    for (int i =0; i<dataFrame.columnCount(); ++i){
      rs[i] = this.getString(i);
    }
    return rs;
  }

  /**
   * Returns a list containing the names of each column in the row
   */
  public List<String> columnNames() {

    return Arrays.asList(dataFrame.columnNames());
  }

  public int columnCount() {

    return dataFrame.columnCount();
  }

  @Override
  public Record next() {

    rowNumber++;
    return this;
  }

  public double getDouble(String columnName) {

    return doubleColumnMap.get(columnName).getDouble(rowNumber);
  }

  public double getDouble(int columnIndex) {

    return getDouble(columnNames[columnIndex]);
  }

  public int getInt(String columnName) {

    return intColumnMap.get(columnName).getInt(rowNumber);
  }

  public int getInt(int columnIndex) {

    return getInt(columnNames[columnIndex]);
  }

  public String getString(String columnName) {
    return columnMap.get(columnName).getString(rowNumber);
  }

  public String getString(int columnIndex) {

    return getString(columnNames[columnIndex]);
  }

  public LocalDate getDate(String columnName) {

    return dateColumnMap.get(columnName).getDate(rowNumber);
  }

  public LocalDate getDate(int columnIndex) {

    return dateColumnMap.get(columnNames[columnIndex]).getDate(rowNumber);
  }

  public void at(int rowNumber) {

    this.rowNumber = rowNumber;
  }

  public int getRowNumber() {

    return rowNumber;
  }

  Record __offset(int rowNumber) {

    this.rowNumber = rowNumber;
    return this;
  }

  public <T> T getObject(String columnName) {

    return (T) columnMap.get(columnName).getObject(rowNumber);
  }

  public <T> T getObject(int columnIndex) {

    return (T) columnMap.get(columnNames[columnIndex]).getObject(rowNumber);
  }

}
