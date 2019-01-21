package com.lchclearnet.market.jandas;


import com.lchclearnet.jandas.column.Column;
import com.lchclearnet.jandas.dataframe.DataFrame;
import com.lchclearnet.jandas.dataframe.Record;
import com.lchclearnet.market.fx.FxSpots;

import com.lchclearnet.trade.TradeService;
import com.lchclearnet.utils.CurrencyPair;
import java.time.LocalDate;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DataFrameSpots extends AbstractDataFrameMarketService implements FxSpots {

  private final Map<CurrencyPair, Double> spotRates;
  private final Map<CurrencyPair, LocalDate> spotDates;

  public DataFrameSpots(DataFrame data) {

    super("CurrentBusinessDate", data);
    spotRates = new ConcurrentHashMap<>();
    spotDates = new ConcurrentHashMap<>();
  }

  @Override
  public Set<CurrencyPair> getCurrencyPairs() {
    Column col = data.column("CurrencyPair");
    col.rebuildIndex();
    return col.uniqueSet();
  }

  @Override
  public double getSpot(CurrencyPair pair) {

    return spotRates.computeIfAbsent(pair,
        p -> MarketDataHelper.buildFxRateTrianglation(this::loadSpot).apply(p));
  }

  private double loadSpot(CurrencyPair pair) {

    double spot = Double.NaN;
    Optional<Record> row = getSpotRow(pair);

    if (row.isPresent()) {
      String cp = row.get().getString(TradeService.CCY_PAIR);
      if (pair.toString().equals(cp)) {
        spot = row.get().getDouble("Mid");
      } else {
        spot = 1. / row.get().getDouble("Mid");
      }
    }
    if (Double.isNaN(spot)) {
      throw new IllegalStateException(String.format("Could not load spot for %s", pair.code()));
    }
    return spot;
  }

  @Override
  public LocalDate getSpotDate(CurrencyPair pair) {

    return spotDates.computeIfAbsent(pair, this::loadSpotDate);
  }

  private Optional<Record> getSpotRow(CurrencyPair pair) {

    Optional<Record> rec = data.matchFirst(TradeService.CCY_PAIR, pair);
    if (rec.isPresent()) {
      return rec;
    } else {
      return data.matchFirst(TradeService.CCY_PAIR, pair.inverse());
    }

  }

  private LocalDate loadSpotDate(CurrencyPair pair) {

    Optional<Record> row = getSpotRow(pair);
    if (row.isPresent()) {
      return row.get().getDate("SpotDate");
    }

    throw new IllegalStateException(String.format("Could not load spot date for %s", pair.code()));

  }
}
