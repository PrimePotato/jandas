package com.lchclearnet.market.fx;

import com.lchclearnet.market.MarketService;
import com.lchclearnet.utils.CurrencyPair;

import java.time.LocalDate;
import java.util.stream.Stream;

public interface FxForwardOutrightService extends MarketService {

    Double getForwardRate(CurrencyPair pair, LocalDate date);

    Stream<Double> getForwardRates(CurrencyPair pair, LocalDate... dates);
}
