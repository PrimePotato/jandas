package io.github.primepotato.jandas.column.impl;

import io.github.primepotato.jandas.column.Column;
import io.github.primepotato.jandas.header.Heading;
import io.github.primepotato.jandas.index.ColIndex;
import io.github.primepotato.jandas.index.impl.ObjectIndex;
import io.github.primepotato.jandas.io.parsers.AbstractParser;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Array;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


@RequiredArgsConstructor
public class ObjectColumn<T> extends ObjectArrayList<T> implements Column<T> {

    @Getter @Setter
    private T missingValue = null;
    @Getter
    private Heading heading;
    @Getter
    private Boolean indexed;
    @Getter
    private final Class<T> elementClass;
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
        this(heading, indexed, Arrays.copyOf(values.elements(), values.size()), elementClass);
    }

    @Override
    public void rebuildIndex() {
        indexed = true;
        colIndex = new ObjectIndex<>(rawData(), elementClass);
    }

    public T[] getRows(int[] rows) {
        T[] res = (T[]) Array.newInstance(elementClass, rows.length);

        for (int i = 0; i < rows.length; i++) {
            if (rows[i] == Integer.MIN_VALUE) {
                res[i] = getMissingValue();
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
        try {
            add((T) parser.parse(value));
        } catch (final NumberFormatException e) {
            throw new NumberFormatException(
                    "Error adding value to column " + heading + ": " + e.getMessage());
        }
    }

    @Override
    public T getObject(int row) {
        return get(row);
    }

    @Override
    public Set<T> uniqueSet() {
        return new HashSet<>(Arrays.asList(a));
    }

    @Override
    public boolean unique() {
        if (indexed) {
            return colIndex.unique();
        } else {
            rebuildIndex();
            return unique();
        }


    }

    @Override
    public String cleanName() {
        return Normalizer.normalize(heading.toString(), Normalizer.Form.NFD);
    }

    @Override
    public ColIndex getIndex() {
        if (colIndex == null) {
            rebuildIndex();
            return getIndex();
        } else {
            return colIndex;
        }
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

        return new ObjectColumn<>(name, indexed, getRows(aryMask), elementClass);
    }

    public T[] rawData() {
        return Arrays.copyOf(elements(), size);
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

}
