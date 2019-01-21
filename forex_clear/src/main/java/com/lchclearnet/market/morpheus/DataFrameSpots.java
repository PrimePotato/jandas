package com.lchclearnet.market.morpheus;

import com.google.common.collect.ImmutableSet;
import com.lchclearnet.market.fx.FxSpots;
import com.lchclearnet.utils.CurrencyPair;
import com.lchclearnet.utils.DoubleValue;
import com.lchclearnet.utils.FinalValue;
import com.zavtech.morpheus.frame.DataFrame;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DataFrameSpots extends AbstractDataFrameMarketService implements FxSpots {

    private final Map<CurrencyPair, Double> spotRates;
    private final Map<CurrencyPair, LocalDate> spotDates;

    public DataFrameSpots(DataFrame<Integer, String> data) {
        super("CurrentBusinessDate", data);
        spotRates = new ConcurrentHashMap<>();
        spotDates = new ConcurrentHashMap<>();
    }

    @Override
    public Set<CurrencyPair> getCurrencyPairs() {
        List pairs = data.col("CurrencyPair").distinct().toList();
        return (Set<CurrencyPair>) ImmutableSet.copyOf(pairs);
    }

    @Override
    public double getSpot(CurrencyPair pair) {
        return spotRates.computeIfAbsent(pair, p -> MarketDataHelper.buildFxRateTrianglation(this::loadSpot).apply(p));
    }

    private double loadSpot(CurrencyPair pair) {
        DoubleValue v = new DoubleValue();
        data.rows().first(r -> r.getValue("CurrencyPair") == pair).ifPresent(r -> {
            v.set(r.getDouble("Mid"));
        });
        if (!v.hasValue()) {
            CurrencyPair inverse = pair.inverse();
            data.rows().first(r -> r.getValue("CurrencyPair") == inverse).ifPresent(r -> {
                v.set(1 / r.getDouble("Mid"));
            });
        }
        if (!v.hasValue()) {
            throw new IllegalStateException(String.format("Could not load spot rate for %s", pair.code()));
        }

        return v.get();
    }

    @Override
    public LocalDate getSpotDate(CurrencyPair pair) {
        return spotDates.computeIfAbsent(pair, this::loadSpotDate);
    }

    private LocalDate loadSpotDate(CurrencyPair pair) {
        FinalValue<LocalDate> v = new FinalValue();
        data.rows().first(r -> r.getValue("CurrencyPair") == pair).ifPresent(r -> {
            v.set(r.getValue("SpotDate"));
        });
        if (!v.hasValue()) {
            CurrencyPair inverse = pair.inverse();
            data.rows().first(r -> r.getValue("CurrencyPair") == inverse).ifPresent(r -> {
                v.set(r.getValue("SpotDate"));
            });
        }
        if (!v.hasValue()) {
            throw new IllegalStateException(String.format("Could not load spot date for %s", pair.code()));
        }

        return v.get();
    }
}
