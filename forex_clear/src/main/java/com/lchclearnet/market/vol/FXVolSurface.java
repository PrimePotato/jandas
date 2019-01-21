package com.lchclearnet.market.vol;

import com.lchclearnet.market.fx.FxForwardOutright;

import com.lchclearnet.calendar.BusinessCalendar;
import com.lchclearnet.calendar.DateUtils;
import java.time.LocalDate;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Function;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;

public class FXVolSurface {

  TreeMap<LocalDate, StrikeVolSlice> slices;
  Function<LocalDate, Double> tradingTime;
  LocalDate valueDate;
  List<LocalDate> tradingDays;
  UnivariateInterpolator timeInterp;

  public FXVolSurface(LocalDate valueDate, TreeMap<LocalDate, StrikeVolSlice> slices,
      BusinessCalendar cal) {

    this.slices = slices;
    this.valueDate = valueDate;
    this.tradingDays = DateUtils.businessDaysBetween(valueDate, slices.lastKey(), cal);
    this.tradingTime = DateUtils.tradingTimeInterp(valueDate, slices.lastKey(), cal);
    this.timeInterp = new LinearInterpolator();
  }

  public UnivariateFunction volInterpFromStrike(double strike) {

    double[] vols = slices.values().stream().mapToDouble(v -> v.getVol.value(strike)).toArray();
    double[] tts = slices.keySet().stream().mapToDouble(d -> tradingTime.apply(d)).toArray();
    return this.timeInterp.interpolate(tts, vols);
  }

  public double volFromStrikeExpiry(LocalDate expiry, double strike) {

    UnivariateFunction vi = volInterpFromStrike(strike);
    return vi.value(this.tradingTime.apply(expiry));
  }

}
