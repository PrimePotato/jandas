package io.github.primepotato.jandas.column;


import io.github.primepotato.jandas.index.ColIndex;
import io.github.primepotato.jandas.index.DoubleIndex;
import io.github.primepotato.jandas.io.parsers.AbstractParser;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import org.ejml.data.DMatrixRBlock;
import org.ejml.data.DMatrixRMaj;
import org.ejml.data.Matrix;
import org.ejml.data.MatrixType;
import org.ejml.simple.SimpleBase;

import java.util.AbstractCollection;
import java.util.Set;
import java.util.stream.Collectors;

public class DoubleColumn extends SimpleBase implements Column {

    private DMatrixRMaj data;

    public ColIndex index;
    public String name;
    public Class dataType;
    public Boolean indexed;

    public DoubleColumn(String name, Boolean indexed, double[] values) {

        this.indexed = indexed;
        data = new DMatrixRMaj(values);
        this.name = name;
        dataType = Double.class;
        this.setMatrix(this.data);
    }

    public DoubleColumn(String name, Boolean indexed, DoubleArrayList values) {
        this(name, indexed, values.toArray(new double[0]));
    }

    public DoubleColumn(String name, Boolean indexed, DMatrixRBlock mat) {
        this(name, indexed, mat.getData());
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
                    "Error adding value to column " + name + ": " + e.getMessage());
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

    public String name() {

        return name;
    }

    public ColIndex index() {

        return index;
    }

    @Override
    public void appendAll(AbstractCollection vals) {
        DoubleArrayList d = (DoubleArrayList) vals;
        double [] out = new double[d.size()-1];
        d.getElements(0,out,0, d.size()-1);
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

    public double[] getRows(int[] rows) {

        double[] res = new double[rows.length];
        for (int i = 0; i < rows.length; i++) {
            res[i] = getObject(rows[i]);
        }
        return res;
    }

    public Column append(double dbl) {
        double[] d = new double[data.numRows-1];
        double[] old = data.getData();
        System.arraycopy(old, 0, d, 0, old.length);
        d[data.numRows] = dbl;
        data = new DMatrixRMaj(d);
        return this;
    }

    public Column append(double[] dbl) {
        double[] d = new double[data.numRows + dbl.length - 1];
        double[] old = data.getData();
        System.arraycopy(old, 0, d, 0, old.length);
        System.arraycopy(dbl, 0, d, old.length, d.length);
        data = new DMatrixRMaj(d);
        return this;
    }

    @Override
    public int size() {

        return data.numRows;
    }

    @Override
    public DoubleColumn subColumn(String name, int[] aryMask) {

        return new DoubleColumn(name, indexed, getRows(aryMask));
    }

    @Override
    public double[] rawData() {

        return data.getData();
    }

    @Override
    protected DoubleColumn createMatrix(int i, int j, MatrixType matrixType) {

        return new DoubleColumn(this.name + "new", indexed, new DoubleArrayList());
    }


    @Override
    protected DoubleColumn wrapMatrix(Matrix matrix) {

        return new DoubleColumn(this.name + "new", indexed, (DMatrixRBlock) matrix);
    }
}
