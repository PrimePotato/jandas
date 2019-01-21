package com.lchclearnet.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.lchclearnet.calendar.BusinessCalendar;
import com.lchclearnet.calendar.BusinessCalendarService;
import com.lchclearnet.fx.AppConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.Map;
import java.util.regex.Pattern;

public enum Currency {
    
    EUR("Euro Member Countries", 0, 1),
    GBP("United Kingdom Pound", 1, 2),
    AUD("Australia Dollar", 2, 3),
    NZD("New Zealand Dollar", 3, 4),
    USD("United States Dollar", 4, 0),
    SEK("Sweden Krona", 5, 5),
    NOK("Norway Krone", 6, 6),
    DKK("Denmark Krone", 7, 7),
    CAD("Canada Dollar", 8, 8, 1),
    CHF("Switzerland Franc", 9, 9),
    JPY("Japan Yen", 10, 10),
    PHP("Philippines Peso", 11, 11, 1),
    TWD("Taiwan New Dollar", 12, 12),
    CLP("Chile Peso", 13, 13),
    INR("India Rupee", 14, 14),
    IDR("Indonesia Rupiah", 15, 15),
    CNY("China Yuan Renminbi", 16, 16),
    MYR("Malaysia Ringgit", 17, 17),
    KRW("Korea (South) Won", 18, 18),
    COP("Colombia Peso", 19, 19),
    BRL("Brazil Real", 20, 20),
    PEN("Peru Sol", 21, 21),
    RUB("Russia Ruble", 22, 22),


    AED("United Arab Emirates Dirham"),
    AFN("Afghanistan Afghani"),
    ALL("Albania Lek"),
    AMD("Armenia Dram"),
    ANG("Netherlands Antilles Guilder"),
    AOA("Angola Kwanza"),
    ARS("Argentina Peso"),

    AWG("Aruba Guilder"),
    AZN("Azerbaijan New Manat"),
    BAM("Bosnia and Herzegovina Convertible Marka"),
    BBD("Barbados Dollar"),
    BDT("Bangladesh Taka"),
    BGN("Bulgaria Lev"),
    BHD("Bahrain Dinar"),
    BIF("Burundi Franc"),
    BMD("Bermuda Dollar"),
    BND("Brunei Darussalam Dollar"),
    BOB("Bolivia Bolíviano"),

    BSD("Bahamas Dollar"),
    BTN("Bhutan Ngultrum"),
    BWP("Botswana Pula"),
    BYR("Belarus Ruble"),
    BZD("Belize Dollar"),

    CDF("Congo/Kinshasa Franc"),

    CRC("Costa Rica Colon"),
    CUC("Cuba Convertible Peso"),
    CUP("Cuba Peso"),
    CVE("Cape Verde Escudo"),
    CZK("Czech Republic Koruna"),
    DJF("Djibouti Franc"),

    DOP("Dominican Republic Peso"),
    DZD("Algeria Dinar"),
    EGP("Egypt Pound"),
    ERN("Eritrea Nakfa"),
    ETB("Ethiopia Birr"),

    FJD("Fiji Dollar"),
    FKP("Falkland Islands (Malvinas) Pound"),

    GEL("Georgia Lari"),
    GGP("Guernsey Pound"),
    GHS("Ghana Cedi"),
    GIP("Gibraltar Pound"),
    GMD("Gambia Dalasi"),
    GNF("Guinea Franc"),
    GTQ("Guatemala Quetzal"),
    GYD("Guyana Dollar"),
    HKD("Hong Kong Dollar"),
    HNL("Honduras Lempira"),
    HRK("Croatia Kuna"),
    HTG("Haiti Gourde"),
    HUF("Hungary Forint"),

    ILS("Israel Shekel"),
    IMP("Isle of Man Pound"),

    IQD("Iraq Dinar"),
    IRR("Iran Rial"),
    ISK("Iceland Krona"),
    JEP("Jersey Pound"),
    JMD("Jamaica Dollar"),
    JOD("Jordan Dinar"),

