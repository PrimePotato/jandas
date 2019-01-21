package com.lchclearnet.fx.morpheus;

import com.lchclearnet.fx.AppConfig;
import com.lchclearnet.fx.DemoHelper;
import com.lchclearnet.utils.Tuple;

public class MarketContainerDemo {

    public static void main(String[] args) {

        //Load the config service
        Tuple params = DemoHelper.parseArgs(args);

        AppConfig conf = params.item(0);
        String date = params.item(1);

        //Initialise the BusinessCalendarServiceFactory
        /*DataFrameMarketAdapter marketAccessor = buildMarketAccessor(configService);

        requestMarketData(env, date, marketAccessor);

        requestForward(env, date, marketAccessor);

        requestZeroCoupon(env, date, marketAccessor);

        requestVol(env, date, marketAccessor);

        requestAll(env, date, marketAccessor);
        */

    }

    /*
    public static void requestMarketData(String env, String date, DataFrameMarketAdapter marketAccessor) {
        System.out.println("==============================================================================================================");
        System.out.println("===================================  SPOT RATES  =============================================================");
        System.out.println("==============================================================================================================");
        //Build a Trade Request loading all trades
        SmartRequest spot_request = new SmartRequest();
        spot_request.put("env", env);
        spot_request.put("date", date);
        spot_request.put("types", SourceType.FX_SPOT.toString());

        DataFrameFxMarket response = DemoHelper.execute_request(marketAccessor, spot_request, String.format("Load all spot fx rates from %s->%s", env, date));
        List<Pair<String, Double>> mids = DataFrameHelper.zip(response.getAttribute(SourceType.FX_SPOT, "CurrencyPair"), response.getAttribute(SourceType.FX_SPOT, "Mid"));
        System.out.println(mids.stream().map(pair -> String.format("%s=%s", pair.getFirst(), String.valueOf(pair.getSecond()))).collect(Collectors.joining(", ")));
    }

    public static void requestForward(String env, String date, DataFrameMarketAdapter marketAccessor) {
        System.out.println("==============================================================================================================");
        System.out.println("===================================  FORWARD POINTS CURVE  ===================================================");
        System.out.println("==============================================================================================================");
        SmartRequest forward_request = new SmartRequest();
        forward_request.put("env", env);
        forward_request.put("date", date);
        forward_request.put("types", SourceType.FX_FORWARD.toString());
        DataFrameFxMarket response = DemoHelper.execute_request(marketAccessor, forward_request, String.format("Load all forward point from %s->%s", env, date));
        final StringBuilder zero_b = new StringBuilder();
        response.forEach(SourceType.FX_FORWARD, fwd -> {
            zero_b.append(String.format("%s[%s]: %s\n", fwd.getValue("Currency"), fwd.getValue("TenorPoint"), fwd.getValue("Mid")));
        });
        System.out.println(zero_b.toString());
    }

    public static void requestZeroCoupon(String env, String date, DataFrameMarketAdapter marketAccessor) {
        System.out.println("==============================================================================================================");
        System.out.println("===================================  ZERO COUPON CURVE  ======================================================");
        System.out.println("==============================================================================================================");
        SmartRequest yield_curve_request = new SmartRequest();
        yield_curve_request.put("env", env);
        yield_curve_request.put("date", date);
        yield_curve_request.put("types", SourceType.ZERO_COUPON.toString());
        DataFrameFxMarket response = DemoHelper.execute_request(marketAccessor, yield_curve_request, String.format("Load all forward point from %s->%s", env, date));
        final StringBuilder zero_b = new StringBuilder();
        response.forEach(SourceType.ZERO_COUPON, fwd -> {
            zero_b.append(String.format("%s[%s]: %s\n", fwd.getValue("Currency"), fwd.getValue("FloatingTenorPoint"), fwd.getValue("ZeroCouponRate")));
        });
        System.out.println(zero_b.toString());
    }

    public static void requestVol(String env, String date, DataFrameMarketAdapter marketAccessor) {
        System.out.println("==============================================================================================================");
        System.out.println("===================================  VOLATILITY SURFACE  ============================================================");
        System.out.println("==============================================================================================================");
        SmartRequest yield_curve_request = new SmartRequest();
        yield_curve_request.put("env", env);
        yield_curve_request.put("date", date);
        yield_curve_request.put("types", SourceType.VOLATILITY.toString());
        DataFrameFxMarket response = DemoHelper.execute_request(marketAccessor, yield_curve_request, String.format("Load all forward point from %s->%s", env, date));
        final StringBuilder zero_b = new StringBuilder();
        response.forEach(SourceType.VOLATILITY, fwd -> {
            zero_b.append(String.format("%s[%s, %s]: %s\n", fwd.getValue("CurrencyPair"), fwd.getValue("TenorPoint"), fwd.getValue("VolNode"), fwd.getValue("Volatility")));
        });
        System.out.println(zero_b.toString());
    }

    public static void requestAll(String env, String date, DataFrameMarketAdapter marketAccessor) {
        System.out.println("==============================================================================================================");
        System.out.println("============================== SPOT, FORWARD, ZERO ===========================================================");
        System.out.println("==============================================================================================================");
        SmartRequest all_request = new SmartRequest();
        all_request.put("env", env);
        all_request.put("date", date);
        all_request.put("types", String.format("%s, %s, %s",
                SourceType.FX_SPOT.toString(),
                SourceType.FX_FORWARD.toString(),
                SourceType.ZERO_COUPON.toString()));

        DataFrameFxMarket response = DemoHelper.execute_request(marketAccessor, all_request, String.format("Load all forward point from %s->%s", env, date));
        final StringBuilder all_b = new StringBuilder();

        response.forEach(SourceType.FX_SPOT, fwd -> {
            all_b.append(String.format("%s: %s, ", fwd.getValue("CurrencyPair"), fwd.getValue("Mid")));
        });
        all_b.append("\n-----------------------------------------------------------------------------------------------------------------------------------");
        response.forEach(SourceType.FX_FORWARD, fwd -> {
            all_b.append(String.format("\n%s[%s]: %s", fwd.getValue("Currency"), fwd.getValue("TenorPoint"), fwd.getValue("Mid")));
        });
        all_b.append("\n-----------------------------------------------------------------------------------------------------------------------------------");
        response.forEach(SourceType.ZERO_COUPON, fwd -> {
            all_b.append(String.format("\n%s[%s]: %s", fwd.getValue("Currency"), fwd.getValue("FloatingTenorPoint"), fwd.getValue("ZeroCouponRate")));
        });
        System.out.println(all_b.toString());
    }

    private static DataFrameMarketAdapter buildMarketAccessor(ConfigurationProvider config) {
        DataFrameMarketAdapter marketAccessor = new DataFrameMarketAdapter(config);
        marketAccessor.startup();
        return marketAccessor;
    }
    */

}
