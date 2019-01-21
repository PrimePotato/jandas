package com.lchclearnet.trade;

import java.util.Objects;

public class CashFlowResult {

    public final CashFlowInstrument cashFlow;
    public final double forwardNativeDiscountFactor;
    public final double paymentDiscountFactor;
    public final double forwardPaymentDiscountFactor;
    public final double spot;
    public final double dcf;
    public final double dcf_spot;
    public final double dcf_end;

    public CashFlowResult(CashFlowInstrument cashFlow, double forwardNativeDiscountFactor,
                          double paymentDiscountFactor, double forwardPaymentDiscountFactor, double spot,
                          double dcf, double dcf_spot, double dcf_end) {
        this.cashFlow = cashFlow;
        this.forwardNativeDiscountFactor = forwardNativeDiscountFactor;
        this.paymentDiscountFactor = paymentDiscountFactor;
        this.forwardPaymentDiscountFactor = forwardPaymentDiscountFactor;
        this.spot = spot;
        this.dcf = dcf;
        this.dcf_spot = dcf_spot;
        this.dcf_end = dcf_end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CashFlowResult that = (CashFlowResult) o;
        return Objects.equals(cashFlow, that.cashFlow);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cashFlow);
    }
}
