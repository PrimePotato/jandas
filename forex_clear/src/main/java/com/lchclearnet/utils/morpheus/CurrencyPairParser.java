package com.lchclearnet.utils.morpheus;

import com.lchclearnet.utils.CurrencyPair;
import com.zavtech.morpheus.util.functions.FunctionStyle;
import com.zavtech.morpheus.util.functions.ToBooleanFunction;
import com.zavtech.morpheus.util.text.FormatException;
import com.zavtech.morpheus.util.text.parser.Parser;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


/**
 * A Parser implementation for CurrencyPair objects.
 */
public class CurrencyPairParser extends Parser<CurrencyPair> {

    private static final Set<String> DEFAULT_NULL_SET = new HashSet<>(Arrays.asList("null", "NULL", "Null", "N/A", "n/a", "-"));
    private static final ToBooleanFunction<String> DEFAULT_NULL_CHECKER = value -> value == null || value.trim().length() == 0 || value.contains("Record Count") || DEFAULT_NULL_SET.contains(value);


    /**
     * Default Constructor with the default null checker
     */
    public CurrencyPairParser() {
        this(DEFAULT_NULL_CHECKER);
    }

    /**
     * Constructor
     *
     * @param nullChecker the null checker function
     */
    public CurrencyPairParser(ToBooleanFunction<String> nullChecker) {
        super(FunctionStyle.OBJECT, CurrencyPair.class, nullChecker);
    }


    @Override
    public Parser<CurrencyPair> optimize(String value) {
        return this;
    }

    @Override
    public final boolean isSupported(String value) {
        return getNullChecker().applyAsBoolean(value) ? false : CurrencyPair.CCYPAIR_PATTERN.matcher(value).reset(value).matches();
    }

    @Override
    public final CurrencyPair apply(String value) {
        try {
            return getNullChecker().applyAsBoolean(value) ? null : CurrencyPair.parse(value);
        } catch (Exception ex) {
            throw new FormatException("Failed to parse value into CurrencyPair: " + value, ex);
        }
    }
}
