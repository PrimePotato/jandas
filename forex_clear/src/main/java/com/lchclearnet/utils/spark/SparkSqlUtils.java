package com.lchclearnet.utils.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.spark.sql.functions.col;

public class SparkSqlUtils {

    public static SparkSession getLocalSession(String appName, String hadoopHomeDir, String sparkSerializer) {
        System.setProperty("hadoop.home.dir", hadoopHomeDir);

        //Create SparkConf object aka sparkConf
        SparkConf sparkConf = new SparkConf().setMaster("local")
                .setAppName(appName)
                .set("spark.driver.host", "localhost");

        if (sparkSerializer != null) {
            sparkConf.set("spark.serializer", sparkSerializer);
        }

        return SparkSession.builder().config(sparkConf).getOrCreate();
    }


    public static Dataset<Row> readCsv(String path, SparkSession session,
                                       String sep, boolean inferSchema, boolean header) {
        return session.read().format("csv")
                .option("sep", sep)
                .option("inferSchema", String.valueOf(inferSchema))
                .option("header", String.valueOf(header))
                .load(path);
    }

    public static List<String> valuesAsString(Dataset<Row> data, String column) {

        return values(data, (MapFunction<Row, String>) row -> row.getString(0),
                Encoders.STRING(), column);
    }

    public static List<Integer> valuesAsInt(Dataset<Row> data, String column) {

        return SparkSqlUtils.values(data, (MapFunction<Row, Integer>) row -> row.getInt(0),
                Encoders.INT(), column);
    }

    public static List<Long> valuesAsLong(Dataset<Row> data, String column) {

        return SparkSqlUtils.values(data, (MapFunction<Row, Long>) row -> row.getLong(0),
                Encoders.LONG(), column);
    }

    public static List<Double> valuesAsDouble(Dataset<Row> data, String column) {

        return SparkSqlUtils.values(data, (MapFunction<Row, Double>) row -> row.getDouble(0),
                Encoders.DOUBLE(), column);
    }

    public static List<Date> valuesAsDate(Dataset<Row> data, String column) {

        return SparkSqlUtils.values(data, (MapFunction<Row, Date>) row -> row.getDate(0),
                Encoders.DATE(), column);
    }

    public static List<Timestamp> valuesAsTimestamp(Dataset<Row> data, String column) {

        return SparkSqlUtils.values(data, (MapFunction<Row, Timestamp>) row -> row.getTimestamp(0),
                Encoders.TIMESTAMP(), column);
    }

    public static <U> List<U> values(Dataset<Row> data, MapFunction<Row, U> map, Encoder<U> encoder,
                                     String... params) {

        List<Column> cols = Arrays.stream(params).map(param -> col(param)).collect(Collectors.toList());
        return data.select(cols.toArray(new Column[cols.size()])).map(map, encoder).collectAsList();
    }


}
