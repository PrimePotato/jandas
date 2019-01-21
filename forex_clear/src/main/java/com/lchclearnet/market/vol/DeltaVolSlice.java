package com.lchclearnet.market.vol;

import com.lchclearnet.calendar.Tenor;
import com.lchclearnet.utils.VolNode;
import java.time.LocalDate;
import java.util.EnumMap;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;

public class DeltaVolSlice implements Comparable<DeltaVolSlice> {

  public final Tenor t;
  public final LocalDate dt;
  public final EnumMap<VolNode, Double> vols;
  public final UnivariateInterpolator interp;
  public final UnivariateFunction getVol;

  public DeltaVolSlice(Tenor t, LocalDate dt, EnumMap<VolNode, Double> vols,
      UnivariateInterpolator interp) {

    this.t = t;
    this.dt = dt;
    this.vols = vols;
    this.interp = interp;

    double[] ds = vols.keySet().stream().mapToDouble(VolNode::offsetDelta).toArray();
    double[] vs = vols.values().stream().mapToDouble(v -> v).toArray();

    this.getVol = this.interp.interpolate(ds, vs);

  }

  @Override
  public int compareTo(DeltaVolSlice o) {

    return dt.compareTo(o.dt);
  }
}
