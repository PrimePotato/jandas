package com.lchclearnet.jandas.column;

import com.lchclearnet.jandas.index.IntegerIndex;
import com.lchclearnet.utils.CurrencyPair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.AbstractCollection;
import java.util.Arrays;

public class CurrencyPairColumn extends AbstractColumn {

  public static final int DEFAULT_MISSING_VALUE_INDICATOR = Integer.MIN_VALUE;
  private ObjectArrayList<CurrencyPair> data;

  public CurrencyPairColumn(String name, Boolean indexed, CurrencyPair[] values) {

    this.indexed = indexed;
    data = ObjectArrayList.wrap(values);
    this.name = name;
    dataType = Integer.class;
  }

  public int valueToInt(CurrencyPair value) {

    if (value == null) {
      return DEFAULT_MISSING_VALUE_INDICATOR;
    }
    return value.ordinal();
  }

  @Override
  public void rebuildIndex() {

    if (data.size() > 0) {
      index = new IntegerIndex(Arrays.stream(rawData()).mapToInt(this::valueToInt).toArray());
    }
  }

  public CurrencyPairColumn append(CurrencyPair val) {

    data.add(val);
    return this;
  }

  public CurrencyPair getObject(int row) {

    try {
      return data.get(row);
    } catch (Exception e) {
      return data.get(row);
    }
  }

//  @Override
  ////  public <T> void appendAll(AbstractCollection vals) {
  ////
  ////    data = ObjectArrayList.wrap((CurrencyPair[]) vals);
  ////  }

  @Override
  public ObjectArrayList newDataContainer(int size) {

    return new ObjectArrayList(size);
  }

  public CurrencyPair[] getRows(int[] rows) {

    CurrencyPair[] res = new CurrencyPair[rows.length];
    for (int i = 0; i < rows.length; i++) {
      res[i] = getObject(rows[i]);
    }
    return res;
  }

  public CurrencyPair getDate(int row) {

    return data.get(row);
  }

  public int size() {

    return data.size();
  }

  @Override
  public Column subColumn(String name, int[] aryMask) {

    return new CurrencyPairColumn(name, indexed, getRows(aryMask));
  }

  @Override
  public String getString(int row) {

    return null;
  }

  @Override
  public void appendString(String value,
      com.lchclearnet.jandas.io.parsers.AbstractParser<?> parser) {

    try {
      append((CurrencyPair) parser.parse(value));
    } catch (final NumberFormatException e) {
      throw new NumberFormatException(
          "Error adding value to column " + name + ": " + e.getMessage());
    }
  }

  @Override
  public void appendAll(AbstractCollection vals) {
    CurrencyPair[] d = new CurrencyPair[vals.size()];
    for (int i=0; i<vals.size();i++){
      d[i] = (CurrencyPair)vals.iterator().next();
    }
    data = ObjectArrayList.wrap(d);
  }

  @Override
  public CurrencyPair[] rawData() {

    return Arrays.copyOfRange(data.elements(), 0, data.size());
  }

  public CurrencyPairColumn append(CurrencyPair[] vals) {

    data.addElements(data.size(), vals, 0, vals.length);
    return this;
  }

  public boolean unique() {

    return index.unique();
  }

}
