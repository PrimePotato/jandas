package com.lchclearnet.trade.table;

import com.lchclearnet.market.FxMarket;
import com.lchclearnet.table.Column;
import com.lchclearnet.table.column.ColumnFactory;
import com.lchclearnet.table.Table;
import com.lchclearnet.table.column.numbers.DoubleColumn;
import com.lchclearnet.trade.TradeService;
import com.lchclearnet.utils.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;


public class TradeTableService implements TradeService {

    private Table data;
    private Set<Currency> ccys;

    public TradeTableService(Table trades) {
        this.data = trades;
        ccys = null;
    }

    @Override
    public Iterable<Currency> getCurrencies() {
        if (ccys == null) {
            synchronized (data) {
                ccys = new HashSet<>();
                Table ccyTable = data.select("BuyCurrency", "SellCurrency", "CallCurrency", "PutCurrency");
                for (Column col : ccyTable.columns()) {
                    ccys.addAll(col.unique().asList());
                }
                ccys.remove(null);
            }
        }
        return ccys;
    }

    public Table getTrades() {
        return data;
    }


    @Override
    public Table getCashFlows(FxMarket marketService) {
        Table buys = buildCashFlows(data, Direction.Buy);
        Table sels = buildCashFlows(data, Direction.Sell);
        return buys.append(sels).sortAscendingOn(MEMBER, TRADE_REF);
    }


    @Override
    public Table getOptions(FxMarket marketService) {

        //extract the source columns


        Column<String> oMembers = (Column<String> ) data.column(MEMBER);
        Column<String> oForexClearRefs = (Column<String> ) data.column(FOREX_CLEAR_REF);
        Column<String> oRefs = (Column<String> ) data.column(TRADE_REF);

        Column<TradeType> oTradeTypes =(Column<TradeType> ) data.column(TRADE_TYPE);

        Column<Currency> oCallCcys = (Column<Currency> ) data.column("CallCurrency");
        Column<Currency> oPutCcys = (Column<Currency> ) data.column("PutCurrency");

        Column<Double> oCallAmnts = (Column<Double> ) data.column("CallAmount");
        Column<Double> oPutAmnts = (Column<Double> ) data.column("PutAmount");


        Column<BuySell> oBuySells = (Column<BuySell> ) data.column("BuySell");
        Column<Double> oStrikes = (Column<Double> ) data.column("Price/Strike");

        Column<Currency> oPremiumCcys = (Column<Currency> ) data.column("PremiumCurrency");
        Column<Double> oPremiumAmnts = (Column<Double> ) data.column("PremiumAmount");
        Column<LocalDate> oPremiumSettlDates = (Column<LocalDate>) data.column("PremiumSettlementDate");

        Column<LocalDate> oSettlDates = (Column<LocalDate>) data.column("SettlementDate");
        Column<LocalDate> oExpiryDates = (Column<LocalDate>) data.column("ExpiryDate");
        Column<LocalTime> oExpiryTimes = (Column<LocalTime>) data.column("ExpiryTime");

        //the columns used to build our option table
        Column<String> members = ColumnFactory.createColumn(MEMBER, ColumnFactory.STRING);
        Column<String> forexClearRefs = ColumnFactory.createColumn(FOREX_CLEAR_REF, ColumnFactory.STRING);
        Column<String> refs = ColumnFactory.createColumn(TRADE_REF, ColumnFactory.TEXT);
        Column<TradeType> types = ColumnFactory.createColumn(TRADE_TYPE, ColumnFactory.TRADE_TYPE);

        Column<CallPut> callPuts = ColumnFactory.createColumn("CallPut", ColumnFactory.CALL_PUT);
        Column<BuySell> buySells = ColumnFactory.createColumn("BuySell", ColumnFactory.BUY_SELL);
        Column<Double> strikes = ColumnFactory.createColumn("Strike", ColumnFactory.DOUBLE);
        Column<Long> longStrikes = ColumnFactory.createColumn("LongStrike", ColumnFactory.LONG);

        Column<LocalDate> settlDates = ColumnFactory.createColumn("SettlementDate", ColumnFactory.DATE);
        Column<LocalDate> expiryDates = ColumnFactory.createColumn("ExpiryDate", ColumnFactory.DATE);
        Column<LocalTime> expiryTimes = ColumnFactory.createColumn("ExpiryTime", ColumnFactory.TIME);

        Column<Double> foreignNotionals = ColumnFactory.createColumn("ForeignNotional", ColumnFactory.DOUBLE);

        Column<Currency> premiumCcys = ColumnFactory.createColumn("PremiumCurrency", ColumnFactory.CURRENCY);
        Column<Double> premiumAmnts = ColumnFactory.createColumn("PremiumAmount", ColumnFactory.DOUBLE);
        Column<LocalDate> premiumSettlDates = ColumnFactory.createColumn("PremiumSettlementDate", ColumnFactory.DATE);

        Column<CurrencyPair> currencyPairs = ColumnFactory.createColumn(CCY_PAIR, ColumnFactory.CURRENCY_PAIR);

        Currency callCcy, putCcy;
        CurrencyPair ccyPair;
        for (int rowIdx = 0; rowIdx < data.rowCount(); ++rowIdx) {
            TradeType tradeType = oTradeTypes.get(rowIdx);
            if(TradeType.FxOption != tradeType) continue;

            callCcy = oCallCcys.get(rowIdx);
            putCcy = oPutCcys.get(rowIdx);
            ccyPair = CurrencyPair.of(callCcy, putCcy).toConvention();

            members.append(oMembers.getString(rowIdx));
            forexClearRefs.append(oForexClearRefs.getString(rowIdx));
            refs.append(oRefs.getString(rowIdx));
            types.append(oTradeTypes.get(rowIdx));

            currencyPairs.append(ccyPair);

            buySells.append(oBuySells.get(rowIdx));
            callPuts.append(callCcy == ccyPair.getForeign() ? CallPut.Call : CallPut.Put);
            foreignNotionals.append(callCcy == ccyPair.getForeign() ? oCallAmnts.get(rowIdx) : oPutAmnts.get(rowIdx));
            longStrikes.append(Math.round(100_000 * oStrikes.get(rowIdx)));
            strikes.append(oStrikes.get(rowIdx));

            expiryDates.append(oExpiryDates.get(rowIdx));
            expiryTimes.append(oExpiryTimes.get(rowIdx));
            settlDates.append(oSettlDates.get(rowIdx));

            premiumCcys.append(oPremiumCcys.get(rowIdx));
            premiumSettlDates.append(oPremiumSettlDates.get(rowIdx));
            premiumAmnts.append(oPremiumAmnts.get(rowIdx));
        }

        return Table.create("Options",
                members, forexClearRefs, refs, types,
                currencyPairs, buySells, callPuts,
                foreignNotionals,
                strikes, longStrikes,
                expiryDates, expiryTimes,
                settlDates,
                premiumSettlDates, premiumCcys, premiumAmnts);
    }

