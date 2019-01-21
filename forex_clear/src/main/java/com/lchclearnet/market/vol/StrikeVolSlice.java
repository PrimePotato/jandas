package com.lchclearnet.market.vol;

import com.lchclearnet.calendar.Tenor;
import com.lchclearnet.utils.Black;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;

public class StrikeVolSlice {

  public final Tenor t;
  public final LocalDate dt;
  public final Map<Double, Double> vols;
  public final UnivariateInterpolator interp;
  public final UnivariateFunction getVol;

  public StrikeVolSlice(DeltaVolSlice dvs, double f, double tau) {

    this.t = dvs.t;
    this.dt = dvs.dt;
    this.interp = dvs.interp;

    vols = new TreeMap<>();
    dvs.vols.forEach((vn, value) -> {
      vols.put(Black.strikeFromDelta(vn.offsetDelta(), f, tau, value), value);
    });

    double[] ks = vols.keySet().stream().mapToDouble(v -> v).toArray();
    double[] vs = vols.values().stream().mapToDouble(v -> v).toArray();

    this.getVol = this.interp.interpolate(ks, vs);

  }

}
