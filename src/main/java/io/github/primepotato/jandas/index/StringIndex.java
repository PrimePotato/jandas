package io.github.primepotato.jandas.index;

import io.github.primepotato.jandas.index.generation.IndexGenerator;
import io.github.primepotato.jandas.index.generation.IntIndex;

public class StringIndex  extends ColIndex {

  public StringIndex(String[] data) {

    int[] intMap = new int[data.length];
    for (int i = 0; i < data.length; i++) {
      intMap[i] = IndexGenerator.nextIndex(data[i], String.class);
    }
    internalIntIndex = new IntIndex(intMap);

  }

  @Override
  public Class elementClass() {
    return String.class;
  }

}
