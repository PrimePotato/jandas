package com.lchclearnet.jandas.column.parsers;

import com.lchclearnet.table.column.numbers.FloatColumnType;
import java.util.Arrays;

public class FloatParser extends AbstractParser<Float> {

    public FloatParser() {
        super((Iterable<String>) null);
    }

    public FloatParser(String missingValueStrings) {
        super(missingValueStrings);
    }

    public FloatParser(String[] missingValueStrings) {
        super(Arrays.asList(missingValueStrings));
    }

    @Override
    public boolean canParse(String s) {
        if (isMissing(s)) {
            return true;
        }
        try {
            Float.parseFloat(AbstractParser.remove(s, ','));
            return true;
        } catch (NumberFormatException e) {
            // it's all part of the plan
            return false;
        }
    }

    @Override
    public Float parse(String s) {
        return parseFloat(s);
    }

  @Override
  public Class<Float> elementClass() {

    return Float.class;
  }

  @Override
    public float parseFloat(String s) {
        if (isMissing(s)) return FloatColumnType.missingValueIndicator();

        return Float.parseFloat(AbstractParser.remove(s, ','));
    }
}
