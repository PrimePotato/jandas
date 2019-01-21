package com.lchclearnet.market.ir;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.lchclearnet.calendar.DayCountFraction;
import com.lchclearnet.fx.AppConfig;
import com.lchclearnet.utils.CurrencyPair;
import com.lchclearnet.utils.InterpolationMethod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.Map;

public class YieldCurveConfig {

    public static Logger logger = LogManager.getLogger();

    private static YieldCurveConfig INSTANCE;
    private String dayCountFraction;
    private String interpolationMethod;
    private Map<String, Map<String, String>> yieldCurves;

    public static YieldCurveConfig instance() {
        return instance(System.getProperty(AppConfig.YIELD_CURVE_CONFIG, "/yield_curve.yaml"));
    }

    public static YieldCurveConfig instance(String configFile) {
        if (INSTANCE == null) {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            try {
                URL ccyUrl = CurrencyPair.class.getResource(configFile);
                INSTANCE = mapper.readValue(ccyUrl, YieldCurveConfig.class);
            } catch (Exception ex) {
                String msg = String.format("Fail to load Yield Curve Config file yield_curve.yaml from '%s' ...", configFile);
                logger.error("%s : %s", msg, ex.getMessage());
                throw new RuntimeException(msg, ex);
            }
        }

        return INSTANCE;
    }

    public static void main(String[] args) {
        DayCountFraction dcf = YieldCurveConfig.instance().getDayCountFraction("XXX XONA");
        InterpolationMethod interpolationMethod = YieldCurveConfig.instance().getInterpolationMethod("XXX XONA");
        System.out.println(String.format("The '%s' curve day count fraction and interpolation method are '%s', '%s'.", "XXX XONA", dcf, interpolationMethod));

        for (String curveId : YieldCurveConfig.instance().yieldCurves.keySet()) {
            dcf = YieldCurveConfig.instance().getDayCountFraction(curveId);
            interpolationMethod = YieldCurveConfig.instance().getInterpolationMethod(curveId);
            System.out.println(String.format("The '%s' curve day count fraction and interpolation method are '%s', '%s'.", curveId, dcf, interpolationMethod));
        }
    }

    public void setDayCountFraction(String dayCountFraction) {
        this.dayCountFraction = dayCountFraction;
    }

    public void setInterpolationMethod(String interpolationMethod) {
        this.interpolationMethod = interpolationMethod;
    }

    public void setYieldCurves(Map<String, Map<String, String>> yieldCurves) {
        this.yieldCurves = yieldCurves;
    }

    public DayCountFraction getDayCountFraction(String yieldCurveId) {
        String dcf = yieldCurves.containsKey(yieldCurveId) ? yieldCurves.get(yieldCurveId).getOrDefault("getDayCountFraction", this.dayCountFraction) : this.dayCountFraction;
        return DayCountFraction.getEnum(dcf);
    }

    public InterpolationMethod getInterpolationMethod(String yieldCurveId) {
        String interpolationMethod = yieldCurves.containsKey(yieldCurveId) ? yieldCurves.get(yieldCurveId).getOrDefault("getInterpolationMethod", this.interpolationMethod) : this.interpolationMethod;
        return InterpolationMethod.getEnum(interpolationMethod);
    }
}
