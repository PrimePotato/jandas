package io.github.primepotato.jandas.utils;

import io.github.primepotato.jandas.io.parsers.*;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParserDataContainer {

  Map<String, IntArrayList> IntData;
  Map<String, DoubleArrayList> DoubleData;
  Map<String, ObjectArrayList<LocalDate>> DateData;
  Map<String, ObjectArrayList<LocalTime>> TimeData;
  Map<String, ObjectArrayList<String>> StringData;

  private List<AbstractParser> parsers;
  private Map<String, AbstractParser> parserMap;

  public ParserDataContainer() {

    defaultParsers();
  }

  private void defaultParsers() {

    parsers = new ArrayList<AbstractParser>() {{
      add(new IntParser());
      add(new DoubleParser());
      add(new DateParser());
      add(new TimeParser());
    }};
  }

  public AbstractCollection convertArrayList(AbstractCollection ac, Class cls) {

    AbstractCollection dc = getNewContainer(cls);
    for (Object d : ac) {
      dc.add(cls.cast(d));
    }
    return dc;
  }

  private Map<String, ? extends AbstractCollection> getDataContainer(Class cls) {

    if (cls.getSimpleName().equals("Integer")) {
      return IntData;
    } else if (cls.getSimpleName().equals("Double")) {
      return DoubleData;
    } else if (cls.getSimpleName().equals("LocalDate")) {
      return DateData;
    } else if (cls.getSimpleName().equals("LocalTime")) {
      return TimeData;
    } else if (cls.getSimpleName().equals("String")) {
      return StringData;
    }
    return null;
  }

  private <T extends AbstractCollection> T getNewContainer(Class cls) {

    if (cls.getSimpleName().equals("Integer")) {
      return (T) new IntArrayList();
    } else if (cls.getSimpleName().equals("Double")) {
      return (T) new DoubleArrayList();
    } else if (cls.getSimpleName().equals("LocalDate")) {
      return (T) new ObjectArrayList<LocalDate>();
    } else if (cls.getSimpleName().equals("LocalTime")) {
      return (T) new ObjectArrayList<LocalTime>();
    } else if (cls.getSimpleName().equals("String")) {
      return (T) new ObjectArrayList<String>();
    }
    return null;
  }

}
