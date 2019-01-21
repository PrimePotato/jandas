package com.lchclearnet.jandas.column.parsers;

import com.lchclearnet.table.column.numbers.LongColumnType;
import java.util.Arrays;

public class LongParser extends AbstractParser<Long> {

    public LongParser() {
        super((Iterable<String>) null);
    }

    public LongParser(String missingValueStrings) {
        super(missingValueStrings);
    }

    public LongParser(String[] missingValueStrings) {
        super(Arrays.asList(missingValueStrings));
    }

    @Override
    public boolean canParse(String str) {
        if (isMissing(str)) {
            return true;
        }
        String s = str;
        try {
            if (s.endsWith(".0")) {
                s = s.substring(0, s.length() - 2);
            }
            Long.parseLong(AbstractParser.remove(s, ','));
            return true;
        } catch (NumberFormatException e) {
            // it's all part of the plan
            return false;
        }
    }

    @Override
    public Long parse(String s) {
        return parseLong(s);
    }

  @Override
  public Class<Long> elementClass() {

    return Long.class;
  }

  @Override
    public double parseDouble(String str) {
        return parseLong(str);
    }

    @Override
    public long parseLong(String str) {
        if (isMissing(str)) return LongColumnType.missingValueIndicator();

        if (str.endsWith(".0")) {
            str = str.substring(0, str.length() - 2);
        }

        return Long.parseLong(AbstractParser.remove(str, ','));
    }
}
