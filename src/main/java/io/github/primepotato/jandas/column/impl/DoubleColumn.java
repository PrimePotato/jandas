package io.github.primepotato.jandas.column.impl;


import io.github.primepotato.jandas.column.Column;
import io.github.primepotato.jandas.header.Heading;
import io.github.primepotato.jandas.index.ColIndex;
import io.github.primepotato.jandas.index.impl.DoubleIndex;
import io.github.primepotato.jandas.io.parsers.AbstractParser;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import lombok.Getter;
import org.ejml.data.DMatrixRBlock;
import org.ejml.data.DMatrixRMaj;
import org.ejml.data.Matrix;
import org.ejml.data.MatrixType;
import org.ejml.simple.SimpleBase;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class DoubleColumn extends SimpleBase implements Column<Double> {
    //TODO: Tidy up this class, all over the place

    private DMatrixRMaj data;
    public static final double DEFAULT_MISSING_VALUE_INDICATOR = Double.NaN;
    public ColIndex index;
    @Getter
    public Heading heading;
    public Class dataType;
    public Boolean indexed;

    public DoubleColumn(Heading heading, Boolean indexed, double[] values) {
        this.indexed = indexed;
        data = new DMatrixRMaj(values);
        this.heading = heading;
        dataType = Double.class;
        this.setMatrix(this.data);
    }

    public DoubleColumn(Heading heading, Boolean indexed, DoubleArrayList values) {
        this(heading, indexed, values.toArray(new double[0]));
    }

    public DoubleColumn(String name, Boolean indexed, double[] values) {
        this(new Heading(name), indexed, values);
    }

    public DoubleColumn(String name, Boolean indexed, DoubleArrayList values) {
        this(name, indexed, values.toArray(new double[0]));
    }

    public DoubleColumn(Heading heading, Boolean indexed, DMatrixRBlock mat) {
        this(heading, indexed, mat.getData());
    }

    public void rebuildIndex(double[] vals) {

        index = buildIndex(vals);
    }

    public DoubleIndex buildIndex(double[] vals) {

        return new DoubleIndex(vals);
    }

    public double getDouble(int row) {
        return data.unsafe_get(row, 0);
    }

    public Double getObject(int row) {
        return data.unsafe_get(row, 0);
    }

    public String name() {
        return heading.toString();
    }

    @Override
    public String getString(int row) {

        return String.valueOf(getDouble(row));
    }

    @Override
    public void appendString(String value, AbstractParser<?> parser) {

        try {
            append(parser.parseDouble(value));
        } catch (final NumberFormatException e) {
            throw new NumberFormatException(
                    "Error adding value to column " + heading + ": " + e.getMessage());
        }
    }

    public boolean unique() {

        return index.unique();
    }

    public Set uniqueSet() {

        return index.positions()
                .values()
                .stream()
                .map(x -> getObject(x.getInt(0)))
                .collect(Collectors.toSet());
    }

    @Override
    public String cleanName() {
        return heading.toString().replaceAll("[^A-Za-z0-9]", "");
    }

    public ColIndex getIndex() {

        return index;
    }

    @Override
    public void appendAll(Collection vals) {
        DoubleArrayList d = (DoubleArrayList) vals;
        double[] out = new double[d.size() - 1];
        d.getElements(0, out, 0, d.size() - 1);
        data = new DMatrixRMaj(out);
    }

    @Override
    public DoubleArrayList newDataContainer(int size) {

        return new DoubleArrayList(size);
    }

    @Override
    public Double firstValue() {

        int idx = 0;
        Double t = null;
        while (t == null) {
            t = getObject(idx);
            idx++;
        }
        return t;
    }

    @Override
    public void append(Double val) {
        append((double)val);
    }

    @Override
    public boolean equals(Object other) {
        try {
            return Arrays.equals((this.getClass().cast(other)).rawData(), rawData());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Column createEmpty() {
        return new DoubleColumn(heading, false, new double[0]);
    }

    @Override
    public Object getMissingValue() {
        return DEFAULT_MISSING_VALUE_INDICATOR;
    }

    public double[] getRows(int[] rows) {

        double[] res = new double[rows.length];
        for (int i = 0; i < rows.length; i++) {
            if (rows[i] == Integer.MIN_VALUE) {
                res[i] = DEFAULT_MISSING_VALUE_INDICATOR;
            } else {
                res[i] = getObject(rows[i]);
            }
        }
        return res;
    }

    public DoubleColumn append(double dbl) {
        double[] d = new double[data.numRows + 1];
        double[] old = data.getData();
        System.arraycopy(old, 0, d, 0, old.length);
        d[data.numRows] = dbl;
        data = new DMatrixRMaj(d);
        return this;
    }

    public DoubleColumn append(double[] dbl) {
        double[] d = new double[data.numRows + dbl.length - 1];
        double[] old = data.getData();
        System.arraycopy(old, 0, d, 0, old.length);
        System.arraycopy(dbl, 0, d, old.length, d.length);
        data = new DMatrixRMaj(d);
        return this;
    }

    @Override
    public void rebuildIndex() {

    }

    @Override
    public int size() {

        return data.numRows;
    }

    @Override
    public DoubleColumn subColumn(Heading heading, int[] aryMask) {

        return new DoubleColumn(heading, indexed, getRows(aryMask));
    }

    @Override
    public DoubleColumn subColumn(String name, int[] aryMask) {

        return new DoubleColumn(name, indexed, getRows(aryMask));
    }

    public Double[] rawData() {
        return Arrays.stream(data.getData()).boxed().toArray(Double[]::new);
    }

    public double[] getData() {
        return data.getData();
    }

    @Override
    protected DoubleColumn createMatrix(int i, int j, MatrixType matrixType) {

        return new DoubleColumn(this.heading, indexed, new DoubleArrayList());
    }


    @Override
    protected DoubleColumn wrapMatrix(Matrix matrix) {

        return new DoubleColumn(this.heading, indexed, (DMatrixRBlock) matrix);
    }

    @Override
    public String toString(){
        return name() + rawData().toString();
    }

}
