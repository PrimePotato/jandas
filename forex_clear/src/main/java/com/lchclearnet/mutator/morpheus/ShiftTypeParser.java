package com.lchclearnet.mutator.morpheus;

import com.lchclearnet.utils.ShiftType;
import com.zavtech.morpheus.util.functions.FunctionStyle;
import com.zavtech.morpheus.util.functions.ToBooleanFunction;
import com.zavtech.morpheus.util.text.FormatException;
import com.zavtech.morpheus.util.text.parser.Parser;

import java.util.*;


/**
 * A Parser implementation for Currency objects.
 */
public class ShiftTypeParser extends Parser<ShiftType> {

    private static final Set<String> DEFAULT_NULL_SET = new HashSet<>(Arrays.asList("null", "NULL", "Null", "N/A", "n/a", "-"));
    private static final ToBooleanFunction<String> DEFAULT_NULL_CHECKER = value -> value == null || value.trim().length() == 0 || value.contains("Record Count") || DEFAULT_NULL_SET.contains(value);

    private static final Map<String, ShiftType> enumMap = new HashMap<>();

    /**
     * Default Constructor with the default null checker
     */
    public ShiftTypeParser() {
        this(DEFAULT_NULL_CHECKER);
    }

    /**
     * Constructor
     *
     * @param nullChecker the null checker function
     */
    public ShiftTypeParser(ToBooleanFunction<String> nullChecker) {
        super(FunctionStyle.OBJECT, ShiftType.class, nullChecker);
        for (ShiftType shiftType : ShiftType.class.getEnumConstants()) {
            for (String value : shiftType.getValues()) {
                enumMap.put(value, shiftType);
            }
        }
    }


    @Override
    public Parser<ShiftType> optimize(String value) {
        return this;
    }

    @Override
    public final boolean isSupported(String value) {
        return getNullChecker().applyAsBoolean(value) ? false : enumMap.containsKey(value);
    }

    @Override
    public final ShiftType apply(String value) {
        try {
            return getNullChecker().applyAsBoolean(value) ? null : enumMap.get(value.toUpperCase());
        } catch (Exception ex) {
            throw new FormatException("Failed to parse value into Currency: " + value, ex);
        }
    }
}
