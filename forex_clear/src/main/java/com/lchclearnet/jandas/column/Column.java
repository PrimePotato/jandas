package com.lchclearnet.jandas.column;

import com.lchclearnet.jandas.column.parsers.AbstractParser;
import com.lchclearnet.jandas.index.ColIndex;

import java.util.AbstractCollection;
import java.util.Set;

public interface Column {

  //void appendMissing();

  //TODO: Make iterable

  default void rebuildIndex() {}

  int size();

  <T> T rawData();

  Column subColumn(String name, int[] aryMask);

  String getString(int row);

  void appendString(final String value, AbstractParser<?> parser);

  <T> T getObject(int row);

  Set uniqueSet();

  String name();

  ColIndex index();

  <T> T firstValue();

  void appendAll(final AbstractCollection vals);

  AbstractCollection newDataContainer(int size);

}
