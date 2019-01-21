package com.lchclearnet.market.table;

import com.lchclearnet.market.ir.YieldCurve;
import com.lchclearnet.market.ir.YieldCurveService;
import com.lchclearnet.table.Table;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.lchclearnet.market.table.MarketDataHelper.buildYieldCurve;

public class TableYieldCurveService extends AbstractTableMarketService implements
        YieldCurveService {

    private final Map<String, YieldCurve> yieldCurves;

    public TableYieldCurveService(Table data) {

        super("ValueDate", data);
        yieldCurves = new ConcurrentHashMap<>();
    }

    @Override
    public YieldCurve getYieldCurve(String curveIndex) {
        return yieldCurves.computeIfAbsent(curveIndex, curve -> buildYieldCurve(curve, data));
    }

}
