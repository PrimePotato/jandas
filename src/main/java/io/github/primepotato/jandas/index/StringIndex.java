package io.github.primepotato.jandas.index;

import io.github.primepotato.jandas.index.generation.IndexGenerator;
import io.github.primepotato.jandas.index.generation.IntIndex;

public class StringIndex  extends ColIndex implements IndexGenerator {

  public StringIndex(String[] data) {

    int[] intMap = new int[data.length];
    for (int i = 0; i < data.length; i++) {
      intMap[i] = IndexGenerator.nextIndex(data[i]);
    }
    internalIntIndex = new IntIndex(intMap);

  }

}
