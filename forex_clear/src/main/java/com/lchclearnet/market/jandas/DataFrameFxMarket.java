package com.lchclearnet.market.jandas;

import com.lchclearnet.market.DataType;
import com.lchclearnet.market.FxMarket;
import com.lchclearnet.market.MarketDataType;
import com.lchclearnet.market.MarketService;
import com.lchclearnet.market.SourceType;
import com.lchclearnet.market.fx.FxSpots;
import com.lchclearnet.market.ir.YieldCurve;
import com.lchclearnet.market.ir.YieldCurveService;
import java.util.HashMap;
import java.util.Map;

public class DataFrameFxMarket implements FxMarket {

    private final Map<MarketDataType<SourceType>, MarketService> serviceContext;

    public DataFrameFxMarket(final Iterable<DataType> mktDataTypes,
                         final Map<SourceType, Object> dataContext) {

        this.serviceContext = new HashMap<>();
        DataFrameMarketServiceFactory mktFactory = new DataFrameMarketServiceFactory();
        mktDataTypes.forEach(mktDataType -> this.serviceContext.put(mktDataType, mktFactory.getMarketData(mktDataType, serviceContext, dataContext)));
    }

    @Override
    public FxSpots getFxSpots() {
        return ((FxSpots) serviceContext.get(DataType.FX_SPOT));
    }

    @Override
    public YieldCurve getYieldCurve(String curveIndex) {
        return ((YieldCurveService) serviceContext.get(DataType.IR_YIELD_CURVE)).getYieldCurve(curveIndex);
    }

}
