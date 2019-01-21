package com.lchclearnet.utils.spark;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public class DataFrameContainer {

    protected final Dataset<Row> data;

    public DataFrameContainer(Dataset<Row> data) {

        this.data = data;
    }

    public List<String> valuesAsString(String param) {
        return SparkSqlUtils.valuesAsString(data, param);
    }

    public List<Integer> valuesAsInt(String param) {
        return SparkSqlUtils.valuesAsInt(data, param);
    }

    public List<Long> valuesAsLong(String param) {
        return SparkSqlUtils.valuesAsLong(data, param);
    }

    public List<Double> valuesAsDouble(String param) {
        return SparkSqlUtils.valuesAsDouble(data, param);
    }

    public List<Date> valuesAsDate(String param) {
        return SparkSqlUtils.valuesAsDate(data, param);
    }

    public List<Timestamp> valuesAsTimestamp(String param) {
        return SparkSqlUtils.valuesAsTimestamp(data, param);
    }

    @Override
    public String toString() {
        return data.showString(20, 20, false);
    }
}
