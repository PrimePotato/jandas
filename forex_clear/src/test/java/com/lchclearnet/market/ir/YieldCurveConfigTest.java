package com.lchclearnet.market.ir;

import com.lchclearnet.calendar.DayCountFraction;
import com.lchclearnet.utils.InterpolationMethod;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

//@Ignore
//public class YieldCurveConfigTest {
//
//    @BeforeClass
//    public static void setup() {
//        YieldCurveConfig.instance("/yield_curve.yaml");
//    }
//
//    @Test
//    public void testDcfAndInterpMethod() {
//        Assert.assertEquals(YieldCurveConfig.instance().getDayCountFraction("XXX XONA"), DayCountFraction.DCF_30_360);
//        Assert.assertEquals(YieldCurveConfig.instance().getInterpolationMethod("XXX XONA"), InterpolationMethod.SPLINE_CONSTANT);
//
//        Assert.assertEquals(YieldCurveConfig.instance().getDayCountFraction("AUD AONIA"), DayCountFraction.DCF_30_360);
//        Assert.assertEquals(YieldCurveConfig.instance().getInterpolationMethod("AUD AONIA"), InterpolationMethod.SPLINE_CONSTANT);
//
//        Assert.assertEquals(YieldCurveConfig.instance().getDayCountFraction("CHF SARON"), DayCountFraction.DCF_ACT_ACT);
//        Assert.assertEquals(YieldCurveConfig.instance().getInterpolationMethod("CHF SARON"), InterpolationMethod.SPLINE_CONSTANT);
//
//        Assert.assertEquals(YieldCurveConfig.instance().getDayCountFraction("EUR EONIA"), DayCountFraction.DCF_30_360);
//        Assert.assertEquals(YieldCurveConfig.instance().getInterpolationMethod("EUR EONIA"), InterpolationMethod.SPLINE_CONSTANT);
//
//        Assert.assertEquals(YieldCurveConfig.instance().getDayCountFraction("GBP SONIA"), DayCountFraction.DCF_30_360);
//        Assert.assertEquals(YieldCurveConfig.instance().getInterpolationMethod("GBP SONIA"), InterpolationMethod.SPLINE_CONSTANT);
//
//        Assert.assertEquals(YieldCurveConfig.instance().getDayCountFraction("JPY TONA"), DayCountFraction.DCF_30_360);
//        Assert.assertEquals(YieldCurveConfig.instance().getInterpolationMethod("JPY TONA"), InterpolationMethod.SPLINE_CONSTANT);
//
//        Assert.assertEquals(YieldCurveConfig.instance().getDayCountFraction("USD FEDFUND"), DayCountFraction.DCF_30_360);
//        Assert.assertEquals(YieldCurveConfig.instance().getInterpolationMethod("USD FEDFUND"), InterpolationMethod.SPLINE_CONSTANT);
//    }
//
//}