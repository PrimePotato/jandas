package com.lchclearnet.market.vol;

import java.util.Collections;
import java.util.List;

public class DeltaVolSurface {

  private final List<DeltaVolSlice> slices;

  public DeltaVolSurface(List<DeltaVolSlice> slices) {

    this.slices = slices;
    Collections.sort(slices);

  }

}

