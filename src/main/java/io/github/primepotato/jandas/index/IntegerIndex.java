package io.github.primepotato.jandas.index;

import io.github.primepotato.jandas.index.generation.IntIndex;
import it.unimi.dsi.fastutil.ints.IntArrayList;

public class IntegerIndex extends ColIndex {

  public IntegerIndex(int[] data) {

    internalIntIndex = new IntIndex(data);

  }

  @Override
  public Class elementClass() {
    return Integer.class;
  }

}
