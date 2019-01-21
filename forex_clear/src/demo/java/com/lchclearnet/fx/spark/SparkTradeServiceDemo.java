package com.lchclearnet.fx.spark;

import com.lchclearnet.fx.AppConfig;
import com.lchclearnet.fx.DemoHelper;
import com.lchclearnet.request.SmartRequest;
import com.lchclearnet.trade.TradeService;
import com.lchclearnet.trade.TradeServiceFactory;
import com.lchclearnet.utils.Tuple;

public class SparkTradeServiceDemo {

    public static void main(String[] args) {

        //Load the config service
        Tuple params = DemoHelper.parseArgs(args);

        AppConfig config = params.item(0);

        String date = params.item(2);


        //Initialise the BusinessCalendarServiceFactory
        TradeServiceFactory tradeServiceFactory = buildTradeService(config);

        //Build a Trade Request loading all trades
        SmartRequest all_trades_request = new SmartRequest();
        all_trades_request.put("date", date);
        DemoHelper.execute_request(tradeServiceFactory, all_trades_request, all_trades_request.get("sql"));

        //Build a Trade Request selecting TEC and TEB members
        SmartRequest member_request = new SmartRequest();
        member_request.put("date", date);
        member_request.put("sql", "SELECT * FROM trades WHERE Member in ('TEC', 'TEB')");
        DemoHelper.execute_request(tradeServiceFactory, member_request, member_request.get("sql"));

        //Build a Trade Request selecting only NDFs trade type
        SmartRequest trade_type_request = new SmartRequest();
        trade_type_request.put("date", date);
        trade_type_request.put("sql", "SELECT * FROM trades WHERE TradeType = 'FxOption'");
        TradeService response = DemoHelper.execute_request(tradeServiceFactory, trade_type_request, trade_type_request.get("sql"));

        System.out.println(response.getCurrencies());
    }

    private static TradeServiceFactory buildTradeService(AppConfig config) {
        //TradeServiceFactory tradeServiceFactory = new TradeServiceFactory(new SparkTradeFileAccessor(config));
        TradeServiceFactory tradeServiceFactory = new TradeServiceFactory(null);
        tradeServiceFactory.startup();
        return tradeServiceFactory;
    }

}
