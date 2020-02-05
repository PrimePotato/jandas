package io.github.primepotato.jandas.dataframe;


import io.github.primepotato.jandas.column.Column;
import io.github.primepotato.jandas.column.impl.DoubleColumn;
import io.github.primepotato.jandas.column.impl.IntegerColumn;
import io.github.primepotato.jandas.column.impl.ObjectColumn;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.*;


@SuppressWarnings("unchecked")
public class RecordSet implements Iterator<RecordSet> {

  public final DataFrame dataFrame;
  private final String[] columnNames;
  private final Map<String, ObjectColumn<LocalDate>> dateColumnMap =
      new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
  private final Map<String, DoubleColumn> doubleColumnMap =
      new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
  private final Map<String, IntegerColumn> intColumnMap =
      new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
  private final Map<String, ObjectColumn<String>> stringColumnMap =
      new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
  private final Map<String, Column> columnMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

  @Getter @Setter
  private int rowNumber;

  public RecordSet(DataFrame df){
    this(df,0);
  }

  public RecordSet(DataFrame df, int rowNumber) {

    this.dataFrame = df;
    this.rowNumber = rowNumber;
    columnNames = df.stream().map(Column::name).toArray(String[]::new);
    for (Column column : df) {
      if (column instanceof DoubleColumn) {
        doubleColumnMap.put(column.name(), (DoubleColumn) column);
      }
      if (column instanceof IntegerColumn) {
        intColumnMap.put(column.name(), (IntegerColumn) column);
      }
      if (column instanceof ObjectColumn) {
        stringColumnMap.put(column.name(), (ObjectColumn) column);
      }
      columnMap.put(column.name(), column);
    }
  }

  @Override
  public boolean hasNext() {

    return rowNumber < dataFrame.rowCount() - 1;
  }

  public String[] rowString(){
    String [] rs = new String[dataFrame.size()];
    for (int i =0; i<dataFrame.size(); ++i){
      rs[i] = this.getString(i);
    }
    return rs;
  }

  @Override
  public RecordSet next() {

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

  public void at(int rowNumber) {

    this.rowNumber = rowNumber;
  }

  public int getRowNumber() {

    return rowNumber;
  }

  RecordSet __offset(int rowNumber) {

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
