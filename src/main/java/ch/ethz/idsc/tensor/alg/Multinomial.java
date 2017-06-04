// code by jph
package ch.ethz.idsc.tensor.alg;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;

/** <p>ordering of coefficients is <em>reversed</em> compared to
 * MATLAB::polyval, MATLAB::polyfit, etc. ! */
// EXPERIMENTAL, api not finalized
public enum Multinomial {
  ;
  // ---
  /** the horner scheme improves speed and stability for the numeric evaluation of large polynomials
   * 
   * horner({a, b, c, d}, x) == a + b*x + c*x^2 + d*x^3
   * 
   * @param coeffs
   * @param scalar
   * @return */
  public static Scalar horner(Tensor coeffs, Scalar scalar) {
    Scalar total = scalar.zero();
    for (Tensor entry : Reverse.of(coeffs))
      total = total.multiply(scalar).add(entry);
    return total;
  }
}
