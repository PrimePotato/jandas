package com.lchclearnet.jandas.io.parsers;

import com.lchclearnet.utils.Currency;
import java.util.Arrays;

public class CurrencyParser extends AbstractParser<Currency> {

    public CurrencyParser() {
        super((Iterable<String>) null);
    }

    public CurrencyParser(String missingValueStrings) {
        super(missingValueStrings);
    }

    public CurrencyParser(String[] missingValueStrings) {
        super(Arrays.asList(missingValueStrings));
    }

    @Override
    public boolean canParse(String s) {
        return isMissing(s) || Currency.matches(s);
    }

    @Override
    public Currency parse(String s) {
        return isMissing(s) ? null : Currency.parse(s.toUpperCase());
    }

  @Override
  public Class<Currency> elementClass() {

    return Currency.class;
  }
}

