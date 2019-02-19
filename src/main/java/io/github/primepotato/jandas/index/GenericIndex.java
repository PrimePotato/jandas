package io.github.primepotato.jandas.index;

import io.github.primepotato.jandas.index.generation.IndexGenerator;
import io.github.primepotato.jandas.index.generation.IntIndex;

public class GenericIndex<T> extends ColIndex {

    Class cls;

    GenericIndex(T[] data, Class cls) {
        this.cls = cls;
        int[] intMap = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            intMap[i] = IndexGenerator.nextIndex(data[i], cls);
        }
        internalIntIndex = new IntIndex(intMap);

    }


    @Override
    public Class elementClass() {
        return Double.class;
    }

}
