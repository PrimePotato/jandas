package com.lchclearnet.trade;

import com.lchclearnet.market.FxMarket;

public interface Instrument<DATA> {

    Long getId();

    DATA getMarketData(FxMarket mktService);
}
