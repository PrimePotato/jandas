package com.lchclearnet.report.morpheus;

import com.lchclearnet.market.FxMarket;
import com.lchclearnet.report.ReportBuilder;
import com.lchclearnet.trade.TradeService;
import com.lchclearnet.utils.Currency;
import com.zavtech.morpheus.array.Array;
import com.zavtech.morpheus.frame.DataFrame;
import com.zavtech.morpheus.frame.DataFrameException;
import com.lchclearnet.utils.Tuple;
import com.zavtech.morpheus.util.text.printer.Printer;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.lchclearnet.trade.TradeService.CCY_PAIR;


public class CashFlowPvReport implements ReportBuilder {

    private DataFrame<String, String> cashFlowReport;
    private TradeService tradeService;
    private FxMarket marketService;
    private DataFrame<?, ?> report;
    private RowGroup rowGroup;

    public CashFlowPvReport setCashFlowReport(DataFrame<String, String> cashFlowReport) {
        this.cashFlowReport = cashFlowReport;
        return this;
    }

    public CashFlowPvReport setTradeService(TradeService tradeService) {
        this.tradeService = tradeService;
        return this;
    }

    public CashFlowPvReport setMarketService(FxMarket marketService) {
        this.marketService = marketService;
        return this;
    }

    public CashFlowPvReport setGroupBy(RowGroup rowGroup) {
        this.rowGroup = rowGroup;
        return this;
    }

    @Override
    public CashFlowPvReport build() {
        //DataFrameGrouping.Rows<String, String> grouping = cashFlowReport.rows().groupBy(groupByColumns);
        //report = grouping.stats(groupDepth).sum().rows().sort(true).cols().select("PV USD");
        report = sum(cashFlowReport, rowGroup, "PvUSD");

        return this;
    }

    protected DataFrame<Tuple, String> sum(DataFrame<String, String> source, RowGroup rowGroup, String... aggrCols) {
        try {
            final Array<String> columnKeys = Array.of(aggrCols);

            final Map<Tuple, double[]> grped = new HashMap<>();
            source.rows().forEach(row -> {
                Tuple key = rowGroup.index(row);
                double[] values = grped.computeIfAbsent(key, k -> new double[aggrCols.length]);
                for (int i = 0; i < aggrCols.length; i++) {
                    values[i] += row.getDouble(aggrCols[i]);
                }
            });

            final DataFrame<Tuple, String> dfSum = DataFrame.of(grped.keySet(), String.class, columns -> {
                for (int aggrIdx = 0; aggrIdx < aggrCols.length; ++aggrIdx) {
                    final Iterator<Double> it = new GroupIterable(grped, aggrIdx).iterator();
                    columns.add(aggrCols[aggrIdx], Double.class).applyDoubles(v -> it.next());
                }
            });

            return dfSum;
        } catch (Exception ex) {
            throw new DataFrameException("Failed to compute grouped row stats: " + ex.getMessage(), ex);
        }
    }

    @Override
    public DataFrame getReport() {

        return report;
    }

    public CashFlowPvReport setReport(DataFrame<String, String> report) {
        this.report = report;
        return this;
    }

    @Override
    public void writeCsv(File csvFile) {
        if (report == null) return;
        csvFile.getParentFile().mkdirs();
        report.write().csv(options -> {
            options.setFile(csvFile);
            options.setSeparator(",");
            options.setIncludeRowHeader(true);
            options.setIncludeColumnHeader(true);
            options.setNullText("");
            //options.setTitle("");

            options.setFormats(formats -> {
                formats.setDecimalFormat(double.class, "0.#################", 1);
                formats.setDecimalFormat(Double.class, "0.#################", 1);
                formats.<Tuple>setPrinter(Tuple.Tuple1.class, Printer.forObject(tuple -> {
                    return tuple.item(0);
                }));
                formats.<Tuple>setPrinter(Tuple.Tuple2.class, Printer.forObject(tuple -> {
                    return String.format("%s-%s", tuple.item(0), tuple.item(1));
                }));
                formats.<Tuple>setPrinter(Tuple.Tuple3.class, Printer.forObject(tuple -> {
                    return String.format("%s-%s-%s", tuple.item(0), tuple.item(1), tuple.item(2));
                }));
                formats.<Currency>setPrinter(CCY_PAIR, Printer.forObject(pair -> String.valueOf(pair)));
            });
        });
    }

    public class GroupIterable implements Iterable {

        private final Map<Tuple, double[]> grouped;
        private final int cursor;


        public GroupIterable(Map<Tuple, double[]> grouped, int cursor) {
            this.grouped = grouped;
            this.cursor = cursor;
        }

        @Override
        public Iterator<Double> iterator() {

            Iterator<double[]> iterator = grouped.values().iterator();

            return new Iterator() {
                @Override
                public boolean hasNext() {
                    return iterator.hasNext();
                }

                @Override
                public Double next() {
                    return iterator.next()[cursor];
                }
            };

        }
    }

}
