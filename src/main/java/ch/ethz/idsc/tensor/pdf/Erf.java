// code by gjoel
// formula from https://en.wikipedia.org/wiki/Error_function
package ch.ethz.idsc.tensor.pdf;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** <pre>
 * Erf[z] == 1 - Erfc[z]
 * </pre>
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/Erf.html">Erf</a> */
public enum Erf implements ScalarUnaryOperator {
  FUNCTION;

  @Override
  public Scalar apply(Scalar z) {
    return RealScalar.ONE.subtract(Erfc.FUNCTION.apply(z));
  }

  /** @param tensor
   * @return tensor with all scalar entries replaced by the evaluation under Erf */
  @SuppressWarnings("unchecked")
  public static <T extends Tensor> T of(T tensor) {
    return (T) tensor.map(FUNCTION);
  }
}
