package io.github.primepotato.jandas.dataframe;


import io.github.primepotato.jandas.column.Column;
import io.github.primepotato.jandas.column.impl.DoubleColumn;
import io.github.primepotato.jandas.column.impl.IntegerColumn;
import io.github.primepotato.jandas.column.impl.ObjectColumn;
import io.github.primepotato.jandas.filter.DataFrameFilter;
import io.github.primepotato.jandas.grouping.DataFrameGroupBy;
import io.github.primepotato.jandas.header.Header;
import io.github.primepotato.jandas.header.Heading;
import io.github.primepotato.jandas.index.ColIndex;
import io.github.primepotato.jandas.index.meta.JoinType;
import io.github.primepotato.jandas.index.meta.MetaIndex;
import io.github.primepotato.jandas.index.utils.IndexUtils;
import io.github.primepotato.jandas.io.DataFramePrinter;
import io.github.primepotato.jandas.io.csv.DataFrameCsvWriter;
import io.github.primepotato.jandas.io.sql.containers.ResultSetContainer;
import io.github.primepotato.jandas.utils.PrintType;
import org.apache.commons.lang3.tuple.Pair;
import org.ejml.equation.Equation;

import java.io.ByteArrayOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static io.github.primepotato.jandas.io.sql.SqlReader.resultSetToContainers;

@SuppressWarnings("unchecked")
public class DataFrame implements Iterable<Record> {

    public List<Column> columns;
    public Header header;
    public String name;

    public DataFrame(ResultSet resultSet) throws SQLException {
        this("", resultSetToContainers(resultSet).stream().map(ResultSetContainer::toColumn).collect(Collectors.toList()));
    }

    public DataFrame(String name, List<Column> cols) {
        this.name = name;
        columns = cols;
        List<Heading> headers = cols.stream().map(Column::heading).collect(Collectors.toList());
        header = new Header(headers);
    }

    @Override
    public boolean equals(Object other) {
        for (int i = 0; i < columns.size(); ++i) {
            if (!columns.get(i).equals(columns.get(i))) {
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

        return this.columns.stream().map(c -> c.heading()).toArray(String[]::new);
    }

    public String name() {

        return name;
    }

    public int rowCount() {

        return columns.get(0).size();
    }

    public void addColumn(Column col) {

        columns.add(col);
        header.addKey(col.heading());
    }

    public void createColumn(String name, String equation) {
        Equation eq = this.equation();
        DoubleColumn dc = new DoubleColumn(name, false, new double[0]);
        eq.alias(dc.getMatrix(), dc.cleanName());
        eq.process(name + " = " + equation);
        this.addColumn(dc);
    }

    public DataFrame select(String name, Heading... colHeaders) {

        List<Heading> cols = Arrays.stream(colHeaders).collect(Collectors.toList());
        return new DataFrame(name, getColumns(cols, Column.class));
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

        return (T) columns.get(header.indexOf(colName));
    }

    public <T extends Column> T column(Heading colName) {

        return (T) columns.get(header.indexOf(colName));
    }

    public Column column(int idx, Class<? extends Column> cls) {

        return cls.cast(columns.get(idx));
    }

    public List<Column> columns() {

        return columns;
    }

    private String toPrintString(int maxRows, PrintType pt) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataFramePrinter dfp = new DataFramePrinter(maxRows, pt, baos);
        dfp.print(this);
        return new String(baos.toByteArray());
    }

    public void head() {
        this.head(20);
    }

    public void head(int maxRows) {
        this.print(maxRows, PrintType.HEAD);
    }

    public void tail(int maxRows) {
        this.print(maxRows, PrintType.TAIL);
    }

    public void tail() {
        this.tail(20);
    }

    public void print() {
        this.print(20, PrintType.BOTH);
    }

    public void print(int maxRows, PrintType pt) {
        if (wellFormed()) {
            System.out.println(toPrintString(maxRows, pt));
        } else {
            System.out.println("Cannot print non well formed data frame");
        }
    }

    Column getColumn(String name) {

        return column(header.indexOf(name));
    }

    List<DoubleColumn> getDoubleColumns() {

        return columns.stream().filter(x -> x instanceof DoubleColumn).map(x -> (DoubleColumn) x).collect(Collectors.toList());
    }


    <T extends Column> T getColumn(Heading name, Class<T> tClass) {

        return tClass.cast(column(name));
    }

    <T extends Column> List<T> getColumns(List<Heading> names, Class<T> tClass) {

        return names.stream().map(x -> getColumn(x, tClass)).collect(Collectors.toList());
    }

    List<Column> getColumns(List<String> names) {

        return names.stream().map(x -> getColumn(x)).collect(Collectors.toList());
    }

    public DataFrameGroupBy groupBy(List<Heading> grpCols, List<Heading> aggCols) {

        List<Column> gCols = getColumns(grpCols, Column.class);
        List<DoubleColumn> aCols = getColumns(aggCols, DoubleColumn.class);
        return new DataFrameGroupBy(gCols, aCols);
    }

    public static MetaIndex buildMetaIndex(List<Column> grpCol) {

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
            joinCols.add(c.subColumn(c.heading().newKeyHeading("L"), left));
        }
        for (Column c : dfRight.columns) {
            joinCols.add(c.subColumn(c.heading().newKeyHeading("R"), right));
        }

        return new DataFrame("Joined" + name, joinCols);
    }


