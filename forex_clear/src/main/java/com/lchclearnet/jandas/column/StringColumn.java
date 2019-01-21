package com.lchclearnet.jandas.column;

import com.lchclearnet.jandas.index.StringIndex;
import com.lchclearnet.table.column.parsers.AbstractParser;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.time.LocalDate;
import java.util.AbstractCollection;

public class StringColumn extends AbstractColumn {

  public static final int DEFAULT_MISSING_VALUE_INDICATOR = Integer.MIN_VALUE;
  private ObjectArrayList<String> data;

  public StringColumn(String name, Boolean indexed, String[] values) {

    this.indexed = indexed;
    index = new StringIndex(values);
    data = ObjectArrayList.wrap(values);
    this.name = name;
    dataType = Integer.class;
  }

  @Override
  public void rebuildIndex() {
    //TODO: remove and make incremental
    index = new StringIndex(data.elements());
  }

  public StringColumn append(String val) {

    data.add(val);
    return this;
  }

  public String getObject(int row) {

    return data.get(row);
  }

  public String[] getRows(int[] rows) {

    String[] res = new String[rows.length];
    for (int i = 0; i < rows.length; i++) {
      res[i] = getObject(rows[i]);
    }
    return res;
  }

  @Override
  public String getString(int row) {

    return String.valueOf(data.get(row));
  }

  @Override
  public void appendString(String value,
      com.lchclearnet.jandas.column.parsers.AbstractParser<?> parser) {

    append(value);
  }

  public int size() {

    return data.size();
  }

  @Override
  public Column subColumn(String name, int[] aryMask) {

    return new StringColumn(name, indexed, getRows(aryMask));
  }

  @Override
  public String[] rawData() {

    return data.elements();
  }

  public StringColumn append(String[] vals) {

    data.addElements(data.size(), vals, 0, vals.length);
    return this;
  }

  @Override
  public void appendAll(AbstractCollection vals) {
    String[] d = new String[vals.size()];
    for (int i=0; i<vals.size();i++){
      d[i] = (String)vals.iterator().next();
    }
    data = ObjectArrayList.wrap(d);
  }

  @Override
  public ObjectArrayList newDataContainer(int size) {

    return new ObjectArrayList<String>(size);
  }

  public boolean unique() {

    return index.unique();
  }

  public StringColumn append(String stringValue, AbstractParser<?> parser) {

    try {
      return append(stringValue);
    } catch (final NumberFormatException e) {
      throw new NumberFormatException(
          "Error adding value to column " + name + ": " + e.getMessage());
    }
  }
}
