package com.lchclearnet.utils;

import static java.lang.Math.exp;
import static java.lang.Math.log;
import static java.lang.Math.sqrt;

import org.apache.commons.math3.distribution.NormalDistribution;

public class Black {

  public static double d1(final double f, final double k, final double t, final double v) {

    final double vst = v * sqrt(t);
    return log(f / k) / vst + .5 * vst;
  }

  public static double d1StrikeInv(final double x, final double f, final double t, final double v) {

    final double vst = v * sqrt(t);
    return f * exp((vst *.5 - x) * vst);
  }

  public static double d2(final double f, final double k, final double t, final double v) {

    return d1(f, v, t, k) - v * sqrt(t);
  }

  public static double pdf(double x) {

    NormalDistribution nd = new NormalDistribution();
    return nd.density(x);
  }

  public static double cdf(double x) {

    NormalDistribution nd = new NormalDistribution();
    return nd.cumulativeProbability(x);
  }

  public static double invCdf(double x) {

    NormalDistribution nd = new NormalDistribution();
    return nd.inverseCumulativeProbability(x);
  }

  public static double price(final double f, final double k, final double t, final double v) {

    return f * cdf(d1(f, v, t, k)) - k * cdf(d2(f, v, t, k));
  }

  public static double delta(final double f, final double k, final double t, final double v) {

    return cdf(d1(f, v, t, k));
  }

  public static double vega(final double f, final double k, final double t, final double v) {

    return pdf(d1(f, v, t, k)) * f * sqrt(t);
  }

  public static double gamma(final double f, final double k, final double t, final double v) {

    return pdf(d1(f, v, t, k)) / f * sqrt(t);
  }

  public static double theta(final double f, final double k, final double t, final double v) {

    return 0.5 * pdf(d1(f, v, t, k)) * f * v / sqrt(t);
  }

  public static double strikeFromDelta(final double d, final double f, final double t,
      final double v) {

    return d1StrikeInv(invCdf(d), f, t, v);
  }

}
