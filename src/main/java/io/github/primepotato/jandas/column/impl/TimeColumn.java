package io.github.primepotato.jandas.column.impl;

import io.github.primepotato.jandas.column.AbstractColumn;
import io.github.primepotato.jandas.column.Column;
import io.github.primepotato.jandas.index.IntegerIndex;
import io.github.primepotato.jandas.io.parsers.AbstractParser;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.time.LocalTime;
import java.util.AbstractCollection;
import java.util.Arrays;

public class TimeColumn extends AbstractColumn {

  public static final LocalTime DEFAULT_MISSING_VALUE_INDICATOR = null;
  private ObjectArrayList<LocalTime> data;

  public TimeColumn(String name, Boolean indexed, LocalTime[] values) {

    this.indexed = indexed;
    index = new IntegerIndex(Arrays.stream(values).mapToInt(LocalTime::toSecondOfDay).toArray());
    data = ObjectArrayList.wrap(values);
    this.name = name;
    dataType = LocalTime.class; //TODO: makes this the golden source in construction calls
  }

  public TimeColumn(String name, Boolean indexed, ObjectArrayList<LocalTime> values) {

    this.indexed = indexed;
    this.name = name;
    dataType = LocalTime.class; //TODO: makes this the golden source in construction calls
    appendAll(values);
  }

  public TimeColumn append(LocalTime val) {

    data.add(val);
    return this;
  }

  public LocalTime getObject(int row) {

    return data.get(row);
  }

  @Override
  public boolean equals(Column other) {
    try{
      return Arrays.equals((this.getClass().cast(other)).rawData(), rawData());
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public Column createEmpty() {
    return new TimeColumn(name, false, new LocalTime[0]);
  }

  public LocalTime[] getRows(int[] rows) {

    LocalTime[] res = new LocalTime[rows.length];
    for (int i = 0; i < rows.length; i++) {
      if (rows[i] == Integer.MIN_VALUE) {
        res[i] = DEFAULT_MISSING_VALUE_INDICATOR;
      } else {
        res[i] = getObject(rows[i]);
      }
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
  public void appendString(String value, AbstractParser<?> parser) {

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
