/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lchclearnet.table.column.enums;

import com.google.common.base.Preconditions;
import com.lchclearnet.table.Column;
import com.lchclearnet.table.Table;
import com.lchclearnet.table.column.AbstractColumn;
import com.lchclearnet.table.column.CategoricalColumn;
import com.lchclearnet.table.column.parsers.AbstractParser;
import com.lchclearnet.table.column.parsers.EnumParser;
import com.lchclearnet.table.column.strings.StringColumn;
import com.lchclearnet.table.selection.Selection;
import com.lchclearnet.table.sorting.comparators.DescendingIntComparator;
import it.unimi.dsi.fastutil.ints.*;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A column in a base table that contains float values
 */
public class EnumColumn<T extends Enum> extends AbstractColumn<T> implements EnumFilters<T>, EnumFillers<T>,
        EnumMapFunctions<T>, CategoricalColumn<T> {


    public static final int MISSING_VALUE = EnumColumnType.missingValueIndicator();
    public final Class<T> enumClass; //= ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    private final T[] values; //= enumClass.getEnumConstants();
    private final IntComparator reverseIntComparator = DescendingIntComparator.instance();
    private final EnumParser<T> defaultParser;// = new EnumParser<>(EnumColumnType.INSTANCE(enumClass));
    private IntArrayList data;
    private final IntComparator comparator = (r1, r2) -> {
        final int f1 = getIntInternal(r1);
        int f2 = getIntInternal(r2);
        return Integer.compare(f1, f2);
    };
    private EnumFormatter printFormatter = new EnumFormatter();

    private EnumColumn(Class<T> enumClass, String name, IntArrayList data) {
        super(EnumColumnType.INSTANCE(enumClass), name);
        this.data = data;
        this.enumClass = enumClass;
        this.values = enumClass.getEnumConstants();
        this.defaultParser = new EnumParser<>(enumClass);
    }

    public static <U extends Enum> EnumColumn<U> create(Class<U> enumClass, String name) {
        return new EnumColumn(enumClass, name, new IntArrayList(DEFAULT_ARRAY_SIZE));
    }

    public static <U extends Enum> EnumColumn<U> create(Class<U> enumClass, String name, int initialSize) {
        EnumColumn column = new EnumColumn<U>(enumClass, name, new IntArrayList(initialSize));
        for (int i = 0; i < initialSize; i++) {
            column.appendMissing();
        }
        return column;
    }

    public static <U extends Enum> EnumColumn<U> create(Class<U> enumClass, String name, List<U> data) {
        EnumColumn<U> column = new EnumColumn(enumClass, name, new IntArrayList(data.size()));
        for (U enu : data) {
            column.append(enu);
        }
        return column;
    }

    public static <U extends Enum> EnumColumn<U> create(Class<U> enumClass, String name, U[] data) {
        EnumColumn column = new EnumColumn(enumClass, name, new IntArrayList(data.length));
        for (U enu : data) {
            column.append(enu);
        }
        return column;
    }

    public static boolean valueIsMissing(int i) {
        return i == MISSING_VALUE;
    }

    private T toEnum(int ordinal) {
        return ordinal != MISSING_VALUE ? values[ordinal] : null;
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public EnumColumn subset(final int[] rows) {
        final EnumColumn c = this.emptyCopy();
        for (final int row : rows) {
            c.appendInternal(getIntInternal(row));
        }
        return c;
    }

    public EnumColumn appendInternal(int f) {
        data.add(f);
        return this;
    }

    @Override
    public IntArrayList data() {
        return data;
    }

    public EnumColumn set(int index, int value) {
        data.set(index, value);
        return this;
    }

    @Override
    public EnumColumn set(int index, T value) {
        data.set(index, value.ordinal());
        return this;
    }

    public void setPrintFormatter(EnumFormatter formatter) {
        Preconditions.checkNotNull(formatter);
        this.printFormatter = formatter;
    }

    @Override
    public String getString(int row) {
        return printFormatter.format(toEnum(getIntInternal(row)));
    }

    @Override
    public String getUnformattedString(int row) {
        return toEnum(getIntInternal(row)).name();
    }

    @Override
    public EnumColumn emptyCopy() {
        EnumColumn empty = create(enumClass, name());
        empty.printFormatter = printFormatter;
        return empty;
    }

    @Override
    public EnumColumn emptyCopy(int rowSize) {
        EnumColumn copy = create(enumClass, name(), rowSize);
        copy.printFormatter = printFormatter;
        return copy;
    }

    @Override
    public EnumColumn copy() {
        EnumColumn copy = emptyCopy(data.size());
        copy.data = data.clone();
        return copy;
    }

    @Override
    public void clear() {
        data.clear();
    }

    @Override
    public EnumColumn lead(int n) {
        EnumColumn column = lag(-n);
        column.setName(name() + " lead(" + n + ")");
        return column;
    }

    @Override
    public EnumColumn lag(int n) {
        int srcPos = n >= 0 ? 0 : 0 - n;
        int[] dest = new int[size()];
        int destPos = n <= 0 ? 0 : n;
        int length = n >= 0 ? size() - n : size() + n;

        for (int i = 0; i < size(); i++) {
            dest[i] = MISSING_VALUE;
        }

        System.arraycopy(data.toIntArray(), srcPos, dest, destPos, length);

        EnumColumn copy = emptyCopy(size());
        copy.data = new IntArrayList(dest);
        copy.setName(name() + " lag(" + n + ")");
        return copy;
    }

    @Override
    public void sortAscending() {
        Arrays.parallelSort(data.elements());
    }

    @Override
    public void sortDescending() {
        IntArrays.parallelQuickSort(data.elements(), reverseIntComparator);
    }

    @Override
    public int countUnique() {
        IntSet ints = new IntOpenHashSet(size());
        for (int i = 0; i < size(); i++) {
            ints.add(data.getInt(i));
        }
        return ints.size();
    }

    @Override
    public EnumColumn unique() {
        IntSet ints = new IntOpenHashSet(data.size());
        for (int i = 0; i < size(); i++) {
            ints.add(data.getInt(i));
        }
        EnumColumn copy = emptyCopy(ints.size());
        copy.setName(name() + " Unique values");
        copy.data = IntArrayList.wrap(ints.toIntArray());
        return copy;
    }

    @Override
    public EnumColumn append(final Column<T> column) {
        Preconditions.checkArgument(column.type() == this.type());
        EnumColumn enumColumn = (EnumColumn) column;
        final int size = enumColumn.size();
        for (int i = 0; i < size; i++) {
            appendInternal(enumColumn.getIntInternal(i));
        }
        return this;
    }

    @Override
    public EnumColumn append(Column<T> column, int row) {
        Preconditions.checkArgument(column.type() == this.type());
        return appendInternal(((EnumColumn) column).getIntInternal(row));
    }

    @Override
    public EnumColumn set(int row, Column<T> column, int sourceRow) {
        Preconditions.checkArgument(column.type() == this.type());
        return set(row, ((EnumColumn) column).getIntInternal(sourceRow));
    }

    @Override
    public T max() {
        if (isEmpty()) {
            return null;
        }

        Integer max = null;
        for (int aData : data) {
            if (EnumColumn.MISSING_VALUE != aData) {
                if (max == null) {
                    max = aData;
                } else {
                    max = (max > aData) ? max : aData;
                }
            }
        }

        if (max == null) {
            return null;
        }
        return toEnum(max);
    }

    @Override
    public T min() {
        if (isEmpty()) {
            return null;
        }

        Integer min = null;
        for (int aData : data) {
            if (EnumColumn.MISSING_VALUE != aData) {
                if (min == null) {
                    min = aData;
                } else {
                    min = (min < aData) ? min : aData;
                }
            }
        }
        if (min == null) {
            return null;
        }
        return toEnum(min);
    }

    /**
     * Conditionally update this column, replacing current values with newValue for all rows where the current value
     * matches the selection criteria
     * <p>
     * Example:
     * myColumn.set(myColumn.valueIsMissing(), T.now()); // no more missing values
     */
    public EnumColumn set(Selection rowSelection, T newValue) {
        int packed = newValue.ordinal();
        for (int row : rowSelection) {
            set(row, packed);
        }
        return this;
    }

    @Override
    public EnumColumn appendMissing() {
        appendInternal(MISSING_VALUE);
        return this;
    }

    @Override
    public T get(int index) {
        return toEnum(getIntInternal(index));
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public IntComparator rowComparator() {
        return comparator;
    }

    @Override
    public EnumColumn append(T value) {
        return this.appendInternal(value.ordinal());
    }

    @Override
    public EnumColumn appendObj(Object obj) {
        if (obj == null) {
            return appendMissing();
        }
        if (!(obj.getClass().isAssignableFrom(enumClass))) {
            throw new IllegalArgumentException(String.format("Cannot append '%s' to '%s'", obj.getClass().getName(), enumClass.getName()));
        }
        return append((T) obj);
    }

    @Override
    public EnumColumn appendCell(String string) {
        T value = defaultParser.parse(string);
        return appendInternal(value != null ? value.ordinal() : MISSING_VALUE);
    }

    @Override
    public EnumColumn appendCell(String string, AbstractParser<?> parser) {
        return appendObj(parser.parse(string));
    }

    @Override
    public int getIntInternal(int index) {
        return data.getInt(index);
    }

    /**
     * Returns a table of dates and the number of observations of those dates
     *
     * @return the summary table
     */
    @Override
    public Table summary() {

        Table table = Table.create("Column: " + name());
        StringColumn measure = StringColumn.create("Measure");
        StringColumn value = StringColumn.create("Value");
        table.addColumns(measure);
        table.addColumns(value);

        measure.append("Count");
        value.append(String.valueOf(size()));

        measure.append("Missing");
        value.append(String.valueOf(countMissing()));

        measure.append("Earliest");
        value.append(String.valueOf(min()));

        measure.append("Latest");
        value.append(String.valueOf(max()));

        return table;
    }

    /**
     * Returns the count of missing values in this column
     */
    @Override
    public int countMissing() {
        int count = 0;
        for (int i = 0; i < size(); i++) {
            if (getIntInternal(i) == MISSING_VALUE) {
                count++;
            }
        }
        return count;
    }

    /**
     * Returns the largest ("top") n values in the column
     *
     * @param n The maximum number of records to return. The actual number will be smaller if n is greater than the
     *          number of observations in the column
     * @return A list, possibly empty, of the largest observations
     */
    public List<T> top(int n) {
        List<T> top = new ArrayList<>();
        int[] values = data.toIntArray();
        IntArrays.parallelQuickSort(values, DescendingIntComparator.instance());
        for (int i = 0; i < n && i < values.length; i++) {
            top.add(toEnum(values[i]));
        }
        return top;
    }

    /**
     * Returns the smallest ("bottom") n values in the column
     *
     * @param n The maximum number of records to return. The actual number will be smaller if n is greater than the
     *          number of observations in the column
     * @return A list, possibly empty, of the smallest n observations
     */
    public List<T> bottom(int n) {
        List<T> bottom = new ArrayList<>();
        int[] values = data.toIntArray();
        IntArrays.parallelQuickSort(values);
        for (int i = 0; i < n && i < values.length; i++) {
            bottom.add(toEnum(values[i]));
        }
        return bottom;
    }

    public IntIterator intIterator() {
        return data.iterator();
    }

    @Override
    public EnumColumn removeMissing() {
        EnumColumn noMissing = emptyCopy();
        IntIterator iterator = intIterator();
        while (iterator.hasNext()) {
            int i = iterator.nextInt();
            if (!valueIsMissing(i)) {
                noMissing.appendInternal(i);
            }
        }
        return noMissing;
    }

    public List<T> asList() {
        List<T> enums = new ArrayList<>(size());
        for (T enumItem : this) {
            enums.add(enumItem);
        }
        return enums;
    }

    @Override
    public EnumColumn where(Selection selection) {
        return subset(selection.toArray());
    }

    public Set<T> asSet() {
        Set<T> dates = new HashSet<>();
        EnumColumn<T> unique = unique();
        for (T d : unique) {
            dates.add(d);
        }
        return dates;
    }

    public boolean contains(T enumItem) {
        return data().contains(enumItem.ordinal());
    }

    @Override
    public Column<T> setMissing(int i) {
        return set(i, EnumColumnType.missingValueIndicator());
    }

    public double[] asDoubleArray() {
        double[] doubles = new double[size()];
        for (int i = 0; i < size(); i++) {
            doubles[i] = data.getInt(i);
        }
        return doubles;
    }

    @Override
    public boolean isMissing(int rowNumber) {
        return valueIsMissing(getIntInternal(rowNumber));
    }

    public double getDouble(int i) {
        return getIntInternal(i);
    }

    @Override
    public int byteSize() {
        return type().byteSize();
    }

    /**
     * Returns the contents of the cell at rowNumber as a byte[]
     *
     * @param rowNumber the number of the row as int
     */
    @Override
    public byte[] asBytes(int rowNumber) {
        return ByteBuffer.allocate(byteSize()).putInt(getIntInternal(rowNumber)).array();
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<T> iterator() {

        return new Iterator<T>() {

            final IntIterator intIterator = intIterator();

            @Override
            public boolean hasNext() {
                return intIterator.hasNext();
            }

            @Override
            public T next() {
                return values[intIterator.nextInt()];
            }
        };
    }

    // fillWith methods

    private EnumColumn fillWith(int count, Iterator<T> iterator, Consumer<T> acceptor) {
        for (int r = 0; r < count; r++) {
            if (!iterator.hasNext()) {
                break;
            }
            acceptor.accept(iterator.next());
        }
        return this;
    }

    @Override
    public EnumColumn fillWith(Iterator<T> iterator) {
        int[] r = new int[1];
        fillWith(size(), iterator, date -> set(r[0]++, date));
        return this;
    }

    private EnumColumn fillWith(int count, Iterable<T> iterable, Consumer<T> acceptor) {
        Iterator<T> iterator = null;
        for (int r = 0; r < count; r++) {
            if (iterator == null || (!iterator.hasNext())) {
                iterator = iterable.iterator();
                if (!iterator.hasNext()) {
                    break;
                }
            }
            acceptor.accept(iterator.next());
        }
        return this;
    }

    @Override
    public EnumColumn<T> fillWith(Iterable<T> iterable) {
        int[] r = new int[1];
        fillWith(size(), iterable, date -> set(r[0]++, date));
        return this;
    }

    private EnumColumn<T> fillWith(int count, Supplier<T> supplier, Consumer<T> acceptor) {
        for (int r = 0; r < count; r++) {
            try {
                acceptor.accept(supplier.get());
            } catch (Exception e) {
                break;
            }
        }
        return this;
    }

    @Override
    public EnumColumn<T> fillWith(Supplier<T> supplier) {
        int[] r = new int[1];
        fillWith(size(), supplier, date -> set(r[0]++, date));
        return this;
    }

    @Override
    public T[] asObjectArray() {
        final T[] output = (T[]) Array.newInstance(enumClass, data.size());
        for (int i = 0; i < data.size(); i++) {
            output[i] = get(i);
        }
        return output;
    }

    @Override
    public int compare(T o1, T o2) {
        return o1.compareTo(o2);
    }

    @Override
    public EnumColumn<T> setName(String name) {
        return (EnumColumn<T>) super.setName(name);
    }

    @Override
    public EnumColumn<T> filter(Predicate<? super T> test) {
        return (EnumColumn<T>) super.filter(test);
    }

    @Override
    public EnumColumn<T> sorted(Comparator<? super T> comp) {
        return (EnumColumn<T>) super.sorted(comp);
    }

    @Override
    public EnumColumn<T> map(Function<? super T, ? extends T> fun) {
        return (EnumColumn<T>) super.map(fun);
    }

    @Override
    public EnumColumn<T> min(Column<T> other) {
        return (EnumColumn<T>) super.min(other);
    }

    @Override
    public EnumColumn<T> max(Column<T> other) {
        return (EnumColumn<T>) super.max(other);
    }

    @Override
    public EnumColumn<T> set(Selection condition, Column<T> other) {
        return (EnumColumn<T>) super.set(condition, other);
    }

    @Override
    public EnumColumn<T> first(int numRows) {
        return (EnumColumn<T>) super.first(numRows);
    }

    @Override
    public EnumColumn<T> last(int numRows) {
        return (EnumColumn<T>) super.last(numRows);
    }

    @Override
    public EnumColumn<T> inRange(int start, int end) {
        return (EnumColumn<T>) super.inRange(start, end);
    }

    @Override
    public EnumColumn<T> sampleN(int n) {
        return (EnumColumn<T>) super.sampleN(n);
    }

    @Override
    public EnumColumn<T> sampleX(double proportion) {
        return (EnumColumn<T>) super.sampleX(proportion);
    }
}
