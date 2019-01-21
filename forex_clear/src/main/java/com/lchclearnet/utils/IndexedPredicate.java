package com.lchclearnet.utils;

public interface IndexedPredicate<T> {
    boolean filter(T item, int index, int count);
}
