package com.lchclearnet.fx.table;

import com.lchclearnet.fx.AppConfig;
import com.lchclearnet.fx.DemoHelper;
import com.lchclearnet.request.SmartRequest;
import com.lchclearnet.table.Table;
import com.lchclearnet.trade.TradeService;
import com.lchclearnet.trade.TradeServiceFactory;
import com.lchclearnet.trade.table.TableTradeAdapter;
import com.lchclearnet.utils.Tuple;

public class TradeServiceDemo {

  public static void main(String[] args) {

    //Load the config service
    Tuple params = DemoHelper.parseArgs(args);

    AppConfig configService = params.item(0);
    //String date = params.item(1);

    //Initialise the BusinessCalendarServiceFactory
    TradeServiceFactory tradeServiceFactory = buildTradeService(configService);

    //Build a Trade Request loading all trades
    SmartRequest all_trades_request = new SmartRequest();
    //all_trades_request.put("env", env);
    //all_trades_request.put("date", date);
    TradeService response = DemoHelper.execute_request(tradeServiceFactory, all_trades_request,
        String.format("Load all trades with '%s'", args[1]));
    response =
        DemoHelper.execute_request(tradeServiceFactory, all_trades_request, "trades in cache");

    System.out.println(response.getCurrencies());

    System.out.println(
        "*****************************************************************************************");
    System.out.println("***********************   Trades   "
        + "******************************************************");
    System.out.println(
        "*****************************************************************************************");
    Table table = response.getTrades();
    System.out.println(table.print(20));
    System.out.println();
    System.out.println();
    System.out.println(
        "*****************************************************************************************");
    System.out.println("***********************   Cash Flows   "
        + "**************************************************");
    System.out.println(
        "*****************************************************************************************");
    Table cashFlows = response.getCashFlows(null);
    System.out.println(cashFlows.print(20));
    System.out.println();
    System.out.println();
    System.out.println(
        "*****************************************************************************************");
    System.out.println("***********************   Options   "
        + "*****************************************************");
    System.out.println(
        "*****************************************************************************************");
    Table options = response.getOptions(null);
    System.out.println(options.print(20));
    System.out.println();
    System.out.println();
  }

  private static TradeServiceFactory buildTradeService(AppConfig config) {

    TradeServiceFactory tradeServiceFactory =
        new TradeServiceFactory(new TableTradeAdapter(config));
    tradeServiceFactory.startup();
    return tradeServiceFactory;
  }

}
