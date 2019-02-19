package io.github.primepotato.jandas.index;

import io.github.primepotato.jandas.index.generation.IntIndex;

public class IntegerIndex extends ColIndex {

  public IntegerIndex(int[] data) {

    internalIntIndex = new IntIndex(data);

  }

}
