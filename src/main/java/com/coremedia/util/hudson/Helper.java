package com.coremedia.util.hudson;

public class Helper {
  /**
   * Round a double with n decimals
   *
   * @param a value to convert
   * @param n Number of decimals
   * @return the rounded number
   */
  public static double floor(double a, int n) {
    double p = Math.pow(10.0, n);
    return Math.floor((a * p) + 0.5) / p;
  }
}