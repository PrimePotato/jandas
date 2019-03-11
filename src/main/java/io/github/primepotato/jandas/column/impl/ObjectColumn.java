package io.github.primepotato.jandas.column.impl;

import io.github.primepotato.jandas.column.AbstractColumn;
import io.github.primepotato.jandas.column.Column;
import io.github.primepotato.jandas.index.impl.ObjectIndex;
import io.github.primepotato.jandas.io.parsers.AbstractParser;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.lang.reflect.Array;
import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

@SuppressWarnings("unchecked")
public class ObjectColumn<T> extends AbstractColumn {

    public static final Object MISSING_VALUE = null;
    private ObjectArrayList<T> data;

    public ObjectColumn(String name, Boolean indexed, T[] values, Class<T> cls) {

        this.indexed = indexed;
        index = new ObjectIndex<>(values, cls);
        data = ObjectArrayList.wrap(values);
        this.name = name;
        dataType = cls;
    }

    public ObjectColumn(String name, Boolean indexed, ObjectArrayList<T> values, Class<T> cls) {

        this.indexed = indexed;
        this.name = name;
        dataType = cls;
        appendAll(values);
    }

    @Override
    public void rebuildIndex() {
        //TODO: remove and make incremental
        index = new ObjectIndex<>(rawData(), dataType);
    }

    public ObjectColumn<T> append(T val) {

        data.add(val);
        return this;
    }

    public T getObject(int row) {

        return data.get(row);
    }

    public T[] getRows(int[] rows) {

        T[] res = (T[]) Array.newInstance(dataType, rows.length);

        for (int i = 0; i < rows.length; i++) {
            if (rows[i] == Integer.MIN_VALUE) {
                res[i] = (T) MISSING_VALUE;
            } else {
                res[i] = getObject(rows[i]);
            }
        }
        return res;
    }

    @Override
    public String getString(int row) {

        return String.valueOf(data.get(row));
    }

    @Override
    public void appendString(String value, AbstractParser<?> parser) {

        append((T) parser.parse(value));
    }

    public int size() {

        return data.size();
    }

    @Override
    public Column subColumn(String name, int[] aryMask) {

        return new ObjectColumn<>(name, indexed, getRows(aryMask), (Class<T>)dataType);
    }

    @Override
    public T[] rawData() {

        return Arrays.copyOfRange(data.elements(), 0, data.size());
    }

    public ObjectColumn<T> append(T[] vals) {

        data.addElements(data.size(), vals, 0, vals.length);
        return this;
    }

    @Override
    public void appendAll(Collection vals) {

        T[] d = (T[]) Array.newInstance(dataType, vals.size());
        Iterator it = vals.iterator();
        for (int i = 0; i < vals.size(); i++) {
            d[i] = (T) it.next();
        }
        data = ObjectArrayList.wrap(d);
    }

    @Override
    public boolean equals(Column other) {
        try {
            return Arrays.equals((this.getClass().cast(other)).rawData(), rawData());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Column createEmpty() {
        return new ObjectColumn<T>(name, false, (T[]) Array.newInstance(dataType, 0), (Class<T>)dataType);
    }

    @Override
    public ObjectArrayList newDataContainer(int size) {

        return new ObjectArrayList<Object>(size);
    }

    public boolean unique() {

        return index.unique();
    }

    public ObjectColumn append(String stringValue, AbstractParser<?> parser) {

        try {
            return append((T) parser.parse(stringValue));
        } catch (final NumberFormatException e) {
            throw new NumberFormatException(
                    "Error adding value to column " + name + ": " + e.getMessage());
        }
    }
}
