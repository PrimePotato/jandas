package com.lchclearnet.utils;

import java.util.Collection;

public interface Dependent<T> {
    Collection<T> getDependencies();
}
