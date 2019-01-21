package com.lchclearnet.utils.morpheus;

import com.lchclearnet.fx.AppConfig;
import com.lchclearnet.utils.Config;
import com.lchclearnet.utils.Counter;
import com.lchclearnet.utils.FileHelper;
import com.lchclearnet.utils.IndexedPredicate;
import com.zavtech.morpheus.frame.DataFrame;
import com.zavtech.morpheus.frame.DataFrameRow;
import com.zavtech.morpheus.frame.DataFrameRows;
import com.zavtech.morpheus.source.CsvSourceOptions;
import com.lchclearnet.utils.Tuple;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class CsvAccessor<R> implements Consumer<CsvSourceOptions<R>> {


    private boolean header;
    private Charset charset;
    private boolean parallel;
    private String dir;
    private String filePattern;
    private Map<String, ?> columns;
    private Tuple rowKeyParser;
    private IndexedPredicate<DataFrameRow<R, String>> rowFilter;

    public CsvAccessor() {
        header = true;
        charset = Charset.forName("UTF-8");
        parallel = true;
        rowFilter = (row, index, count) -> row.getValue(0) != null;
        rowKeyParser = null;
    }

    public static <R> CsvAccessor<R> of(Config config, String... accessorProps) {

        CsvAccessor<R> accessor = new CsvAccessor<>();

        String[] props = new String[accessorProps.length + 1];
        System.arraycopy(accessorProps, 0, props, 0, accessorProps.length);

        //optional fields
        props[accessorProps.length] = AppConfig.HEADER;
        accessor.setHeader(config.get(props));

        props[accessorProps.length] = AppConfig.CHARSET;
        accessor.setCharset(config.get(props));

        props[accessorProps.length] = AppConfig.PARALLEL;
        accessor.setParallel(config.get(props));

        //required fields
        props[accessorProps.length] = AppConfig.DIR;
        accessor.setDir(config.get(props));

        props[accessorProps.length] = AppConfig.FILE_PATTERN;
        accessor.setFilePattern(config.get(props));

        props[accessorProps.length] = AppConfig.COLUMNS;
        accessor.setColumns(config.get(props));

        props[accessorProps.length] = AppConfig.ROW_INDEX;
        accessor.setRowIndex(config.get(props));


        return accessor;
    }

    public CsvAccessor<R> setHeader(Boolean header) {
        if (header == null) return this;

        this.header = header;
        return this;
    }

    public CsvAccessor<R> setCharset(String charset) {
        if (charset == null) return this;
        this.charset = Charset.forName(charset);
        return this;
    }

    public CsvAccessor<R> setParallel(Boolean parallel) {
        if (parallel == null) return this;

        this.parallel = parallel;
        return this;
    }

    public CsvAccessor<R> setDir(String dir) {
        this.dir = dir;
        return this;
    }

    public CsvAccessor<R> setFilePattern(String filePattern) {
        this.filePattern = filePattern;
        return this;
    }

    public CsvAccessor<R> setColumns(Map<String, ?> columns) {
        this.columns = columns;
        return this;
    }

    public CsvAccessor<R> setRowIndex(List rowIndex) {
        if (rowIndex == null) return this;

        Class rowIndexType = ColumnFormats.getColumnType((String) rowIndex.get(0));
        int columnIndex = (int) rowIndex.get(1);

        Function<String[], ?> rowIndexParser;
        if (rowIndexType == int.class) {
            rowIndexParser = values -> values.length > columnIndex ? Integer.parseInt(values[columnIndex]) : null;
        } else if (rowIndexType == Integer.class) {
            rowIndexParser = values -> values.length > columnIndex ? (R) Integer.valueOf(values[columnIndex]) : null;
        } else if (rowIndexType == long.class) {
            rowIndexParser = values -> values.length > columnIndex ? Long.parseLong(values[columnIndex]) : null;
        } else if (rowIndexType == Long.class) {
            rowIndexParser = values -> values.length > columnIndex ? (R) Long.valueOf(values[columnIndex]) : null;
        } else if (rowIndexType == LocalDate.class) {
            rowIndexParser = values -> values.length > columnIndex ? LocalDate.parse(values[columnIndex], DateTimeFormatter.ofPattern((String) rowIndex.get(2))) : null;
        } else if (rowIndexType == LocalDateTime.class) {
            rowIndexParser = values -> values.length > columnIndex ? LocalDateTime.parse(values[columnIndex], DateTimeFormatter.ofPattern((String) rowIndex.get(2))) : null;
        } else {
            rowIndexParser = values -> values.length > columnIndex ? (R) values[columnIndex] : null;
        }

        return setRowKeyParser(rowIndexType, rowIndexParser);
    }

    public CsvAccessor<R> setRowFilter(IndexedPredicate<DataFrameRow<R, String>> rowFilter) {
        this.rowFilter = rowFilter;
        return this;
    }

    public CsvAccessor<R> setRowKeyParser(Class<R> rowIndexType, Function<String[], ?> rowIndexParser) {
        this.rowKeyParser = Tuple.of(rowIndexType, rowIndexParser);
        return this;
    }

    @Override
    public void accept(CsvSourceOptions<R> options) {
        options.setFile(sourceFile());
        options.setHeader(header);
        options.setCharset(charset);
        options.setParallel(parallel);

        columns.forEach((column, parameters) -> {
            String type;
            String[] params;
            if (parameters instanceof String) {
                type = (String) parameters;
                params = new String[0];
            } else {
                List<String> ps = (List) parameters;
                type = ps.get(0);
                params = new String[ps.size() - 1];
                for (int i = 0; i < params.length; i++) {
                    params[i] = ps.get(i + 1);
                }
            }
            ColumnFormats.setColumnFormat(options, column, type, params);
        });

        if (rowKeyParser != null) {
            options.setRowKeyParser(rowKeyParser.item(0), rowKeyParser.item(1));
        }
    }

    public File sourceFile() {
        Path parent = Paths.get(dir);
        Queue<Path> paths = FileHelper.find(parent, filePattern, new PriorityQueue<>(11, Collections.reverseOrder()));
        return paths.poll().toFile();
    }

    public DataFrame<R, String> load() {

        DataFrame df = DataFrame.read().csv(options -> {
            accept((CsvSourceOptions<R>) options);
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
}
