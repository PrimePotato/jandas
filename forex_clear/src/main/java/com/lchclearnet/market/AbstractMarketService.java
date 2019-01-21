package com.lchclearnet.market;

import java.time.LocalDate;

public abstract class AbstractMarketService {

    private final LocalDate valueDate;

    public AbstractMarketService(LocalDate valueDate) {
        this.valueDate = valueDate;
    }

    public LocalDate getMarketDate() {
        return valueDate;
    }
}
