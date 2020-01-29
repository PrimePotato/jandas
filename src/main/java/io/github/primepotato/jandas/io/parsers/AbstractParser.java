package io.github.primepotato.jandas.io.parsers;

import com.google.common.collect.ImmutableList;

import java.util.Arrays;
import java.util.stream.Collectors;


public abstract class AbstractParser<T> {

    public static final ImmutableList<String> MISSING_INDICATORS = ImmutableList.of(
            "null", "NULL", "Null",
            "N/A", "NA", "na", "n/a",
            "NaN", "nan", "Nan",
            "*", "-");
    protected ImmutableList<String> missingValueStrings;

    public AbstractParser(String missingValueString) {
        this(Arrays.stream(missingValueString.split(",")).map(s -> s.trim()).collect(Collectors.toList()));
    }

    public AbstractParser(Iterable<String> missingValueStrings) {
        this.missingValueStrings = missingValueStrings != null ? ImmutableList.copyOf(missingValueStrings) : MISSING_INDICATORS;
    }

    protected static String remove(final String str, final char remove) {
        if (str == null || str.indexOf(remove) == -1) {
            return str;
        }
        final char[] chars = str.toCharArray();
        int pos = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] != remove) {
                chars[pos++] = chars[i];
            }
        }
        return new String(chars, 0, pos);
    }

    public abstract boolean canParse(String s);

    public abstract T parse(String s);

    public abstract Class<T> elementClass();

    protected boolean isMissing(String s) {
        return s == null || s.isEmpty() || missingValueStrings.contains(s);
    }

    public byte parseByte(String s) {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + " doesn't support parsing to booleans");
    }

    public int parseInt(String s) {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + " doesn't support parsing to ints");
    }

    public short parseShort(String s) {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + " doesn't support parsing to shorts");
    }

    public long parseLong(String s) {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + " doesn't support parsing to longs");
    }

    public double parseDouble(String s) {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + " doesn't support parsing to doubles");
    }

    public float parseFloat(String s) {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + " doesn't support parsing to floats");
    }

}
