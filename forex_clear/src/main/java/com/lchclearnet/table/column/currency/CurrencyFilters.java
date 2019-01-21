package com.lchclearnet.table.column.currency;

import com.lchclearnet.table.Column;
import com.lchclearnet.table.predicates.IntBiPredicate;
import com.lchclearnet.table.selection.BitmapBackedSelection;
import com.lchclearnet.table.selection.Selection;
import com.lchclearnet.utils.Currency;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntIterator;

import java.util.function.BiPredicate;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

import static com.lchclearnet.table.column.currency.CurrencyPredicates.*;


public interface CurrencyFilters extends Column<Currency> {

    CurrencyColumn where(Selection selection);

    /**
     * This version operates on predicates that treat the given IntPredicate as operating on a packed local time
     * This is much more efficient that using a LocalTimePredicate, but requires that the developer understand the
     * semantics of packedLocalTimes
     */
    default Selection eval(IntPredicate predicate) {
        Selection selection = new BitmapBackedSelection();
        for (int idx = 0; idx < data().size(); idx++) {
            int next = data().getInt(idx);
            if (predicate.test(next)) {
                selection.add(idx);
            }
        }
        return selection;
    }

    default Selection eval(IntBiPredicate predicate, int value) {
        Selection selection = new BitmapBackedSelection();
        for (int idx = 0; idx < data().size(); idx++) {
            int next = data().getInt(idx);
            if (predicate.test(next, value)) {
                selection.add(idx);
            }
        }
        return selection;
    }

    default Selection eval(IntBiPredicate predicate, CurrencyColumn otherColumn) {
        Selection selection = new BitmapBackedSelection();
        for (int idx = 0; idx < size(); idx++) {
            if (predicate.test(this.getIntInternal(idx), otherColumn.getIntInternal(idx))) {
                selection.add(idx);
            }
        }
        return selection;
    }

    int getIntInternal(int idx);

    default Selection eval(BiPredicate<Currency, Currency> predicate, Currency valueToCompare) {
        Selection selection = new BitmapBackedSelection();
        for (int idx = 0; idx < size(); idx++) {
            if (predicate.test(get(idx), valueToCompare)) {
                selection.add(idx);
            }
        }
        return selection;
    }

    /**
     * Returns a selection formed by applying the given predicate
     * <p>
     * Prefer using an IntPredicate where the int is a PackedDate, as this version creates a date object
     * for each value in the column
     */
    default Selection eval(Predicate<Currency> predicate) {
        Selection selection = new BitmapBackedSelection();
        for (int idx = 0; idx < size(); idx++) {
            if (predicate.test(get(idx))) {
                selection.add(idx);
            }
        }
        return selection;
    }

    default Selection isAfter(Currency value) {
        return eval(isGreaterThan, value.ordinal());
    }


    default Selection isBefore(Currency value) {
        return eval(isLessThan, value.ordinal());
    }

    default Selection isEqualTo(Currency value) {
        return eval(isEqualTo, value.ordinal());
    }

    default Selection isGreaterThanOrEqualTo(Currency value) {
        return eval(isLessThanOrEqualTo, value.ordinal());
    }

    default Selection isLessThanOrEqualTo(Currency value) {
        return eval(isGreaterThanOrEqualTo, value.ordinal());
    }

    /**
     * Returns a bitmap flagging the records for which the value in this column is equal to the value in the given
     * column
     * Columnwise isEqualTo.
     */
    default Selection isEqualTo(CurrencyColumn column) {
        Selection results = new BitmapBackedSelection();
        int i = 0;
        IntIterator intIterator = column.intIterator();
        for (int next : data()) {
            if (next == intIterator.nextInt()) {
                results.add(i);
            }
            i++;
        }
        return results;
    }

    default Selection isNotEqualTo(CurrencyColumn column) {
        Selection results = Selection.withRange(0, size());
        return results.andNot(isEqualTo(column));
    }

    default Selection isOnOrBefore(CurrencyColumn column) {
        Selection results = Selection.withRange(0, size());
        return results.andNot(isAfter(column));
    }

    default Selection isOnOrAfter(CurrencyColumn column) {
        Selection results = Selection.withRange(0, size());
        return results.andNot(isBefore(column));
    }

    default Selection isAfter(CurrencyColumn column) {
        Selection results = new BitmapBackedSelection();
        int i = 0;
        IntIterator intIterator = column.intIterator();
        for (long next : data()) {
            if (next > intIterator.nextInt()) {
                results.add(i);
            }
            i++;
        }
        return results;
    }

    default Selection isBefore(CurrencyColumn column) {
        Selection results = new BitmapBackedSelection();
        int i = 0;
        IntIterator intIterator = column.intIterator();
        for (long next : data()) {
            if (next < intIterator.nextInt()) {
                results.add(i);
            }
            i++;
        }
        return results;
    }

    @Override
    default Selection isMissing() {
        return eval(isMissing);
    }

    @Override
    default Selection isNotMissing() {
        return eval(isNotMissing);
    }

    IntArrayList data();
}
