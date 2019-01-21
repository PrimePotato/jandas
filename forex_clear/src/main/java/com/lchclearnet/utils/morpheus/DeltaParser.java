package com.lchclearnet.utils.morpheus;

import com.lchclearnet.utils.VolNode;
import com.zavtech.morpheus.util.functions.FunctionStyle;
import com.zavtech.morpheus.util.functions.ToBooleanFunction;
import com.zavtech.morpheus.util.text.FormatException;
import com.zavtech.morpheus.util.text.parser.Parser;

import java.util.*;


/**
 * A Parser implementation for Currency objects.
 */
public class DeltaParser extends Parser<VolNode> {

    private static final Set<String> DEFAULT_NULL_SET = new HashSet<>(Arrays.asList("null", "NULL", "Null", "N/A", "n/a", "-"));
    private static final ToBooleanFunction<String> DEFAULT_NULL_CHECKER = value -> value == null || value.trim().length() == 0 || value.contains("Record Count") || DEFAULT_NULL_SET.contains(value);

    private static final Map<String, VolNode> enumMap = new HashMap<>();

    /**
     * Default Constructor with the default null checker
     */
    public DeltaParser() {
        this(DEFAULT_NULL_CHECKER);
    }

    /**
     * Constructor
     *
     * @param nullChecker the null checker function
     */
    public DeltaParser(ToBooleanFunction<String> nullChecker) {
        super(FunctionStyle.OBJECT, VolNode.class, nullChecker);
        for (VolNode volNode : VolNode.class.getEnumConstants()) {
            for (String value : volNode.getValues()) {
                enumMap.put(value, volNode);
            }
        }
    }


    @Override
    public Parser<VolNode> optimize(String value) {
        return this;
    }

    @Override
    public final boolean isSupported(String value) {
        return getNullChecker().applyAsBoolean(value) ? false : enumMap.containsKey(value);
    }

    @Override
    public final VolNode apply(String value) {
        try {
            return getNullChecker().applyAsBoolean(value) ? null : enumMap.get(value.toUpperCase());
        } catch (Exception ex) {
            throw new FormatException("Failed to parse value into Currency: " + value, ex);
        }
    }
}
