package io.github.primepotato.jandas.index.impl;

import io.github.primepotato.jandas.index.ColIndex;
import io.github.primepotato.jandas.index.generation.IndexGenerator;
import io.github.primepotato.jandas.index.generation.IntIndex;

public class DoubleIndex extends ColIndex {


    public DoubleIndex(double[] data) {

        int[] intMap = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            intMap[i] = IndexGenerator.nextIndex(data[i]);
        }
        internalIntIndex = new IntIndex(intMap);

    }

    @Override
    public Class elementClass() {
        return Double.class;
    }
}