    private Table buildCashFlows(Table trades, Direction direction) {

        String cashFlowCcyCol = String.format("%sCurrency", direction.name());
        String otherCashFlowCcyCol = String.format("%sCurrency", direction.other().name());
        String cashFlowAmntCol = String.format("%sAmount", direction.name());


        Column<String> csMembers = (Column<String> ) trades.column(MEMBER);
        Column<String> csForexClearRefs = (Column<String> ) trades.column(FOREX_CLEAR_REF);
        Column<String> csRefs = (Column<String> ) trades.column(TRADE_REF);

        Column<TradeType> tradeTypes =(Column<TradeType> ) trades.column(TRADE_TYPE);
        Column<LocalDate> settlDates = (Column<LocalDate>) trades.column("SettlementDate");
        Column<LocalDate> maturityDates = (Column<LocalDate>) trades.column("MaturityDate");
        Column<Currency> settlCcys = (Column<Currency> ) trades.column("SettlementCurrency");
        Column<Currency> cashFlowCcys = (Column<Currency> ) trades.column(cashFlowCcyCol);
        Column<Currency> otherCashFlowCcys = (Column<Currency> ) trades.column(otherCashFlowCcyCol);
        Column<Double> otherCashFlowAmnts = (Column<Double> ) trades.column(cashFlowAmntCol);

        int sign = direction.sign();
        Column<String> members = ColumnFactory.createColumn(MEMBER, ColumnFactory.STRING);
        Column<String> forexClearRefs = ColumnFactory.createColumn(FOREX_CLEAR_REF, ColumnFactory.STRING);
        Column<String> refs = ColumnFactory.createColumn(TRADE_REF, ColumnFactory.TEXT);
        Column<TradeType> types = ColumnFactory.createColumn(TRADE_TYPE, ColumnFactory.TRADE_TYPE);

        Column<LocalDate> paymentDate = ColumnFactory.createColumn(PAYEMENT_DATE, ColumnFactory.DATE);
        Column<Currency> paymentCurrency = ColumnFactory.createColumn(PAYEMENT_CCY, ColumnFactory.CURRENCY);
        Column<CurrencyPair> currencyPair = ColumnFactory.createColumn(CCY_PAIR, ColumnFactory.CURRENCY_PAIR);
        Column<Currency> nativeCurrency = ColumnFactory.createColumn(NATIVE_CCY, ColumnFactory.CURRENCY);
        Column<Double> nativeCashFlow = ColumnFactory.createColumn(NATIVE_CASHFLOW, ColumnFactory.DOUBLE);
        for (int rowIdx = 0; rowIdx < trades.rowCount(); ++rowIdx) {
            TradeType tradeType = tradeTypes.get(rowIdx);
            if(TradeType.FxOption == tradeType) continue;

            LocalDate settlDate;
            Currency settlCcy;
            CurrencyPair ccyPair;
            switch (tradeType) {
                case FxNDF:
                    settlDate = settlDates.get(rowIdx);
                    settlCcy = settlCcys.get(rowIdx);
                    ccyPair = CurrencyPair.of(cashFlowCcys.get(rowIdx), otherCashFlowCcys.get(rowIdx)).toConvention();
                    break;
                case FxSpot:
                case FxForward:
                    settlDate = maturityDates.get(rowIdx);
                    settlCcy = cashFlowCcys.get(rowIdx);
                    ccyPair = CurrencyPair.of(cashFlowCcys.get(rowIdx), otherCashFlowCcys.get(rowIdx)).toConvention();
                    break;
                default:
                    throw new IllegalArgumentException(String.format("'%s' is not a cashflow generating trade type.", String.valueOf(tradeType)));
            }
            members.append(csMembers.getString(rowIdx));
            forexClearRefs.append(csForexClearRefs.getString(rowIdx));
            refs.append(csRefs.getString(rowIdx));
            types.append(tradeTypes.get(rowIdx));
            paymentDate.append(settlDate);
            paymentCurrency.append(settlCcy);
            currencyPair.append(ccyPair);
            nativeCurrency.append(cashFlowCcys.get(rowIdx));
            nativeCashFlow.append(sign * otherCashFlowAmnts.get(rowIdx));
        }

        return Table.create("Cash Flows", members, forexClearRefs, refs, types, paymentDate, paymentCurrency, nativeCurrency, nativeCashFlow, currencyPair);
    }

}
