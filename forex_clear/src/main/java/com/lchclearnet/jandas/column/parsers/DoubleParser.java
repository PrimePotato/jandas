package com.lchclearnet.jandas.column.parsers;

import com.lchclearnet.table.column.numbers.DoubleColumnType;
import java.util.Arrays;

public class DoubleParser extends AbstractParser<Double> {

    public DoubleParser() {
        super((Iterable<String>) null);
    }

    public DoubleParser(String missingValueStrings) {
        super(missingValueStrings);
    }

    public DoubleParser(String[] missingValueStrings) {
        super(Arrays.asList(missingValueStrings));
    }

    @Override
    public boolean canParse(String s) {
        if (isMissing(s)) {
            return true;
        }
        try {
            Double.parseDouble(AbstractParser.remove(s, ','));
            return true;
        } catch (NumberFormatException e) {
            // it's all part of the plan
            return false;
        }
    }

    @Override
    public Double parse(String s) {
        return parseDouble(s);
    }

  @Override
  public Class<Double> elementClass() {

    return Double.class;
  }

  @Override
    public double parseDouble(String s) {
        if (isMissing(s)) return DoubleColumnType.missingValueIndicator();

        return Double.parseDouble(AbstractParser.remove(s, ','));
    }
}
