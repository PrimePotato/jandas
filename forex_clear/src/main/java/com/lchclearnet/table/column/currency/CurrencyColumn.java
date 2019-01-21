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

package com.lchclearnet.table.column.currency;

import com.google.common.base.Preconditions;
import com.lchclearnet.table.Column;
import com.lchclearnet.table.Table;
import com.lchclearnet.table.column.AbstractColumn;
import com.lchclearnet.table.column.CategoricalColumn;
import com.lchclearnet.table.column.parsers.AbstractParser;
import com.lchclearnet.table.column.strings.StringColumn;
import com.lchclearnet.table.selection.Selection;
import com.lchclearnet.table.sorting.comparators.DescendingIntComparator;
import com.lchclearnet.utils.Currency;
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
public class CurrencyColumn extends AbstractColumn<Currency> implements CurrencyFilters, CurrencyFillers,
        CurrencyMapFunctions, CategoricalColumn<Currency> {

    public static final int MISSING_VALUE = CurrencyColumnType.missingValueIndicator();

    private static Currency[] values = Currency.values();
    private final IntComparator reverseIntComparator = DescendingIntComparator.instance();
    private IntArrayList data;
    private final IntComparator comparator = (r1, r2) -> {
        final int f1 = getIntInternal(r1);
        int f2 = getIntInternal(r2);
        return Integer.compare(f1, f2);
    };
    private CurrencyFormatter printFormatter = new CurrencyFormatter();

    private CurrencyColumn(String name, IntArrayList data) {
        super(CurrencyColumnType.INSTANCE, name);
        this.data = data;
    }

    private static Currency toCurrency(int ordinal) {
        return ordinal != MISSING_VALUE ? values[ordinal] : null;
    }

    public static CurrencyColumn create(final String name) {
        return new CurrencyColumn(name, new IntArrayList(DEFAULT_ARRAY_SIZE));
    }

    public static CurrencyColumn create(final String name, final int initialSize) {
        CurrencyColumn column = new CurrencyColumn(name, new IntArrayList(initialSize));
        for (int i = 0; i < initialSize; i++) {
            column.appendMissing();
        }
        return column;
    }

    public static CurrencyColumn create(String name, List<Currency> data) {
        CurrencyColumn column = new CurrencyColumn(name, new IntArrayList(data.size()));
        for (Currency date : data) {
            column.append(date);
        }
        return column;
    }

    public static CurrencyColumn create(String name, Currency[] data) {
        CurrencyColumn column = new CurrencyColumn(name, new IntArrayList(data.length));
        for (Currency date : data) {
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
    public CurrencyColumn subset(final int[] rows) {
        final CurrencyColumn c = this.emptyCopy();
        for (final int row : rows) {
            c.appendInternal(getIntInternal(row));
        }
        return c;
    }

    public CurrencyColumn appendInternal(int f) {
        data.add(f);
        return this;
    }

    @Override
    public IntArrayList data() {
        return data;
    }

    public CurrencyColumn set(int index, int value) {
        data.set(index, value);
        return this;
    }

    @Override
    public CurrencyColumn set(int index, Currency value) {
        data.set(index, value.ordinal());
        return this;
    }

    public void setPrintFormatter(CurrencyFormatter formatter) {
        Preconditions.checkNotNull(formatter);
        this.printFormatter = formatter;
    }

    @Override
    public String getString(int row) {
        return printFormatter.format(getCurrency(row));
    }

    @Override
    public String getUnformattedString(int row) {
        return values[getPackedCurrency(row)].getCurrencyCode();

    }

    @Override
    public CurrencyColumn emptyCopy() {
        CurrencyColumn empty = create(name());
        empty.printFormatter = printFormatter;
        return empty;
    }

    @Override
    public CurrencyColumn emptyCopy(int rowSize) {
        CurrencyColumn copy = create(name(), rowSize);
        copy.printFormatter = printFormatter;
        return copy;
    }

    @Override
    public CurrencyColumn copy() {
        CurrencyColumn copy = emptyCopy(data.size());
        copy.data = data.clone();
        return copy;
    }

    @Override
    public void clear() {
        data.clear();
    }

    @Override
    public CurrencyColumn lead(int n) {
        CurrencyColumn column = lag(-n);
        column.setName(name() + " lead(" + n + ")");
        return column;
    }

    @Override
    public CurrencyColumn lag(int n) {
        int srcPos = n >= 0 ? 0 : 0 - n;
        int[] dest = new int[size()];
        int destPos = n <= 0 ? 0 : n;
        int length = n >= 0 ? size() - n : size() + n;

        for (int i = 0; i < size(); i++) {
            dest[i] = MISSING_VALUE;
        }

        System.arraycopy(data.toIntArray(), srcPos, dest, destPos, length);

        CurrencyColumn copy = emptyCopy(size());
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
    public CurrencyColumn unique() {
        IntSet ints = new IntOpenHashSet(data.size());
        for (int i = 0; i < size(); i++) {
            ints.add(data.getInt(i));
        }
        CurrencyColumn copy = emptyCopy(ints.size());
        copy.setName(name() + " Unique values");
        copy.data = IntArrayList.wrap(ints.toIntArray());
        return copy;
    }

    @Override
    public CurrencyColumn append(final Column<Currency> column) {
        Preconditions.checkArgument(column.type() == this.type());
        CurrencyColumn dateColumn = (CurrencyColumn) column;
        final int size = dateColumn.size();
        for (int i = 0; i < size; i++) {
            appendInternal(dateColumn.getPackedCurrency(i));
        }
        return this;
    }

    @Override
    public CurrencyColumn append(Column<Currency> column, int row) {
        Preconditions.checkArgument(column.type() == this.type());
        return appendInternal(((CurrencyColumn) column).getIntInternal(row));
    }

    @Override
    public CurrencyColumn set(int row, Column<Currency> column, int sourceRow) {
        Preconditions.checkArgument(column.type() == this.type());
        return set(row, ((CurrencyColumn) column).getIntInternal(sourceRow));
    }

    @Override
    public Currency max() {
        if (isEmpty()) {
            return null;
        }

        Integer max = null;
        for (int aData : data) {
            if (CurrencyColumn.MISSING_VALUE != aData) {
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
        return toCurrency(max);
    }

    @Override
    public Currency min() {
        if (isEmpty()) {
            return null;
        }

        Integer min = null;
        for (int aData : data) {
            if (CurrencyColumn.MISSING_VALUE != aData) {
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
        return toCurrency(min);
    }

    /**
     * Conditionally update this column, replacing current values with newValue for all rows where the current value
     * matches the selection criteria
     * <p>
     * Example:
     * myColumn.set(myColumn.valueIsMissing(), Currency.now()); // no more missing values
     */
    public CurrencyColumn set(Selection rowSelection, Currency newValue) {
        int packed = newValue.ordinal();
        for (int row : rowSelection) {
            set(row, packed);
        }
        return this;
    }

    @Override
    public CurrencyColumn appendMissing() {
        appendInternal(MISSING_VALUE);
        return this;
    }

    @Override
    public Currency get(int index) {
        return toCurrency(getPackedCurrency(index));
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
    public CurrencyColumn append(Currency value) {
        return this.appendInternal(value.ordinal());
    }

    @Override
    public CurrencyColumn appendObj(Object obj) {
        if (obj == null) {
            return appendMissing();
        }
        if (!(obj instanceof Currency)) {
            throw new IllegalArgumentException("Cannot append " + obj.getClass().getName() + " to DateColumn");
        }
        return append((Currency) obj);
    }

    @Override
    public CurrencyColumn appendCell(String string) {
        Currency value = CurrencyColumnType.DEFAULT_PARSER.parse(string);
        return appendInternal(value != null ? value.ordinal() : MISSING_VALUE);
    }

    @Override
    public CurrencyColumn appendCell(String string, AbstractParser<?> parser) {
        return appendObj(parser.parse(string));
    }

    @Override
    public int getIntInternal(int index) {
        return data.getInt(index);
    }

    protected int getPackedCurrency(int index) {
        return getIntInternal(index);
    }

    protected Currency getCurrency(int index) {
        return toCurrency(getIntInternal(index));
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
            if (getPackedCurrency(i) == MISSING_VALUE) {
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
    public List<Currency> top(int n) {
        List<Currency> top = new ArrayList<>();
        int[] values = data.toIntArray();
        IntArrays.parallelQuickSort(values, DescendingIntComparator.instance());
        for (int i = 0; i < n && i < values.length; i++) {
            top.add(toCurrency(values[i]));
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
    public List<Currency> bottom(int n) {
        List<Currency> bottom = new ArrayList<>();
        int[] values = data.toIntArray();
        IntArrays.parallelQuickSort(values);
        for (int i = 0; i < n && i < values.length; i++) {
            bottom.add(toCurrency(values[i]));
        }
        return bottom;
    }

    public IntIterator intIterator() {
        return data.iterator();
    }

    @Override
    public CurrencyColumn removeMissing() {
        CurrencyColumn noMissing = emptyCopy();
        IntIterator iterator = intIterator();
        while (iterator.hasNext()) {
            int i = iterator.nextInt();
            if (!valueIsMissing(i)) {
                noMissing.appendInternal(i);
            }
        }
        return noMissing;
    }

    public List<Currency> asList() {
        List<Currency> ccys = new ArrayList<>(size());
        for (Currency currency : this) {
            ccys.add(currency);
        }
        return ccys;
    }

    @Override
    public CurrencyColumn where(Selection selection) {
        return subset(selection.toArray());
    }

    public Set<Currency> asSet() {
        Set<Currency> dates = new HashSet<>();
        CurrencyColumn unique = unique();
        for (Currency d : unique) {
            dates.add(d);
        }
        return dates;
    }

    public boolean contains(Currency currency) {
        return data().contains(currency.ordinal());
    }

    @Override
    public Column<Currency> setMissing(int i) {
        return set(i, CurrencyColumnType.missingValueIndicator());
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
        return ByteBuffer.allocate(byteSize()).putInt(getPackedCurrency(rowNumber)).array();
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<Currency> iterator() {

        return new Iterator<Currency>() {

            final IntIterator intIterator = intIterator();

            @Override
            public boolean hasNext() {
                return intIterator.hasNext();
            }

            @Override
            public Currency next() {
                int nextInt = intIterator.nextInt();
                return nextInt != MISSING_VALUE ? Currency.values()[nextInt] : null;
            }
        };
    }

    // fillWith methods

    private CurrencyColumn fillWith(int count, Iterator<Currency> iterator, Consumer<Currency> acceptor) {
        for (int r = 0; r < count; r++) {
            if (!iterator.hasNext()) {
                break;
            }
            acceptor.accept(iterator.next());
        }
        return this;
    }

    @Override
    public CurrencyColumn fillWith(Iterator<Currency> iterator) {
        int[] r = new int[1];
        fillWith(size(), iterator, date -> set(r[0]++, date));
        return this;
    }

    private CurrencyColumn fillWith(int count, Iterable<Currency> iterable, Consumer<Currency> acceptor) {
        Iterator<Currency> iterator = null;
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
    public CurrencyColumn fillWith(Iterable<Currency> iterable) {
        int[] r = new int[1];
        fillWith(size(), iterable, date -> set(r[0]++, date));
        return this;
    }

    private CurrencyColumn fillWith(int count, Supplier<Currency> supplier, Consumer<Currency> acceptor) {
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
    public CurrencyColumn fillWith(Supplier<Currency> supplier) {
        int[] r = new int[1];
        fillWith(size(), supplier, date -> set(r[0]++, date));
        return this;
    }

    @Override
    public Object[] asObjectArray() {
        final Currency[] output = new Currency[data.size()];
        for (int i = 0; i < data.size(); i++) {
            output[i] = get(i);
        }
        return output;
    }

    @Override
    public int compare(Currency o1, Currency o2) {
        return o1.compareTo(o2);
    }

    @Override
    public CurrencyColumn setName(String name) {
        return (CurrencyColumn) super.setName(name);
    }

    @Override
    public CurrencyColumn filter(Predicate<? super Currency> test) {
        return (CurrencyColumn) super.filter(test);
    }

    @Override
    public CurrencyColumn sorted(Comparator<? super Currency> comp) {
        return (CurrencyColumn) super.sorted(comp);
    }

    @Override
    public CurrencyColumn map(Function<? super Currency, ? extends Currency> fun) {
        return (CurrencyColumn) super.map(fun);
    }

    @Override
    public CurrencyColumn min(Column<Currency> other) {
        return (CurrencyColumn) super.min(other);
    }

    @Override
    public CurrencyColumn max(Column<Currency> other) {
        return (CurrencyColumn) super.max(other);
    }

    @Override
    public CurrencyColumn set(Selection condition, Column<Currency> other) {
        return (CurrencyColumn) super.set(condition, other);
    }

    @Override
    public CurrencyColumn first(int numRows) {
        return (CurrencyColumn) super.first(numRows);
    }

    @Override
    public CurrencyColumn last(int numRows) {
        return (CurrencyColumn) super.last(numRows);
    }

    @Override
    public CurrencyColumn inRange(int start, int end) {
        return (CurrencyColumn) super.inRange(start, end);
    }

    @Override
    public CurrencyColumn sampleN(int n) {
        return (CurrencyColumn) super.sampleN(n);
    }

    @Override
    public CurrencyColumn sampleX(double proportion) {
        return (CurrencyColumn) super.sampleX(proportion);
    }
}
