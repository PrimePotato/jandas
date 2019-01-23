package com.lchclearnet.jandas.io.parsers;

import com.lchclearnet.utils.ShiftType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A Parser implementation for Currency objects.
 */
public class ShiftTypeParser extends EnumParser<ShiftType> {

    private static final Map<String, ShiftType> enumMap = new HashMap<>();

    public ShiftTypeParser() {
        this((Iterable<String>) null);
    }

    public ShiftTypeParser(String missingValueString) {
        this(Arrays.stream(missingValueString.split(",")).map(s -> s.trim()).collect(Collectors.toList()));
    }

    public ShiftTypeParser(String[] missingValueStrings) {
        this(Arrays.asList(missingValueStrings));
    }

    public ShiftTypeParser(Iterable<String> missingValueStrings) {
        super(ShiftType.class, missingValueStrings);
        enumMap.clear();
        for (ShiftType shiftType : ShiftType.class.getEnumConstants()) {
            for (String value : shiftType.getValues()) {
                enumMap.put(value.toUpperCase(), shiftType);
            }
        }
    }

    @Override
    public boolean canParse(String value) {
        return true;
    }

    @Override
    public ShiftType parse(String s) {
        if (isMissing(s)) return null;
        return enumMap.get(s.toUpperCase());
    }
}
