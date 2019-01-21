package com.lchclearnet.fx;

import com.google.common.collect.ImmutableSet;
import com.lchclearnet.utils.Currency;
import com.lchclearnet.utils.CurrencyPair;


public final class CurrencyPairDemo {


    public static void main(String[] args) {
        CurrencyPair ccyPair = CurrencyPair.of("JPY", "USD");
        CurrencyPair conventionPair = ccyPair.toConvention();
        System.out.println(conventionPair.toString());
        for (Currency ccy : ccyPair.toConventionSet()) {
            System.out.println(String.format("%s: %d", ccy.getCurrencyCode(), ccy.getDescription()));
        }

        for (String code : ImmutableSet.of("eur-usd", "EUR_usd", "eurUSD", "usd/EUR", "EURUSD", "eurusd")) {
            ccyPair = CurrencyPair.parse(code);
            System.out.println(String.format("%s: %s", code, ccyPair.code()));
        }
    }

}
