package com.lchclearnet.market.vol;

import com.lchclearnet.calendar.Tenor;
import com.lchclearnet.market.vol.DeltaVolSlice;
import com.lchclearnet.utils.ExtraPolationMethod;
import com.lchclearnet.utils.Polater;
import com.lchclearnet.utils.VolNode;
import java.time.LocalDate;
import java.util.EnumMap;
import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.junit.Before;
import org.junit.Test;

public class DeltaVolSliceTest {

  private Tenor t;
  private LocalDate dt;
  private EnumMap<VolNode, Double> vols;
  private DeltaVolSlice dvs;

  @Before
  public void setUp() {

    t = Tenor.parse("1M");
    dt = LocalDate.of(2019, 2, 15);
    vols = new EnumMap(VolNode.class);

    vols.put(VolNode.PUT_10, 0.15);
    vols.put(VolNode.PUT_25, 0.14);
    vols.put(VolNode.ATM, 0.15);
    vols.put(VolNode.CALL_25, 0.17);
    vols.put(VolNode.CALL_10, 0.19);

    dvs = new DeltaVolSlice(t, dt, vols, Polater.decorate(new LinearInterpolator(), ExtraPolationMethod.CONTINUE, ExtraPolationMethod.CONTINUE));

  }

  @Test
  public void interp() {
    System.out.println(dvs.getVol.value(-0.4));
    System.out.println(dvs.getVol.value(-0.25));
    System.out.println(dvs.getVol.value(0.0));
    System.out.println(dvs.getVol.value(0.25));
    System.out.println(dvs.getVol.value(0.4));

    System.out.println(dvs.getVol.value(0.35));
    System.out.println(dvs.getVol.value(0.25));

  }

}