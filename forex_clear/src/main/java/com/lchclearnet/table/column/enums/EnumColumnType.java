package com.lchclearnet.table.column.enums;

import com.lchclearnet.table.column.AbstractColumnType;
import com.lchclearnet.table.column.numbers.DoubleColumn;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EnumColumnType<T extends Enum> extends AbstractColumnType {

    private static final Map<Class<? extends Enum>, EnumColumnType> INSTANCES = new ConcurrentHashMap<>();

    private static byte BYTE_SIZE = 4;
    private final Class<T> enumClass;

    private EnumColumnType(Class<T> enumClass) {
        super(BYTE_SIZE, enumClass.getTypeName(), enumClass.getSimpleName());
        this.enumClass = enumClass;
    }

    public static final <U extends Enum> EnumColumnType<U> INSTANCE(Class<U> enumClass) {
        return INSTANCES.computeIfAbsent(enumClass, ec -> new EnumColumnType<U>(enumClass));
    }

    public static int missingValueIndicator() {
        return Integer.MIN_VALUE;
    }

    @Override
    public EnumColumn<T> create(String name) {
        return EnumColumn.create(enumClass, name);
    }

    @Override
    public EnumColumn create(String name, int initialSize) { return EnumColumn.create(enumClass, name, initialSize); }
}
