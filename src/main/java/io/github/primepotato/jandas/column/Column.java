package io.github.primepotato.jandas.column;

import io.github.primepotato.jandas.header.Heading;
import io.github.primepotato.jandas.index.ColIndex;
import io.github.primepotato.jandas.io.parsers.AbstractParser;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Set;

public interface Column<T> {

  default void rebuildIndex() {}

  int size();

  T[] rawData();

  Column subColumn(String name, int[] aryMask);

  Column subColumn(Heading name, int[] aryMask);

  String getString(int row);

  void appendString(final String value, AbstractParser<?> parser);

  T getObject(int row);

  Set uniqueSet();

  boolean unique();

  String cleanName();

  ColIndex index();

  T firstValue();

  void append(T val);

  void appendAll(Collection vals);

  AbstractCollection newDataContainer(int size);

  boolean equals(Object other);

  Column createEmpty();

  Heading getHeading();

  public Object getMissingValue();

}
