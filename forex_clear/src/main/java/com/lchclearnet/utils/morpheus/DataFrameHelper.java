package com.lchclearnet.utils.morpheus;

import com.lchclearnet.utils.Counter;
import com.lchclearnet.utils.FileHelper;
import com.lchclearnet.utils.IndexedPredicate;
import com.zavtech.morpheus.array.Array;
import com.zavtech.morpheus.array.ArrayCollector;
import com.zavtech.morpheus.frame.*;
import com.zavtech.morpheus.source.CsvSourceOptions;
import com.zavtech.morpheus.stats.Statistic1;
import com.lchclearnet.utils.Tuple;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;

public class DataFrameHelper {

    public static IndexedPredicate<DataFrameRow<Integer, String>> default_row_filter = new IndexedPredicate<DataFrameRow<Integer, String>>() {
        @Override
        public boolean filter(DataFrameRow<Integer, String> item, int index, int count) {
            Object value = item.getValue(0);
            return index < (count - 1) || !String.valueOf(value).startsWith("Record Count");
        }
    };

    public static <R> DataFrame<R, String> loadCsv(Path parent, String filePattern, Consumer<CsvSourceOptions<R>> configurator) {
        return loadCsv(parent, filePattern, configurator, (row, index, count) -> {
            Object value = row.getValue(0);
            return index < (count - 1) || (value != null && !value.toString().startsWith("Record Count"));
        });
    }

    public static <R> DataFrame<R, String> loadCsv(Path parent, String filePattern,
                                                   Consumer<CsvSourceOptions<R>> configurator, IndexedPredicate<DataFrameRow<R, String>> rowFilter) {
        Queue<Path> paths = FileHelper.find(parent, filePattern, new PriorityQueue<>(11, Collections.reverseOrder()));

        DataFrame df = DataFrame.read().csv(options -> {
            options.setFile(paths.poll().toFile());
            options.setHeader(true);
            options.setCharset(StandardCharsets.UTF_8);
            configurator.accept((CsvSourceOptions<R>) options);
            options.setParallel(false);
        });

        if (rowFilter != null) {
            DataFrameRows<R, String> rows = ((DataFrame<R, String>) df).rows();
            final int count = rows.count();
            final Counter counter = new Counter(count);
            //filter out the last row starting with Record Count
            return rows.select(row -> rowFilter.filter(row, counter.increment(), count));
        }

        return df;
    }

    public static <T> List<T> distinct(DataFrame<?, String> dataFrame, String colname) {
        return (List<T>) dataFrame.col(colname).distinct().toList();
    }

    public static void forEachTrade(DataFrame<?, String> dataFrame, Consumer<? super DataFrameRow<?, String>> tradeConsumer) {
        dataFrame.rows().forEach(tradeConsumer);
    }

    public static <T> void forEach(DataFrame<?, String> dataFrame, String tradeAttribute, Consumer<T> tradeValueConsumer) {
        dataFrame.rows().forEach((Consumer<? super DataFrameRow<?, String>>) row -> {
            tradeValueConsumer.accept(row.getValue(tradeAttribute));
        });
    }

    public static <T> List<T> getColValues(DataFrame<?, String> dataFrame, final String colName) {
        final List<T> values = new ArrayList<>();
        forEachTrade(dataFrame, row -> {
            values.add(row.getValue(colName));
        });
        return values;
    }

    public static <R, C> DataFrame<R, C> innerJoin(DataFrame<R, C> left, DataFrame<R, C> right) {
        if (left.rows().count() == 0 || right.rows().count() == 0) {
            return DataFrame.concatColumns(left, right);
        }
        DataFrame<R, C> result = DataFrame.combineFirst(left, right);
        if (result.rows().count() == 0) {
            return result;
        }
        return result.rows().select(row -> !row.hasNulls());
    }
}
