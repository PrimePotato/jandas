package com.lchclearnet.utils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.util.regex.Pattern.quote;

/**
 * Utility methods for common argument validations/parsing.
 *
 * <P>Replaces <tt>if</tt> statements at the start of a method with
 * more compact method calls.
 *
 * <P>Example use case.
 * <P>Instead of :
 * <PRE>
 * public void doThis(String aText){
 * if (!hasContent(aText)){
 * throw new IllegalArgumentException();
 * }
 * //..main body elided
 * }
 * </PRE>
 * <P>One may instead write :
 * <PRE>
 * public void doThis(String aText){
 * ArgHelper.checkHasContent(aText);
 * //..main body elided
 * }
 * </PRE>
 */
public final class ArgHelper {

    // PRIVATE
    /**
     * The application's date format.
     */
    private static final String DATE_FORMAT = "yyyyMMdd";


    private ArgHelper() {
        //empty - prevent construction
    }

    /**
     * Return <tt>true</tt> only if <tt>aText</tt> is not null,
     * and is not empty after trimming. (Trimming removes both
     * leading/trailing whitespace and ASCII control characters.)
     *
     * <P> For checking argument validity, {@link ArgHelper#checkHasContent} should
     * be used instead of this method.
     *
     * <P>This method is particularly useful, since it is very commonly required.
     *
     * @param aText possibly-null.
     * @return boolean
     */
    public static boolean hasContent(String aText) {
        return (aText != null) && (aText.trim().length() > 0);
    }

    /**
     * Return <tt>true</tt> only if <tt>aNumber</tt> is in the range
     * <tt>aLow..aHigh</tt> (inclusive).
     *
     * <P> For checking argument validity, {@link ArgHelper#checkInRange} should
     * be used instead of this method.
     *
     * @param aNumber
     * @param aLow    ess than or equal to <tt>aHigh</tt>.
     * @param aHigh
     * @return
     */
    public static boolean isInRange(double aNumber, double aLow, double aHigh) {
        if (aLow > aHigh) {
            throw new IllegalArgumentException("Low is greater than High.");
        }
        return (aLow <= aNumber && aNumber <= aHigh);
    }

    /**
     * Return <tt>true</tt> if <tt>aBoolean</tt> equals "true" (ignore case), or
     * <tt>false</tt> if <tt>aBoolean</tt> equals "false" (ignore case).
     *
     * <P>Note that this behavior is different from that of <tt>Boolean.getValue</tt>.
     *
     * @param aBoolean equals "true" or "false" (not case-sensitive).
     * @return Boolean
     */
    public static Boolean parseBoolean(String aBoolean) {
        if (aBoolean.equalsIgnoreCase("true")) {
            return Boolean.TRUE;
        } else if (aBoolean.equalsIgnoreCase("false")) {
            return Boolean.FALSE;
        } else {
            throw new IllegalArgumentException("Cannot parse into Boolean: " + aBoolean);
        }
    }

    /**
     * Parse text into a {@link Date}. If the text has no content, then return <tt>null</tt>.
     *
     * @param aDate
     * @param aName
     * @return
     * @throws IllegalArgumentException
     */
    public static Date parseDate(String aDate, String aName) throws IllegalArgumentException {
        Date result = null;
        if (hasContent(aDate)) {
            DateFormat FORMAT = new SimpleDateFormat(DATE_FORMAT);
            FORMAT.setLenient(false);
            try {
                result = FORMAT.parse(aDate);
            } catch (ParseException ex) {
                throw new IllegalArgumentException(aName + " is not a valid date: " + aDate);
            }
        }
        return result;
    }

    /**
     * Format an arbitrary Object, into a form suitable for the end user.
     *
     * @param aObject
     * @return
     */
    public static String format(Object aObject) {
        String result = "";
        if (aObject != null) {
            if (aObject instanceof Date) {
                Date date = (Date) aObject;
                DateFormat FORMAT = new SimpleDateFormat(DATE_FORMAT);
                FORMAT.setLenient(false);
                result = FORMAT.format(date);
            } else {
                result = String.valueOf(aObject);
            }
        }
        return result;
    }

    /**
     * Parse text into a {@link BigDecimal}. If the text has no content, then return <tt>null</tt>.
     *
     * @param aBigDecimal
     * @param aName
     * @return
     * @throws IllegalArgumentException
     */
    public static BigDecimal parseBigDecimal(String aBigDecimal, String aName) throws IllegalArgumentException {
        BigDecimal result = null;
        if (hasContent(aBigDecimal)) {
            try {
                result = new BigDecimal(aBigDecimal);
            } catch (NumberFormatException exception) {
                throw new IllegalArgumentException(aName + " is not a valid number.");
            }
        }
        return result;
    }

    /**
     * If <code>aText</code> does not satisfy, then
     * throw an <code>IllegalArgumentException</code>.
     *
     * <P>Most text used in an application is meaningful only if it has visible content.
     *
     * @param aText
     * @param msg
     */
    public static void checkHasContent(String aText, String msg) {
        if (!hasContent(aText)) {
            throw new IllegalArgumentException(String.format("text '%s' has no visible content", msg));
        }
    }

    /**
     * If returns <code>false</code>, then
     * throw an <code>IllegalArgumentException</code>.
     *
     * @param aNumber
     * @param aLow    is less than or equal to <code>aHigh</code>.
     * @param aHigh
     */
    public static void checkInRange(double aNumber, double aLow, double aHigh) {
        if (!isInRange(aNumber, aLow, aHigh)) {
            throw new IllegalArgumentException(aNumber + " not in range " + aLow + ".." + aHigh);
        }
    }

    /**
     * If <tt>aNumber</tt> is less than <tt>1</tt>, then throw an <tt>IllegalArgumentException</tt>.
     *
     * @param aNumber
     */
    public static void checkPositive(double aNumber) {
        if (aNumber < 0) {
            throw new IllegalArgumentException(aNumber + " is not positive");
        }
    }

    /**
     * If <tt>aNumber</tt> is less than <tt>1</tt>, then throw an <tt>IllegalArgumentException</tt>.
     *
     * @param aNumber
     */
    public static void checkNegative(double aNumber) {
        if (aNumber > 0) {
            throw new IllegalArgumentException(aNumber + " is not negative");
        }
    }

    /**
     * If returns <tt>false</tt>, then
     * throw an <code>IllegalArgumentException</code>.
     *
     * @param aText
     * @param aPattern
     */
    public static void checkMatch(String aText, String aPattern) {
        if (!aText.matches(aText)) {
            throw new IllegalArgumentException("Text " + quote(aText) + " does not match '" + aPattern + "'");
        }
    }

    /**
     * If <code>aObject</code> is null, then throw a <code>NullPointerException</code>.
     *
     * @param aObject
     * @param message
     */
    public static void checkNotNull(Object aObject, String message) {
        if (aObject == null) {
            throw new NullPointerException(message);
        }
    }
}
