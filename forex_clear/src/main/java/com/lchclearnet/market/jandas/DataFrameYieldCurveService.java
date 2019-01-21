package com.lchclearnet.market.jandas;




import static com.lchclearnet.market.jandas.MarketDataHelper.buildYieldCurve;

import com.lchclearnet.jandas.dataframe.DataFrame;
import com.lchclearnet.market.ir.YieldCurve;
import com.lchclearnet.market.ir.YieldCurveService;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataFrameYieldCurveService extends AbstractDataFrameMarketService implements
        YieldCurveService {

    private final Map<String, YieldCurve> yieldCurves;

    public DataFrameYieldCurveService(DataFrame data) {

        super("ValueDate", data);
        yieldCurves = new ConcurrentHashMap<>();
    }

    @Override
    public YieldCurve getYieldCurve(String curveIndex) {
        return yieldCurves.computeIfAbsent(curveIndex, curve -> buildYieldCurve(curve, data));
    }

}
