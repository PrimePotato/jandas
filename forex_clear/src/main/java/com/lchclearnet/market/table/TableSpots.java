package com.lchclearnet.market.table;

import com.google.common.collect.ImmutableSet;
import com.lchclearnet.market.fx.FxSpots;
import com.lchclearnet.table.Column;
import com.lchclearnet.table.Row;
import com.lchclearnet.table.Table;
import com.lchclearnet.utils.CurrencyPair;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TableSpots extends AbstractTableMarketService implements FxSpots {

    private final Map<CurrencyPair, Double> spotRates;
    private final Map<CurrencyPair, LocalDate> spotDates;

    public TableSpots(Table data) {
        super("CurrentBusinessDate", data);
        spotRates = new ConcurrentHashMap<>();
        spotDates = new ConcurrentHashMap<>();
    }

    @Override
    public Set<CurrencyPair> getCurrencyPairs() {
        return ImmutableSet.copyOf((Column<CurrencyPair>)(data.column("CurrencyPair")).unique());
    }

    @Override
    public double getSpot(CurrencyPair pair) {
        return spotRates.computeIfAbsent(pair, p -> MarketDataHelper.buildFxRateTrianglation(this::loadSpot).apply(p));
    }

    private double loadSpot(CurrencyPair pair) {
        double spot = Double.NaN;
        Optional<Row> row = data.first(r -> pair == r.getObject("CurrencyPair"));

        if (row.isPresent()) {
            spot = row.get().getDouble("Mid");
        }

        if (Double.isNaN(spot)) {
            CurrencyPair inverse = pair.inverse();
            row = data.first(r -> inverse == r.getObject("CurrencyPair"));
            if (row.isPresent()) {
                spot = 1.0 / row.get().getDouble("Mid");
            }
        }

        if (Double.isNaN(spot)) {
            throw new IllegalStateException(String.format("Could not load spot for %s", pair.code()));
        }

        return spot;
    }

    @Override
    public LocalDate getSpotDate(CurrencyPair pair) {
        return spotDates.computeIfAbsent(pair, this::loadSpotDate);
    }

    private LocalDate loadSpotDate(CurrencyPair pair) {
        LocalDate spot = null;
        Optional<Row> row = data.first(r -> pair == r.getObject("CurrencyPair"));

        if (row.isPresent()) {
            spot = row.get().getDate("SpotDate");
        }
        if (spot == null) {
            CurrencyPair inverse = pair.inverse();
            row = data.first(r -> inverse == r.getObject("CurrencyPair"));
            if (row.isPresent()) {
                spot = row.get().getDate("SpotDate");
            }
        }

        if (spot == null) {
            throw new IllegalStateException(String.format("Could not load spot date for %s", pair.code()));
        }

        return spot;
    }
}
