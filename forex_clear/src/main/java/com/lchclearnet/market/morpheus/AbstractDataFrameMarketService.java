package com.lchclearnet.market.morpheus;

import com.lchclearnet.market.MarketService;
import com.lchclearnet.utils.FinalValue;
import com.zavtech.morpheus.frame.DataFrame;
import com.zavtech.morpheus.frame.DataFrameRow;

import java.time.LocalDate;

public abstract class AbstractDataFrameMarketService implements MarketService {

    protected final DataFrame<Integer, String> data;
    private final String marketDateFieldName;

    public AbstractDataFrameMarketService(String marketDateFieldName, DataFrame<Integer, String> data) {
        this.marketDateFieldName = marketDateFieldName;
        this.data = data;
    }

    @Override
    public LocalDate getMarketDate() {
        final FinalValue<LocalDate> value = new FinalValue();
        data.col(marketDateFieldName).first(v -> v != null).ifPresent(v -> {
            final DataFrameRow<Integer, String> row = data.rowAt(v.rowKey());
            value.set(row.getValue(marketDateFieldName));
        });

        return value.get();
    }
}
