package com.lchclearnet.jandas.column.parsers;

import static com.lchclearnet.table.column.booleans.BooleanColumn.BYTE_FALSE;
import static com.lchclearnet.table.column.booleans.BooleanColumn.BYTE_TRUE;
import static com.lchclearnet.table.column.booleans.BooleanColumn.MISSING_VALUE;

import java.util.Arrays;
import java.util.List;

public class BooleanParser extends AbstractParser<Boolean> {

    // A more restricted set of 'false' strings that is used for column type detection
    private static final List<String> FALSE_STRINGS_FOR_DETECTION =
            Arrays.asList("F", "f", "N", "n", "FALSE", "false", "False");

    // A more restricted set of 'true' strings that is used for column type detection
    private static final List<String> TRUE_STRINGS_FOR_DETECTION =
            Arrays.asList("T", "t", "Y", "y", "TRUE", "true", "True");

    // These Strings will convert to true booleans
    private static final List<String> TRUE_STRINGS =
            Arrays.asList("T", "t", "Y", "y", "TRUE", "true", "True", "1");

    // These Strings will convert to false booleans
    private static final List<String> FALSE_STRINGS =
            Arrays.asList("F", "f", "N", "n", "FALSE", "false", "False", "0");

    public BooleanParser() {
        super((Iterable<String>) null);
    }

    public BooleanParser(String missingValueStrings) {
        super(missingValueStrings);
    }

    public BooleanParser(String[] missingValueStrings) {
        super(Arrays.asList(missingValueStrings));
    }

    @Override
    public boolean canParse(String s) {
        return isMissing(s) || TRUE_STRINGS_FOR_DETECTION.contains(s) || FALSE_STRINGS_FOR_DETECTION.contains(s);
    }

    @Override
    public Boolean parse(String s) {
        if (isMissing(s)) {
            return null;
        } else if (TRUE_STRINGS.contains(s)) {
            return true;
        } else if (FALSE_STRINGS.contains(s)) {
            return false;
        } else {
            throw new IllegalArgumentException("Attempting to convert non-boolean value " + s + " to Boolean");
        }
    }

  @Override
  public Class<Boolean> elementClass() {

    return Boolean.class;
  }

  @Override
    public byte parseByte(String s) {
        if (isMissing(s)) {
            return MISSING_VALUE;
        } else if (TRUE_STRINGS.contains(s)) {
            return BYTE_TRUE;
        } else if (FALSE_STRINGS.contains(s)) {
            return BYTE_FALSE;
        } else {
            throw new IllegalArgumentException("Attempting to convert non-boolean value " + s + " to Boolean");
        }
    }
}

