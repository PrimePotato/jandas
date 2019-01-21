package com.lchclearnet.trade.spark;

import com.lchclearnet.utils.Currency;
import com.lchclearnet.utils.spark.DataFrameContainer;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class SparkTradeService extends DataFrameContainer {

    private volatile Set<String> ccys;

    public SparkTradeService(Dataset<Row> trades) {
        super(trades);
        this.ccys = null;
    }

    public Iterable<Currency> getCurrencies() {
        if (ccys == null) {
            synchronized (data) {
                ccys = new HashSet<>();
                ccys.addAll(valuesAsString("BuyCurrency"));
                ccys.addAll(valuesAsString("SellCurrency"));
                ccys.addAll(valuesAsString("CallCurrency"));
                ccys.addAll(valuesAsString("PutCurrency"));
                ccys.remove(null);
            }
        }
        return ccys.stream().map(ccy -> Currency.parse(ccy)).collect(Collectors.toList());
    }

}