    KES("Kenya Shilling"),
    KGS("Kyrgyzstan Som"),
    KHR("Cambodia Riel"),
    KMF("Comoros Franc"),
    KPW("Korea (North) Won"),

    KWD("Kuwait Dinar"),
    KYD("Cayman Islands Dollar"),
    KZT("Kazakhstan Tenge"),
    LAK("Laos Kip"),
    LBP("Lebanon Pound"),
    LKR("Sri Lanka Rupee"),
    LRD("Liberia Dollar"),
    LSL("Lesotho Loti"),
    LYD("Libya Dinar"),
    MAD("Morocco Dirham"),
    MDL("Moldova Leu"),
    MGA("Madagascar Ariary"),
    MKD("Macedonia Denar"),
    MMK("Myanmar (Burma) Kyat"),
    MNT("Mongolia Tughrik"),
    MOP("Macau Pataca"),
    MRO("Mauritania Ouguiya"),
    MUR("Mauritius Rupee"),
    MVR("Maldives (Maldive Islands) Rufiyaa"),
    MWK("Malawi Kwacha"),
    MXN("Mexico Peso"),

    MZN("Mozambique Metical"),
    NAD("Namibia Dollar"),
    NGN("Nigeria Naira"),
    NIO("Nicaragua Cordoba"),

    NPR("Nepal Rupee"),

    OMR("Oman Rial"),
    PAB("Panama Balboa"),

    PGK("Papua New Guinea Kina"),

    PKR("Pakistan Rupee"),
    PLN("Poland Zloty"),
    PYG("Paraguay Guarani"),
    QAR("Qatar Riyal"),
    RON("Romania New Leu"),
    RSD("Serbia Dinar"),

    RWF("Rwanda Franc"),
    SAR("Saudi Arabia Riyal"),
    SBD("Solomon Islands Dollar"),
    SCR("Seychelles Rupee"),
    SDG("Sudan Pound"),

    SGD("Singapore Dollar"),
    SHP("Saint Helena Pound"),
    SLL("Sierra Leone Leone"),
    SOS("Somalia Shilling"),
    SPL("Seborga Luigino"),
    SRD("Suriname Dollar"),
    STD("São Tomé and Príncipe Dobra"),
    SVC("El Salvador Colon"),
    SYP("Syria Pound"),
    SZL("Swaziland Lilangeni"),
    THB("Thailand Baht"),
    TJS("Tajikistan Somoni"),
    TMT("Turkmenistan Manat"),
    TND("Tunisia Dinar"),
    TOP("Tonga Pa'anga"),
    TRY("Turkey Lira"),
    TTD("Trinidad and Tobago Dollar"),
    TVD("Tuvalu Dollar"),

    TZS("Tanzania Shilling"),
    UAH("Ukraine Hryvnia"),
    UGX("Uganda Shilling"),

    UYU("Uruguay Peso"),
    UZS("Uzbekistan Som"),
    VEF("Venezuela Bolivar"),
    VND("Viet Nam Dong"),
    VUV("Vanuatu Vatu"),
    WST("Samoa Tala"),
    XAF("Communauté Financière Africaine (BEAC) CFA Franc BEAC"),
    XCD("East Caribbean Dollar"),
    XDR("International Monetary Fund (IMF) Special Drawing Rights"),
    XOF("Communauté Financière Africaine (BCEAO) Franc"),
    XPF("Comptoirs Français du Pacifique (CFP) Franc"),
    YER("Yemen Rial"),
    ZAR("South Africa Rand"),
    ZMW("Zambia Kwacha"),
    ZWD("Zimbabwe Dollar");

    private static class CurrencyConfig {

        public static Logger logger = LogManager.getLogger();

        private static CurrencyConfig INSTANCE;
        private String fxForwardImpliedCurve;
        private String discountCurve;
        private Map<String, Map<String, String>> currencies;

