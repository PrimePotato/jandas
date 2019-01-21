package com.lchclearnet.fx.spark;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

public class Csv2DataFrame {

    public static void main(String[] args) {

        System.setProperty("hadoop.home.dir", "D:/home/djamel/project/smart_fx/resources");

        //Create SparkSession object aka spark
        SparkSession spark = SparkSession.builder().appName("ForeX Trade Service").master("local")
                .getOrCreate();

        //Create Schema for Record with StructType TODO; Sort header base on columns in csv file
        StructType schema = new StructType()
                .add(new StructField("CurrencyPair", DataTypes.StringType, false, Metadata.empty()))
                .add(new StructField("Ask", DataTypes.DoubleType, false, Metadata.empty()))
                .add(new StructField("Bid", DataTypes.DoubleType, false, Metadata.empty()))
                .add(new StructField("Mid", DataTypes.DoubleType, false, Metadata.empty()))
                .add(new StructField("MDSetId", DataTypes.StringType, false, Metadata.empty()))
                .add(new StructField("MDSetDateTime", DataTypes.StringType, false, Metadata.empty()))
                .add(new StructField("CurrentBusinessDate", DataTypes.StringType, false, Metadata.empty()))
                .add(new StructField("SpotDate", DataTypes.StringType, false, Metadata.empty()));

        //Create morpheus from CSV file and apply schema to it
        Dataset<Row> df = spark.read()
                .option("mode", "DROPMALFORMED")
                .option("header", true)
                .schema(schema)
                .csv("file:///D:/jandas/forex/MT1/20180705/20180705_2359_EOD_21751_FXMD0001.csv");
        //df = sqlContext.read.format('com.databricks.spark.csv').options(header='true').load('file:///home/vagrant/jandas/nyctaxisub.csv')


        df.show();
        Dataset<Row> ccy_pairs = df.select(new Column("CurrencyPair"), new Column("Bid"), new Column("Ask"));
        System.out.println(ccy_pairs.collectAsList());
    }
}
