package io.github.primepotato.jandas.index.impl;

import io.github.primepotato.jandas.index.ColIndex;
import io.github.primepotato.jandas.index.generation.IndexGenerator;
import io.github.primepotato.jandas.index.generation.IntIndex;

public class ObjectIndex<T> extends ColIndex {

    Class cls;

    public ObjectIndex(T[] data, Class cls) {
        this.cls = cls;
        int[] intMap = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            intMap[i] = IndexGenerator.nextIndex(data[i], cls);
        }
        internalIntIndex = new IntIndex(intMap);

    }

    @Override
    public Class elementClass() {
        return cls;
    }

    public <T> T indexValue(int i) {
        if (cls.equals(String.class)) {
            int a = 1;
        }
        return IndexGenerator.indexValue(i, elementClass());
    }

}
