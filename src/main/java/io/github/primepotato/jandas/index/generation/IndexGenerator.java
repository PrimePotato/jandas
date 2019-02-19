package io.github.primepotato.jandas.index.generation;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public interface IndexGenerator {

    Object2IntOpenHashMap<String> stringIndex = new Object2IntOpenHashMap<>();
    Object2IntOpenHashMap<LocalDate> dateIndex = new Object2IntOpenHashMap<>();
    Object2IntOpenHashMap<Double> doubleIndex = new Object2IntOpenHashMap<>();

    Map<String, Object2IntOpenHashMap> indexClassMap = new HashMap<>();

    static int nextIndex(String val) {
        return stringIndex.computeIntIfAbsent(val, x -> stringIndex.size());
    }

    static int nextIndex(LocalDate val) {
        return dateIndex.computeIntIfAbsent(val, x -> dateIndex.size());
    }

    static int nextIndex(double val) {
        return doubleIndex.computeIntIfAbsent(val, x -> doubleIndex.size());
    }

    static int nextIndex(int val) {
        return val;
    }

    static <T> int nextIndex(T val, Class cls) {
        Object2IntOpenHashMap<T> indexMap = indexClassMap.computeIfAbsent(cls.getSimpleName(), x -> new Object2IntOpenHashMap<T>());
        return indexMap.computeIntIfAbsent(val, x -> indexMap.size());
    }

}
