package com.lchclearnet.market.table;

import com.lchclearnet.market.MarketService;
import com.lchclearnet.table.Row;
import com.lchclearnet.table.Table;

import java.time.LocalDate;
import java.util.Optional;

public abstract class AbstractTableMarketService implements MarketService {

    protected final Table data;
    private final String marketDateFieldName;

    public AbstractTableMarketService(String marketDateFieldName, Table data) {
        this.marketDateFieldName = marketDateFieldName;
        this.data = data;
    }

    @Override
    public LocalDate getMarketDate() {
        Optional<Row> row = data.first(r -> r.getDate(marketDateFieldName) != null);
        return row.isPresent() ? row.get().getDate(marketDateFieldName) : null;
    }
}
