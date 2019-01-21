package com.lchclearnet.market.table;

import com.google.common.collect.Streams;
import com.lchclearnet.market.fx.FxForwardSourceService;
import com.lchclearnet.market.ir.YieldCurve;
import com.lchclearnet.table.Table;
import com.lchclearnet.utils.CurrencyPair;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static com.lchclearnet.market.table.MarketDataHelper.buildYieldCurve;

public class TableFxForwardFromYieldSourceService extends AbstractTableMarketService implements
    FxForwardSourceService {

  private final Map<String, YieldCurve> yieldCurves;

  private final Map<CurrencyPair, Map<LocalDate, Double>> fxFwdRates;

  public TableFxForwardFromYieldSourceService(Table data) {

    super("CurrentBusinessDate", data);
    this.yieldCurves = new ConcurrentHashMap<>();
    this.fxFwdRates = new ConcurrentHashMap<>();
  }

  @Override
  public LocalDate getMarketDate() {

    return null;
  }

  @Override
  public Double getForwardRate(CurrencyPair ccyPair, Double fxSpot, LocalDate date) {

    CurrencyPair conventionPair = ccyPair.toConvention();
    Double fxFwdRate = _getForwardRate(ccyPair, date);
    if (fxFwdRate != null) {
      return ccyPair == conventionPair ? fxFwdRate : 1 / fxFwdRate;
    }

    LocalDate[] dates = new LocalDate[]{date};
    return getForwardRates(conventionPair, fxSpot, dates).toArray(Double[]::new)[0];
  }

  @Override
  public Stream<Double> getForwardRates(final CurrencyPair ccyPair, final Double fxSpot,
      LocalDate... dates) {

    YieldCurve fyc = yieldCurves.computeIfAbsent(ccyPair.getForeign().getFxForwardYieldCurve(),
        curve -> buildYieldCurve(curve, data));
    YieldCurve dyc = yieldCurves.computeIfAbsent(ccyPair.getDomestic().getFxForwardYieldCurve(),
        curve -> buildYieldCurve(curve, data));

    return Streams.zip(fyc.discountFactors(dates), dyc.discountFactors(dates),
        (dff, dfd) -> fxSpot * dfd / dff);
  }

  /**
   * Retrieve the FX Forward Rate if it is cached, null otherwise. The currency pair passed has to
   * be in convention order.
   *
   * @param conventionPair the currency pair in convention order
   * @param date the fx forward settlement date
   * @return the fx forward if it is cached.
   */
  private Double _getForwardRate(CurrencyPair conventionPair, LocalDate date) {

    if (!fxFwdRates.containsKey(conventionPair)) {
      return null;
    }
    Map<LocalDate, Double> rates = fxFwdRates.get(conventionPair);
    return rates.get(date);
  }
}
