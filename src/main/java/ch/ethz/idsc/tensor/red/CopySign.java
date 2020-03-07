// code by jph
package ch.ethz.idsc.tensor.red;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.sca.Sign;
import ch.ethz.idsc.tensor.sca.SignInterface;

/** Composes a Scalar with the magnitude of a and the sign of b.
 * Function returns a or a.negate() depending on sign of a and b.
 * 
 * <p>The function appears
 * <ul>
 * <li>in the Fortran language and old literature
 * <li>{@link Math#copySign(double, double)}
 * <li><a href="http://en.cppreference.com/w/cpp/numeric/math/copysign">std::copysign</a>
 * </ul> */
public enum CopySign {
  ;
  /** Hint:
   * implementation is not consistent with {@link Math#copySign(double, double)}
   * in the special case when the second argument b == -0.0.
   * The tensor library treats the case b == b.zero() as if b was positive.
   * 
   * @param magnitude implements {@link SignInterface}
   * @param signum implements {@link SignInterface}
   * @return {@link Scalar} of type of a with the magnitude of a and the sign of b */
  public static Scalar of(Scalar magnitude, Scalar signum) {
    boolean sign = Sign.isPositiveOrZero(magnitude);
    return Sign.isPositiveOrZero(signum) //
        ? (sign ? magnitude : magnitude.negate())
        : (sign ? magnitude.negate() : magnitude);
  }
}
