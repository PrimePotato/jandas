package com.lchclearnet.trade.spark;

import com.lchclearnet.fx.AppConfig;
import com.lchclearnet.request.AbstractSmartService;
import com.lchclearnet.request.SmartRequest;
import com.lchclearnet.utils.spark.SparkSqlUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Queue;

public class SparkTradeFileAccessor extends AbstractSmartService<SparkTradeService> {

    private final AppConfig config;
    private SparkSession spark;

    public SparkTradeFileAccessor(AppConfig config) {
        this.config = config;
    }

    @Override
    public void startup() {
        //Create SparkSession object aka spark
        String hadoopHomeDir = null;
        String sparkSerializer = null;
        spark = SparkSqlUtils.getLocalSession("ForeX Trade File Accessor", hadoopHomeDir, sparkSerializer);
    }

    public SparkTradeService submit(SmartRequest request) {
        Dataset<Row> response;
        String tradeDir = request.get("trade_dir", config.get(AppConfig.TRADE, AppConfig.DIR));
        File tradesFile = findTradesFile(Paths.get(tradeDir, request.get("env")));

        //Create a row dataset from a CSV file and apply schema to it
        Dataset<Row> trades = SparkSqlUtils.readCsv(tradesFile.getAbsolutePath(), spark, ",", true, true);

        String sql = request.get("sql");
        if (sql != null) {
            // Register the trades DataFrame as a SQL temporary view
            trades.createOrReplaceTempView("trades");
            //Instant start = Instant.now();
            response = spark.sql(sql);
      /*Instant end = Instant.now();
      Duration duration = Duration.between(start, end);
      String sql_request = request.getObject("sql");
      String msg =
          String.format("Time to executre %s in %ss %sms",
              sql_request != null ? String.format("[%s]", sql_request): "",
              duration.getSeconds(), duration.getNano()/1000_000);
      System.out.println(msg);*/
        } else {
            response = trades;
        }

        return new SparkTradeService(response);
    }

    private File findTradesFile(Path tradeDir) {
        Queue<Path> paths = new PriorityQueue<>(11, Collections.reverseOrder());

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(tradeDir, "*_FRPT0001.csv")) {
            for (Path entry : stream) {
                paths.offer(entry);
            }
        } catch (IOException x) {
            String msg = String.format("error reading folder %s: %s", tradeDir, x.getMessage());
            throw new RuntimeException(msg, x);
        }

        return paths.poll().toFile();
    }

}
