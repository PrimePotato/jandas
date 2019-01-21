package com.lchclearnet.fx.table;

import com.google.common.base.Strings;
import com.lchclearnet.calendar.BusinessCalendarService;
import com.lchclearnet.calendar.BusinessCalendarServiceFactory;
import com.lchclearnet.calendar.table.TableBusinessCalendarAdapter;
import com.lchclearnet.fx.AppConfig;
import com.lchclearnet.fx.DemoHelper;
import com.lchclearnet.market.DataType;
import com.lchclearnet.market.FxMarket;
import com.lchclearnet.market.MarketServiceProviderFactory;
import com.lchclearnet.market.fx.FxSpots;
import com.lchclearnet.market.table.TableMarketAdapter;
import com.lchclearnet.pricer.SimpleCashFlowPricer;
import com.lchclearnet.pricer.TableCashFlowPricer;
import com.lchclearnet.report.table.CashFlowReport;
import com.lchclearnet.request.SmartRequest;
import com.lchclearnet.table.Table;
import com.lchclearnet.trade.CashFlowInstrument;
import com.lchclearnet.trade.CashFlowResults;
import com.lchclearnet.trade.TradeService;
import com.lchclearnet.trade.TradeServiceFactory;
import com.lchclearnet.trade.table.TableTradeAdapter;
import com.lchclearnet.utils.CurrencyPair;
import com.lchclearnet.utils.Tuple;


import com.lchclearnet.table.aggregate.AggregateFunctions;
import org.apache.commons.io.IOExceptionWithCause;


import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.lchclearnet.trade.TradeService.*;

public class OptionCashFlowDemo {

