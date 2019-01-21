package com.lchclearnet.fx;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.lchclearnet.utils.Config;
import com.lchclearnet.utils.FieldsHelper;
import com.lchclearnet.utils.FileHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.Map;

public class AppConfig implements Config {

    public static final String APPLICATION_CONFIG = "conf";

    public static final String CURRENCY_PAIR_CONFIG = "currencyPairConfig";
    public static final String CURRENCY_CONFIG = "currencyConfig";
    public static final String YIELD_CURVE_CONFIG = "yieldCurveConfig";
    public static final String WORKING_DIR = "workingDir";

    public static final String ENV = "env";
    public static final String DIR = "dir";

    public static final String CALENDAR = "calendar";
    public static final String TRADE = "trade";

    public static final String FXNDF = "fxndf";

    public static final String MARKET = "market";
    public static final String SCENARIO = "scenario";

    public static final String FX_SPOT = "fx_spt";
    public static final String FX_FORWARD = "fx_fwd";
    public static final String FX_VOLATILITY = "fx_vol";

    public static final String IR_YIELD_CURVE = "ir_yld";

    public static final String FILE_PATTERN = "file_pattern";
    public static final String TABLE_NAME = "table_name";
    public static final String HEADER = "header";
    public static final String CHARSET = "charset";
    public static final String PARALLEL = "parallel";
    public static final String COLUMNS = "columns";
    public static final String PARSER_SETTINGS = "parser_settings";

    public static final String ROW_INDEX = "row_index";

    private static Logger logger = LogManager.getLogger();

    private static AppConfig INSTANCE;

    private String currencyPairConfig;
    private String currencyConfig;
    private String yieldCurveConfig;
    private String workingDir;

    private String env;

    private Map<String, ?> calendar;
    private Map<String, ?> trade;
    private Map<String, ?> market;
    private Map<String, ?> scenario;
    private Map<String, ?> fxndf;


    public static AppConfig instance() {
        return instance(System.getProperty(APPLICATION_CONFIG, "./forex_clear.yaml"));
    }

    public static AppConfig instance(String configFile) {
        if (INSTANCE == null) {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            try {
                URL appUrl = FileHelper.getResource(configFile);
                INSTANCE = mapper.readValue(appUrl, AppConfig.class);
            } catch (Exception ex) {
                String msg = "Fail to load Application Configuration file ...";
                logger.error("%s : %s".format(msg, ex.getMessage()));
                throw new RuntimeException(msg, ex);
            }
        }

        return INSTANCE;
    }

    public static void main(String[] args) {
        System.out.println(String.format("The calendar dir is %s.", (String) AppConfig.instance().get(CALENDAR, DIR)));
        System.out.println(String.format("The calendar file pattern is %s.", (String) AppConfig.instance().get(CALENDAR, FILE_PATTERN)));
        System.out.println(String.format("The trade directory is %s.", (String) AppConfig.instance().get(TRADE, DIR)));
        System.out.println(String.format("The trade file pattern is %s.", (String) AppConfig.instance().get(TRADE, FILE_PATTERN)));
        System.out.println(String.format("The trade TradeRef column is %s.", (String) AppConfig.instance().get(TRADE, COLUMNS, "TradeRef")));
        System.out.println(String.format("The trade TradeRef  '^\\\\w+Currency$' regex columns is %s.", (String) AppConfig.instance().get(TRADE, COLUMNS, "^\\w+Currency$")));
        System.out.println(String.format("The market fx spot file pattern is %s.", (String) AppConfig.instance().get(MARKET, FX_SPOT, FILE_PATTERN)));

    }


    //***********************************************************************************************************//
    //**************  Yaml Parser Specific Methods  *************************************************************//
    //***********************************************************************************************************//
    public void setCurrencyPairConfig(String currencyPairConfig) {
        this.currencyPairConfig = currencyPairConfig;
        //setup some system properties
        System.setProperty(CURRENCY_PAIR_CONFIG, currencyPairConfig);
    }

    public void setCurrencyConfig(String currencyConfig) {
        this.currencyConfig = currencyConfig;    //setup some system properties
        System.setProperty(CURRENCY_CONFIG, currencyConfig);
    }

    public void setYieldCurveConfig(String yieldCurveConfig) {
        this.yieldCurveConfig = yieldCurveConfig;
        System.setProperty(YIELD_CURVE_CONFIG, yieldCurveConfig);
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public void setWorkingDir(String workingDir) {
        this.workingDir = workingDir;
    }

    public void setCalendar(Map<String, ?> calendar) {
        this.calendar = calendar;
    }

    public void setFxndf(Map<String, ?> fxndf) {
        this.fxndf = fxndf;
    }

    public void setTrade(Map<String, ?> trade) {
        this.trade = trade;
    }

    public void setMarket(Map<String, ?> market) {
        this.market = market;
    }

    public void setScenario(Map<String, ?> scenario) {
        this.scenario = scenario;
    }

    //************************************************************************************************************//
    //********************** Config Interface ********************************************************************//
    //************************************************************************************************************//
    @Override
    public boolean has(String... keys) {
        if (keys == null || keys.length == 0) return false;

        Object value;
        if (DIR.equalsIgnoreCase(keys[keys.length - 1])) {
            value = FieldsHelper.getOrDefault(FieldsHelper.get(this, ENV), this, keys);
        } else {
            value = FieldsHelper.get(this, keys);
        }

        return value != null;
    }

    @Override
    public <T> T get(String... keys) {
        if (keys == null || keys.length == 0) return null;

        T value;
        if (DIR.equalsIgnoreCase(keys[keys.length - 1])) {
            value = FieldsHelper.getOrDefault(FieldsHelper.get(this, ENV), this, keys);
        } else {
            value = FieldsHelper.get(this, keys);
        }

        return value;
    }
}
