// code by jph
package ch.ethz.idsc.tensor.usr;

import junit.framework.Assert;

/* package */ enum DoubleInversion {
  ;
  private static boolean invertible(double value) {
    return Double.isFinite(1.0 / value);
  }

  private static boolean invertible2x(double value) {
    double inverse = 1.0 / value;
    if (!Double.isFinite(inverse))
      return false;
    return Double.isFinite(1.0 / inverse);
  }

  private static boolean invariantInvert2x(double value) {
    Assert.assertTrue(invertible2x(value));
    double inverse = 1.0 / value;
    return value == 1.0 / inverse;
  }

  private static void bisectionSearch1(double min, double max) {
    double sec = (max + min) * 0.5;
    if (min == sec || sec == max) {
      System.out.println("BISECTION RESULT:");
      System.out.println("found " + min + " " + max);
      Assert.assertFalse(invertible2x(min));
      Assert.assertTrue(invertible2x(max));
      return;
    }
    if (invertible2x(sec)) {
      bisectionSearch1(min, sec);
    } else {
      bisectionSearch1(sec, max);
    }
  }

  private static void bisectionSearch2(double min, double max) {
    double sec = max * 0.5 + min * 0.5;
    // System.out.println(min+" "+max);
    if (min == sec || sec == max) {
      System.out.println("BISECTION RESULT:");
      System.out.println("found " + min + " " + max);
      Assert.assertTrue(invertible2x(min));
      Assert.assertFalse(invertible2x(max));
      return;
    }
    if (!invertible2x(sec)) {
      bisectionSearch2(min, sec);
    } else {
      bisectionSearch2(sec, max);
    }
  }

  public static void main(String[] args) {
    // ......................... -> 5.562684646268010E-309 inverse^2 of minimum
    // ......................... -> 5.562684646268010E-309 inverse of maximum
    final double MIN_INVERTIBLE2X = 5.562684646268010E-309;
    final double MAX_INVERTIBLE2X = 1.7976931348623151E308;
    // ......................... -> 1.7976931348623143E308 inverse of minimum
    // System.out.println(1.0/MIN_INVERTIBLE2X);
    {
      // System.out.println(1.0/(1.0/Double.MAX_VALUE));
      double value = 1.0 / MIN_INVERTIBLE2X;
      double again = 1.0 / value;
      System.out.println(again);
    }
    System.out.println(Double.hashCode(+0.0));
    System.out.println(Double.hashCode(-0.0));
    System.exit(0);
    Assert.assertFalse(invertible(5.562684646268003E-309));
    Assert.assertTrue(invertible(MIN_INVERTIBLE2X));
    Assert.assertFalse(invertible2x(5.562684646268003E-309));
    Assert.assertTrue(invertible2x(MIN_INVERTIBLE2X));
    // 5.562684646268003E-309 <- not invertible
    // 5.562684646268010E-309 <- invertible
    // 5.562684646268003E-309
    // 5.562684646268010E-309
    // 1.7976931348623151E308 <- invertible2x
    // 1.7976931348623153E308 <- not invertible2x
    Assert.assertTrue(invertible(MAX_INVERTIBLE2X));
    // assertFalse(invertible(1.7976931348623153E308));
    Assert.assertTrue(invertible2x(MAX_INVERTIBLE2X));
    Assert.assertFalse(invertible2x(1.7976931348623153E308));
    System.out.println("CHECK:" + invariantInvert2x(MIN_INVERTIBLE2X));
    // double res = 1.0 / Double.MAX_VALUE;
    // res = Math.nextDown(res);
    // System.out.println("RES " + res);
    // System.out.println("BLU " + (1.0 / res));
    {
      double min = Double.MIN_VALUE;
      double max = 1E-300;
      Assert.assertFalse(invertible(min));
      Assert.assertTrue(invertible(max));
      bisectionSearch1(min, max);
      System.out.println("---");
    }
    {
      double min = 1E200;
      double max = Double.MAX_VALUE;
      Assert.assertTrue(invertible2x(min));
      Assert.assertFalse(invertible2x(max));
      bisectionSearch2(min, max);
      System.out.println("---");
    }
    // double eps = Math.nextUp(0.0);
    // System.out.println(eps);
    System.out.println("max exponent = " + Double.MAX_EXPONENT);
    System.out.println("min exponent = " + Double.MIN_EXPONENT);
    System.out.println("max value  = " + Double.MAX_VALUE);
    System.out.println("min value  = " + Double.MIN_VALUE);
    System.out.println("min normal = " + Double.MIN_NORMAL);
    // ---
    System.out.println("closed under inversion");
    System.out.println(1 / Double.MIN_NORMAL);
    System.out.println(1 / Double.MAX_VALUE);
    // ---
    {
      System.out.println(Math.nextUp(1.0) - 1.0);
    }
    {
      double value = MIN_INVERTIBLE2X;
      for (int c = 0; c < 1000; ++c) {
        Assert.assertTrue(invertible(value));
        Assert.assertTrue(invertible2x(value));
        Assert.assertTrue(invariantInvert2x(value));
        value = Math.nextUp(value);
      }
    }
    {
      double value = 1.0 / Double.MAX_VALUE;
      System.out.println("truemin=" + value);
      // assertClosed(value);
      double inv = 1.0 / value;
      System.out.println(inv);
    }
    {
      double inf = Math.nextUp(Double.MAX_VALUE);
      Assert.assertEquals(inf, Double.POSITIVE_INFINITY);
    }
  }
}