    public DataFrame quickJoin(List<String> joinHeaders, DataFrame other, JoinType jt) {
        if (jt == JoinType.RIGHT) {
            return other.quickJoin(joinHeaders, this, JoinType.LEFT);
        }

        MetaIndex thisMi = buildMetaIndex(getColumns(joinHeaders));
        MetaIndex otherMi = buildMetaIndex(other.getColumns(joinHeaders));
        int[][] qjAry = IndexUtils.quickJoin(thisMi, otherMi, jt);
        return resolveJoin(Pair.of(true, qjAry), this, other);
    }

    public DataFrame join(List<String> joinHeaders, DataFrame other, JoinType jt) {
        if (jt == JoinType.RIGHT) {
            return other.join(joinHeaders, this, JoinType.LEFT);
        }

        MetaIndex thisMi = buildMetaIndex(getColumns(joinHeaders));
        MetaIndex otherMi = buildMetaIndex(other.getColumns(joinHeaders));
        Pair<Boolean, int[][]> qjAry = IndexUtils.join(thisMi, otherMi, jt);
        return resolveJoin(qjAry, this, other);
    }

    public Equation equation() {
        Equation eq = new Equation();
        for (DoubleColumn dc : getDoubleColumns()) {
            String cn = dc.cleanName();
            eq.alias(dc.getMatrix(), cn);
        }
        return eq;
    }

    public void addRecord(Record rec) {

        int i = 0;
        for (Column col : columns) {
            if (col instanceof DoubleColumn) {
                ((DoubleColumn) col).append(rec.getDouble(i));
            } else if (col instanceof IntegerColumn) {
                ((IntegerColumn) col).append(rec.getInt(i));
            } else if (col instanceof ObjectColumn) {
                if (((ObjectColumn) col).dataType == LocalDate.class) {
                    ((ObjectColumn) col).append(rec.getObject(i));
                } else if (((ObjectColumn) col).dataType == String.class) {
                    ((ObjectColumn) col).append(rec.getString(i));
                }
            }
            ++i;
        }

    }

    public List<Record> getRecords(Collection<Integer> row) {
        return row.stream().map(this::getRecord).collect(Collectors.toList());
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("header", this.header.toList().toArray());

        Map<Heading, Object> m = new HashMap<>();
        for (Column c : columns()) {
            m.put(c.heading(), c.rawData());
        }
        map.put("columns", m);
        return map;
    }

    public Record getRecord(int row) {
        Record rec = new Record(DataFrame.this);
        rec.rowNumber = row;
        return rec;
    }

    public void toCsv(String fPath) {
        DataFrameCsvWriter.toCsv(this, fPath);
    }

    public DataFrame filter(Predicate<Record> predicate) {
        DataFrameFilter dataFrameFilter = new DataFrameFilter(predicate);
        return dataFrameFilter.apply(this);
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
