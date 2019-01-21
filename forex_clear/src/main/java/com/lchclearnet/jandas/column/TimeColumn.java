package com.lchclearnet.jandas.column;

import com.lchclearnet.jandas.index.IntegerIndex;
import com.lchclearnet.table.column.parsers.AbstractParser;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.time.LocalTime;
import java.util.AbstractCollection;
import java.util.Arrays;

public class TimeColumn extends AbstractColumn {

  public static final int DEFAULT_MISSING_VALUE_INDICATOR = Integer.MIN_VALUE;
  private ObjectArrayList<LocalTime> data;

  public TimeColumn(String name, Boolean indexed, LocalTime[] values) {

    this.indexed = indexed;
    index = new IntegerIndex(Arrays.stream(values).mapToInt(LocalTime::toSecondOfDay).toArray());
    data = ObjectArrayList.wrap(values);
    this.name = name;
    dataType = LocalTime.class; //TODO: makes this the golden source in construction calls
  }

  public TimeColumn append(LocalTime val) {

    data.add(val);
    return this;
  }

  public LocalTime getObject(int row) {

    return data.get(row);
  }

  public LocalTime[] getRows(int[] rows) {

    LocalTime[] res = new LocalTime[rows.length];
    for (int i = 0; i < rows.length; i++) {
      res[i] = getObject(rows[i]);
    }
    return res;
  }

  public LocalTime getDate(int row) {

    return data.get(row);
  }

  public int size() {

    return data.size();
  }

  @Override
  public Column subColumn(String name, int[] aryMask) {

    return new TimeColumn(name, indexed, getRows(aryMask));
  }

  @Override
  public String getString(int row) {

    return null;
  }

  @Override
  public void appendString(String value,
      com.lchclearnet.jandas.column.parsers.AbstractParser<?> parser) {

    try {
      append((LocalTime) parser.parse(value));
    } catch (final NumberFormatException e) {
      throw new NumberFormatException(
          "Error adding value to column " + name + ": " + e.getMessage());
    }
  }

  @Override
  public void appendAll(AbstractCollection vals) {

    LocalTime[] d = new LocalTime[vals.size()];
    for (int i=0; i<vals.size();i++){
      d[i] = (LocalTime) vals.iterator().next();
    }
    data = ObjectArrayList.wrap(d);
  }

  @Override
  public void rebuildIndex() {

  }

  @Override
  public AbstractCollection newDataContainer(int size) {

    return new ObjectArrayList(size);
  }

  @Override
  public LocalTime[] rawData() {

    return data.elements();
  }

  public TimeColumn append(LocalTime[] vals) {

    data.addElements(data.size(), vals, 0, vals.length);
    return this;
  }

  public boolean unique() {

    return index.unique();
  }

  public TimeColumn append(LocalTime stringValue, AbstractParser<?> parser) {

    try {
      return append(stringValue);
    } catch (final NumberFormatException e) {
      throw new NumberFormatException(
          "Error adding value to column " + name + ": " + e.getMessage());
    }
  }
}
