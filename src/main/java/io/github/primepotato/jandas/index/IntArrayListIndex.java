package io.github.primepotato.jandas.index;

import io.github.primepotato.jandas.index.generation.IndexGenerator;
import io.github.primepotato.jandas.index.generation.IntIndex;
import it.unimi.dsi.fastutil.ints.IntArrayList;


public class IntArrayListIndex extends ColIndex {

  //TODO: optimize, used a lot in grouping/join

  public IntArrayListIndex(IntArrayList[] data) {

    int[] intMap = new int[data.length];
    for (int i = 0; i < data.length; i++) {
      intMap[i] = IndexGenerator.nextIndex(data[i], IntArrayList.class);
    }
    internalIntIndex = new IntIndex(intMap);

  }

}
