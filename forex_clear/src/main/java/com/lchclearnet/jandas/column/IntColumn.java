package com.lchclearnet.jandas.column;

import com.lchclearnet.table.column.parsers.AbstractParser;
import it.unimi.dsi.fastutil.ints.*;

import java.util.Collection;
import java.util.ListIterator;

public class IntColumn implements Iterable<Integer> {

  public static final int DEFAULT_MISSING_VALUE_INDICATOR = Integer.MIN_VALUE;

  public static IntColumn create(final String name) {

    return new IntColumn(name, new IntArrayList(), new Int2ObjectAVLTreeMap<>());
  }

  public static IntColumn create(final String name, int[] values) {

    return new IntColumn(name, values);
  }

  public static IntColumn create(final String name, Collection<? extends Integer> values) {

    return new IntColumn(name, values);
  }

  public static IntColumn create(final String name, final int initialSize) {

    IntColumn column =
        new IntColumn(name, new IntArrayList(initialSize), new Int2ObjectAVLTreeMap<>());
    for (int i = 0; i < initialSize; i++) {
      column.appendMissing();
    }
    return column;
  }

  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////   None Static
  // ////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  private final IntArrayList data;

  private final Int2ObjectAVLTreeMap<IntArrayList> indices;

  private final int missingValueIndicator = DEFAULT_MISSING_VALUE_INDICATOR;

  private String name;

  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////   Constructors  and Builders
  // /////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  public IntColumn(String name, int[] values) {

    this.data = new IntArrayList(values.length);
    this.indices = new Int2ObjectAVLTreeMap<>();
    setName(name);
    append(values);
  }

  public IntColumn(String name, Collection<? extends Integer> values) {

    this(name, new IntArrayList(values.size()), new Int2ObjectAVLTreeMap<>());
    setName(name);
    append(values);
  }

  public IntColumn(String name) {

    this(name, new IntArrayList());
  }

  public IntColumn(String name, int rowSize) {

    this(name, new IntArrayList(rowSize));
  }

  public IntColumn(String name, IntArrayList data) {

    this(name, data, new Int2ObjectAVLTreeMap<>());
    setName(name);
    rebuildIndices();
  }

  protected IntColumn(String name, IntArrayList data, Int2ObjectAVLTreeMap<IntArrayList> indices) {

    this.data = data;
    this.indices = indices;
    setName(name);
  }

  public IntColumn prototype(String name) {

    return new IntColumn(name);
  }

  public IntColumn prototype(String name, int rowSize) {

    return new IntColumn(name, rowSize);
  }

  public IntColumn copy() {

    return new IntColumn(name(), data.clone(), indices.clone());
  }

  public IntColumn setName(String name) {

    this.name = name.trim();
    return this;
  }

  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////   Accessors
  // /////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  public String name() {

    return name;
  }

  public int[] elements() {

    return data.elements();
  }

  public int[] copyElements() {

    int[] output = new int[size()];
    System.arraycopy(data.elements(), 0, output, 0, output.length);
    return output;
  }

  public Integer get(int row) {

    return data.get(row);
  }

  public int[] rows(int value) {

    return indices.get(value).elements();
  }

  public int size() {

    return data.size();
  }

  @Override
  public ListIterator<Integer> iterator() {

    return data.iterator();
  }

  public boolean isMissingValue(int value) {

    return missingValueIndicator == value;
  }

  public boolean isMissing(int rowIndex) {

    return isMissingValue(get(rowIndex));
  }

  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////   Modifiers
  // /////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  public void clear() {

    data.clear();
    indices.clear();
  }

  public IntColumn append(final String value, AbstractParser<?> parser) {

    try {
      return append(parser.parseInt(value));
    } catch (final NumberFormatException e) {
      throw new NumberFormatException(
          "Error adding value to column " + name() + ": " + e.getMessage());
    }
  }

  public IntColumn appendMissing() {

    return append(missingValueIndicator);
  }

  public IntColumn append(Integer val) {

    append(val.intValue());
    return this;
  }

  public IntColumn append(int val) {
    //handle the index
    IntArrayList rows = indices.get(val);
    if (rows == null) {
      rows = new IntArrayList();
      indices.put(val, rows);
    }
    rows.add(data.size());

    //handle data
    data.add(val);

    return this;
  }

  public IntColumn append(final IntColumn other) {

    append(other.elements());
    return this;
  }

  public IntColumn append(final int[] values) {

    int offset = data.size();

    //append the data from the other column
    data.addElements(offset, values, 0, values.length);

    //build the indices for the added values
    buildIndices(offset, offset);

    return this;
  }

  public IntColumn append(final Collection<? extends Integer> values) {

    int offset = data.size();

    //append the data from the other column
    data.addAll(values);

    //build the indices for the added values
    buildIndices(offset, offset);

    return this;
  }

  public void rebuildIndices() {

    indices.clear();
    buildIndices(0, 0);
  }

  private void buildIndices(int startingRow, int rowOffset) {

    int[] data = elements();
    for (int row = startingRow; row < data.length; ++row) {
      IntArrayList rows = indices.get(data[row]);
      if (rows == null) {
        rows = new IntArrayList();
        indices.put(data[row], rows);
      }
      rows.add(row + rowOffset);
    }
  }

  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////   Utilities
  // /////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  public IntColumn subset(int[] rows, String columnName) {

    final IntColumn c = this.prototype(columnName, rows.length);
    for (final int row : rows) {
      c.append(get(row));
    }
    return c;
  }

  public IntColumn unique(String columnName) {

    final IntSet values = new IntOpenHashSet();
    for (int i = 0; i < size(); i++) {
      if (!isMissing(i)) {
        values.add(get(i));
      }
    }
    final IntColumn column = IntColumn.create(columnName, indices.size());
    for (Int2ObjectMap.Entry<IntArrayList> index : indices.int2ObjectEntrySet()) {
      column.append(index.getValue().get(0));
    }
    return column;
  }

  public IntColumn removeMissing() {

    IntColumn result = copy();
    result.clear();
    IntListIterator iterator = data.iterator();
    while (iterator.hasNext()) {
      final int v = iterator.nextInt();
      if (!isMissingValue(v)) {
        result.append(v);
      }
    }
    return result;
  }
}
