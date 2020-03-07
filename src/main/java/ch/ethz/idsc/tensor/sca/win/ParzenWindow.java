// code by jph
package ch.ethz.idsc.tensor.sca.win;

import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Series;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** ParzenWindow[1/2]=0
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/ParzenWindow.html">ParzenWindow</a> */
public enum ParzenWindow implements ScalarUnaryOperator {
  FUNCTION;

  private static final Scalar _1_4 = RationalScalar.of(1, 4);
  private static final ScalarUnaryOperator S1 = Series.of(Tensors.vector(1, 0, -24, 48));
  private static final ScalarUnaryOperator S2 = Series.of(Tensors.vector(2, -12, 24, -16));

  @Override
  public Scalar apply(Scalar x) {
    x = x.abs();
    if (Scalars.lessThan(x, RationalScalar.HALF))
      return Scalars.lessEquals(x, _1_4) //
          ? S1.apply(x)
          : S2.apply(x);
    return RealScalar.ZERO;
  }

  /** @param tensor
   * @return tensor with all scalars replaced with their function value */
  @SuppressWarnings("unchecked")
  public static <T extends Tensor> T of(T tensor) {
    return (T) tensor.map(FUNCTION);
  }
}
