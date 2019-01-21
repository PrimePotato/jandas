package com.lchclearnet.trade.morpheus;

import com.lchclearnet.market.FxMarket;
import com.lchclearnet.trade.TradeService;
import com.lchclearnet.utils.*;
import com.lchclearnet.utils.morpheus.DataFrameHelper;
import com.zavtech.morpheus.frame.DataFrame;
import com.zavtech.morpheus.frame.DataFrameRow;
import com.zavtech.morpheus.util.Collect;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


public class DataFrameTradeService implements TradeService {

    private DataFrame<Long, String> data;
    private Set ccys;

    public DataFrameTradeService(DataFrame trades) {
        this.data = trades;
        ccys = null;
    }

    @Override
    public Iterable<Currency> getCurrencies() {
        if (ccys == null) {
            synchronized (data) {
                ccys = new HashSet<>();
                DataFrameHelper.forEachTrade(data, row -> {
                    ccys.add(row.getValue("BuyCurrency"));
                    ccys.add(row.getValue("SellCurrency"));
                    ccys.add(row.getValue("CallCurrency"));
                    ccys.add(row.getValue("PutCurrency"));
                });
                ccys.remove(null);
            }
        }
        return ccys;
    }


    @Override
    public DataFrame<String, String> getCashFlows(FxMarket marketService) {
        DataFrame<Long, String> trades = data.rows().select(row -> TradeType.FxOption != row.getValue(TRADE_TYPE) && TradeState.Novated == row.getValue("StateName"));
        DataFrame<String, String> cashFlows = DataFrame.concatRows(buildCashFlows(trades, Direction.Buy), buildCashFlows(trades, Direction.Sell));
        cashFlows = cashFlows.rows().sort(true, Collect.asList(MEMBER, TRADE_REF, PAYEMENT_DATE, PAYEMENT_CCY));
        return cashFlows;
    }

    @Override
    public DataFrame<String, String> getOptions(FxMarket marketService) {
        throw new NotImplementedException();
    }

    private DataFrame<String, String> buildCashFlows(DataFrame<Long, String> trades, Direction direction) {

        String cashFlowCcyCol = String.format("%sCurrency", direction.name());
        String otherCashFlowCcyCol = String.format("%sCurrency", direction.other().name());
        String cashFlowAmntCol = String.format("%sAmount", direction.name());

        DataFrame<Long, String> cashFlows = trades.cols().select(
                MEMBER, FOREX_CLEAR_REF, TRADE_REF, TRADE_TYPE,
                "SettlementDate", "MaturityDate", "SettlementCurrency",
                cashFlowCcyCol, cashFlowAmntCol, otherCashFlowCcyCol).copy();

        cashFlows = cashFlows.cols().add(PAYEMENT_DATE, LocalDate.class, v -> {
            LocalDate settlDate;
            DataFrameRow<Long, String> row = v.row();
            TradeType tradeType = row.getValue(TRADE_TYPE);
            switch (tradeType) {
                case FxNDF:
                    settlDate = row.getValue("SettlementDate");
                    break;
                case FxSpot:
                case FxForward:
                    settlDate = row.getValue("MaturityDate");
                    break;
                default:
                    throw new IllegalArgumentException(String.format("'%s' is not a cashflow generating trade type.", String.valueOf(tradeType)));
            }
            return settlDate;
        });

        cashFlows = cashFlows.cols().add(PAYEMENT_CCY, Currency.class, v -> {
            Currency settlCcy;
            DataFrameRow<Long, String> row = v.row();
            TradeType tradeType = row.getValue(TRADE_TYPE);
            switch (tradeType) {
                case FxNDF:
                    settlCcy = row.getValue("SettlementCurrency");
                    break;
                case FxSpot:
                case FxForward:
                    settlCcy = row.getValue(cashFlowCcyCol);
                    break;
                default:
                    throw new IllegalArgumentException(String.format("'%s' is not a cashflow generating trade type.", String.valueOf(tradeType)));
            }
            return settlCcy;
        });

        cashFlows = cashFlows.cols().add(CCY_PAIR, CurrencyPair.class, v -> {
            CurrencyPair ccyPair;
            DataFrameRow<Long, String> row = v.row();
            TradeType tradeType = row.getValue(TRADE_TYPE);
            switch (tradeType) {
                case FxNDF:
                case FxSpot:
                case FxForward:
                    ccyPair = CurrencyPair.of((Currency) row.getValue(cashFlowCcyCol), row.getValue(otherCashFlowCcyCol)).toConvention();
                    break;
                default:
                    throw new IllegalArgumentException(String.format("'%s' is not a cashflow generating trade type.", String.valueOf(tradeType)));
            }
            return ccyPair;
        });

        int sign = direction.sign();
        cashFlows = cashFlows.cols().add(NATIVE_CCY, Currency.class, v -> v.row().getValue(cashFlowCcyCol));
        cashFlows = cashFlows.cols().add(NATIVE_CASHFLOW, Double.class, v -> sign * v.row().getDouble(cashFlowAmntCol));

        return cashFlows.rows().mapKeys(row -> {
            final long tradeRef = row.getLong(TRADE_REF);
            final long pdate = ((LocalDate) row.getValue(PAYEMENT_DATE)).toEpochDay();
            final int nCcy = ((Currency) row.getValue(NATIVE_CCY)).ordinal();
            final int pCcy = ((Currency) row.getValue(PAYEMENT_CCY)).ordinal();
            return String.format("%d-%d-%d-%d", tradeRef, pdate, nCcy, pCcy);
        }).cols().select(
                MEMBER, FOREX_CLEAR_REF, TRADE_REF, TRADE_TYPE,
                PAYEMENT_DATE, PAYEMENT_CCY,
                NATIVE_CCY, NATIVE_CASHFLOW, CCY_PAIR);
    }

    public DataFrame<Long, String> getTrades() {
        return data;
    }

}
