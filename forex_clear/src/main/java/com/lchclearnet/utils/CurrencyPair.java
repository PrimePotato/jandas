package com.lchclearnet.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.collect.ImmutableSet;
import com.lchclearnet.calendar.BusinessCalendar;
import com.lchclearnet.calendar.BusinessCalendarService;
import com.lchclearnet.calendar.DayCountFraction;
import com.lchclearnet.fx.AppConfig;
import com.lchclearnet.utils.Tuple;

import java.io.Serializable;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An ordered pair of currencies, such as 'EUR/USD'.
 * <p>
 * This could be used to identify a pair of currencies for quoting rates in FX Trades.
 * <p>
 * This class is immutable.
 */
public final class CurrencyPair implements Serializable, Comparable<CurrencyPair> {

    public static final Currency USD = Currency.parse("USD");
    /**
     * Regular expression to parse the textual format.
     * Three upper or lower case letters, a (forward/backward)slash or dash or underscore, and another three upper case letters.
     */
    public static final Pattern CCYPAIR_PATTERN = Pattern.compile("([a-zA-Z]{3})(-|_|/|\\\\)?([a-zA-Z]{3})");
    private static final long serialVersionUID = 1L;
    /**
     * The configured instances and associated rate digits.
     */
    private static final Map<String, Map<String, String>> CCY_PAIRS_CONFIG = new HashMap<>();
    private static final Map<Currency, Integer> CONVENTION_ORDERING = new HashMap<>();
    private static final Map<Currency, Integer> PREMIUM_ORDERING = new HashMap<>();
    private static final Map<String, CurrencyPair> CCY_PAIRS = new HashMap<>();
    private static int size = 0;
    private static CurrencyPair[] values = new CurrencyPair[400];
    private static BusinessCalendarService businessCalendarService;

