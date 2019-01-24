package com.lchclearnet.jandas.column;

import com.lchclearnet.jandas.index.IntegerIndex;
import com.lchclearnet.table.column.parsers.AbstractParser;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.time.LocalDate;
import java.util.AbstractCollection;
import java.util.Arrays;

public class DateColumn extends AbstractColumn {

  public static final int DEFAULT_MISSING_VALUE_INDICATOR = Integer.MIN_VALUE;
  private ObjectArrayList<LocalDate> data;

  public DateColumn(String name, Boolean indexed, LocalDate[] values) {

    this.indexed = indexed;
    index = new IntegerIndex(Arrays.stream(values).mapToInt(x -> (int) x.toEpochDay()).toArray());
    data = ObjectArrayList.wrap(values);
    this.name = name;
    dataType = Integer.class;
  }

  public DateColumn(String name, Boolean indexed, ObjectArrayList<LocalDate> col) {

    this.indexed = indexed;
    this.name = name;
    dataType = LocalDate.class;
    appendAll(col);
  }

  public DateColumn append(LocalDate val) {

    data.add(val);
    return this;
  }

  public LocalDate getObject(int row) {

    return data.get(row);
  }

  public LocalDate[] getRows(int[] rows) {

    LocalDate[] res = new LocalDate[rows.length];
    LocalDate[] elm = data.elements();
    for (int i = 0; i < rows.length; i++) {
      res[i] = elm[rows[i]];
    }
    return res;
  }

  public LocalDate getDate(int row) {

    return data.get(row);
  }

  public int size() {

    return data.size();
  }

  @Override
  public Column subColumn(String name, int[] aryMask) {

    return new DateColumn(name, indexed, getRows(aryMask));
  }

  @Override
  public String getString(int row) {

    return data.get(row).toString();
  }

  @Override
  public void appendString(String value,
      com.lchclearnet.jandas.io.parsers.AbstractParser<?> parser) {

    try {
      append((LocalDate) parser.parse(value));
    } catch (final NumberFormatException e) {
      throw new NumberFormatException(
          "Error adding value to column " + name + ": " + e.getMessage());
    }
  }

  @Override
  public void appendAll(AbstractCollection vals) {
    LocalDate[] d = new LocalDate[vals.size()];
    for (int i=0; i<vals.size();i++){
      d[i] = (LocalDate)vals.iterator().next();
    }
    data = ObjectArrayList.wrap(d);
  }

  @Override
  public ObjectArrayList newDataContainer(int size) {

    return new ObjectArrayList(size);
  }

  @Override
  public void rebuildIndex() {

  }

  @Override
  public LocalDate[] rawData() {

    return data.elements();
  }

  public DateColumn append(LocalDate[] vals) {

    data.addElements(data.size(), vals, 0, vals.length);
    return this;
  }

  public boolean unique() {

    return index.unique();
  }

  public DateColumn append(LocalDate stringValue, AbstractParser<?> parser) {

    try {
      return append(stringValue);
    } catch (final NumberFormatException e) {
      throw new NumberFormatException(
          "Error adding value to column " + name + ": " + e.getMessage());
    }
  }
}
