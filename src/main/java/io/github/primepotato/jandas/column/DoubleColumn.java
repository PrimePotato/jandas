package io.github.primepotato.jandas.column;


import io.github.primepotato.jandas.index.ColIndex;
import io.github.primepotato.jandas.index.DoubleIndex;
import io.github.primepotato.jandas.io.parsers.AbstractParser;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import org.ejml.data.DMatrixRBlock;
import org.ejml.data.DMatrixRMaj;
import org.ejml.data.Matrix;
import org.ejml.data.MatrixType;
import org.ejml.ops.ConvertDMatrixStruct;
import org.ejml.simple.SimpleBase;

import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class DoubleColumn extends SimpleBase implements Column {

  private DoubleArrayList data;

  public ColIndex index;
  public String name;
  public Class dataType;
  public Boolean indexed;

  private static double GROWTH_BASE = 1.5;

  public DoubleColumn(String name, Boolean indexed, double[] values) {

    this.indexed = indexed;
    data = new DoubleArrayList();
    this.name = name;
    dataType = Double.class;
    append(values);
    buildMatrix();
  }

  public DoubleColumn(String name, Boolean indexed, DoubleArrayList values) {

    this.indexed = indexed;
    data = values;
    this.name = name;
    dataType = Double.class;
    buildMatrix();
  }

  public DoubleColumn(String name, Boolean indexed, DMatrixRBlock mat) {

    this.indexed = indexed;
    data = new DoubleArrayList();
    this.name = name;
    dataType = Double.class;
    append(mat.data);
    index = buildIndex(mat.data);
    DMatrixRMaj a = new DMatrixRMaj(mat.getNumRows(), mat.getNumCols());
    ConvertDMatrixStruct.convert(mat, a);
    this.setMatrix(a);
  }

  public void buildMatrix() {

    this.setMatrix(new DMatrixRMaj(data.size(), 1, false, rawData()));
  }

  public void rebuildIndex(double[] vals) {

    index = buildIndex(vals);
  }

  public DoubleIndex buildIndex(double[] vals) {

    return new DoubleIndex(vals);
  }

  public double getDouble(int row) {

    return data.getDouble(row);
  }

  public Double getObject(int row) {

    return data.getDouble(row);
  }

  @Override
  public String getString(int row) {

    return String.valueOf(data.getDouble(row));
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

    data = (DoubleArrayList) vals;
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

    data.add(dbl);
    return this;
  }

  public Column append(double[] dbl) {

    data.addElements(data.size(), dbl);
    return this;
  }

  @Override
  public int size() {

    return data.size();
  }

  @Override
  public DoubleColumn subColumn(String name, int[] aryMask) {

    return new DoubleColumn(name, indexed, getRows(aryMask));
  }

  @Override
  public double[] rawData() {

    return Arrays.copyOfRange(data.elements(), 0, data.size());
  }

  @Override
  protected DoubleColumn createMatrix(int i, int i1, MatrixType matrixType) {

    return new DoubleColumn(this.name + "new", indexed, data.clone());
  }

  @Override
  protected DoubleColumn wrapMatrix(Matrix matrix) {

    return new DoubleColumn(this.name + "new", indexed, (DMatrixRBlock) matrix);
  }
}