        public static CurrencyConfig instance() {
            return instance(System.getProperty(AppConfig.CURRENCY_CONFIG, "/currency.yaml"));
        }

        public static CurrencyConfig instance(String configFile) {
            if (INSTANCE == null) {
                ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
                try {
                    URL ccyUrl = CurrencyPair.class.getResource(configFile);
                    INSTANCE = mapper.readValue(ccyUrl, CurrencyConfig.class);
                } catch (Exception ex) {
                    String msg = "Fail to load Currency Config file currency.yaml ...";
                    logger.error("%s : %s", msg, ex.getMessage());
                    throw new RuntimeException(msg, ex);
                }
            }

            return INSTANCE;
        }

        public void setFxForwardImpliedCurve(String fxForwardImpliedCurve) {
            this.fxForwardImpliedCurve = fxForwardImpliedCurve;
        }

        public void setDiscountCurve(String discountCurve) {
            this.discountCurve = discountCurve;
        }

        public void setCurrencies(Map<String, Map<String, String>> currencies) {
            this.currencies = currencies;
        }

        public String getDiscountCurve(Currency ccy) {
            String code = ccy.getCurrencyCode();
            return (currencies.containsKey(code) ? currencies.get(code).getOrDefault("discountCurve", this.discountCurve) : discountCurve).replace("$CCY$", code);
        }

        public String getFxForwardYieldCurve(Currency ccy) {
            String code = ccy.getCurrencyCode();
            return (currencies.containsKey(code) ? currencies.get(code).getOrDefault("fxForwardImpliedCurve", this.fxForwardImpliedCurve) : fxForwardImpliedCurve).replace("$CCY$", code);
        }
    }

    public static final Pattern CCY_PATTERN = java.util.regex.Pattern.compile("[a-zA-Z]{3}");
    private static BusinessCalendarService businessCalendarService;
    private final int conventionPriority;
    private final int premiumPriority;
    private final int spotLag;
    private final String description;

    Currency(String description) {
        this(description, Integer.MAX_VALUE, Integer.MAX_VALUE, 2);
    }

    Currency(String description, int conventionPriority, int premiumPriority) {
        this(description, conventionPriority, premiumPriority, 2);
    }

    Currency(String description, int conventionPriority, int premiumPriority, int spotLag) {
        this.description = description;
        this.conventionPriority = conventionPriority;
        this.premiumPriority = premiumPriority;
        this.spotLag = spotLag;
    }

    public static void setCalendarService(BusinessCalendarService bc) {
        businessCalendarService = bc;
    }

    public static boolean matches(String currencyCode) {
        return CCY_PATTERN.matcher(currencyCode).matches();
    }

    public static Currency parse(String currencyCode) {
        if (matches(currencyCode)) {
            return Currency.valueOf(currencyCode.toUpperCase());
        }
        throw new IllegalArgumentException(String.format("The provided currency code '%s' is invalid and cannot be parsed into a Currency", currencyCode));
    }

    //TODO use the config instead
    public int getSpotLag() {
        return spotLag;
    }


    public BusinessCalendar getBusinessCalendar() {
        return Currency.businessCalendarService.getBusinessCalendar(this);
    }

    public String getBusinessCalendarName() {
        return Currency.businessCalendarService.getBusinessCalendarName(this);
    }

    public String getDescription() {
        return description;
    }

    public String getCurrencyCode() {
        return name();
    }

    public String getDiscountCurve() {
        return CurrencyConfig.instance().getDiscountCurve(this);
    }

    public String getFxForwardYieldCurve() {
        return CurrencyConfig.instance().getFxForwardYieldCurve(this);
    }

    public int compareConvention(Currency other) {
        return other.conventionPriority - this.conventionPriority;
    }

    public int comparePremium(Currency other) {
        return other.premiumPriority - this.premiumPriority;
    }
}
