package com.lchclearnet.market.ir;

import com.lchclearnet.calendar.DayCountFraction;
import com.lchclearnet.market.RateType;
import com.lchclearnet.utils.InterpolationMethod;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class YieldCurveTest {

    YieldCurve yc_dc, yc_zr;
    LinkedList<LocalDate> dts;
    LinkedList<Double> discounts;
    LinkedList<Double> zero_rates;
    LocalDate val_date;

    @Before
    public void setup() {

        dts = new LinkedList<>();
        discounts = new LinkedList<>();
        zero_rates = new LinkedList<>();

        val_date = LocalDate.of(2018, 1, 1);

        dts.add(LocalDate.of(2018, 9, 23));
        dts.add(LocalDate.of(2018, 12, 23));
        dts.add(LocalDate.of(2019, 2, 23));
        dts.add(LocalDate.of(2019, 8, 23));

        discounts.add(0.99);
        discounts.add(0.98);
        discounts.add(0.97);
        discounts.add(0.96);

        zero_rates.add(0.04);
        zero_rates.add(0.05);
        zero_rates.add(0.06);
        zero_rates.add(0.07);

//        yc_dc = new YieldCurve(dts, discounts, RateType.DISCOUNT_FACTOR, InterpolationMethod
//            .LINEAR, false,
//                DayCountFraction.DCF_ACT_360, val_date);
        yc_zr = new YieldCurve(dts, zero_rates, RateType.ZERO_RATE, InterpolationMethod.LINEAR_CONSTANT,
            DayCountFraction.DCF_ACT_360, val_date);
    }

    @Test
    public void zeroRates() {

        LocalDate[] dts = {
                LocalDate.of(2018, 9, 23),
                LocalDate.of(2019, 2, 23),
                LocalDate.of(2019, 3, 23),
                LocalDate.of(2019, 4, 23),
                LocalDate.of(2019, 5, 23)
        };
        Stream<Double> zrs = yc_zr.zeroRates(dts);
        System.out.println(Arrays.toString(zrs.toArray()));
    }

    @Test
    public void zeroRateCubic() {

        LocalDate[] ary_dts = {
                LocalDate.of(2018, 9, 23),
                LocalDate.of(2019, 2, 23),
                LocalDate.of(2019, 3, 23),
                LocalDate.of(2019, 4, 23),
                LocalDate.of(2019, 5, 23)
        };
        SplineInterpolator si = new SplineInterpolator();
        YieldCurve yc_cb = new YieldCurve(dts, zero_rates, RateType.ZERO_RATE, InterpolationMethod.SPLINE_CONSTANT, DayCountFraction.DCF_ACT_360,  val_date);

        YieldCurve yc_cb_log = new YieldCurve(dts, zero_rates, RateType.ZERO_RATE,  InterpolationMethod.SPLINE_CONSTANT, DayCountFraction.DCF_ACT_360, val_date);
    }

    @Test
    public void implyDiscountFactor() {

        double df = YieldCurve.implyDiscountFactor(0.05, 1.0);
        System.out.println(df);
    }

    @Test
    public void implyZeroRate() {

        double df = YieldCurve.implyZeroRate(0.9523809523809523, 1.0);
        System.out.println(df);
    }

    @Test
    public void implyRateConsistent() {

        double r = 0.05;
        double df = YieldCurve.implyDiscountFactor(r, 1.0);
        double zr = YieldCurve.implyZeroRate(df, 1.0);
        assertEquals(r, zr, 1e-15);
    }

//    @Test
//    public void rateConsistency() {
//
//        LocalDate[] local_dates = dts.toArray(new LocalDate[0]);
//
//        List<Double> lst_dfs = yc_zr.discountFactors(local_dates).collect(Collectors
//                .toList());
//
//        YieldCurve yc_from_dfs = new YieldCurve(dts, lst_dfs, RateType.DISCOUNT_FACTOR,
//            InterpolationMethod.LINEAR, false,
//                DayCountFraction.DCF_ACT_360, val_date);
//
//        Double[] zrs = yc_from_dfs.zeroRates(local_dates).toArray(Double[]::new);
//
//        System.out.println(Arrays.toString(zrs));
//
//        for (int i = 0; i < zrs.rowCount; ++i) {
//            assertEquals(zrs[i], zero_rates.getObject(i), 1e-15);
//        }
//
//    }

}