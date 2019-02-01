package io.github.primepotato.jandas.dataframe;


import io.github.primepotato.jandas.column.Column;
import io.github.primepotato.jandas.column.DoubleColumn;
import io.github.primepotato.jandas.index.ColIndex;
import io.github.primepotato.jandas.index.IndexUtils;
import io.github.primepotato.jandas.index.MetaIndex;
import io.github.primepotato.jandas.utils.DataFramePrinter;
import io.github.primepotato.jandas.utils.DoubleAggregateFunc;
import it.unimi.dsi.fastutil.ints.Int2DoubleOpenHashMap;

import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.stream.Collectors;

public class DataFrame implements Iterable<Record> {

  public List<Column> columns;
  public List<String> headers;
  public String name;

  public DataFrame(String name, List<Column> cols) {

    this.name = name;
    columns = cols;
    headers = cols.stream().map(Column::name).collect(Collectors.toList());
  }

  public boolean wellFormed() {

    for (Column c : columns) {
      if (rowCount() != c.size()) {
        return false;
      }
    }
    return true;
  }

  public int columnCount() {

    return columns.size();
  }

  public String[] columnNames() {

    return this.columns.stream().map(c -> c.name()).toArray(String[]::new);
  }

  public String name() {

    return name;
  }

  public int rowCount() {

    return columns.get(0).size();
  }

  public void addColumn(Column col) {

    columns.add(col);
    headers.add(col.name());
  }

  public DataFrame select(String name, String... colNames) {

    List<String> cols = Arrays.stream(colNames).collect(Collectors.toList());
    return new DataFrame(this.name, getColumns(cols, Column.class));
  }

  public String getString(int row, int col) {

    return column(col).getString(row);
  }

  public <T> T getObject(int r, int c) {

    return (T) column(c).getObject(r);
  }

  public <T extends Column> T column(int idx) {

    return (T) columns.get(idx);
  }

  public <T extends Column> T column(String colName) {

    return (T) columns.get(headers.indexOf(colName));
  }

  public Column column(int idx, Class<? extends Column> cls) {

    return cls.cast(columns.get(idx));
  }

  public List<Column> columns() {

    return columns;
  }

  private String toPrintString(int maxRows) {

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataFramePrinter dfp = new DataFramePrinter(maxRows, baos);
    dfp.print(this);
    return new String(baos.toByteArray());
  }

  public void print(int maxRows) {

    System.out.println(toPrintString(maxRows));
  }

  Column getColumn(String name) {

    return column(headers.indexOf(name));
  }

  <T extends Column> T getColumn(String name, Class<T> tClass) {

    return tClass.cast(column(name));
  }

  <T extends Column> List<T> getColumns(List<String> names, Class<T> tClass) {

    return names.stream().map(x -> getColumn(x, tClass)).collect(Collectors.toList());
  }

  public Map<String, Int2DoubleOpenHashMap> groupBy(List<String> grpCols, List<String> aggCols) {

    List<Column> gCols = getColumns(grpCols, Column.class);
    List<DoubleColumn> aCols = getColumns(aggCols, DoubleColumn.class);
    MetaIndex mi = buildMetaIndex(gCols);
    Map<String, Int2DoubleOpenHashMap> results = new HashMap<>();
    for (DoubleColumn dc : aCols) {
      results.put(dc.name, mi.aggregateDouble(dc.rawData(), DoubleAggregateFunc.SUM));
    }
    return results;
  }

  MetaIndex buildMetaIndex(List<Column> grpCol) {

    List<ColIndex> colIdxs = grpCol.stream().map(x -> x.index()).collect(Collectors.toList());
    return new MetaIndex(colIdxs);
  }

  DataFrame resolveJoin(int[][] joinArray, DataFrame dfLeft, DataFrame dfRight) {

    int[] idx = joinArray[0];
    int[] left = joinArray[1];
    int[] right = joinArray[2];

    List<Column> joinCols = new ArrayList<>();
    for (Column c : dfLeft.columns) {
      joinCols.add(c.subColumn(c.name() + "L", left));
    }
    for (Column c : dfRight.columns) {
      joinCols.add(c.subColumn(c.name() + "R", right));
    }

    return new DataFrame("Joined" + name, joinCols);
  }

  public DataFrame quickJoin(List<String> joinHeaders, DataFrame other) {

    List<Column> thisCols = getColumns(joinHeaders, Column.class);
    MetaIndex thisMi = buildMetaIndex(thisCols);
    List<Column> otherCols = other.getColumns(joinHeaders, Column.class);
    MetaIndex otherMi = buildMetaIndex(otherCols);
    int[][] qjAry = IndexUtils.quickJoin(thisMi, otherMi);
    return resolveJoin(qjAry, this, other);
  }

  public Optional<Record> matchFirst(String colName, Object value) {

    Column col = column(colName);
    col.index();
    return Optional.of(new Record(this));
  }

  @Override
  public Iterator<Record> iterator() {

    return new Iterator<Record>() {

      final private Record row = new Record(DataFrame.this);

      @Override
      public Record next() {

        return row.next();
      }

      @Override
      public boolean hasNext() {

        return row.hasNext();
      }
    };
  }

}
