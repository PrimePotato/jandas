package io.github.primepotato.jandas.column.impl;

import io.github.primepotato.jandas.column.Column;
import io.github.primepotato.jandas.header.Header;
import io.github.primepotato.jandas.header.Heading;
import io.github.primepotato.jandas.index.ColIndex;
import io.github.primepotato.jandas.index.impl.ObjectIndex;
import io.github.primepotato.jandas.io.parsers.AbstractParser;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


@RequiredArgsConstructor
public class ObjectColumn<T> extends ObjectArrayList<T> implements Column<T> {

    private static final Object MISSING_VALUE = null;

    @Getter
    private Heading heading;
    @Getter
    private Boolean indexed;
    @Getter
    private Class<T> elementClass;
    @Getter
    private ColIndex colIndex;

    public ObjectColumn(Heading heading, Boolean indexed, T[] values, Class<T> elementClass) {
        super(values);
        this.heading = heading;
        this.elementClass = elementClass;
        this.indexed = indexed;
        if (indexed) rebuildIndex();
    }

    public ObjectColumn(Heading heading, Boolean indexed, ObjectArrayList<T> values, Class<T> elementClass) {
        this(heading, indexed, values.elements(), elementClass);
    }

    @Override
    public Object getMissingValue() {
        return MISSING_VALUE;
    }

    @Override
    public void rebuildIndex() {
        colIndex = new ObjectIndex<>(rawData(), elementClass);
    }

    public T[] getRows(int[] rows) {
        T[] res = (T[]) Array.newInstance(elementClass, rows.length);

        for (int i = 0; i < rows.length; i++) {
            if (rows[i] == Integer.MIN_VALUE) {
                res[i] = (T) MISSING_VALUE;
            } else {
                res[i] = get(rows[i]);
            }
        }
        return res;
    }

    @Override
    public String getString(int row) {

        return String.valueOf(this.get(row));
    }

    @Override
    public void appendString(String value, AbstractParser<?> parser) {

    }

    @Override
    public T getObject(int row) {
        return (T) get(row);
    }

    @Override
    public Set<T> uniqueSet() {
        return new HashSet<>(Arrays.asList(a));
    }

    @Override
    public boolean unique() {
        return colIndex.unique();
    }

    @Override
    public String cleanName() {
        return heading.toString();
    }

    @Override
    public ColIndex index() {
        return colIndex;
    }

    @Override
    public T firstValue() {
        return this.a[0];
    }

    @Override
    public Column subColumn(String name, int[] aryMask) {
        return subColumn(new Heading(name), aryMask);
    }

    @Override
    public Column subColumn(Heading name, int[] aryMask) {

        return new ObjectColumn<>(name, indexed, getRows(aryMask), (Class<T>) elementClass);
    }

    public T[] rawData() {
        return this.elements();
    }

    @Override
    public void appendAll(Collection vals) {
        this.addAll(vals);
    }

    @Override
    public void append(T val) {
        this.add(val);
    }

    @Override
    public boolean equals(Object other) {
        try {
            return Arrays.equals((this.getClass().cast(other)).rawData(), rawData());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Column createEmpty() {
        return new ObjectColumn<>(heading, false, (T[]) Array.newInstance(elementClass, 0), elementClass);
    }

    @Override
    public ObjectArrayList newDataContainer(int size) {
        return new ObjectArrayList<Object>(size);
    }

//    public ObjectColumn append(String stringValue, AbstractParser<?> parser) {
//
//        try {
//            return add((T) parser.parse(stringValue));
//        } catch (final NumberFormatException e) {
//            throw new NumberFormatException(
//                    "Error adding value to column " + heading + ": " + e.getMessage());
//        }
//    }
}
