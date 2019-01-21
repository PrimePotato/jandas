package com.lchclearnet.market;

import com.lchclearnet.market.fx.FxSpots;
import com.lchclearnet.market.ir.YieldCurve;

public interface FxMarket {

    FxSpots getFxSpots();

    YieldCurve getYieldCurve(String curveIndex);

//    <T extends MarketService> T getFxForwardService(boolean relative);

//    <T extends MarketService> T getFxVolatilityService(boolean relative);
}
