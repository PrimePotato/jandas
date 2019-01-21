package com.lchclearnet.fx.table;

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
import com.lchclearnet.request.SmartRequest;
import com.lchclearnet.table.Table;
import com.lchclearnet.trade.CashFlowInstrument;
import com.lchclearnet.trade.CashFlowResults;
import com.lchclearnet.trade.TradeService;
import com.lchclearnet.trade.TradeServiceFactory;
import com.lchclearnet.trade.table.TableTradeAdapter;
import com.lchclearnet.utils.CurrencyPair;
import com.lchclearnet.utils.Tuple;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import static com.lchclearnet.trade.TradeService.*;

public class CashFlowDemo {

    public static void main(String[] args) {

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
        //cashFlows = cashFlows.rows().select(row -> row.getDouble("TradeRef") == 4648048);

        //Decorate cash flows with the spot date
        FxSpots spotService = marketService.getFxSpots();
        cashFlows = cashFlows.addColumn(SPOT_DATE, LocalDate.class, row -> {
            CurrencyPair ccyPair = row.getObject(CCY_PAIR);
            return spotService.getSpotDate(ccyPair);
        });
        //System.out.println(cashFlows.print(20));
        final Set<CashFlowInstrument> cashFlowInstruments = new HashSet<>();
        cashFlows.forEach(row -> cashFlowInstruments.add(new CashFlowInstrument(row.getDate(PAYEMENT_DATE), row.getObject(NATIVE_CCY), row.getObject(PAYEMENT_CCY), row.getDate(SPOT_DATE))));

        //TODO Hack to getObject the value date
        String[] env = ((String) configService.get(AppConfig.ENV)).split("\\\\");
        LocalDate valueDate = LocalDate.parse(env[env.length - 1], DateTimeFormatter.ofPattern("yyyyMMdd"));

        CashFlowResults results = new SimpleCashFlowPricer().price(valueDate, cashFlowInstruments, marketService, calendarService);

        final CashFlowReportWithIteration cashFlowReportWithIteration = new CashFlowReportWithIteration().setResults(results)
                .setCashFlows(cashFlows)
                .setTradeService(tradeService)
                .setMarketService(marketService);

        DemoHelper.monitor((Consumer) v -> cashFlowReportWithIteration.build(), "", "Build Cash Flow Report Non Vectorized");

        DemoHelper.monitor((Consumer) v -> cashFlowReportWithIteration.writeCsv(Paths.get(configService.get("workingDir"), "cashflows.csv").toFile()), "", "Write Cash Flow Report Non Vectorized");
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

}
