package com.lchclearnet.market.fx;

import com.lchclearnet.market.MarketService;
import com.lchclearnet.utils.CurrencyPair;

import java.time.LocalDate;
import java.util.Set;

public interface FxSpots extends MarketService {
    Set<CurrencyPair> getCurrencyPairs();

    double getSpot(CurrencyPair pair);

    LocalDate getSpotDate(CurrencyPair pair);
}
