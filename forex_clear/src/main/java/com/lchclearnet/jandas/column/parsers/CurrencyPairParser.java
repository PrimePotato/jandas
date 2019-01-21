package com.lchclearnet.jandas.column.parsers;

import com.lchclearnet.utils.CurrencyPair;
import java.util.Arrays;

public class CurrencyPairParser extends AbstractParser<CurrencyPair> {

  public CurrencyPairParser() {

    super((Iterable<String>) null);
  }

  public CurrencyPairParser(String missingValueStrings) {

    super(missingValueStrings);
  }

  public CurrencyPairParser(String[] missingValueStrings) {

    super(Arrays.asList(missingValueStrings));
  }

  @Override
  public boolean canParse(String s) {

    return isMissing(s) || CurrencyPair.matches(s);
  }

  @Override
  public CurrencyPair parse(String s) {

    return isMissing(s) ? null : CurrencyPair.parse(s.toUpperCase());
  }

  @Override
  public Class<CurrencyPair> elementClass() {

    return CurrencyPair.class;
  }
}

