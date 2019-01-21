package com.lchclearnet.report.morpheus;

import com.lchclearnet.market.FxMarket;
import com.lchclearnet.market.fx.FxSpots;
import com.lchclearnet.report.ReportBuilder;
import com.lchclearnet.trade.CashFlowInstrument;
import com.lchclearnet.trade.CashFlowResults;
import com.lchclearnet.trade.TradeService;
import com.lchclearnet.utils.Currency;
import com.lchclearnet.utils.CurrencyPair;
import com.zavtech.morpheus.frame.DataFrame;
import com.zavtech.morpheus.frame.DataFrameRow;
import com.zavtech.morpheus.util.text.printer.Printer;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.lchclearnet.trade.TradeService.*;

public class CashFlowReport implements ReportBuilder {

    private CashFlowResults results;
    private DataFrame<String, String> cashFlows;
    private TradeService tradeService;
    private FxMarket marketService;
    private DataFrame<String, String> report;

    public CashFlowReport setResults(CashFlowResults results) {

        this.results = results;
        return this;
    }

    public CashFlowReport setCashFlows(DataFrame<String, String> cashFlows) {

        this.cashFlows = cashFlows.copy();
        ;
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

    @Override
    public CashFlowReport build() {
        //TODO: THIS IS AWFUL!!!!! Remove immediately
        final FxSpots fxSpot = marketService.getFxSpots();
        final DataFrame<Long, String> trades = tradeService.getTrades();
        //        cashFlows = cashFlows.cols().add("CcyPair", String.class, v -> {
        //            DataFrameRow<Long, String> trade = trades.row(v.row().getValue("TradeRef"));
        //            CurrencyPair ccypair = CurrencyPair.of((Currency) trade.getValue("BuyCurrency")
        // , trade.getValue("SellCurrency"));
        //            return ccypair.toConvention().code();
        //        });
        cashFlows = cashFlows.cols()
                .add("NativePV", Double.class, v -> results.getPresentValue(toCashFlowIndex(v.row()),
                        v.row().getDouble(NATIVE_CASHFLOW)));
        cashFlows = cashFlows.cols()
                .add("PaymentPV", Double.class, v -> v.row().getDouble("NativePV") * fxSpot.getSpot(
                        CurrencyPair.of((Currency) v.row().getValue(NATIVE_CCY),
                                v.row().getValue(PAYEMENT_CCY))));
        cashFlows = cashFlows.cols()
                .add("PvUSD", Double.class, v -> v.row().getDouble("NativePV") * fxSpot.getSpot(
                        CurrencyPair.of(v.row().getValue(NATIVE_CCY), Currency.USD)));

        cashFlows = cashFlows.cols()
                .add("FxdNativeDF", Double.class,
                        v -> results.getForwardNativeDiscountFactor(toCashFlowIndex(v.row())));
        cashFlows = cashFlows.cols()
                .add("FxdPaymentDF", Double.class,
                        v -> results.getForwardPaymentDiscountFactor(toCashFlowIndex(v.row())));
        cashFlows = cashFlows.cols()
                .add("PaymentDF", Double.class,
                        v -> results.getPaymentDiscountFactor(toCashFlowIndex(v.row())));

        cashFlows = cashFlows.cols()
                .add("DCF", Double.class, v -> results.getCashFlowResult(toCashFlowIndex(v.row())).dcf);
        cashFlows = cashFlows.cols()
                .add("DCFSpot", Double.class,
                        v -> results.getCashFlowResult(toCashFlowIndex(v.row())).dcf_spot);
        cashFlows = cashFlows.cols()
                .add("DCFEnd", Double.class,
                        v -> results.getCashFlowResult(toCashFlowIndex(v.row())).dcf_end);

        cashFlows = cashFlows.cols()
                .add("Native/Payment", Double.class, v -> fxSpot.getSpot(
                        CurrencyPair.of((Currency) v.row().getValue(NATIVE_CCY),
                                v.row().getValue(PAYEMENT_CCY))));
        cashFlows = cashFlows.cols()
                .add("Native/USD", Double.class,
                        v -> fxSpot.getSpot(CurrencyPair.of(v.row().getValue(NATIVE_CCY), Currency.USD)));

        report = cashFlows.rows()
                .mapKeys(row -> {
                    final long tradeRef = row.getLong("TradeRef");
                    final long sdate = ((LocalDate) row.getValue(SPOT_DATE)).toEpochDay();
                    final long pdate = ((LocalDate) row.getValue(PAYEMENT_DATE)).toEpochDay();
                    final int nCcy = ((Currency) row.getValue(NATIVE_CCY)).ordinal();
                    final int pCcy = ((Currency) row.getValue(PAYEMENT_CCY)).ordinal();
                    return String.format("%d-%d-%d-%d-%d", tradeRef, sdate, pdate, nCcy, pCcy);
                })
                .cols()
                .select(MEMBER, FOREX_CLEAR_REF, TRADE_REF, TRADE_TYPE, CCY_PAIR, SPOT_DATE, PAYEMENT_DATE,
                        PAYEMENT_CCY, NATIVE_CCY, NATIVE_CASHFLOW, "NativePV", "PaymentPV", "PvUSD",
                        "FxdNativeDF", "FxdPaymentDF", "PaymentDF", "DCF", "DCFSpot", "DCFEnd",
                        "Native/Payment", "Native/USD");

        return this;
    }

    private CashFlowInstrument toCashFlowIndex(DataFrameRow<String, String> row) {

        return new CashFlowInstrument(row.getValue(PAYEMENT_DATE), row.getValue(NATIVE_CCY),
                row.getValue(PAYEMENT_CCY), row.getValue(SPOT_DATE));
    }

    @Override
    public DataFrame<String, String> getReport() {

        return report;
    }

    @Override
    public void writeCsv(File csvFile) {

        csvFile.getParentFile().mkdirs();
        report.write().csv(options -> {
            options.setFile(csvFile);
            options.setSeparator(",");
            options.setIncludeRowHeader(false);
            options.setIncludeColumnHeader(true);
            options.setNullText("");
            options.setTitle("Cash Flow Report");

            Printer datePrinter = Printer.ofLocalTime(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            //options.setRowKeyPrinter(datePrinter);

            options.setFormats(formats -> {
                formats.setDecimalFormat(double.class, "0.#################", 1);
                formats.setDecimalFormat(Double.class, "0.#################", 1);
                formats.<CurrencyPair>setPrinter(CCY_PAIR, Printer.forObject(pair -> String.valueOf(pair)));
                formats.<Currency>setPrinter(PAYEMENT_CCY, Printer.forObject(ccy -> String.valueOf(ccy)));
                formats.<Currency>setPrinter(NATIVE_CCY, Printer.forObject(ccy -> String.valueOf(ccy)));
                formats.setPrinter(PAYEMENT_DATE, datePrinter);
            });
        });
    }

}
