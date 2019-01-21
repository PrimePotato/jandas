package com.lchclearnet.market.ir;

import com.lchclearnet.market.MarketService;

public interface YieldCurveService extends MarketService {

    YieldCurve getYieldCurve(String curveIndex);
}
