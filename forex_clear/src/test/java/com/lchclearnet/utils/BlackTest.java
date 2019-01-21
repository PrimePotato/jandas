package com.lchclearnet.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class BlackTest {

  final double f = 100, k = 105, t = 0.25, v = 0.15;

  @Test
  public void d1() {

    System.out.println(Black.d1(f, v, t, k));
  }

  @Test
  public void d1StrikeInv() {

    System.out.println(Black.d1StrikeInv(0.25, f, v, t));
  }

  @Test
  public void d2() {

    System.out.println(Black.d2(f, v, t, k));
  }

  @Test
  public void pdf() {

    System.out.println(Black.pdf(0.8));
  }

  @Test
  public void cdf() {

    System.out.println(Black.cdf(0.75));
  }

  @Test
  public void invCdf() {

    System.out.println(Black.invCdf(0.95));
  }

  @Test
  public void price() {

    System.out.println(Black.price(f, k, t, v));
  }

  @Test
  public void delta() {

    System.out.println(Black.delta(f, k, t, v));
  }

  @Test
  public void vega() {

    System.out.println(Black.vega(f, k, t, v));
  }

  @Test
  public void gamma() {

    System.out.println(Black.gamma(f, k, t, v));
  }

  @Test
  public void theta() {

    System.out.println(Black.theta(f, v, t, k));
  }

  @Test
  public void strikeFromDelta() {

    System.out.println(Black.strikeFromDelta(0.25, f, t, v));

    System.out.println(Black.strikeFromDelta(0.75, f, t, v));

  }
}