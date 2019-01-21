package com.lchclearnet.market.jandas;

import com.lchclearnet.calendar.DayCountFraction;
import com.lchclearnet.jandas.dataframe.DataFrame;
import com.lchclearnet.jandas.dataframe.Record;
import com.lchclearnet.market.RateType;
import com.lchclearnet.market.ir.YieldCurve;
import com.lchclearnet.market.ir.YieldCurveConfig;

import com.lchclearnet.utils.CurrencyPair;
import com.lchclearnet.utils.InterpolationMethod;
import com.lchclearnet.utils.Tuple;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class MarketDataHelper {

    public static Function<CurrencyPair, Double> buildFxRateTrianglation(Function<CurrencyPair, Double> loadRate) {
        return pair -> {
            if (pair.isIdentity()) return 1.0;
            double fxRate = 1.0;
            CurrencyPair p;
            Double s;
            Tuple pairs = pair.uncross();
            for (int i = 0; i < pairs.size(); i++) {
                p = pairs.item(i);
                s = loadRate.apply(p);
                if (s != null) {
                    fxRate = fxRate * s;
                } else {
                    s = loadRate.apply(p.inverse());
                    if (s != null) {
                        fxRate = fxRate / s;
                    } else {
                        throw new RuntimeException(String.format("Cannot find '%s' FX Rate", p.code()));
                    }
                }
            }

            return fxRate;
        };
    }

    public static YieldCurve buildYieldCurve(String curveId, DataFrame data) {
        DayCountFraction dcf = YieldCurveConfig.instance().getDayCountFraction(curveId);
        InterpolationMethod interpMethod = YieldCurveConfig.instance().getInterpolationMethod(curveId);
        //System.out.println(jandas.print(20));
        List<LocalDate> maturityDates = new LinkedList<>();
        List<Double> zeroRates = new LinkedList<>();
        for (Record row : data) {
            if( curveId.equalsIgnoreCase(row.getString("CurveId"))){
                maturityDates.add(row.getDate("MaturityDate"));
                zeroRates.add(row.getDouble("ZeroCouponRate") / 100);
            }
        }

        Optional<Record> row = data.column("ValueDate").firstValue();
        if (!row.isPresent()) throw new RuntimeException("Cannot find 'ValueDate' field in Yield Curve File.");

        return new YieldCurve(maturityDates, zeroRates, RateType.ZERO_RATE, interpMethod, dcf, row.get().getDate("ValueDate"));
    }
}
