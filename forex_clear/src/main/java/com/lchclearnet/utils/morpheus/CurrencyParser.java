package com.lchclearnet.utils.morpheus;

import com.lchclearnet.utils.Currency;
import com.zavtech.morpheus.util.functions.FunctionStyle;
import com.zavtech.morpheus.util.functions.ToBooleanFunction;
import com.zavtech.morpheus.util.text.FormatException;
import com.zavtech.morpheus.util.text.parser.Parser;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


/**
 * A Parser implementation for Currency objects.
 */
public class CurrencyParser extends Parser<Currency> {

    /**
     * Regular expression to parse the textual format.
     * Three upper or lower case letters.
     */
    private static final Set<String> DEFAULT_NULL_SET = new HashSet<>(Arrays.asList("null", "NULL", "Null", "N/A", "n/a", "-"));
    private static final ToBooleanFunction<String> DEFAULT_NULL_CHECKER = value -> value == null || value.trim().length() == 0 || value.contains("Record Count") || DEFAULT_NULL_SET.contains(value);


    /**
     * Default Constructor with the default null checker
     */
    public CurrencyParser() {
        this(DEFAULT_NULL_CHECKER);
    }

    /**
     * Constructor
     *
     * @param nullChecker the null checker function
     */
    public CurrencyParser(ToBooleanFunction<String> nullChecker) {
        super(FunctionStyle.OBJECT, Currency.class, nullChecker);
    }


    @Override
    public Parser<Currency> optimize(String value) {
        return this;
    }

    @Override
    public final boolean isSupported(String value) {
        return getNullChecker().applyAsBoolean(value) ? false : Currency.matches(value);
    }

    @Override
    public final Currency apply(String value) {
        try {
            return getNullChecker().applyAsBoolean(value) ? null : Currency.parse(value.toUpperCase());
        } catch (Exception ex) {
            throw new FormatException("Failed to parse value into Currency: " + value, ex);
        }
    }
}
