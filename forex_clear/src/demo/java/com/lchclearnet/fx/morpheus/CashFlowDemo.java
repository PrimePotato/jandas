package com.lchclearnet.fx.morpheus;

import com.lchclearnet.calendar.BusinessCalendarService;
import com.lchclearnet.calendar.BusinessCalendarServiceFactory;
import com.lchclearnet.calendar.morpheus.DataFrameBusinessCalendarAdapter;
import com.lchclearnet.fx.AppConfig;
import com.lchclearnet.fx.DemoHelper;
import com.lchclearnet.market.DataType;
import com.lchclearnet.market.fx.FxSpots;
import com.lchclearnet.market.FxMarket;
import com.lchclearnet.market.MarketServiceProviderFactory;
import com.lchclearnet.market.morpheus.DataFrameMarketAdapter;
import com.lchclearnet.pricer.SimpleCashFlowPricer;
import com.lchclearnet.report.morpheus.AbstractRowGroup;
import com.lchclearnet.report.morpheus.CashFlowPvReport;
import com.lchclearnet.report.morpheus.CashFlowReport;
import com.lchclearnet.report.morpheus.RowGroup;
import com.lchclearnet.request.SmartRequest;
import com.lchclearnet.trade.CashFlowInstrument;
import com.lchclearnet.trade.CashFlowResults;
import com.lchclearnet.trade.TradeService;
import com.lchclearnet.trade.TradeServiceFactory;
import com.lchclearnet.trade.morpheus.DataFrameTradeAdapter;
import com.lchclearnet.utils.CurrencyPair;
import com.zavtech.morpheus.frame.DataFrame;
import com.zavtech.morpheus.frame.DataFrameRow;
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

        DataFrame<String, String> cashFlows = tradeService.getCashFlows(marketService);
        //cashFlows = cashFlows.rows().select(row -> row.getDouble("TradeRef") == 4648048);

        //Decorate cash flows with the spot date
        FxSpots spotService = marketService.getFxSpots();
        cashFlows = cashFlows.cols().add(SPOT_DATE, LocalDate.class, v -> {
            CurrencyPair ccyPair = v.row().getValue(CCY_PAIR);
            return spotService.getSpotDate(ccyPair);

        });

        final Set<CashFlowInstrument> cashFlowInstruments = new HashSet<>();
        cashFlows.rows().forEach(row -> cashFlowInstruments.add(new CashFlowInstrument(row.getValue(PAYEMENT_DATE), row.getValue(NATIVE_CCY), row.getValue(PAYEMENT_CCY), row.getValue(SPOT_DATE))));

        //TODO Hack to getObject the value date
        String[] env = ((String) configService.get(AppConfig.ENV)).split("\\\\");
        LocalDate valueDate = LocalDate.parse(env[env.length - 1], DateTimeFormatter.ofPattern("yyyyMMdd"));

        CashFlowResults results = new SimpleCashFlowPricer().price(valueDate, cashFlowInstruments, marketService, calendarService);

        final CashFlowReport cashFlowReport = new CashFlowReport().setResults(results)
                .setCashFlows(cashFlows)
                .setTradeService(tradeService)
                .setMarketService(marketService);

        DemoHelper.monitor((Consumer) v -> cashFlowReport.build(), "", "Build Cash Flow report");

        DemoHelper.monitor((Consumer) v -> cashFlowReport.writeCsv(Paths.get(configService.get("workingDir"), "cashflows.csv").toFile()), "", "Write Cash Flow report");


        CashFlowPvReport csPvReport = new CashFlowPvReport()
                .setCashFlowReport(cashFlowReport.getReport())
                .setTradeService(tradeService)
                .setMarketService(marketService);

        RowGroup<String, String> groupByMember = new AbstractRowGroup<String, String>(new String[]{MEMBER}) {
            @Override
            public Tuple index(DataFrameRow<String, String> row) {
                return Tuple.of(new Object[]{row.getValue(MEMBER)});
            }
        };
        DemoHelper.monitor((Consumer) v -> csPvReport.setGroupBy(groupByMember).build(), "", "Build Member PV report with depth 0");
        DemoHelper.monitor((Consumer) v -> csPvReport.writeCsv(Paths.get(configService.get("workingDir"), "member_pv.csv").toFile()), "", "Write Member PV report with depth 0");

        RowGroup<String, String> groupByCcyPair = new AbstractRowGroup<String, String>(new String[]{MEMBER}) {
            @Override
            public Tuple index(DataFrameRow<String, String> row) {
                return Tuple.of(row.getValue(MEMBER), row.getValue(CCY_PAIR));
            }
        };
        DemoHelper.monitor((Consumer) v -> csPvReport.setGroupBy(groupByCcyPair).build(), "", "Build Currency Pair PV report with depth 1");
        DemoHelper.monitor((Consumer) v -> csPvReport.writeCsv(Paths.get(configService.get("workingDir"), "ccypair_pv.csv").toFile()), "", "Write Currency Pair PV report");

        //DemoHelper.monitor((Consumer) v -> csPvReport.setGroupBy(2, MEMBER, "CcyPair", TRADE_REF).build(), "", "Build Trade PV report with depth 2");
        RowGroup<String, String> groupByTrade = new AbstractRowGroup<String, String>(new String[]{MEMBER}) {
            @Override
            public Tuple index(DataFrameRow<String, String> row) {
                return Tuple.of(row.getValue(MEMBER), row.getValue(CCY_PAIR), row.getLong(TRADE_REF));
            }
        };
        DemoHelper.monitor((Consumer) v -> csPvReport.setGroupBy(groupByTrade).build(), "", "Build Trade PV report with depth 2");
        DemoHelper.monitor((Consumer) v -> csPvReport.writeCsv(Paths.get(configService.get("workingDir"), "trade_pv.csv").toFile()), "", "Write Trade PV report");


//        File csvFile = Paths.getObject(configService.getObject("workingDir"), "cashflows.csv").toFile();
//        results.print(csvFile);
    }

    private static BusinessCalendarServiceFactory buildBusinessCalendarService(AppConfig config) {
        BusinessCalendarServiceFactory service = new BusinessCalendarServiceFactory(new DataFrameBusinessCalendarAdapter(config));
        service.startup();
        return service;
    }

    private static TradeServiceFactory buildTradeServiceFactory(AppConfig config) {
        TradeServiceFactory serviceFactory = new TradeServiceFactory(new DataFrameTradeAdapter(config));
        serviceFactory.startup();
        return serviceFactory;
    }

    private static MarketServiceProviderFactory buildMarketServiceProviderFactory(AppConfig config) {
        MarketServiceProviderFactory serviceFactory = new MarketServiceProviderFactory(new DataFrameMarketAdapter(config));
        serviceFactory.startup();
        return serviceFactory;
    }

}
