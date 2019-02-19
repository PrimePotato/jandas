package io.github.primepotato.jandas.index;

import io.github.primepotato.jandas.index.generation.IndexGenerator;
import io.github.primepotato.jandas.index.generation.IntIndex;

import java.time.LocalDate;

public class DateIndex extends ColIndex {

  public DateIndex(LocalDate[] data) {

    int[] intMap = new int[data.length];
    for (int i = 0; i < data.length; i++) {
      intMap[i] = IndexGenerator.nextIndex(data[i], LocalDate.class);
    }
    internalIntIndex = new IntIndex(intMap);

  }


  @Override
  public Class elementClass() {
    return LocalDate.class;
  }
}
