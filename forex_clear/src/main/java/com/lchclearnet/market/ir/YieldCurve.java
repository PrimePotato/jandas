package com.lchclearnet.market.ir;

import com.lchclearnet.calendar.DayCountFraction;
import com.lchclearnet.market.RateType;
import com.lchclearnet.utils.InterpolationMethod;
import com.lchclearnet.utils.Tuple;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static java.lang.Math.pow;
import static org.apache.commons.math3.util.FastMath.exp;

public class YieldCurve {

    private UnivariateFunction interpFunction;

    private LocalDate valuationDate;
    private DayCountFraction dayCountFraction;
    private RateType rateType;

    private Map<LocalDate, Double> discountFactorCache = new ConcurrentHashMap<>();
    private Map<LocalDate, Double> zeroRateCache = new ConcurrentHashMap<>();

    public YieldCurve(List<LocalDate> dates, List<Double> zero_rates, RateType rate_type,
                      InterpolationMethod im, DayCountFraction dcf, LocalDate val_date) {

        this.valuationDate = val_date;
        this.dayCountFraction = dcf;
        this.rateType = rate_type;
        this.buildInterpolator(dates, zero_rates, rate_type, im);
    }

    public static Double implyDiscountFactor(Double zero_rate, Double dcf) {

        return 1. / pow(1. + zero_rate, dcf);
    }

    public static Double implyZeroRate(Double discount, Double dcf) {

        return pow(1. / discount, 1. / dcf) - 1;
    }

    public double getDayCountFraction(LocalDate dt) {

        return this.dayCountFraction.calc(this.valuationDate, dt);
    }

    public double getDayCountFraction(LocalDate start_dt, LocalDate end_dt) {

        return this.dayCountFraction.calc(start_dt, end_dt);
    }

    public double discountFactor(LocalDate dt) {

        return exp(-getDayCountFraction(dt) * zeroRate(dt));
    }

    public double discountFactor(LocalDate start_dt, LocalDate end_dt) {

        double start_df = discountFactor(start_dt);
        double end_df = discountFactor(end_dt);
        return end_df / start_df;
    }

    public double zeroRate(LocalDate dt) {

        return interpFunction.value(getDayCountFraction(dt));
    }

    public double zeroRate(LocalDate start_dt, LocalDate end_dt) {

        if (start_dt == end_dt) {
            return zeroRate(end_dt);
        }

        Double st_t = getDayCountFraction(start_dt);
        Double ed_t = getDayCountFraction(end_dt);

        Double st_zr = zeroRate(start_dt);
        Double ed_zr = zeroRate(end_dt);

        return (ed_zr * ed_t - st_zr * st_t) / (ed_t - st_t);
    }

    private void buildInterpolator(List<LocalDate> dates, List<Double> rates, RateType rate_type,
                                   UnivariateInterpolator interpolator) {

        double[] taus = dates.stream().map(this::getDayCountFraction).mapToDouble(x -> x).toArray();

        if (rate_type == RateType.ZERO_RATE) {
            this.interpFunction =
                    interpolator.interpolate(taus, rates.stream().mapToDouble(x -> x).toArray());
        } else {
            throw new IllegalArgumentException("Unsupported build type"); //#TODO: Add
        }
    }

    public Stream<Double> discountFactors(LocalDate[] dates) {

        Arrays.stream(dates)
                .distinct()
                .filter(x -> !discountFactorCache.containsKey(x))
                .map(d -> Tuple.of(discountFactor(d), d))
                .forEach(t -> discountFactorCache.put(t.item(1), t.item(0)));

        return Arrays.stream(dates).map(x -> discountFactorCache.get(x));
    }

    public Stream<Double> zeroRates(LocalDate[] dates) {

        Arrays.stream(dates)
                .distinct()
                .filter(x -> !zeroRateCache.containsKey(x))
                .map(d -> Tuple.of(zeroRate(d), d))
                .forEach(t -> zeroRateCache.put(t.item(1), t.item(0)));

        return Arrays.stream(dates).map(x -> zeroRateCache.get(x));
    }
}
