package com.lchclearnet.report.table;

import com.lchclearnet.fx.table.CashFlowReportWithIteration;
import com.lchclearnet.market.FxMarket;
import com.lchclearnet.market.fx.FxSpots;
import com.lchclearnet.report.ReportBuilder;
import com.lchclearnet.table.Row;
import com.lchclearnet.table.Table;
import com.lchclearnet.trade.CashFlowInstrument;
import com.lchclearnet.trade.CashFlowResults;
import com.lchclearnet.trade.TradeService;
import com.lchclearnet.utils.Currency;
import com.lchclearnet.utils.CurrencyPair;

import java.io.File;
import java.io.IOException;

import static com.lchclearnet.trade.TradeService.*;

public class CashFlowReport implements ReportBuilder {

    private CashFlowResults results;
    private Table cashFlows;
    private TradeService tradeService;
    private FxMarket marketService;
    private Table report;

    public CashFlowReport setCashFlowInstruments(CashFlowResults results) {

        this.results = results;
        return this;
    }

    public CashFlowReport setCashFlows(Table cashFlows) {

        this.cashFlows = cashFlows.copy();

        return this;
    }

    public CashFlowReport setTradeService(TradeService tradeService) {

        this.tradeService = tradeService;
        return this;
    }

    public CashFlowReport setMarketService(FxMarket marketService) {

        this.marketService = marketService;
        return this;
    }

    public CashFlowReport build() {
        //TODO: THIS IS AWFUL!!!!! Remove immediately
        final FxSpots fxSpot = marketService.getFxSpots();
        final Table trades = tradeService.getTrades();

        cashFlows = cashFlows
                .addColumn("NativePV", Double.class, v -> results.getPresentValue(toCashFlowIndex(v),
                        v.getDouble(NATIVE_CASHFLOW)));
        cashFlows = cashFlows
                .addColumn("PaymentPV", Double.class, v -> v.getDouble("NativePV") * fxSpot.getSpot(
                        CurrencyPair.of((Currency) v.getObject(NATIVE_CCY),
                                v.getObject(PAYEMENT_CCY))));
        cashFlows = cashFlows
                .addColumn("PvUSD", Double.class, v -> v.getDouble("NativePV") * fxSpot.getSpot(
                        CurrencyPair.of(v.getObject(NATIVE_CCY), Currency.USD)));

        cashFlows = cashFlows
                .addColumn("FxdNativeDF", Double.class,
                        v -> results.getForwardNativeDiscountFactor(toCashFlowIndex(v)));
        cashFlows = cashFlows
                .addColumn("FxdPaymentDF", Double.class,
                        v -> results.getForwardPaymentDiscountFactor(toCashFlowIndex(v)));
        cashFlows = cashFlows
                .addColumn("PaymentDF", Double.class,
                        v -> results.getPaymentDiscountFactor(toCashFlowIndex(v)));

        cashFlows = cashFlows
                .addColumn("DCF", Double.class, v -> results.getCashFlowResult(toCashFlowIndex(v)).dcf);
        cashFlows = cashFlows
                .addColumn("DCFSpot", Double.class,
                        v -> results.getCashFlowResult(toCashFlowIndex(v)).dcf_spot);
        cashFlows = cashFlows
                .addColumn("DCFEnd", Double.class,
                        v -> results.getCashFlowResult(toCashFlowIndex(v)).dcf_end);

        cashFlows = cashFlows
                .addColumn("Native/Payment", Double.class, v -> fxSpot.getSpot(
                        CurrencyPair.of((Currency) v.getObject(NATIVE_CCY),
                                v.getObject(PAYEMENT_CCY))));
        cashFlows = cashFlows
                .addColumn("Native/USD", Double.class,
                        v -> fxSpot.getSpot(CurrencyPair.of(v.getObject(NATIVE_CCY), Currency.USD)));

        report = cashFlows.select(MEMBER, FOREX_CLEAR_REF, TRADE_REF, TRADE_TYPE, CCY_PAIR, SPOT_DATE, PAYEMENT_DATE,
                PAYEMENT_CCY, NATIVE_CCY, NATIVE_CASHFLOW, "NativePV", "PaymentPV", "PvUSD",
                "FxdNativeDF", "FxdPaymentDF", "PaymentDF", "DCF", "DCFSpot", "DCFEnd",
                "Native/Payment", "Native/USD");

        return this;
    }

//    @Override
//    public CashFlowReport build() {
//        final FxSpots fxSpot = marketService.getFxSpots();
//
//        report = cashFlows.join(PAYEMENT_DATE, NATIVE_CCY, PAYEMENT_CCY, SPOT_DATE).leftOuter(cashFlowInstruments);
//
////        cashFlows = cashFlows.addColumn("NativePV", Double.class, v -> cashFlowInstruments.getPresentValue(toCashFlowIndex(v), v.getDouble(NATIVE_CASHFLOW)));
////        cashFlows = cashFlows.addColumn("PaymentPV", Double.class, v -> v.getDouble("NativePV") * fxSpot.getSpot(CurrencyPair.of((Currency) v.getObject(NATIVE_CCY), v.getObject(PAYEMENT_CCY))));
////        cashFlows = cashFlows.addColumn("PvUSD", Double.class, v -> v.getDouble("NativePV") * fxSpot.getSpot(CurrencyPair.of(v.getObject(NATIVE_CCY), Currency.USD)));
////        cashFlows = cashFlows.addColumn("FxdNativeDF", Double.class, v -> cashFlowInstruments.getForwardNativeDiscountFactor(toCashFlowIndex(v)));
////        cashFlows = cashFlows.addColumn("FxdPaymentDF", Double.class, v -> cashFlowInstruments.getForwardPaymentDiscountFactor(toCashFlowIndex(v)));
////        cashFlows = cashFlows.addColumn("PaymentDF", Double.class, v -> cashFlowInstruments.getPaymentDiscountFactor(toCashFlowIndex(v)));
////
////        cashFlows = cashFlows.addColumn("Native/Payment", Double.class, v -> fxSpot.getSpot(CurrencyPair.of((Currency) v.getObject(NATIVE_CCY), v.getObject(PAYEMENT_CCY))));
////        cashFlows = cashFlows.addColumn("Native/USD", Double.class, v -> fxSpot.getSpot(CurrencyPair.of(v.getObject(NATIVE_CCY), Currency.USD)));
////
////        report = cashFlows.select(MEMBER, FOREX_CLEAR_REF, TRADE_REF, TRADE_TYPE, CCY_PAIR, SPOT_DATE, PAYEMENT_DATE,
////                PAYEMENT_CCY, NATIVE_CCY, NATIVE_CASHFLOW, "NativePV", "PaymentPV", "PvUSD",
////                "FxdNativeDF", "FxdPaymentDF", "PaymentDF", "DCF", "DCFSpot", "DCFEnd",
////                "Native/Payment", "Native/USD");
//
//        return this;
//    }

    private CashFlowInstrument toCashFlowIndex(Row row) {
        return new CashFlowInstrument(row.getDate(PAYEMENT_DATE), row.getObject(NATIVE_CCY), row.getObject(PAYEMENT_CCY), row.getDate(SPOT_DATE));
    }

    @Override
    public Table getReport() {
        return report;
    }

    @Override
    public void writeCsv(File csvFile) {
        try {
            csvFile.getParentFile().mkdirs();
            report.write().csv(csvFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write cash flow report.", e);
        }
    }

}
