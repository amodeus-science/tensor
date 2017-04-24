// code by jph
// implementation adapted from Ruby code of https://en.wikipedia.org/wiki/Exponentiation_by_squaring
package ch.ethz.idsc.tensor.alg;

import java.math.BigInteger;

/** https://en.wikipedia.org/wiki/Exponentiation_by_squaring
 * 
 * interface used by MatrixPower and GaussScalar */
// EXPERIMENTAL
public abstract class BinaryPower<T> {
  public abstract T zeroth();

  public abstract T invert(T object);

  public abstract T multiply(T fac1, T object);

  public final T apply(T x, long exponent) {
    return apply(x, BigInteger.valueOf(exponent));
  }

  public final T apply(T x, BigInteger exponent) {
    T result = zeroth();
    if (exponent.signum() == 0)
      return result;
    if (exponent.signum() == -1) { // convert problem to positive exponent
      exponent = exponent.negate();
      x = invert(x);
    }
    while (true) { // iteration
      if (exponent.testBit(0))
        result = multiply(x, result);
      exponent = exponent.shiftRight(1);
      if (exponent.signum() == 0)
        return result;
      x = multiply(x, x);
    }
  }
}
