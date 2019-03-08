//package io.github.primepotato.jandas.column.impl;
//
//import io.github.primepotato.jandas.column.AbstractColumn;
//import io.github.primepotato.jandas.column.Column;
//import io.github.primepotato.jandas.index.impl.ObjectIndex;
//import io.github.primepotato.jandas.io.parsers.AbstractParser;
//import it.unimi.dsi.fastutil.objects.ObjectArrayList;
//
//import java.util.AbstractCollection;
//import java.util.Arrays;
//import java.util.Iterator;
//
//public class StringColumn extends AbstractColumn {
//
//  public static final String DEFAULT_MISSING_VALUE_INDICATOR = null;
//  private ObjectArrayList<String> data;
//
//  public StringColumn(String name, Boolean indexed, String[] values) {
//
//    this.indexed = indexed;
//    index = new ObjectIndex<>(values, String.class);
//    data = ObjectArrayList.wrap(values);
//    this.name = name;
//    dataType = Integer.class;
//  }
//
//  public StringColumn(String name, Boolean indexed, ObjectArrayList<String> values) {
//
//    this.indexed = indexed;
//    this.name = name;
//    dataType = Integer.class;
//    appendAll(values);
//  }
//
//  @Override
//  public void rebuildIndex() {
//    //TODO: remove and make incremental
//    index = new ObjectIndex<>(rawData(), String.class);
//  }
//
//  public StringColumn append(String val) {
//
//    data.add(val);
//    return this;
//  }
//
//  public String getObject(int row) {
//
//    return data.get(row);
//  }
//
//  public String[] getRows(int[] rows) {
//
//    String[] res = new String[rows.length];
//    for (int i = 0; i < rows.length; i++) {
//      if (rows[i] == Integer.MIN_VALUE) {
//        res[i] = DEFAULT_MISSING_VALUE_INDICATOR;
//      } else {
//        res[i] = getObject(rows[i]);
//      }
//    }
//    return res;
//  }
//
//  @Override
//  public String getString(int row) {
//
//    return String.valueOf(data.get(row));
//  }
//
//  @Override
//  public void appendString(String value, AbstractParser<?> parser) {
//
//    append(value);
//  }
//
//  public int size() {
//
//    return data.size();
//  }
//
//  @Override
//  public Column subColumn(String name, int[] aryMask) {
//
//    return new StringColumn(name, indexed, getRows(aryMask));
//  }
//
//  @Override
//  public String[] rawData() {
//
//    return Arrays.copyOfRange(data.elements(), 0, data.size());
//  }
//
//  public StringColumn append(String[] vals) {
//
//    data.addElements(data.size(), vals, 0, vals.length);
//    return this;
//  }
//
//  @Override
//  public void appendAll(AbstractCollection vals) {
//
//    String[] d = new String[vals.size()];
//    Iterator it = vals.iterator();
//    for (int i = 0; i < vals.size(); i++) {
//      d[i] = (String) it.next();
//    }
//    data = ObjectArrayList.wrap(d);
//  }
//
//  @Override
//  public boolean equals(Column other) {
//    try{
//      return Arrays.equals((this.getClass().cast(other)).rawData(), rawData());
//    } catch (Exception e) {
//      return false;
//    }
//  }
//
//    @Override
//    public Column createEmpty() {
//        return new StringColumn(name, false, new String[0]);
//    }
//
//    @Override
//  public ObjectArrayList newDataContainer(int size) {
//
//    return new ObjectArrayList<String>(size);
//  }
//
//  public boolean unique() {
//
//    return index.unique();
//  }
//
//  public StringColumn append(String stringValue, AbstractParser<?> parser) {
//
//    try {
//      return append(stringValue);
//    } catch (final NumberFormatException e) {
//      throw new NumberFormatException(
//          "Error adding value to column " + name + ": " + e.getMessage());
//    }
//  }
//}
