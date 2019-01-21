package com.lchclearnet.market.fx;

import com.lchclearnet.utils.CurrencyPair;

import java.time.LocalDate;
import java.util.stream.Stream;

public class FxForwardOutrightCurves implements FxForwardOutrightService {

    private final FxSpots spotService;

    private final FxForwardSourceService forwardService;

    public FxForwardOutrightCurves(FxSpots spotService, FxForwardSourceService forwardService) {
        this.spotService = spotService;
        this.forwardService = forwardService;
    }

    @Override
    public LocalDate getMarketDate() {
        return forwardService.getMarketDate();
    }

    @Override
    public Double getForwardRate(CurrencyPair ccyPair, LocalDate date) {
        return forwardService.getForwardRate(ccyPair, spotService.getSpot(ccyPair), date);
    }

    @Override
    public Stream<Double> getForwardRates(CurrencyPair ccyPair, LocalDate... dates) {
        return forwardService.getForwardRates(ccyPair, spotService.getSpot(ccyPair), dates);
    }
}
