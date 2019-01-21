package com.lchclearnet.fx.table;

import com.lchclearnet.fx.AppConfig;
import com.lchclearnet.fx.DemoHelper;
import com.lchclearnet.market.DataType;
import com.lchclearnet.market.FxMarket;
import com.lchclearnet.market.fx.FxSpots;
import com.lchclearnet.market.MarketServiceProviderFactory;
import com.lchclearnet.market.table.TableMarketAdapter;
import com.lchclearnet.request.SmartRequest;
import com.lchclearnet.utils.Currency;
import com.lchclearnet.utils.CurrencyPair;
import com.lchclearnet.utils.Tuple;

import java.time.LocalDate;
import java.util.Set;

public class MarketServiceDemo {

    public static void main(String[] args) {

        //Load the AppConfigig service
        Tuple params = DemoHelper.parseArgs(args);

        AppConfig AppConfigigService = params.item(0);

        //Initialise the BusinessCalendarServiceFactory
        MarketServiceProviderFactory marketServiceProviderFactory = buildMarketService(AppConfigigService);

        requestMarketData(marketServiceProviderFactory);

    }

    public static void requestMarketData(MarketServiceProviderFactory marketServiceProviderFactory) {
        System.out.println("==============================================================================================================");
        System.out.println("===================================  Get FX SPOT Rates  ======================================================");
        System.out.println("==============================================================================================================");
        //Build a Trade Request loading all trades
        SmartRequest mkt_request = new SmartRequest();
        mkt_request.put("types", String.format("%s, %s, %s", DataType.FX_SPOT, DataType.IR_YIELD_CURVE, DataType.FX_FORWARD_OUTRIGHT));

        FxMarket mktServiceProvider = DemoHelper.execute_request(marketServiceProviderFactory, mkt_request, "Load all spot fx rates");
        FxSpots spotService = mktServiceProvider.getFxSpots();
        Set<CurrencyPair> pairs = spotService.getCurrencyPairs();
        StringBuilder builder = new StringBuilder(String.format("Spot Rate[Market Date=%s]: ", spotService.getMarketDate()));
        for (CurrencyPair pair : pairs) {
            builder.append(String.format("%s = %f ; ", pair, spotService.getSpot(pair)));
        }
        System.out.println(builder.toString());

        LocalDate[] yc_pillars = new LocalDate[]{
                LocalDate.of(2018, 11, 12), LocalDate.of(2018, 9, 19),
                LocalDate.of(2018, 10, 12), LocalDate.of(2018, 11, 12),
                LocalDate.of(2018, 12, 12), LocalDate.of(2019, 1, 14),
                LocalDate.of(2019, 2, 12), LocalDate.of(2019, 3, 12),
                LocalDate.of(2019, 6, 12), LocalDate.of(2019, 9, 12),
                LocalDate.of(2020, 3, 12), LocalDate.of(2020, 9, 14),
        };

        LocalDate[] fx_pillars = new LocalDate[]{
                LocalDate.of(2018, 9, 13), LocalDate.of(2018, 9, 19),
                LocalDate.of(2018, 10, 12), LocalDate.of(2018, 11, 13)
        };

        Currency aud = Currency.parse("AUD");

    }

    private static MarketServiceProviderFactory buildMarketService(AppConfig appConfig) {
        MarketServiceProviderFactory marketServiceProviderFactory = new MarketServiceProviderFactory(new TableMarketAdapter(appConfig));
        marketServiceProviderFactory.startup();
        return marketServiceProviderFactory;
    }

}
