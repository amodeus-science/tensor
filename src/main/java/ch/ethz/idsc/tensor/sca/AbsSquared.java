// code by jph
package ch.ethz.idsc.tensor.sca;

import ch.ethz.idsc.tensor.ComplexScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;

/** the purpose of AbsSquared is to preserve the precision when working with complex numbers.
 * Since {@link ComplexScalar}::abs involves a square root the square of the absolute value
 * is better computed using <code>z * conjugate(z)</code>.
 * 
 * <p>If a {@link Scalar} does not implement {@link ComplexEmbedding}, then
 * the function AbsSquared is computed simply as
 * <code>abs(x) ^ 2</code> */
public enum AbsSquared implements ScalarUnaryOperator {
  FUNCTION;

  @Override
  public Scalar apply(Scalar scalar) {
    if (scalar instanceof ConjugateInterface)
      return scalar.multiply(Conjugate.FUNCTION.apply(scalar));
    Scalar abs = scalar.abs();
    return abs.multiply(abs);
  }

  /** @param tensor
   * @return tensor with all scalars replaced with their absolute value */
  @SuppressWarnings("unchecked")
  public static <T extends Tensor> T of(T tensor) {
    return (T) tensor.map(FUNCTION);
  }

  /** @param a
   * @param b
   * @return |a - b| ^ 2 */
  public static Scalar between(Scalar a, Scalar b) {
    return FUNCTION.apply(a.subtract(b));
  }
}
