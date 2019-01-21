package com.lchclearnet.market.morpheus;

import com.lchclearnet.calendar.DayCountFraction;
import com.lchclearnet.market.RateType;
import com.lchclearnet.market.ir.YieldCurve;
import com.lchclearnet.market.ir.YieldCurveConfig;
import com.lchclearnet.utils.CurrencyPair;
import com.lchclearnet.utils.FinalValue;
import com.lchclearnet.utils.InterpolationMethod;
import com.zavtech.morpheus.frame.DataFrame;
import com.zavtech.morpheus.frame.DataFrameRow;
import com.lchclearnet.utils.Tuple;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
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

    public static YieldCurve buildYieldCurve(String curveId, DataFrame<Integer, String> data) {
        DayCountFraction dcf = YieldCurveConfig.instance().getDayCountFraction(curveId);
        InterpolationMethod interpMethod = YieldCurveConfig.instance().getInterpolationMethod(curveId);

        List<LocalDate> maturityDates = new LinkedList<>();
        List<Double> zeroRates = new LinkedList<>();
        data.rows()
                .select(row -> curveId.equalsIgnoreCase(row.getValue("CurveId")))
                .rows().forEach(r -> {
            maturityDates.add(r.getValue("MaturityDate"));
            zeroRates.add(r.getDouble("ZeroCouponRate") / 100);
        });

        final FinalValue<LocalDate> marketDate = new FinalValue<>();
        data.col("ValueDate").first(v -> v != null).ifPresent(v -> {
            final DataFrameRow<Integer, String> row = data.rowAt(v.rowKey());
            marketDate.set(row.getValue("ValueDate"));
        });

        if (!marketDate.hasValue()) throw new RuntimeException("Cannot find 'ValueDate' field in Yield Curve File.");

        return new YieldCurve(maturityDates, zeroRates, RateType.ZERO_RATE, interpMethod, dcf, marketDate.get());
    }
}
