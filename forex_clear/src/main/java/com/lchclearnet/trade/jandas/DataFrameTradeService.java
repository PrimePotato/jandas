package com.lchclearnet.trade.jandas;

import com.lchclearnet.market.FxMarket;
import com.lchclearnet.jandas.column.Column;
import com.lchclearnet.jandas.dataframe.DataFrame;
import com.lchclearnet.trade.TradeService;
import com.lchclearnet.utils.Currency;
import com.lchclearnet.utils.Direction;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataFrameTradeService implements TradeService {

  private DataFrame data;
  private Set<Currency> ccys;

  public DataFrameTradeService(DataFrame trades) {

    this.data = trades;
    ccys = null;
  }

  @Override
  public Iterable<Currency> getCurrencies() {

    if (ccys == null) {
      synchronized (data) {
        ccys = new HashSet<>();
        DataFrame ccyDataFrame =
            data.select("ccys","BuyCurrency", "SellCurrency", "CallCurrency", "PutCurrency");
        for (Column col : ccyDataFrame.columns()) {
          col.rebuildIndex(); //TODO: remove
          ccys.addAll(col.uniqueSet());
        }
        ccys.remove(null);
      }
    }
    return ccys;
  }

  @Override
  public DataFrame getCashFlows(FxMarket marketService) {

    DataFrame buys = buildCashFlows(data, Direction.Buy);
    DataFrame sels = buildCashFlows(data, Direction.Sell);
    return buys;
  }

  @Override
  public <T> T getOptions(FxMarket marketService) {

    return null;
  }

  public DataFrame getTrades() {

    return data;
  }

  private DataFrame buildCashFlows(DataFrame trades, Direction direction) {

    String[] importCols = {
        MEMBER,
        FOREX_CLEAR_REF,
        TRADE_REF,
        TRADE_TYPE,
        BUY_SELL,
        PRICE_STRIKE,
        PREMIUM_CURRENCY,
        PREMIUM_AMOUNT,
        PREMIUM_SETTLEMENT_DATE,
        SETTLEMENT_DATE,
        EXPIRY_DATE,
        EXPIRY_TIME,
        CCY_PAIR,
        CALL_CURRENCY,
        PUT_CURRENCY,
        CALL_AMOUNT,
        PUT_AMOUNT
    };

    return trades.select("flows", importCols);
  }

}
