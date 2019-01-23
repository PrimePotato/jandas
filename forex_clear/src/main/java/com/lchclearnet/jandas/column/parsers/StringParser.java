package com.lchclearnet.jandas.column.parsers;

import com.lchclearnet.table.column.strings.StringColumnType;
import java.time.LocalTime;
import java.util.Arrays;

public class StringParser extends AbstractParser<String> {

    public StringParser() {
        super((Iterable<String>) null);
    }

    public StringParser(String missingValueStrings) {
        super(missingValueStrings);
    }

    public StringParser(String[] missingValueStrings) {
        super(Arrays.asList(missingValueStrings));
    }

    @Override
    public boolean canParse(String s) {
        return true;
    }

    @Override
    public String parse(String s) {
        if (isMissing(s)) return StringColumnType.missingValueIndicator();

        return s;
    }

  @Override
  public Class<String> elementClass() {

    return String.class;
  }
}