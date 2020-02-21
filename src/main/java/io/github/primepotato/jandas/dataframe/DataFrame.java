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
import io.github.primepotato.jandas.io.containers.ResultSetContainer;
import io.github.primepotato.jandas.utils.PrintType;
import lombok.Getter;
import lombok.Setter;
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


@Getter
@Setter
public class DataFrame extends ArrayList<Column> {

    private Header header;
    private String name;

    public DataFrame(ResultSet resultSet) throws SQLException {
        this("", resultSetToContainers(resultSet).stream().map(ResultSetContainer::toColumn).collect(Collectors.toList()));
    }

    public DataFrame(String name, Collection<Column> columns) {
        super(columns);
        this.name = name;
        header = new Header(columns.stream().map(x -> x.getHeading().toString()).toArray(String[]::new));
    }

    public boolean wellFormed() {
        for (Column c : this) {
            if (rowCount() != c.size()) {
                return false;
            }
        }
        return true;
    }

    public int rowCount() {
        if (this.size() == 0) {
            return 0;
        }
        return this.get(0).size();
    }

    @Override
    public boolean add(Column col) {
        header.add(col.cleanName());
        return super.add(col);
    }

    public void createColumn(String name, String equation) {
        Equation eq = this.equation();
        DoubleColumn dc = new DoubleColumn(name, false, new double[0]);
        eq.alias(dc.getMatrix(), dc.cleanName());
        eq.process(name + " = " + equation);
        this.add(dc);
    }

    public DataFrame select(String name, String... colNames) {
        List<String> cols = Arrays.stream(colNames).collect(Collectors.toList());
        return new DataFrame(this.name, getColumns(cols, Column.class));
    }

    public String getString(int row, int col) {
        return column(col).getString(row);
    }


    public <T extends Column> T column(int idx) {
        return (T) this.get(idx);
    }

    public <T extends Column> T column(String colName) {
        return (T) this.get(header.indexOf(new Heading(colName)));
    }

    public Column column(int idx, Class<? extends Column> cls) {
        return cls.cast(this.get(idx));
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
        return this.stream().filter(x -> x instanceof DoubleColumn).map(x -> (DoubleColumn) x).collect(Collectors.toList());
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

    public static MetaIndex buildMetaIndex(List<Column> grpCol) {
        List<ColIndex> colIdxs = grpCol.stream().map(Column::getIndex).collect(Collectors.toList());
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
        for (Column c : dfLeft) {
            joinCols.add(c.subColumn(c.cleanName() + "L", left));
        }
        for (Column c : dfRight) {
            joinCols.add(c.subColumn(c.cleanName() + "R", right));
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

    public Equation equation() {
        Equation eq = new Equation();
        for (DoubleColumn dc : getDoubleColumns()) {
            String cn = dc.cleanName();
            eq.alias(dc.getMatrix(), cn);
        }
        return eq;
    }

    public void addRecord(RecordSet rec) {
        int i = 0;
        for (Column col : this) {
            if (col instanceof DoubleColumn) {
                ((DoubleColumn) col).append(rec.getDouble(i));
            } else if (col instanceof IntegerColumn) {
                ((IntegerColumn) col).append(rec.getInt(i));
            } else if (col instanceof ObjectColumn) {
                if (((ObjectColumn) col).getElementClass() == LocalDate.class) {
                    ((ObjectColumn) col).add(rec.getObject(i));
                } else if (((ObjectColumn) col).getElementClass() == String.class) {
                    ((ObjectColumn) col).add(rec.getString(i));
                }
            }
            ++i;
        }

    }

    public List<RecordSet> getRecords(Collection<Integer> row) {
        return row.stream().map(this::getRecord).collect(Collectors.toList());
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("header", this.header.toArray());

        Map<String, Object> m = new HashMap<>();
        for (Column c : this) {
            m.put(c.cleanName(), c.rawData());
        }
        map.put("columns", m);
        return map;
    }

    public RecordSet getRecord(int row) {
        RecordSet rec = new RecordSet(DataFrame.this);
        rec.setRowNumber(row);
        return rec;
    }

    public void toCsv(String fPath) {
        DataFrameCsvWriter.toCsv(this, fPath);
    }

    public DataFrame filter(Predicate<RecordSet> predicate) {
        DataFrameFilter dataFrameFilter = new DataFrameFilter(predicate);
        return dataFrameFilter.apply(this);
    }

    public Iterator<RecordSet> recordSet() {

        return new Iterator<RecordSet>() {
            final private RecordSet record = new RecordSet(DataFrame.this, -1);

            @Override
            public RecordSet next() {
                return record.next();
            }

            @Override
            public boolean hasNext() {
                return record.hasNext();
            }
        };
    }

}
