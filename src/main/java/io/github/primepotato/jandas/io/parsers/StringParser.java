package io.github.primepotato.jandas.io.parsers;


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
        if (isMissing(s)) return null;

        return s;
    }

  @Override
  public Class<String> elementClass() {

    return String.class;
  }
}
