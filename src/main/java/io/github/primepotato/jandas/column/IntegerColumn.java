package io.github.primepotato.jandas.column;

import io.github.primepotato.jandas.index.IntegerIndex;
import io.github.primepotato.jandas.io.parsers.AbstractParser;
import it.unimi.dsi.fastutil.ints.IntArrayList;

import java.util.AbstractCollection;
import java.util.Arrays;

public class IntegerColumn extends AbstractColumn {

    public static final int DEFAULT_MISSING_VALUE_INDICATOR = Integer.MIN_VALUE;
    private IntArrayList data;

    public IntegerColumn(String name, Boolean indexed, int[] values) {

        this.indexed = indexed;
        data = new IntArrayList(values);
        this.name = name;
        dataType = Integer.class;
        index = indexed ? new IntegerIndex(values) : null;
    }

    public IntegerColumn(String name, Boolean indexed, IntArrayList values) {

        this.indexed = indexed;
        data = values;
        this.name = name;
        dataType = Integer.class;
        index = indexed ? new IntegerIndex(rawData()) : null;
    }

    public IntegerColumn append(int val) {

        data.add(val);
        return this;
    }

    public int getInt(int row) {

        return data.getInt(row);
    }

    public Integer getObject(int row) {

        return data.getInt(row);
    }

    public int[] getRows(int[] rows) {

        int[] res = new int[rows.length];
        for (int i = 0; i < rows.length; i++) {
            if (rows[i] == Integer.MIN_VALUE) {
                res[i] = DEFAULT_MISSING_VALUE_INDICATOR;
            } else {
                res[i] = getObject(rows[i]);
            }
        }
        return res;
    }

    public int size() {

        return data.size();
    }

    @Override
    public IntegerColumn subColumn(String name, int[] aryMask) {

        return new IntegerColumn(name, indexed, getRows(aryMask));
    }

    @Override
    public String getString(int row) {

        return String.valueOf(data.getInt(row));
    }

    @Override
    public void appendString(String value, AbstractParser<?> parser) {

        try {
            append(parser.parseInt(value));
        } catch (final NumberFormatException e) {
            throw new NumberFormatException(
                    "Error adding value to column " + name + ": " + e.getMessage());
        }
    }

    @Override
    public IntArrayList newDataContainer(int size) {

        return new IntArrayList(size);
    }

    @Override
    public void appendAll(AbstractCollection vals) {

        data = (IntArrayList) vals;
    }

    @Override
    public void rebuildIndex() {

        index = new IntegerIndex(rawData());
    }

    @Override
    public int[] rawData() {

        return Arrays.copyOfRange(data.elements(), 0, data.size());
    }

    public IntegerColumn append(int[] vals) {

        data.addElements(data.size(), vals, 0, vals.length);
        return this;
    }

}
