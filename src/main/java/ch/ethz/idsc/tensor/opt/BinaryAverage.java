// code by jph
package ch.ethz.idsc.tensor.opt;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;

/** Reference:
 * "Smoothing using Geodesic Averages", 2018
 * http://www.vixra.org/abs/1810.0283 */
public interface BinaryAverage {
  /** @param p
   * @param q
   * @param scalar <em>not</em> constrained to the interval [0, 1]
   * @return point on curve/geodesic that connects p and q at parameter scalar
   * for scalar == 0 the function returns p, for scalar == 1 the function returns q */
  Tensor split(Tensor p, Tensor q, Scalar scalar);
}
