package com.lchclearnet.fx.jandas;

import com.lchclearnet.fx.AppConfig;
import com.lchclearnet.fx.DemoHelper;
import com.lchclearnet.market.DataType;
import com.lchclearnet.market.FxMarket;
import com.lchclearnet.market.MarketServiceProviderFactory;
import com.lchclearnet.market.fx.FxSpots;

import com.lchclearnet.market.jandas.DataFrameMarketAdapter;
import com.lchclearnet.request.SmartRequest;
import com.lchclearnet.utils.Currency;
import com.lchclearnet.utils.CurrencyPair;
import com.lchclearnet.utils.PrintUtils;
import com.lchclearnet.utils.Tuple;
import java.time.LocalDate;
import java.util.Set;

public class MarketServiceDemo {

  public static void main(String[] args) {

    //Load the AppConfigig service
    Tuple params = DemoHelper.parseArgs(args);

    AppConfig AppConfigigService = params.item(0);

    //Initialise the BusinessCalendarServiceFactory
    MarketServiceProviderFactory marketServiceProviderFactory =
        buildMarketService(AppConfigigService);

    requestMarketData(marketServiceProviderFactory);

  }

  public static void requestMarketData(MarketServiceProviderFactory marketServiceProviderFactory) {

    PrintUtils.separator("SpotRate");
    SmartRequest mkt_request = new SmartRequest();
    mkt_request.put("types", String.format("%s, %s, %s", DataType.FX_SPOT, DataType.IR_YIELD_CURVE,
        DataType.FX_FORWARD_OUTRIGHT));

    FxMarket mktServiceProvider =
        DemoHelper.execute_request(marketServiceProviderFactory, mkt_request,
            "Load all spot fx rates");

    FxSpots spotService = mktServiceProvider.getFxSpots();
    Set<CurrencyPair> pairs = spotService.getCurrencyPairs();

    StringBuilder builder = new StringBuilder(
        String.format("Spot Rate[Market Date=%s]: ", spotService.getMarketDate()));
    for (CurrencyPair pair : pairs) {
      builder.append(String.format("%s = %f ; ", pair, spotService.getSpot(pair)));
    }
    System.out.println(builder.toString());

  }

  private static MarketServiceProviderFactory buildMarketService(AppConfig appConfig) {

    MarketServiceProviderFactory marketServiceProviderFactory =
        new MarketServiceProviderFactory(new DataFrameMarketAdapter(appConfig));
    marketServiceProviderFactory.startup();
    return marketServiceProviderFactory;
  }

}
