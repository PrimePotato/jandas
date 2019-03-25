package io.github.primepotato.jandas.column;

import io.github.primepotato.jandas.index.ColIndex;
import io.github.primepotato.jandas.io.parsers.AbstractParser;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Set;

public interface Column {

  default void rebuildIndex() {}

  int size();

  <T> T rawData();

  Column subColumn(String name, int[] aryMask);

  String getString(int row);

  void appendString(final String value, AbstractParser<?> parser);

  <T> T getObject(int row);

  Set uniqueSet();

  String name();

  String cleanName();

  ColIndex index();

  <T> T firstValue();

  void appendAll(Collection vals);

  AbstractCollection newDataContainer(int size);

  boolean equals(Object other);

  Column createEmpty();

}
