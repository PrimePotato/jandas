package io.github.primepotato.jandas.dataframe;


import io.github.primepotato.jandas.column.Column;
import io.github.primepotato.jandas.column.impl.DoubleColumn;
import io.github.primepotato.jandas.column.impl.IntegerColumn;
import io.github.primepotato.jandas.column.impl.ObjectColumn;
import io.github.primepotato.jandas.header.Heading;

import java.time.LocalDate;
import java.util.*;


@SuppressWarnings("unchecked")
public class Record implements Iterator<Record> {

    public final DataFrame dataFrame;
    private final Heading[] columnHeadings;
    private final Map<Heading, ObjectColumn<LocalDate>> dateColumnMap = new TreeMap<>();
    private final Map<Heading, DoubleColumn> doubleColumnMap = new TreeMap<>();
    private final Map<Heading, IntegerColumn> intColumnMap = new TreeMap<>();
    private final Map<Heading, ObjectColumn<String>> stringColumnMap = new TreeMap<>();
    private final Map<Heading, Column> columnMap = new TreeMap<>();
    public int rowNumber;

    public Record(DataFrame df) {
        this(df, 0);
    }

    public Record(DataFrame df, int rowNumber) {

        this.dataFrame = df;
        columnHeadings = df.columns.stream().map(Column::heading).toArray(Heading[]::new);
        for (Column column : df.columns) {
            if (column instanceof DoubleColumn) {
                doubleColumnMap.put(column.heading(), (DoubleColumn) column);
            }
            if (column instanceof IntegerColumn) {
                intColumnMap.put(column.heading(), (IntegerColumn) column);
            }
            if (column instanceof ObjectColumn) {
                stringColumnMap.put(column.heading(), (ObjectColumn) column);
            }
            columnMap.put(column.heading(), column);
        }
    }

    @Override
    public boolean hasNext() {

        return rowNumber < dataFrame.rowCount() - 1;
    }

    public String[] rowString() {
        String[] rs = new String[dataFrame.columnCount()];
        for (int i = 0; i < dataFrame.columnCount(); ++i) {
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

        return doubleColumnMap.get(new Heading(columnName)).getDouble(rowNumber);
    }

    public double getDouble(Heading columnName) {

        return doubleColumnMap.get(columnName).getDouble(rowNumber);
    }

    public double getDouble(int columnIndex) {

        return getDouble(columnHeadings[columnIndex]);
    }

    public int getInt(Heading columnName) {

        return intColumnMap.get(columnName).getInt(rowNumber);
    }

    public int getInt(String columnName) {

        return intColumnMap.get(new Heading(columnName)).getInt(rowNumber);
    }

    public int getInt(int columnIndex) {

        return getInt(columnHeadings[columnIndex]);
    }

    public String getString(Heading columnName) {
        return columnMap.get(columnName).getString(rowNumber);
    }

    public String getString(int columnIndex) {

        return getString(columnHeadings[columnIndex]);
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

    public <T> T getObject(Heading columnName) {

        return (T) columnMap.get(columnName).getObject(rowNumber);
    }

    public <T> T getObject(int columnIndex) {

        return (T) columnMap.get(columnHeadings[columnIndex]).getObject(rowNumber);
    }

}
