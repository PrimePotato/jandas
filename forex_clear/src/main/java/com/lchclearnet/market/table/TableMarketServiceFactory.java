package com.lchclearnet.market.table;

import com.lchclearnet.market.*;
import com.lchclearnet.market.fx.FxForwardOutrightCurves;
import com.lchclearnet.market.fx.FxForwardSourceService;
import com.lchclearnet.market.fx.FxSpots;
import com.lchclearnet.table.Table;

import java.util.Map;


public class TableMarketServiceFactory implements MarketServiceFactory {

    public TableMarketServiceFactory() {

    }

    @Override
    public <T extends MarketService> T getMarketSource(SourceType type,
                                                       Map<MarketDataType<SourceType>, MarketService> serviceContext,
                                                       Map<SourceType, Object> dataContext) {
        MarketService mktData;
        switch (type) {
            case FX_SPOT:
                mktData = serviceContext.computeIfAbsent(SourceType.FX_SPOT, x -> new TableSpots((Table)dataContext.get(SourceType.FX_SPOT)));
                break;
            case FX_FORWARD:
                mktData = serviceContext.computeIfAbsent(SourceType.FX_FORWARD, x -> new TableFxForwardFromYieldSourceService((Table)dataContext.get(SourceType.IR_YIELD_CURVE)));
                break;
            case IR_YIELD_CURVE:
                mktData = serviceContext.computeIfAbsent(SourceType.IR_YIELD_CURVE, x -> new TableYieldCurveService((Table)dataContext.get(SourceType.IR_YIELD_CURVE)));
                break;
            case FX_VOLATILITY:
                mktData = null;
                break;
            default:
                throw new RuntimeException(String.format("Can not create Market Data  for Type '%s' because it is not handled", String.valueOf(type)));

        }
        return (T) mktData;
    }

    @Override
    public <T extends MarketService> T getMarketData(DataType type,
                                                     Map<MarketDataType<SourceType>, MarketService> serviceContext,
                                                     Map<SourceType, Object> dataContext) {
        MarketService mktData;
        switch (type) {
            case IR_YIELD_CURVE:
                mktData = serviceContext.computeIfAbsent(DataType.IR_YIELD_CURVE, x -> new TableYieldCurveService((Table)dataContext.get(SourceType.IR_YIELD_CURVE)));
                break;
            case FX_SPOT:
                mktData = serviceContext.computeIfAbsent(DataType.FX_SPOT, x -> new TableSpots((Table)dataContext.get(SourceType.FX_SPOT)));
                break;
            case FX_FORWARD_OUTRIGHT:
                FxSpots spotService = getMarketSource(SourceType.FX_SPOT, serviceContext, dataContext);
                FxForwardSourceService fwdService = getMarketSource(SourceType.FX_FORWARD, serviceContext, dataContext);
                mktData = serviceContext.computeIfAbsent(DataType.FX_FORWARD_OUTRIGHT, x -> new FxForwardOutrightCurves(spotService, fwdService));
                break;
            case FX_VOLATILITY_SURFACE:
                mktData = null;
                break;
            default:
                throw new RuntimeException(String.format("Can not create Market Data  for Type '%s' because it is not handled", String.valueOf(type)));

        }
        return (T) mktData;
    }

}
