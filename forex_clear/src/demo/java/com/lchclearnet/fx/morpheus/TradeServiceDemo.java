package com.lchclearnet.fx.morpheus;

import com.lchclearnet.fx.AppConfig;
import com.lchclearnet.fx.DemoHelper;
import com.lchclearnet.request.SmartRequest;
import com.lchclearnet.trade.TradeService;
import com.lchclearnet.trade.TradeServiceFactory;
import com.lchclearnet.trade.morpheus.DataFrameTradeAdapter;
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
        TradeService response = DemoHelper.execute_request(tradeServiceFactory, all_trades_request, String.format("Load all trades with '%s'", args[1]));
        response = DemoHelper.execute_request(tradeServiceFactory, all_trades_request, "trades in cache");

        System.out.println(response.getCurrencies());
    }

    private static TradeServiceFactory buildTradeService(AppConfig config) {
        TradeServiceFactory tradeServiceFactory = new TradeServiceFactory(new DataFrameTradeAdapter(config));
        tradeServiceFactory.startup();
        return tradeServiceFactory;
    }

}
