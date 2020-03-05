package io.github.primepotato.jandas.index.generation;

import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public interface IndexGenerator {

    Object2IntLinkedOpenHashMap<Double> doubleIndex = new Object2IntLinkedOpenHashMap<>();

    Map<String, Object2IntLinkedOpenHashMap> indexClassMap = new HashMap<>();

    static int nextIndex(double val) {
        return doubleIndex.computeIntIfAbsent(val, x -> doubleIndex.size());
    }

    static int nextIndex(int val) {
        return val;
    }

    static <T> int nextIndex(T val, Class cls) {
        Object2IntLinkedOpenHashMap<T> idx = indexClassMap.computeIfAbsent(cls.getSimpleName(), x -> new Object2IntLinkedOpenHashMap<T>());
        return idx.computeIntIfAbsent(val, x -> idx.size());
    }

    static int intIndexValue(int val) {
        return val;
    }

    static double doubleIndexValue(int val) {
        try {
            return (double) doubleIndex.keySet().toArray()[val];
        }
        catch (Exception e) {
            return Double.NaN;
        }
    }

    static <T> T indexValue(int val, Class cls) {
        try {
            switch (cls.getSimpleName()) {
                case "int":
                    return (T) (Integer) intIndexValue(val);
                case "double":
                    return (T) (Double) doubleIndexValue(val);
                default:
                    Object2IntLinkedOpenHashMap<T> idx = indexClassMap.computeIfAbsent(cls.getSimpleName(), x -> new Object2IntLinkedOpenHashMap<T>());
                    return (T) idx.keySet().toArray()[val];

            }
        } catch (Exception e) {
            return null;
        }
    }

}
