package io.github.primepotato.jandas.column.impl;

import io.github.primepotato.jandas.column.Column;
import io.github.primepotato.jandas.header.Heading;
import io.github.primepotato.jandas.index.ColIndex;
import io.github.primepotato.jandas.index.impl.IntegerIndex;
import io.github.primepotato.jandas.io.parsers.AbstractParser;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import lombok.Getter;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class IntegerColumn extends IntArrayList implements Column<Integer> {

    public static final int DEFAULT_MISSING_VALUE_INDICATOR = Integer.MIN_VALUE;

    @Getter
    private Heading heading;
    @Getter
    private Boolean indexed;
    @Getter
    private Class elementClass = Integer.class;
    @Getter
    private ColIndex colIndex;

    public IntegerColumn(Heading heading, Boolean indexed, int[] values) {
        super(values);
        this.indexed = indexed;
        this.heading = heading;
        colIndex = indexed ? new IntegerIndex(values) : null;
    }

    public IntegerColumn(Heading heading, Boolean indexed, IntArrayList values) {
        this(heading, indexed, values.toArray(new int[0]));
    }

    public IntegerColumn(String name, Boolean indexed, IntArrayList values) {
        this(new Heading(name), indexed, values.toArray(new int[0]));
    }

    public IntegerColumn(String name, Boolean indexed, int[] values) {
        this(new Heading(name), indexed, values);
    }

    public IntegerColumn append(int val) {

        this.add(val);
        return this;
    }

    public Integer getObject(int row) {

        return this.getInt(row);
    }


    public boolean unique() {
        return getIndex().unique();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public Set<?> uniqueSet() {

        return getIndex().positions()
                .values()
                .stream()
                .map(x -> getObject(x.getInt(0)))
                .collect(Collectors.toSet());
    }

    public String cleanName() {

        return Normalizer.normalize(heading.toString(), Normalizer.Form.NFD);
    }

    public ColIndex getIndex() {
        if (colIndex == null) {
            rebuildIndex();
            return getIndex();
        } else {
            return colIndex;
        }
    }

    public Integer firstValue() {

        int idx = 0;
        Integer t = null;
        while (t == null) {
            t = getObject(idx);
            idx++;
        }
        return t;
    }

    public String name() {
        return cleanName();
    }

    @Override
    public String toString(){
        return name() + rawData().toString();
    }


    @Override
    public void append(Integer val) {
        this.add((int) val);
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
        return new IntegerColumn(heading, false, new int[0]);
    }

    @Override
    public Integer getMissingValue() {
        return DEFAULT_MISSING_VALUE_INDICATOR;
    }


    @Override
    public Column subColumn(String name, int[] aryMask) {
        return subColumn(new Heading(name), aryMask);
    }


    @Override
    public Column subColumn(Heading heading, int[] aryMask) {
        return new IntegerColumn(heading, indexed, getRows(aryMask));
    }

    @Override
    public String getString(int row) {

        return String.valueOf(this.getInt(row));
    }

    @Override
    public void appendString(String value, AbstractParser<?> parser) {

        try {
            append(parser.parseInt(value));
        } catch (final NumberFormatException e) {
            throw new NumberFormatException(
                    "Error adding value to column " + heading + ": " + e.getMessage());
        }
    }


    @Override
    public IntArrayList newDataContainer(int size) {
        return new IntArrayList(size);
    }

    @Override
    public void appendAll(Collection vals) {
        this.addAll(vals);
    }

    @Override
    public void rebuildIndex() {
        colIndex = new IntegerIndex(this.elements());
    }

    public Integer[] rawData() {
        return Arrays.stream(this.elements()).boxed().toArray(Integer[]::new);
    }

    public IntegerColumn append(int[] vals) {
        this.addElements(this.size(), vals, 0, vals.length);
        return this;
    }

}
