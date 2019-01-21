package com.lchclearnet.table.column.strings;

import com.lchclearnet.table.Table;
import com.lchclearnet.table.column.booleans.BooleanColumn;
import com.lchclearnet.table.selection.BitmapBackedSelection;
import com.lchclearnet.table.selection.Selection;
import it.unimi.dsi.fastutil.ints.IntArrayList;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public interface DictionaryMap {

    void sortDescending();

    void sortAscending();

    String getValueForKey(int key);

    int size();

    String getValueForIndex(int rowIndex);

    int countOccurrences(String value);

    Set<String> asSet();

    IntArrayList dataAsIntArray();

    int getKeyForIndex(int i);

    int firstIndexOf(String string);

    Object[] asObjectArray();

    Selection selectIsIn(String... strings);

    Selection selectIsIn(Collection<String> strings);

    void append(String value) throws NoKeysAvailableException;

    void set(int rowIndex, String stringValue) throws NoKeysAvailableException;

    void clear();

    int countUnique();

    Table countByCategory(String columnName);

    Selection isEqualTo(String string);

    default Selection isNotEqualTo(String string) {
        Selection selection = new BitmapBackedSelection();
        selection.addRange(0, size());
        selection.andNot(isEqualTo(string));
        return selection;
    }

    List<BooleanColumn> getDummies();

    /**
     * Returns the contents of the cell at rowNumber as a byte[]
     */
    public byte[] asBytes(int rowNumber);

    /**
     * Returns the count of missing values in this column
     */
    int countMissing();

    Iterator<String> iterator();

    void appendMissing();

    boolean isMissing(int rowNumber);

    DictionaryMap promoteYourself();
}