    public static void main(String[] args) {
      try {
        //Load the config service
        Tuple params = DemoHelper.parseArgs(args);

        AppConfig configService = params.item(0);
        //String date = params.item(1);

        //Initialise the service factories
        BusinessCalendarServiceFactory calendarServiceFactory = buildBusinessCalendarService(configService);
        TradeServiceFactory tradeServiceFactory = buildTradeServiceFactory(configService);
        MarketServiceProviderFactory marketServiceProviderFactory = buildMarketServiceProviderFactory(configService);

        SmartRequest calendar_service_request = new SmartRequest();
        BusinessCalendarService calendarService = DemoHelper.execute_request(calendarServiceFactory, calendar_service_request, String.format("Load calendar from '%s'", args[1]));

        //Build a Trade Request loading all trades
        SmartRequest trade_service_request = new SmartRequest();
        TradeService tradeService = DemoHelper.execute_request(tradeServiceFactory, trade_service_request, String.format("Load trades from '%s'", args[1]));

        SmartRequest mkt_request = new SmartRequest();
        mkt_request.put("types", String.format("%s, %s, %s", DataType.FX_SPOT, DataType.IR_YIELD_CURVE, DataType.FX_FORWARD_OUTRIGHT));
        FxMarket marketService = DemoHelper.execute_request(marketServiceProviderFactory, mkt_request, String.format("Load market jandas from '%s'", args[1]));

        Table cashFlows = tradeService.getCashFlows(marketService);
        Table options = tradeService.getOptions(marketService);
        //cashFlows = cashFlows.rows().select(row -> row.getDouble("TradeRef") == 4648048);

        //Decorate cash flows with the spot date
        FxSpots spotService = marketService.getFxSpots();
        cashFlows = cashFlows.addColumn(SPOT_DATE, LocalDate.class, row -> {
            CurrencyPair ccyPair = row.getObject(CCY_PAIR);
            return spotService.getSpotDate(ccyPair);
        });



//        final Table cashFlowInstruments = cashFlows.summarize(TRADE_REF, AggregateFunctions.count)
//                                                   .by(PAYEMENT_DATE, NATIVE_CCY, PAYEMENT_CCY, SPOT_DATE);
//
//        print(cashFlows.print(20), "Cash Flows & Instruments");
//        print(cashFlowInstruments.sortOn("Count [TradeRef]").print());
//        print(cashFlowInstruments.column("Count [TradeRef]").summary().printAll());

          final Set<CashFlowInstrument> cashFlowInstruments = new HashSet<>();
          cashFlows.forEach(row -> cashFlowInstruments.add(new CashFlowInstrument(row.getDate(PAYEMENT_DATE), row.getObject(NATIVE_CCY), row.getObject(PAYEMENT_CCY), row.getDate(SPOT_DATE))));


          final Table optionInstruments = options.summarize(TRADE_REF, AggregateFunctions.count)
                                               .by("CallPut", "LongStrike", "ExpiryDate", "SettlementDate");

        print(options.print(), "Options & Instruments");
        print(optionInstruments.sortOn("Count [TradeRef]").printAll());
        print(optionInstruments.column("Count [TradeRef]").summary().printAll());


        //TODO Hack to getObject the value date
        String[] env = ((String) configService.get(AppConfig.ENV)).split("\\\\");
        LocalDate valueDate = LocalDate.parse(env[env.length - 1], DateTimeFormatter.ofPattern("yyyyMMdd"));

        CashFlowResults results = DemoHelper.monitor((Function<Void, CashFlowResults>) v -> new SimpleCashFlowPricer().price(valueDate, cashFlowInstruments, marketService, calendarService), null, "Price Cash Flow Instruments");


        final CashFlowReport cashFlowReport = new CashFlowReport().setCashFlows(cashFlows).setCashFlowInstruments(results)
                                                                  .setTradeService(tradeService)
                                                                  .setMarketService(marketService);



        DemoHelper.monitor((Consumer) v -> cashFlowReport.build(), "", "Build Cash Flow Report");
        DemoHelper.monitor((Consumer) v -> cashFlowReport.writeCsv(Paths.get(configService.get("workingDir"), "cashflows.csv").toFile()), "", "Write Cash Flow Report");

          final Table report = cashFlowReport.getReport();
          print(report.print());
          DemoHelper.monitor((Consumer) v -> {
            try{
            report.summarize("PvUSD", AggregateFunctions.sum).by(MEMBER).write().csv(Paths.get(configService.get("workingDir"), "trades.csv").toFile());
            } catch (IOException ioe){
                  ioe.printStackTrace();
              }
        }, "", "Write Cash Flow Report");
        DemoHelper.monitor((Consumer) v -> {
            try{
            report.summarize("PvUSD", AggregateFunctions.sum).by(MEMBER, CCY_PAIR).write().csv(Paths.get(configService.get("workingDir"), "trades.csv").toFile());
            } catch (IOException ioe){
                  ioe.printStackTrace();
              }
        }, "", "Write Cash Flow Report");
        DemoHelper.monitor((Consumer) v -> {
            try {
                report.summarize("PvUSD", AggregateFunctions.sum).by(MEMBER, CCY_PAIR, TRADE_REF).write().csv(Paths.get(configService.get("workingDir"), "trades.csv").toFile());
            } catch (IOException ioe){
                ioe.printStackTrace();
            }
            }, "", "Write Cash Flow Report");

        //print(results.print(), "Pricing Results");
        print(cashFlowReport.getReport().print(), "Cash Flow Report");

      } catch (Exception e) {
        e.printStackTrace();
       }
    }

    private static BusinessCalendarServiceFactory buildBusinessCalendarService(AppConfig config) {
        BusinessCalendarServiceFactory service = new BusinessCalendarServiceFactory(new TableBusinessCalendarAdapter(config));
        service.startup();
        return service;
    }

    private static TradeServiceFactory buildTradeServiceFactory(AppConfig config) {
        TradeServiceFactory serviceFactory = new TradeServiceFactory(new TableTradeAdapter(config));
        serviceFactory.startup();
        return serviceFactory;
    }

    private static MarketServiceProviderFactory buildMarketServiceProviderFactory(AppConfig config) {
        MarketServiceProviderFactory serviceFactory = new MarketServiceProviderFactory(new TableMarketAdapter(config));
        serviceFactory.startup();
        return serviceFactory;
    }


    private static void print(String table, String... args){
        if(args.length > 0 && !Strings.isNullOrEmpty(args[0])){
            System.out.println("**********************************************************************");
            System.out.println(String.format("         %s", args[0]));
            System.out.println("**********************************************************************");
        }
        System.out.println(table);
        System.out.println();
        System.out.println();
    }

}