    static {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            URL pairsUrl = CurrencyPair.class.getResource(System.getProperty(AppConfig.CURRENCY_PAIR_CONFIG, "/currency_pair.yaml"));
            CurrencyPairConfigs configs = mapper.readValue(pairsUrl, CurrencyPairConfigs.class);
            String[] priorities = configs.getConventionPriority();
            for (int i = 0; i < priorities.length; i++) {
                CONVENTION_ORDERING.put(Currency.parse(priorities[i].toUpperCase()), i);
            }
            priorities = configs.getPremiumPriority();
            for (int i = 0; i < priorities.length; i++) {
                PREMIUM_ORDERING.put(Currency.parse(priorities[i].toUpperCase()), i);
            }
            CCY_PAIRS_CONFIG.putAll(configs.getCcyPairs());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * The foreign currency of the pair.
     * In the pair 'XXX/YYY' the foreign is 'XXX'.
     */
    private final Currency foreign;
    /**
     * The domestic currency of the pair.
     * In the pair 'XXX/YYY' the domestic is 'YYY'.
     */
    private final Currency domestic;
    private final String code, inverseCode;
    private final int ordinal;
    private final int spotLag;
    private final DayCountFraction dayCountFraction;
    private final int pipConvention;
    private BusinessCalendar spotCalendar;
    private BusinessCalendar rollCalendar;

    /**
     * Creates a currency pair.
     *
     * @param foreign  the foreign currency, validated not null
     * @param domestic the domestic currency, validated not null
     */
    private CurrencyPair(Currency foreign, Currency domestic) {
        this.foreign = foreign;
        this.domestic = domestic;
        this.code = String.format("%s%s", foreign.getCurrencyCode(), domestic.getCurrencyCode());
        this.inverseCode = String.format("%s%s", domestic.getCurrencyCode(), foreign.getCurrencyCode());

        if (foreign != domestic) {
            this.spotLag = Integer.valueOf(get("spotLag"));
            this.dayCountFraction = DayCountFraction.getEnum(get("dayCountFraction"));
            this.pipConvention = Integer.valueOf(get("pipConvention"));
        } else {
            this.spotLag = foreign.getSpotLag();
            this.dayCountFraction = null;
            this.pipConvention = 4;
        }

        synchronized (CurrencyPair.class) {
            this.ordinal = size++;
            if (ordinal == values.length) {
                CurrencyPair[] oldValues = values;
                values = new CurrencyPair[oldValues.length * 2];
                System.arraycopy(oldValues, 0, values, 0, oldValues.length);
            }
            values[ordinal] = this;
        }
    }

    //*************************************************************************************************************//
    //*************************************************************************************************************//
    //*************************************************************************************************************//

    public static void main(String[] args) {
        CurrencyPair ccyPair = CurrencyPair.of("JPY", "USD");
        CurrencyPair conventionPair = ccyPair.toConvention();
        System.out.println(conventionPair.toString());
        for (Currency ccy : ccyPair.toConventionSet()) {
            System.out.println(String.format("%s: %d", ccy.getCurrencyCode(), ccy.getDescription()));
        }

        for (String code : ImmutableSet.of("eur-usd", "EUR_usd", "eurUSD", "usd/EUR", "EURUSD", "eurusd")) {
            ccyPair = CurrencyPair.parse(code);
            System.out.println(String.format("%s: %s", code, ccyPair.code()));
        }
    }

    public static CurrencyPair fromOrdinal(int ordinal) {
        return values[ordinal];
    }

    public static int size() {
        return size;
    }

    /**
     * Obtains the set of configured currency pairs.
     * <p>
     * This contains all the currency pairs that have been defined in configuration.
     * Any currency pair instances that have been dynamically created are not included.
     *
     * @return an immutable set containing all registered currency pairs
     */
    public static Collection<CurrencyPair> getAvailablePairs() {
        return CCY_PAIRS.values();
    }

    /**
     * Obtains an INSTANCE from two currencies.
     * <p>
     * The first currency is the foreign and the second is the domestic.
     * The two currencies may be the same.
     *
     * @param foreign  the foreign currency
     * @param domestic the domestic currency
     * @return the currency pair
     */
    public static CurrencyPair of(String foreign, String domestic) {
        ArgHelper.checkNotNull(foreign, "foreign");
        ArgHelper.checkNotNull(domestic, "domestic");
        ArgHelper.checkHasContent(foreign, "foreign");
        ArgHelper.checkHasContent(domestic, "domestic");

        return of(Currency.parse(foreign.toUpperCase()), Currency.parse(domestic.toUpperCase()));
    }

    /**
     * Obtains an INSTANCE from two currencies.
     * <p>
     * The first currency is the foreign and the second is the domestic.
     * The two currencies may be the same.
     *
     * @param foreign  the foreign currency
     * @param domestic the domestic currency
     * @return the currency pair
     */
    public static CurrencyPair of(Currency foreign, Currency domestic) {
        ArgHelper.checkNotNull(foreign, "foreign");
        ArgHelper.checkNotNull(domestic, "domestic");

        return CCY_PAIRS.computeIfAbsent(String.format("%s%s", foreign.getCurrencyCode(), domestic.getCurrencyCode()), p -> new CurrencyPair(foreign, domestic));
    }

    /**
     * Parses a currency pair from a string with format XXX(_|-|/|\)?YYY.
     * <p>
     * The parsed format is '${foreignCurrency}/${domesticCurrency}'.
     * Currency parsing is case insensitive.
     *
     * @param pairStr the currency pair as a string XXX(_|-|/|\)?YYY
     * @return the currency pair
     * @throws IllegalArgumentException if the pair cannot be parsed
     */
    public static CurrencyPair parse(String pairStr) {
        ArgHelper.checkNotNull(pairStr, "pairStr");

        Matcher matcher = CCYPAIR_PATTERN.matcher(pairStr);
        if (matcher.matches()) {
            Currency foreign = Currency.parse(matcher.group(1).toUpperCase());
            Currency domestic = Currency.parse(matcher.group(3).toUpperCase());
            return of(foreign, domestic);
        }

        throw new IllegalArgumentException("Invalid currency pair: " + pairStr);
    }

    public static boolean matches(String pairStr) {
        ArgHelper.checkNotNull(pairStr, "pairStr");
        Matcher matcher = CCYPAIR_PATTERN.matcher(pairStr);
        return matcher.matches();
    }

    public static CurrencyPair toConventionPair(Currency ccy) {
        return CurrencyPair.of(ccy, USD).toConvention();
    }

    public static void setCalendarService(BusinessCalendarService bc) {
        businessCalendarService = bc;
    }

    private String get(String key) {
        String value;

        Map<String, String> config = CCY_PAIRS_CONFIG.get(code);
        if (config == null) {
            config = CCY_PAIRS_CONFIG.get(inverseCode);
        }

        if (config == null) {
            value = CCY_PAIRS_CONFIG.get("FORDOM").get(key);
        } else{
            value = config.get(key) ;
        }

        if(value == null){
            value = CCY_PAIRS_CONFIG.get("FORDOM").get(key);
            //throw new IllegalStateException(String.format("Could not find any value for key[%s] in CurrencyPair[%s]", key, code));
        }

        return value;
    }

    public int ordinal() {
        return ordinal;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public int getSpotLag() {
        return spotLag;
    }

    public DayCountFraction getDayCountFraction() {
        return dayCountFraction;
    }

    public int getPipConvention() {
        return pipConvention;
    }

    public BusinessCalendar getSpotCalendar() {
        if(this.spotCalendar == null){
            if(foreign == domestic){
                this.spotCalendar = foreign.getBusinessCalendar();
            } else {
                String spotCalendar = get("spotCalendar");
                if (spotCalendar == null) {
                    if (foreign == Currency.USD || domestic == Currency.USD) {
                        spotCalendar = Currency.USD == foreign ?
                                domestic.getBusinessCalendarName() : foreign.getBusinessCalendarName();
                    } else {
                        spotCalendar = String.format("%s*%s",
                                foreign.getBusinessCalendarName(),
                                domestic.getBusinessCalendarName());
                    }

                }
                this.spotCalendar = businessCalendarService.getBusinessCalendar(spotCalendar);
            }
        }
        return this.spotCalendar;
    }

    public BusinessCalendar getRollCalendar() {
        if(this.rollCalendar == null) {
            if(foreign == domestic){
                this.rollCalendar = foreign.getBusinessCalendar();
            } else {
                String rollCalendar = get("rollCalendar");
                if (rollCalendar == null) {
                    if (foreign == Currency.USD || domestic == Currency.USD) {
                        rollCalendar = String.format("%s+%s",
                                foreign.getBusinessCalendarName(),
                                domestic.getBusinessCalendarName());
                    } else {
                        rollCalendar = String.format("%s+%s+%s",
                                foreign.getBusinessCalendarName(),
                                domestic.getBusinessCalendarName(),
                                Currency.USD.getBusinessCalendarName());
                    }
                }
                this.rollCalendar = businessCalendarService.getBusinessCalendar(rollCalendar);
            }
        }
        return this.rollCalendar;
    }

    /**
     * Gets the inverse currency pair.
     * <p>
     * The inverse pair has the same currencies but in reverse order.
     *
     * @return the inverse pair
     */
    public CurrencyPair inverse() {
        return of(domestic, foreign);
    }

    //-------------------------------------------------------------------------

    /**
     * Checks if the currency pair contains the supplied currency as either its foreign or domestic.
     *
     * @param currency the currency to check against the pair
     * @return true if the currency is either the foreign or domestic currency in the pair
     */
    public boolean contains(String currency) {
        ArgHelper.checkNotNull(currency, "currency");
        return contains(Currency.parse(currency.toUpperCase()));
    }

    /**
     * Checks if the currency pair contains the supplied currency as either its foreign or domestic.
     *
     * @param currency the currency to check against the pair
     * @return true if the currency is either the foreign or domestic currency in the pair
     */
    public boolean contains(Currency currency) {
        ArgHelper.checkNotNull(currency, "currency");
        return foreign.equals(currency) || domestic.equals(currency);
    }

    /**
     * Finds the other currency in the pair.
     * <p>
     * If the pair is XXX/YYY, then passing in XXX will return YYY, and passing in YYY will return XXX.
     * Passing in ZZZ will throw an exception.
     *
     * @param currency the currency to check
     * @return the other currency in the pair
     * @throws IllegalArgumentException if the specified currency is not one of those in the pair
     */
    public Currency other(String currency) {
        ArgHelper.checkNotNull(currency, "currency");
        return other(Currency.parse(currency.toUpperCase()));
    }

    /**
     * Finds the other currency in the pair.
     * <p>
     * If the pair is XXX/YYY, then passing in XXX will return YYY, and passing in YYY will return XXX.
     * Passing in ZZZ will throw an exception.
     *
     * @param currency the currency to check
     * @return the other currency in the pair
     * @throws IllegalArgumentException if the specified currency is not one of those in the pair
     */
    public Currency other(Currency currency) {
        ArgHelper.checkNotNull(currency, "currency");
        if (currency.equals(foreign)) {
            return domestic;
        }
        if (currency.equals(domestic)) {
            return foreign;
        }
        throw new IllegalArgumentException("Unable to find other currency, " + currency + " is not present in " + toString());
    }

    /**
     * Checks if this currency pair is an identity pair.
     * <p>
     * The identity pair is one where the foreign and domestic currency are the same..
     *
     * @return true if this pair is an identity pair
     */
    public boolean isIdentity() {
        return foreign.equals(domestic);
    }

    /**
     * Checks if this currency pair is the inverse of the specified pair.
     * <p>
     * This could be used to check if an FX rate specified in one currency pair needs inverting.
     *
     * @param other the other currency pair
     * @return true if the currency is the inverse of the specified pair
     */
    public boolean isInverse(CurrencyPair other) {
        ArgHelper.checkNotNull(other, "currencyPair");
        return foreign.equals(other.domestic) && domestic.equals(other.foreign);
    }

    public boolean isCross() {
        return !(USD.equals(foreign) || USD.equals(domestic));
    }

    /**
     * Finds the currency pair that is a cross between this pair and the other pair.
     * <p>
     * The cross is only returned if the two pairs contains three currencies in total,
     * such as XXX/YYY and YYY/ZZZ and neither pair is an identity such as XXX/XXX.
     * <ul>
     * <li>Given two pairs XXX/YYY and YYY/ZZZ the result will be XXX/ZZZ or ZZZ/XXX as per the market convention.
     * <li>Given two pairs XXX/YYY and ZZZ/YYY the result will be empty.
     * <li>Given two pairs XXX/XXX and XXX/YYY the result will be empty.
     * <li>Given two pairs XXX/YYY and XXX/YYY the result will be empty.
     * <li>Given two pairs XXX/XXX and XXX/XXX the result will be empty.
     * </ul>
     *
     * @param other the other currency pair
     * @return the cross currency pair, or empty if no cross currency pair can be created
     */
    public Optional<CurrencyPair> cross(CurrencyPair other) {
        ArgHelper.checkNotNull(other, "other");
        if (isIdentity() || other.isIdentity() || this.equals(other) || this.equals(other.inverse())) {
            return Optional.empty();
        }
        // XXX/YYY cross YYY/ZZZ
        if (domestic.equals(other.foreign)) {
            return Optional.of(of(foreign, other.domestic).toConvention());
        }
        // XXX/YYY cross ZZZ/YYY
        if (domestic.equals(other.domestic)) {
            return Optional.of(of(foreign, other.foreign).toConvention());
        }
        // YYY/XXX cross YYY/ZZZ
        if (foreign.equals(other.foreign)) {
            return Optional.of(of(domestic, other.domestic).toConvention());
        }
        // YYY/XXX cross ZZZ/YYY
        if (foreign.equals(other.domestic)) {
            return Optional.of(of(domestic, other.foreign).toConvention());
        }
        return Optional.empty();
    }

    public Tuple uncross() {
        return isCross() ? Tuple.of(CurrencyPair.of(foreign, USD), CurrencyPair.of(USD, domestic)) : Tuple.of(this);
    }

    /**
     * Checks if this currency pair is a conventional currency pair.
     * <p>
     * A market convention determines that 'EUR/USD' should be used and not 'USD/EUR'.
     * This knowledge is encoded in configuration for a standard set of pairs.
     * <p>
     * It is possible to create two different currency pairs from any two currencies, and it is guaranteed that
     * exactly one of the pairs will be the market convention pair.
     * <p>
     * If a currency pair is not explicitly listed in the configuration, a priority ordering of currencies
     * is used to choose foreign currency of the pair that is treated as conventional.
     * <p>
     *
     * @return true if the currency pair follows the market convention, false if it does not
     */
    public boolean isConvention() {
        // Get the priorities of the currencies to determine which should be the foreign
        Integer foreignPriority = CONVENTION_ORDERING.getOrDefault(foreign, Integer.MAX_VALUE);
        Integer domesticPriority = CONVENTION_ORDERING.getOrDefault(domestic, Integer.MAX_VALUE);

        // If a currency is earlier in the list it has a higher priority
        return foreignPriority < domesticPriority;
    }
    //*************************************************************************************************************//
    //*************************************************************************************************************//
    //*************************************************************************************************************//

    /**
     * Returns the market convention currency pair for the currencies in the pair.
     * <p>
     * If {@link #isConvention()} is {@code true} this method returns {@code this}, otherwise
     * it returns the {@link #inverse} pair.
     *
     * @return the market convention currency pair for the currencies in the pair
     */
    public CurrencyPair toConvention() {
        return isConvention() ? this : inverse();
    }

    /**
     * Returns the set of currencies contains in the pair.
     *
     * @return the set of currencies, with iteration in conventional order
     */
    public Set<Currency> toConventionSet() {
        if (isConvention()) {
            return ImmutableSet.of(foreign, domestic);
        } else {
            return ImmutableSet.of(domestic, foreign);
        }
    }

    /**
     * Returns the set of currencies contains in the pair.
     *
     * @return the set of currencies, with iteration in natural order
     */
    public Set<Currency> toSet() {
        return ImmutableSet.of(foreign, domestic);
    }

    public Currency nonUsdCurrency() {
        if (isCross())
            throw new RuntimeException(String.format("nonUsdCurrency() cannot be called on cross currency pair '%s'", code()));
        return foreign != USD ? foreign : domestic;
    }


    /**
     * Gets the foreign currency of the pair.
     * <p>
     * In the pair 'XXX/YYY' the foreign is 'YYY'.
     *
     * @return the foreign currency
     */
    public Currency getForeign() {
        return foreign;
    }

    //*************************************************************************************************************//
    //*************************************************************************************************************//
    //*************************************************************************************************************//

    /**
     * Gets the domestic currency of the pair.
     * <p>
     * In the pair 'XXX/YYY' the domestic is 'XXX'.
     * <p>
     * The domestic currency is also known as the <i>quote currency</i> or the <i>variable currency</i>.
     *
     * @return the domestic currency
     */
    public Currency getDomestic() {
        return domestic;
    }

    public String code() {
        return code;
    }

    public String inverseCode() {
        return inverseCode;
    }

    /**
     * Checks if this currency pair equals another.
     * <p>
     * The comparison checks the two currencies.
     *
     * @param obj the other currency pair, null returns false
     * @return true if equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj != null && obj.getClass() == this.getClass()) {
            CurrencyPair other = (CurrencyPair) obj;
            return foreign.equals(other.foreign) && domestic.equals(other.domestic);
        }
        return false;
    }

    //*************************************************************************************************************//
    //*************************************************************************************************************//
    //*************************************************************************************************************//
    @Override
    public int compareTo(CurrencyPair o) {
        return ordinal - o.ordinal;
    }

    /**
     * Returns a suitable hash code for the currency.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return foreign.hashCode() ^ domestic.hashCode();
    }

    /**
     * Returns the formatted string version of the currency pair.
     * <p>
     * The format is '${foreignCurrency}${domesticCurrency}'.
     *
     * @return the formatted string
     */
    @Override
    public String toString() {
        return String.format("%s/%s", foreign.getCurrencyCode(), domestic.getCurrencyCode());
    }
    //*************************************************************************************************************//

    //*************************************************************************************************************//
    //*************************************************************************************************************//
    //*************************************************************************************************************//
    private static class CurrencyPairConfigs {
        private String[] conventionPriority;
        private String[] premiumPriority;
        private Map<String, Map<String, String>> ccyPairs;

        public String[] getConventionPriority() {
            return conventionPriority;
        }

        public void setConventionPriority(String[] conventionPriority) {
            this.conventionPriority = conventionPriority;
        }

        public String[] getPremiumPriority() {
            return premiumPriority;
        }

        public void setPremiumPriority(String[] premiumPriority) {
            this.premiumPriority = premiumPriority;
        }

        public Map<String, Map<String, String>> getCcyPairs() {
            return ccyPairs;
        }

        public void setCcyPairs(Map<String, Map<String, String>> ccyPairs) {
            this.ccyPairs = ccyPairs;
        }
    }

}
