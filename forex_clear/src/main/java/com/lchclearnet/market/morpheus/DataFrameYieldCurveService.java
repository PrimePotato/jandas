package com.lchclearnet.market.morpheus;

import com.lchclearnet.market.ir.YieldCurve;
import com.lchclearnet.market.ir.YieldCurveService;
import com.zavtech.morpheus.frame.DataFrame;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.lchclearnet.market.morpheus.MarketDataHelper.buildYieldCurve;

public class DataFrameYieldCurveService extends AbstractDataFrameMarketService implements
        YieldCurveService {

    private final Map<String, YieldCurve> yieldCurves;

    public DataFrameYieldCurveService(DataFrame<Integer, String> data) {

        super("ValueDate", data);
        yieldCurves = new ConcurrentHashMap<>();
    }

    @Override
    public YieldCurve getYieldCurve(String curveIndex) {
        return yieldCurves.computeIfAbsent(curveIndex, curve -> buildYieldCurve(curve, data));
    }

}
