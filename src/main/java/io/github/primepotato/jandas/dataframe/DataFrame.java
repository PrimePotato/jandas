package io.github.primepotato.jandas.dataframe;


import io.github.primepotato.jandas.column.Column;
import io.github.primepotato.jandas.column.DoubleColumn;
import io.github.primepotato.jandas.containers.dynamic.DynamicResultSetContainer;
import io.github.primepotato.jandas.index.ColIndex;
import io.github.primepotato.jandas.index.meta.JoinType;
import io.github.primepotato.jandas.index.utils.IndexUtils;
import io.github.primepotato.jandas.index.meta.MetaIndex;
import io.github.primepotato.jandas.io.csv.DataFrameCsvWriter;
import io.github.primepotato.jandas.utils.DataFramePrinter;
import org.apache.commons.lang3.tuple.Pair;
import org.ejml.equation.Equation;

import java.io.ByteArrayOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static io.github.primepotato.jandas.io.sql.SqlReader.resultSetToContainers;

public class DataFrame implements Iterable<Record> {

    public List<Column> columns;
    public List<String> headers;
    public String name;

    public DataFrame(ResultSet resultSet) throws SQLException {
//        this("", resultSetToContainers(resultSet).stream().map(DynamicResultSetContainer::toColumn).collect(Collectors.toList()));
    }

    public DataFrame(String name, List<Column> cols) {

        this.name = name;
        columns = cols;
        headers = cols.stream().map(Column::name).collect(Collectors.toList());
    }

    public boolean equals(DataFrame other){
        for (int i=0; i<columns.size(); ++i){
            if (!columns.get(i).equals(columns.get(i))){
                return false;
            }
        }
        return true;
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

    public void createColumn(String name, String equation) {
        Equation eq = this.equation();
        DoubleColumn dc = new DoubleColumn(name, false, new double[0]);
        eq.alias(dc.getMatrix(), dc.cleanName());
        eq.process(name + " = " + equation);
        this.addColumn(dc);
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

    public void print() {
        this.print(20);
    }

    public void print(int maxRows) {
        if (wellFormed()) {
            System.out.println(toPrintString(maxRows));
        } else {
            System.out.println("Cannot print non well formed data frame");
        }
    }

    Column getColumn(String name) {

        return column(headers.indexOf(name));
    }

    List<DoubleColumn> getDoubleColumns() {

        return columns.stream().filter(x -> x instanceof DoubleColumn).map(x -> (DoubleColumn) x).collect(Collectors.toList());
    }


    <T extends Column> T getColumn(String name, Class<T> tClass) {

        return tClass.cast(column(name));
    }

    <T extends Column> List<T> getColumns(List<String> names, Class<T> tClass) {

        return names.stream().map(x -> getColumn(x, tClass)).collect(Collectors.toList());
    }

    public DataFrameGroupBy groupBy(List<String> grpCols, List<String> aggCols) {

        List<Column> gCols = getColumns(grpCols, Column.class);
        List<DoubleColumn> aCols = getColumns(aggCols, DoubleColumn.class);
        return new DataFrameGroupBy(gCols, aCols);
    }

    static MetaIndex buildMetaIndex(List<Column> grpCol) {

        List<ColIndex> colIdxs = grpCol.stream().map(x -> x.index()).collect(Collectors.toList());
        return new MetaIndex(colIdxs);
    }

    DataFrame resolveJoin(Pair<Boolean, int[][]> joinData, DataFrame dfLeft, DataFrame dfRight) {

        int[][] joinArray = joinData.getRight();
        int[] left, right;
        if (joinData.getLeft()) {
            left = joinArray[1];
            right = joinArray[2];
        } else {
            right = joinArray[1];
            left = joinArray[2];
        }

        List<Column> joinCols = new ArrayList<>();
        for (Column c : dfLeft.columns) {
            joinCols.add(c.subColumn(c.name() + "L", left));
        }
        for (Column c : dfRight.columns) {
            joinCols.add(c.subColumn(c.name() + "R", right));
        }

        return new DataFrame("Joined" + name, joinCols);
    }


    public DataFrame quickJoin(List<String> joinHeaders, DataFrame other, JoinType jt) {
        if (jt == JoinType.RIGHT) {
            return other.quickJoin(joinHeaders, this, JoinType.LEFT);
        }

        MetaIndex thisMi = buildMetaIndex(getColumns(joinHeaders, Column.class));
        MetaIndex otherMi = buildMetaIndex(other.getColumns(joinHeaders, Column.class));
        int[][] qjAry = IndexUtils.quickJoin(thisMi, otherMi, jt);
        return resolveJoin(Pair.of(true, qjAry), this, other);
    }

    public DataFrame join(List<String> joinHeaders, DataFrame other, JoinType jt) {
        if (jt == JoinType.RIGHT) {
            return other.join(joinHeaders, this, JoinType.LEFT);
        }

        MetaIndex thisMi = buildMetaIndex(getColumns(joinHeaders, Column.class));
        MetaIndex otherMi = buildMetaIndex(other.getColumns(joinHeaders, Column.class));
        Pair<Boolean, int[][]> qjAry = IndexUtils.join(thisMi, otherMi, jt);
        return resolveJoin(qjAry, this, other);
    }

    public Optional<Record> matchFirst(String colName, Object value) {

        Column col = column(colName);
        col.index();
        return Optional.of(new Record(this));
    }

    public Equation equation() {
        Equation eq = new Equation();
        for (DoubleColumn dc : getDoubleColumns()) {
            String cn = dc.cleanName();
            eq.alias(dc.getMatrix(), cn);
        }
        return eq;
    }


    public void toCsv(String fPath) {
        DataFrameCsvWriter.toCsv(this, fPath);
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
