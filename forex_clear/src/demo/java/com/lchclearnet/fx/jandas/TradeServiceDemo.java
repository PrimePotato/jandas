package com.lchclearnet.fx.jandas;

import com.lchclearnet.fx.AppConfig;
import com.lchclearnet.fx.DemoHelper;
import com.lchclearnet.jandas.column.DoubleColumn;
import com.lchclearnet.jandas.dataframe.DataFrame;
import com.lchclearnet.request.SmartRequest;
import com.lchclearnet.trade.TradeService;
import com.lchclearnet.trade.TradeServiceFactory;
import com.lchclearnet.trade.jandas.DataFrameTradeAdapter;
import com.lchclearnet.utils.PrintUtils;
import com.lchclearnet.utils.Tuple;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;

public class TradeServiceDemo {

  public static void main(String[] args) {

    Tuple params = DemoHelper.parseArgs(args);
    AppConfig configService = params.item(0);
    TradeServiceFactory tradeServiceFactory = buildTradeService(configService);
    SmartRequest all_trades_request = new SmartRequest();
     TradeService response =
        DemoHelper.execute_request(tradeServiceFactory, all_trades_request, "trades in cache");

    PrintUtils.separator("Currencies");
    System.out.println(response.getCurrencies());

    PrintUtils.separator("Trades");
    DataFrame df = response.getTrades();
    df.print(20);

    DoubleColumn dc = (DoubleColumn)df.column(22);
    dc.buildMatrix();
    dc.plus(dc);

    PrintUtils.separator("Cash Flow");
    DataFrame cashFlows = response.getCashFlows(null);
    cashFlows.print(20);

//    DoubleArrayList

//    PrintUtils.separator("Options");
//    DataFrame options = response.getOptions(null);
//    options.print(20);

  }

  private static TradeServiceFactory buildTradeService(AppConfig config) {

    TradeServiceFactory tradeServiceFactory =
        new TradeServiceFactory(new DataFrameTradeAdapter(config));
    tradeServiceFactory.startup();
    return tradeServiceFactory;
  }

}
