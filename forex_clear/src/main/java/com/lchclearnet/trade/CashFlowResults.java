package com.lchclearnet.trade;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class CashFlowResults {

    private final Map<CashFlowInstrument, CashFlowResult> cashFlows;

    public CashFlowResults() {

        this.cashFlows = new HashMap<>();
    }

    public void add(CashFlowResult result) {

        cashFlows.put(result.cashFlow, result);
    }

    public CashFlowResult getCashFlowResult(CashFlowInstrument cashFlow) {

        return cashFlows.get(cashFlow);
    }

    public double getForwardNativeDiscountFactor(CashFlowInstrument cashFlow) {

        return getCashFlowResult(cashFlow).forwardNativeDiscountFactor;
    }

    public double getPaymentDiscountFactor(CashFlowInstrument cashFlow) {

        return getCashFlowResult(cashFlow).paymentDiscountFactor;
    }

    public double getForwardPaymentDiscountFactor(CashFlowInstrument cashFlow) {

        return getCashFlowResult(cashFlow).forwardPaymentDiscountFactor;
    }

    public double getPresentValue(CashFlowInstrument cashFlow, double amount) {

        return amount * getForwardNativeDiscountFactor(cashFlow) / getForwardPaymentDiscountFactor(
                cashFlow) * getPaymentDiscountFactor(cashFlow);
    }

    public void print(File csvFile) {

        csvFile.getParentFile().mkdirs();
        try (
                PrintWriter pw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(csvFile)))
        ) {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
            pw.println(
                    "Currency, Payment, Settlement, ForwardNativeDiscountFactor, PaymentDiscountFactor,"
                            + "ForwardPaymentDiscountFactor, Spot");
            cashFlows.forEach((cashFlow, result) -> pw.println(
                    String.format("%s, %s, %s, %s, %s, %s, &s", cashFlow.currency, cashFlow.paymentCurrency,
                            cashFlow.settlementDate.format(formatter), result.forwardNativeDiscountFactor,
                            result.paymentDiscountFactor, result.forwardPaymentDiscountFactor, result.spot)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
