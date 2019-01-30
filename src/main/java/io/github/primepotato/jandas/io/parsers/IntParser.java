package io.github.primepotato.jandas.io.parsers;

import java.util.Arrays;

public class IntParser extends AbstractParser<Integer> {

    public IntParser() {
        super((Iterable<String>) null);
    }

    public IntParser(String missingValueStrings) {
        super(missingValueStrings);
    }

    public IntParser(String[] missingValueStrings) {
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
            Integer.parseInt(AbstractParser.remove(s, ','));
            return true;
        } catch (NumberFormatException e) {
            // it's all part of the plan
            return false;
        }
    }

    @Override
    public Integer parse(String s) {
        return parseInt(s);
    }

  @Override
  public Class<Integer> elementClass() {

    return Integer.class;
  }

  @Override
    public double parseDouble(String s) {
        return parseInt(s);
    }

    @Override
    public int parseInt(String str) {
        if (isMissing(str)) return Integer.MIN_VALUE;

        if (str.endsWith(".0")) {
            str = str.substring(0, str.length() - 2);
        }

        return Integer.parseInt(AbstractParser.remove(str, ','));
    }
}
