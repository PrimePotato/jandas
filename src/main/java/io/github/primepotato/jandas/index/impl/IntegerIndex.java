package io.github.primepotato.jandas.index.impl;

import io.github.primepotato.jandas.index.ColIndex;
import io.github.primepotato.jandas.index.generation.IntIndex;


public class IntegerIndex extends ColIndex {

  public IntegerIndex(int[] data) {

    internalIntIndex = new IntIndex(data);

  }

  @Override
  public Class elementClass() {
    return Integer.class;
  }

}
