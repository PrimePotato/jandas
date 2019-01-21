package com.lchclearnet.table.column.currency;

import com.lchclearnet.utils.Currency;

public class CurrencyFormatter {

    private String missingString = "";

    public CurrencyFormatter() {
    }

    public CurrencyFormatter(String missingString) {

        this.missingString = missingString;
    }

    public String format(Currency value) {
        return value != null ? value.getCurrencyCode() : missingString;
    }

    @Override
    public String toString() {
        return String.format("CurrencyFormatter{ missingString='%s'}", missingString);
    }
}
