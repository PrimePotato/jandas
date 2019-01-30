package io.github.primepotato.jandas.column;


import io.github.primepotato.jandas.index.IntegerIndex;
import io.github.primepotato.jandas.io.parsers.AbstractParser;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.lang.reflect.Array;
import java.util.AbstractCollection;
import java.util.Arrays;

public class EnumColumn<E extends Enum> extends AbstractColumn {

  public static final int DEFAULT_MISSING_VALUE_INDICATOR = Integer.MIN_VALUE;
  private ObjectArrayList<E> data;
  private Class<E> persistentClass;

  public EnumColumn(String name, Boolean indexed, E[] values, Class<E> cls) {

    this.indexed = indexed;
    data = ObjectArrayList.wrap(values);
    this.name = name;
    dataType = Enum.class;
    rebuildIndex();
    persistentClass = cls;

  }

  private int valueToInt(E value) {

    if (value == null) {
      return DEFAULT_MISSING_VALUE_INDICATOR;
    }
    return value.ordinal();
  }

  @Override
  public void rebuildIndex() {
    //TODO: remove and make incremental
    if (data.size() > 0) {
      index = new IntegerIndex(Arrays.stream(rawData()).mapToInt(this::valueToInt).toArray());
    }
  }

  public EnumColumn<E> append(E val) {

    data.add(val);
    return this;
  }

  public E getObject(int row) {

    return data.get(row);
  }

  public E[] getRows(int[] rows) {

    E[] res = (E[]) Array.newInstance(persistentClass, rows.length);
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
  public void appendString(String value, AbstractParser<?> parser) {

    try {
      append((E) parser.parse(value));
    } catch (final NumberFormatException e) {
      throw new NumberFormatException(
          "Error adding value to column " + name + ": " + e.getMessage());
    }
  }

  @Override
  public void appendAll(AbstractCollection vals) {

    E[] d = (E[])Array.newInstance(this.persistentClass, vals.size());
    for (int i=0; i<vals.size();i++){
      d[i] = this.persistentClass.cast(vals.iterator().next());
    }
    data = ObjectArrayList.wrap(d);
  }

  @Override
  public ObjectArrayList newDataContainer(int size) {

    return new ObjectArrayList(size);
  }

  public int size() {

    return data.size();
  }

  @Override
  public Column subColumn(String name, int[] aryMask) {

    return new EnumColumn<>(name, indexed, getRows(aryMask), persistentClass);
  }

  @Override
  public E[] rawData() {

    return data.elements();
  }

  public EnumColumn<E> append(E[] vals) {

    data.addElements(data.size(), vals, 0, vals.length);
    return this;
  }

  public boolean unique() {

    return index.unique();
  }

}
