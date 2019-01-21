package com.lchclearnet.mutator.morpheus;


import com.lchclearnet.market.fx.FxSpots;
import com.lchclearnet.market.MarketService;
import com.lchclearnet.market.morpheus.MarketDataHelper;
import com.lchclearnet.mutator.ScenarioService;
import com.lchclearnet.utils.CurrencyPair;
import com.lchclearnet.utils.ShiftType;
import com.zavtech.morpheus.frame.DataFrame;
import it.unimi.dsi.fastutil.ints.Int2DoubleArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DataFrameYieldCurveScenarioService implements ScenarioService {

    private final Int2DoubleArrayMap shifts;
    private final Int2ObjectArrayMap<ShiftType> shiftTypes;

    public DataFrameYieldCurveScenarioService(DataFrame<Integer, String> data) {
        this.shifts = new Int2DoubleArrayMap(CurrencyPair.size());
        this.shiftTypes = new Int2ObjectArrayMap(CurrencyPair.size());
        data.rows().forEach(row -> {
            CurrencyPair pair = row.getValue("CurrencyPair");
            //shifts are always express in term of convention pair
            CurrencyPair conventionPair = pair.toConvention();
            shifts.put(conventionPair.ordinal(), row.getDouble("SpotRateShift_Core"));
            shiftTypes.put(conventionPair.ordinal(), row.getValue("ShiftType"));
        });
    }

    @Override
    public MarketService apply(MarketService marketService, String... scenarioNames) {
        return new ScenarioSpots((FxSpots) marketService);
    }

    private class ScenarioSpots implements FxSpots {

        private final FxSpots fxSpots;
        private final Map<CurrencyPair, Double> spotRates;

        public ScenarioSpots(FxSpots fxSpots) {
            this.fxSpots = fxSpots;
            spotRates = new ConcurrentHashMap<>();
        }

        @Override
        public Set<CurrencyPair> getCurrencyPairs() {
            return fxSpots.getCurrencyPairs();
        }

        @Override
        public LocalDate getMarketDate() {
            return fxSpots.getMarketDate();
        }

        @Override
        public LocalDate getSpotDate(CurrencyPair pair) {
            return fxSpots.getSpotDate(pair);
        }

        @Override
        public double getSpot(CurrencyPair pair) {
            return spotRates.computeIfAbsent(pair, p -> MarketDataHelper.buildFxRateTrianglation(this::loadSpot).apply(p));
        }

        private double loadSpot(CurrencyPair currencyPair) {

            CurrencyPair conventionPair = currencyPair.toConvention();

            double spotRate;

            switch (shiftTypes.get(conventionPair.ordinal())) {
                case ABSOLUTE:
                    spotRate = fxSpots.getSpot(conventionPair) + shifts.get(conventionPair.ordinal());
                    break;
                case PERCENT:
                    spotRate = fxSpots.getSpot(conventionPair) * (1 + shifts.get(conventionPair.ordinal()) / 100);
                    break;
                default:
                    throw new IllegalArgumentException(String.format("%s is not supported as Shift Type", shiftTypes.get(conventionPair.ordinal())));
            }

            return currencyPair == conventionPair ? spotRate : 1 / spotRate;
        }
    }
}
