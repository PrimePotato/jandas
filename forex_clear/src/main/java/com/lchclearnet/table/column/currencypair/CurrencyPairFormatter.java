package com.lchclearnet.table.column.currencypair;

import com.lchclearnet.utils.CurrencyPair;

public class CurrencyPairFormatter {

    private String missingString = "";

    public CurrencyPairFormatter() {
    }

    public CurrencyPairFormatter(String missingString) {

        this.missingString = missingString;
    }

    public String format(CurrencyPair value) {
        return value != null ? value.code() : missingString;
    }

    @Override
    public String toString() {
        return String.format("CurrencyPairFormatter{ missingString='%s'}", missingString);
    }
}
