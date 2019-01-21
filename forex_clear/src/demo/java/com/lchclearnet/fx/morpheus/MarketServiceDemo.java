package com.lchclearnet.fx.morpheus;

import com.lchclearnet.fx.AppConfig;
import com.lchclearnet.fx.DemoHelper;
import com.lchclearnet.market.DataType;
import com.lchclearnet.market.fx.FxSpots;
import com.lchclearnet.market.FxMarket;
import com.lchclearnet.market.MarketServiceProviderFactory;
import com.lchclearnet.market.morpheus.DataFrameMarketAdapter;
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
                LocalDate.of(2018, 9, 12), LocalDate.of(2018, 9, 19),
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

//        Stream<Double> zrs = mktServiceProvider.getYieldCurve().getZeroRates(aud.getDiscountCurve(), yc_pillars);
//        System.out.println("\nZero Rates for 'AUD': ");
//        System.out.println(Streams.zip(Arrays.stream(yc_pillars), zrs, (p, r) -> String.format("%s= %s", p, r)).collect(Collectors.joining(", ")));
//
//        Stream<Double> dfs = mktServiceProvider.getYieldCurve().getDiscountFactors(aud.getDiscountCurve(), yc_pillars);
//        System.out.println("\nDiscount Factors for 'AUD': ");
//        System.out.println(Streams.zip(Arrays.stream(yc_pillars), dfs, (p, r) -> String.format("%s= %s", p, r)).collect(Collectors.joining(", ")));
//
//        CurrencyPair eurgbp = CurrencyPair.of("EUR", "GBP");
//
//        Double fxSpot = spotService.getSpot(eurgbp);
//
//        Stream<Double> fxf = ((FxForwardSourceService) mktServiceProvider.getFxForwardService(true)).getForwardRates(eurgbp, fxSpot, fx_pillars);
//        System.out.println("\nFX Forward Factors for 'EURGBP': ");
//        System.out.println(Streams.zip(Arrays.stream(fx_pillars), fxf, (p, r) -> String.format("%s= %s", p, r)).collect(Collectors.joining(", ")));
//
//        Stream<Double> fxfo = ((FxForwardOutrightService) mktServiceProvider.getFxForwardService(false)).getForwardRates(eurgbp, fx_pillars);
//        System.out.println("\nFX Forward Outrights for 'EURGBP': ");
//        System.out.println(Streams.zip(Arrays.stream(fx_pillars), fxfo, (p, r) -> String.format("%s= %s", p, r)).collect(Collectors.joining(", ")));

    }

    private static MarketServiceProviderFactory buildMarketService(AppConfig appConfig) {
        MarketServiceProviderFactory marketServiceProviderFactory = new MarketServiceProviderFactory(new DataFrameMarketAdapter(appConfig));
        marketServiceProviderFactory.startup();
        return marketServiceProviderFactory;
    }

}
