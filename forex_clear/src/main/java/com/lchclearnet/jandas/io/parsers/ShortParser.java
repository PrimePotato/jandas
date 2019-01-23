package com.lchclearnet.jandas.io.parsers;

import com.lchclearnet.table.column.numbers.ShortColumnType;
import java.util.Arrays;

public class ShortParser extends AbstractParser<Short> {

    public ShortParser() {
        super((Iterable<String>) null);
    }

    public ShortParser(String missingValueStrings) {
        super(missingValueStrings);
    }

    public ShortParser(String[] missingValueStrings) {
        super(Arrays.asList(missingValueStrings));
    }

    @Override
    public boolean canParse(String str) {
        if (isMissing(str)) return true;

        try {
            if (str.endsWith(".0")) {
                str = str.substring(0, str.length() - 2);
            }
            Short.parseShort(AbstractParser.remove(str, ','));
            return true;
        } catch (NumberFormatException e) {
            // it's all part of the plan
            return false;
        }
    }

    @Override
    public Short parse(String s) {
        return parseShort(s);
    }

  @Override
  public Class<Short> elementClass() {

    return Short.class;
  }

  @Override
    public double parseDouble(String s) {
        return parseInt(s);
    }

    @Override
    public short parseShort(String str) {
        if (isMissing(str)) return ShortColumnType.missingValueIndicator();

        if (str.endsWith(".0")) {
            str = str.substring(0, str.length() - 2);
        }
        return Short.parseShort(AbstractParser.remove(str, ','));
    }
}
