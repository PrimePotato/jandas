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

package com.lchclearnet.table.column.currencypair;

import com.google.common.base.Preconditions;
import com.lchclearnet.table.Column;
import com.lchclearnet.table.Table;
import com.lchclearnet.table.column.AbstractColumn;
import com.lchclearnet.table.column.CategoricalColumn;
import com.lchclearnet.table.column.parsers.AbstractParser;
import com.lchclearnet.table.column.strings.StringColumn;
import com.lchclearnet.table.selection.Selection;
import com.lchclearnet.table.sorting.comparators.DescendingIntComparator;
import com.lchclearnet.utils.CurrencyPair;
import it.unimi.dsi.fastutil.ints.*;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A column in a base table that contains float values
 */
public class CurrencyPairColumn extends AbstractColumn<CurrencyPair> implements CurrencyPairFilters, CurrencyPairFillers,
        CurrencyPairMapFunctions, CategoricalColumn<CurrencyPair> {

    public static final int MISSING_VALUE = CurrencyPairColumnType.missingValueIndicator();

    private final IntComparator reverseIntComparator = DescendingIntComparator.instance();
    private IntArrayList data;
    private final IntComparator comparator = (r1, r2) -> {
        final int f1 = getIntInternal(r1);
        int f2 = getIntInternal(r2);
        return Integer.compare(f1, f2);
    };
    private CurrencyPairFormatter printFormatter = new CurrencyPairFormatter();

    private CurrencyPairColumn(String name, IntArrayList data) {
        super(CurrencyPairColumnType.INSTANCE, name);
        this.data = data;
    }

    private static CurrencyPair toCurrencyPair(int ordinal) {
        return ordinal != MISSING_VALUE ? CurrencyPair.fromOrdinal(ordinal) : null;
    }

    public static CurrencyPairColumn create(final String name) {
        return new CurrencyPairColumn(name, new IntArrayList(DEFAULT_ARRAY_SIZE));
    }

    public static CurrencyPairColumn create(final String name, final int initialSize) {
        CurrencyPairColumn column = new CurrencyPairColumn(name, new IntArrayList(initialSize));
        for (int i = 0; i < initialSize; i++) {
            column.appendMissing();
        }
        return column;
    }

    public static CurrencyPairColumn create(String name, List<CurrencyPair> data) {
        CurrencyPairColumn column = new CurrencyPairColumn(name, new IntArrayList(data.size()));
        for (CurrencyPair date : data) {
            column.append(date);
        }
        return column;
    }

    public static CurrencyPairColumn create(String name, CurrencyPair[] data) {
        CurrencyPairColumn column = new CurrencyPairColumn(name, new IntArrayList(data.length));
        for (CurrencyPair date : data) {
            column.append(date);
        }
        return column;
    }

    public static boolean valueIsMissing(int i) {
        return i == MISSING_VALUE;
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public CurrencyPairColumn subset(final int[] rows) {
        final CurrencyPairColumn c = this.emptyCopy();
        for (final int row : rows) {
            c.appendInternal(getIntInternal(row));
        }
        return c;
    }

    public CurrencyPairColumn appendInternal(int f) {
        data.add(f);
        return this;
    }

    @Override
    public IntArrayList data() {
        return data;
    }

    public CurrencyPairColumn set(int index, int value) {
        data.set(index, value);
        return this;
    }

    @Override
    public CurrencyPairColumn set(int index, CurrencyPair value) {
        data.set(index, value.ordinal());
        return this;
    }

    public void setPrintFormatter(CurrencyPairFormatter formatter) {
        Preconditions.checkNotNull(formatter);
        this.printFormatter = formatter;
    }

    @Override
    public String getString(int row) {
        return printFormatter.format(getCurrencyPair(row));
    }

    @Override
    public String getUnformattedString(int row) {
        return getCurrencyPair(row).code();
    }

    @Override
    public CurrencyPairColumn emptyCopy() {
        CurrencyPairColumn empty = create(name());
        empty.printFormatter = printFormatter;
        return empty;
    }

    @Override
    public CurrencyPairColumn emptyCopy(int rowSize) {
        CurrencyPairColumn copy = create(name(), rowSize);
        copy.printFormatter = printFormatter;
        return copy;
    }

    @Override
    public CurrencyPairColumn copy() {
        CurrencyPairColumn copy = emptyCopy(data.size());
        copy.data = data.clone();
        return copy;
    }

    @Override
    public void clear() {
        data.clear();
    }

    @Override
    public CurrencyPairColumn lead(int n) {
        CurrencyPairColumn column = lag(-n);
        column.setName(name() + " lead(" + n + ")");
        return column;
    }

    @Override
    public CurrencyPairColumn lag(int n) {
        int srcPos = n >= 0 ? 0 : 0 - n;
        int[] dest = new int[size()];
        int destPos = n <= 0 ? 0 : n;
        int length = n >= 0 ? size() - n : size() + n;

        for (int i = 0; i < size(); i++) {
            dest[i] = MISSING_VALUE;
        }

        System.arraycopy(data.toIntArray(), srcPos, dest, destPos, length);

        CurrencyPairColumn copy = emptyCopy(size());
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
    public CurrencyPairColumn unique() {
        IntSet ints = new IntOpenHashSet(data.size());
        for (int i = 0; i < size(); i++) {
            ints.add(data.getInt(i));
        }
        CurrencyPairColumn copy = emptyCopy(ints.size());
        copy.setName(name() + " Unique values");
        copy.data = IntArrayList.wrap(ints.toIntArray());
        return copy;
    }

    @Override
    public CurrencyPairColumn append(final Column<CurrencyPair> column) {
        Preconditions.checkArgument(column.type() == this.type());
        CurrencyPairColumn dateColumn = (CurrencyPairColumn) column;
        final int size = dateColumn.size();
        for (int i = 0; i < size; i++) {
            appendInternal(dateColumn.getPackedCurrencyPair(i));
        }
        return this;
    }

    @Override
    public CurrencyPairColumn append(Column<CurrencyPair> column, int row) {
        Preconditions.checkArgument(column.type() == this.type());
        return appendInternal(((CurrencyPairColumn) column).getIntInternal(row));
    }

    @Override
    public CurrencyPairColumn set(int row, Column<CurrencyPair> column, int sourceRow) {
        Preconditions.checkArgument(column.type() == this.type());
        return set(row, ((CurrencyPairColumn) column).getIntInternal(sourceRow));
    }

    @Override
    public CurrencyPair max() {
        if (isEmpty()) {
            return null;
        }

        Integer max = null;
        for (int aData : data) {
            if (CurrencyPairColumn.MISSING_VALUE != aData) {
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
        return toCurrencyPair(max);
    }

    @Override
    public CurrencyPair min() {
        if (isEmpty()) {
            return null;
        }

        Integer min = null;
        for (int aData : data) {
            if (CurrencyPairColumn.MISSING_VALUE != aData) {
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
        return toCurrencyPair(min);
    }

    /**
     * Conditionally update this column, replacing current values with newValue for all rows where the current value
     * matches the selection criteria
     * <p>
     * Example:
     * myColumn.set(myColumn.valueIsMissing(), CurrencyPair.now()); // no more missing values
     */
    public CurrencyPairColumn set(Selection rowSelection, CurrencyPair newValue) {
        int packed = newValue.ordinal();
        for (int row : rowSelection) {
            set(row, packed);
        }
        return this;
    }

    @Override
    public CurrencyPairColumn appendMissing() {
        appendInternal(MISSING_VALUE);
        return this;
    }

    @Override
    public CurrencyPair get(int index) {
        return toCurrencyPair(getPackedCurrencyPair(index));
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
    public CurrencyPairColumn append(CurrencyPair value) {
        return this.appendInternal(value.ordinal());
    }

    @Override
    public CurrencyPairColumn appendObj(Object obj) {
        if (obj == null) {
            return appendMissing();
        }
        if (!(obj instanceof CurrencyPair)) {
            throw new IllegalArgumentException("Cannot append " + obj.getClass().getName() + " to DateColumn");
        }
        return append((CurrencyPair) obj);
    }

    @Override
    public CurrencyPairColumn appendCell(String string) {
        CurrencyPair value = CurrencyPairColumnType.DEFAULT_PARSER.parse(string);
        return appendInternal(value != null ? value.ordinal() : MISSING_VALUE);
    }

    @Override
    public CurrencyPairColumn appendCell(String string, AbstractParser<?> parser) {
        return appendObj(parser.parse(string));
    }

    @Override
    public int getIntInternal(int index) {
        return data.getInt(index);
    }

    protected int getPackedCurrencyPair(int index) {
        return getIntInternal(index);
    }

    protected CurrencyPair getCurrencyPair(int index) {
        return CurrencyPair.fromOrdinal(getIntInternal(index));
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
            if (getPackedCurrencyPair(i) == MISSING_VALUE) {
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
    public List<CurrencyPair> top(int n) {
        List<CurrencyPair> top = new ArrayList<>();
        int[] values = data.toIntArray();
        IntArrays.parallelQuickSort(values, DescendingIntComparator.instance());
        for (int i = 0; i < n && i < values.length; i++) {
            top.add(toCurrencyPair(values[i]));
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
    public List<CurrencyPair> bottom(int n) {
        List<CurrencyPair> bottom = new ArrayList<>();
        int[] values = data.toIntArray();
        IntArrays.parallelQuickSort(values);
        for (int i = 0; i < n && i < values.length; i++) {
            bottom.add(toCurrencyPair(values[i]));
        }
        return bottom;
    }

    public IntIterator intIterator() {
        return data.iterator();
    }

    @Override
    public CurrencyPairColumn removeMissing() {
        CurrencyPairColumn noMissing = emptyCopy();
        IntIterator iterator = intIterator();
        while (iterator.hasNext()) {
            int i = iterator.nextInt();
            if (!valueIsMissing(i)) {
                noMissing.appendInternal(i);
            }
        }
        return noMissing;
    }

    public List<CurrencyPair> asList() {
        List<CurrencyPair> dates = new ArrayList<>(size());
        for (CurrencyPair currencyPair : this) {
            dates.add(currencyPair);
        }
        return dates;
    }

    @Override
    public CurrencyPairColumn where(Selection selection) {
        return subset(selection.toArray());
    }

    public Set<CurrencyPair> asSet() {
        Set<CurrencyPair> dates = new HashSet<>();
        CurrencyPairColumn unique = unique();
        for (CurrencyPair d : unique) {
            dates.add(d);
        }
        return dates;
    }

    public boolean contains(CurrencyPair currencyPair) {
        return data().contains(currencyPair.ordinal());
    }

    @Override
    public Column<CurrencyPair> setMissing(int i) {
        return set(i, CurrencyPairColumnType.missingValueIndicator());
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
        return ByteBuffer.allocate(byteSize()).putInt(getPackedCurrencyPair(rowNumber)).array();
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<CurrencyPair> iterator() {

        return new Iterator<CurrencyPair>() {

            final IntIterator intIterator = intIterator();

            @Override
            public boolean hasNext() {

                return intIterator.hasNext();
            }

            @Override
            public CurrencyPair next() {
                int nextInt = intIterator.nextInt();
                return nextInt != MISSING_VALUE ? CurrencyPair.fromOrdinal(nextInt) : null;
            }
        };
    }

    // fillWith methods

    private CurrencyPairColumn fillWith(int count, Iterator<CurrencyPair> iterator, Consumer<CurrencyPair> acceptor) {
        for (int r = 0; r < count; r++) {
            if (!iterator.hasNext()) {
                break;
            }
            acceptor.accept(iterator.next());
        }
        return this;
    }

    @Override
    public CurrencyPairColumn fillWith(Iterator<CurrencyPair> iterator) {
        int[] r = new int[1];
        fillWith(size(), iterator, date -> set(r[0]++, date));
        return this;
    }

    private CurrencyPairColumn fillWith(int count, Iterable<CurrencyPair> iterable, Consumer<CurrencyPair> acceptor) {
        Iterator<CurrencyPair> iterator = null;
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
    public CurrencyPairColumn fillWith(Iterable<CurrencyPair> iterable) {
        int[] r = new int[1];
        fillWith(size(), iterable, date -> set(r[0]++, date));
        return this;
    }

    private CurrencyPairColumn fillWith(int count, Supplier<CurrencyPair> supplier, Consumer<CurrencyPair> acceptor) {
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
    public CurrencyPairColumn fillWith(Supplier<CurrencyPair> supplier) {
        int[] r = new int[1];
        fillWith(size(), supplier, date -> set(r[0]++, date));
        return this;
    }

    @Override
    public Object[] asObjectArray() {
        final CurrencyPair[] output = new CurrencyPair[data.size()];
        for (int i = 0; i < data.size(); i++) {
            output[i] = get(i);
        }
        return output;
    }

    @Override
    public int compare(CurrencyPair o1, CurrencyPair o2) {
        return o1.compareTo(o2);
    }

    @Override
    public CurrencyPairColumn setName(String name) {
        return (CurrencyPairColumn) super.setName(name);
    }

    @Override
    public CurrencyPairColumn filter(Predicate<? super CurrencyPair> test) {
        return (CurrencyPairColumn) super.filter(test);
    }

    @Override
    public CurrencyPairColumn sorted(Comparator<? super CurrencyPair> comp) {
        return (CurrencyPairColumn) super.sorted(comp);
    }

    @Override
    public CurrencyPairColumn map(Function<? super CurrencyPair, ? extends CurrencyPair> fun) {
        return (CurrencyPairColumn) super.map(fun);
    }

    @Override
    public CurrencyPairColumn min(Column<CurrencyPair> other) {
        return (CurrencyPairColumn) super.min(other);
    }

    @Override
    public CurrencyPairColumn max(Column<CurrencyPair> other) {
        return (CurrencyPairColumn) super.max(other);
    }

    @Override
    public CurrencyPairColumn set(Selection condition, Column<CurrencyPair> other) {
        return (CurrencyPairColumn) super.set(condition, other);
    }

    @Override
    public CurrencyPairColumn first(int numRows) {
        return (CurrencyPairColumn) super.first(numRows);
    }

    @Override
    public CurrencyPairColumn last(int numRows) {
        return (CurrencyPairColumn) super.last(numRows);
    }

    @Override
    public CurrencyPairColumn inRange(int start, int end) {
        return (CurrencyPairColumn) super.inRange(start, end);
    }

    @Override
    public CurrencyPairColumn sampleN(int n) {
        return (CurrencyPairColumn) super.sampleN(n);
    }

    @Override
    public CurrencyPairColumn sampleX(double proportion) {
        return (CurrencyPairColumn) super.sampleX(proportion);
    }
}
