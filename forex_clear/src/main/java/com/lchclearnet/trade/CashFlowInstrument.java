package com.lchclearnet.trade;

import com.lchclearnet.utils.Currency;

import java.time.LocalDate;
import java.util.Objects;

public class CashFlowInstrument {

    public final LocalDate settlementDate;
    public final Currency currency;
    public final Currency paymentCurrency;

    public final LocalDate spotDate;


    public CashFlowInstrument(LocalDate settlementDate, Currency currency, Currency paymentCurrency, LocalDate spotDate) {
        this.settlementDate = settlementDate;
        this.currency = currency;
        this.paymentCurrency = paymentCurrency != null ? paymentCurrency : currency;
        this.spotDate = spotDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CashFlowInstrument that = (CashFlowInstrument) o;
        return Objects.equals(settlementDate, that.settlementDate) &&
                currency == that.currency &&
                paymentCurrency == that.paymentCurrency &&
                Objects.equals(spotDate, that.spotDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(settlementDate, currency, paymentCurrency, spotDate);
    }
}
